package br.com.mrcontador.file.planoconta;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

public class PdfReaderPreserveSpace extends PDFTextStripper {

	public float fixedCharWidth = 3;
	boolean endsWithWS = true;
	boolean needsWS = false;
	int chars = 0;

	PDRectangle cropBox = null;
	float pageLeft = 0;

	public PdfReaderPreserveSpace() throws IOException {
		super();
		this.setSortByPosition(true);
	}
	
	  
    float getEndX(TextPosition textPosition) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
    {
        Field field = textPosition.getClass().getDeclaredField("endX");
        field.setAccessible(true);
        return field.getFloat(textPosition);
    }
    
    int insertSpaces(int charsInLineAlready, float chunkStart, boolean spaceRequired)
    {
        int indexNow = charsInLineAlready;
        int indexToBe = (int)((chunkStart - pageLeft) / fixedCharWidth);
        int spacesToInsert = indexToBe - indexNow;
        if (spacesToInsert < 1 && spaceRequired)
            spacesToInsert = 1;

        return spacesToInsert;
    }
    void beginLine()
    {
        endsWithWS = true;
        needsWS = false;
        chars = 0;
    }
    
    protected void writeWordSeparator() throws IOException
    {
        needsWS = true;
    }
    
    protected void writeLineSeparator() throws IOException
    {
        super.writeLineSeparator();
        beginLine();
    }
    
    @Override
    protected void startPage(PDPage page) throws IOException
    {
        super.startPage(page);
        cropBox = page.getArtBox();
        pageLeft = cropBox.getLowerLeftX();
        beginLine();
    }

    protected void writeString(String text, List<TextPosition> textPositions) throws IOException
    {
        float recentEnd = 0;
        for (TextPosition textPosition: textPositions)
        {
            String textHere = textPosition.getUnicode();
            if (textHere.trim().length() == 0)
                continue;

            float start = textPosition.getTextMatrix().getTranslateX();
            boolean spacePresent = endsWithWS | textHere.startsWith(" ");

            if (needsWS | spacePresent | Math.abs(start - recentEnd) > 1)
            {
                int spacesToInsert = insertSpaces(chars, start, needsWS & !spacePresent);

                for (; spacesToInsert > 0; spacesToInsert--)
                {
                    writeString(" ");
                    chars++;
                }
            }

            writeString(textHere);
            chars += textHere.length();

            needsWS = false;
            endsWithWS = textHere.endsWith(" ");
            try
            {
                recentEnd = getEndX(textPosition);
            }
            catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e)
            {
                throw new IOException("Failure retrieving endX of TextPosition", e);
            }
        }
    }

}
