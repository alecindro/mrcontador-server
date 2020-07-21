package br.com.mrcontador.file.comprovante;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.DeltaType;
import com.github.difflib.patch.Patch;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;

import br.com.mrcontador.file.pdf.PdfReaderPreserveSpace;

public class Diff {
	//$1 = parceiro
	//$ag = agencia
	//$conta = conta
	//$2 = fornecedor
	//$pagto = data pagamento
	//$valor_doc = valor documento
	//$valor_pag = valor do pagamento
	//$doc = documento
	//$cnpj = cnpj do pagador
	//$cnpj_ben = cnpj do beneficiario
	//$data_venc = data de vencimento
	//$3 = banco
	//$4 = observacao

	//$valor_pag = valor do pagamento
	
	private String[] hashs = {"$1","$ag","$conta","$2","$pagto","$valor_doc","$3","$valor_pag","$doc","$cnpj","$data_venc"};
	
	public static String modelBradesco = "                                                   Comprovante             de   Transação          Bancária\n" + 
			"                                                   Boleto   de  Cobrança\n" + 
			"                                                   Data   da  operação:    07/05/2020\n" + 
			"                                                   N°  de  controle:   752.221.588.152.371.615         |Documento:      #{d_o}\n" + 
			"                       Conta   de  débito:    Agência:    #{agencia}   | Conta:   #{conta}     | Tipo:  Conta-Corrente\n" + 
			"                               Empresa:       #{p_r}   | CNPJ:    #{cnpj}\n" + 
			"                     Código    de  barras:    23792   37403    59606   535488    19010   520005    6  82480000018961\n" + 
			"                    Banco   destinatário:     237  - BANCO      BRADESCO        S.A.\n" + 
			"                           Razao    Social    Não   informado\n" + 
			"                            Beneficiário:\n" + 
			"                        Nome     Fantasia     Não   informado\n" + 
			"                            Beneficiário:\n" + 
			"              CPF/CNPJ      Beneficiário:     Não   informado\n" + 
			"                Razao    Social  Sacador      Não   informado\n" + 
			"                                 Avalista:\n" + 
			"                   CPF/CNPJ      Sacador      Não   informado\n" + 
			"                                 Avalista:\n" + 
			"              Instituição  Recebedora:        237  - BANCO      BRADESCO        S.A.\n" + 
			"                    Nome    do  Pagador:      Não   informado\n" + 
			"              CPF/CNPJ      do  Pagador:      Não   informado\n" + 
			"                        Data   de  débito:    #{data-pag}\n" + 
			"                  Data  de  vencimento:       #{data-venc}\n" + 
			"                                    Valor:    R$  #{valor_doc}\n" + 
			"                               Desconto:      R$  0,00\n" + 
			"                            Abatimento:       R$  0,00\n" + 
			"                            Bonificação:      R$  0,00\n" + 
			"                                    Multa:    R$  0,00\n" + 
			"                                    Juros:    R$  0,00\n" + 
			"                              Valor  total:   R$  #{valor_pag}\n" + 
			"                              Descrição:      #{o_s}\n" + 
			"                 A  transação   acima    foirealizada   por  meio   do  #{b_o}    COBRANCA        COM     DEBITO     AUTOMATICO\n" + 
			"                                                                                            Autenticação\n" + 
			"                                       TP?IBMT4        y2yKO3I2         z??Z2hqv        KIJuQgkK        I7fU5YK#        sVbyMHGo        ZtXbUjcw         6LkJ436y\n" + 
			"                                       sW7Zr*cv        mLbxr95Y         kmc*gvuy        e#5yHK@p        fhp97oij        NsC2vClX        lV4xLC#L         NKKu?fY@\n" + 
			"                                       5nleseRk        Ohxe73fi         vg?Q*Pzf        f4h?6fdA        RdHewaA2        2*cSPAAL        67173280         27749010\n" + 
			"                 SAC    -Serviço    de     Alô  Bradesco           Deficiente   Auditivo   ou  de Fala      Cancelamentos,     Reclamações     e                     Demais   telefones\n" + 
			"                  Apoio   ao  Cliente      0800   704   8383       0800   722  0099                         Informações.                                             consulte  o site\n" + 
			"                                                                                                            Atendimento    24 horas,  7 dias por  semana.            Fale Conosco.\n" + 
			"                Ouvidoria       0800   727   9933      Atendimento   de  segunda   a sexta-feira, das  8h às  18h, exceto  feriados.";

	public static String modelSantander = "                                                           \n"
			+ "                       #{p_r}                                     Agência: #{agencia}              Conta    Corrente: #{conta}\n"
			+ "                       Pagamento        com    codigo    de  barras    >  2ª  via de  comprovante\n"
			+ "                                                                                 COMPROVANTE         DE   PAGAMENTO\n"
			+ "                        \n" + "                       Empresa:                       #{f_r}\n"
			+ "                       Convenio     de\n" + "                       Arrecadacao:\n"
			+ "                                                      00330067001004016510\n"
			+ "                       Codigo    de  Barras:          84620000001-2         01940004000-2        04468260140-4        30997316600-7\n"
			+ "                       Data   de  Pagamento:          #{data_pagto}\n"
			+ "                       Valor:                         R$   #{valor_pag}\n"
			+ "                       Data   da  Transacao:          24/03/2020\n"
			+ "                       Hora   da  Transacao:          13:20:59\n"
			+ "                       Canal:                         INTERNET      BANKING\n"
			+ "                       Autenticacao:                  #{d_o}\n" + "                        \n"
			+ "                       Pagamento      efetuado    com   base   nas  informacoes     do  codigo   de  barras.\n"
			+ "                       Guarde    este  recibo   junto  com   o  documento      original  para  eventual    comprovacao       do  pagamento.\n"
			+ "                                                                                                        SAC   - Atendimento      24h   por  dia,  todos  os  dias.\n"
			+ "                                                                                                        0800   762   7777\n"
			+ "                          Central    de  Atendimento        #{b_o}      Empresarial                  0800   771   0401   (Pessoas    com   deficiência   auditiva   ou  de\n"
			+ "                          4004-2125     (Regiões    Metropolitanas)                                     fala)\n"
			+ "                          0800   726   2125   (Demais    Localidades)                                   Ouvidoria     -  Das  9h  às  18h,  de  segunda     a sexta-feira,\n"
			+ "                          0800   723   5007   (Pessoas    com   deficiência   auditiva   ou  de         exceto   feriado.\n"
			+ "                          fala)                                                                         0800   726   0322\n"
			+ "                                                                                                        0800   771   0301   (Pessoas    com   deficiência   auditiva   ou  de\n"
			+ "                                                                                                        fala)\n"
			+ "\n" + " ";

	public static void main(String[] args) {
		try {
			// Diff.patch(modelSantander, identifier());
			DiffText d = new DiffText();
			Map<Integer, DiffValue> map = d.doDiff(modelBradesco, identifier());
			List<DiffValue> list = new ArrayList<DiffValue>();
			for (DiffValue _values : map.values()) {
				//System.out.println(_values.getOldValue() + " - " + _values.getNewValue());
				diffpatch(list,_values.getOldValue(), _values.getNewValue());
			}
			/*
			 * Patch<String> patchs = DiffUtils.diffInline(modelSantander, identifier());
			 * List<AbstractDelta<String>> deltas = patchs.getDeltas();
			 * for(AbstractDelta<String> delta : deltas) { System.out.println(delta); }
			 */
			for(DiffValue _values : list) {
				System.out.println(_values.toString());
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// UnifiedDiffUtils.

	}
	
	public static void twopatch(String original, String revision) throws DiffException {
		DiffText d = new DiffText();
		Map<Integer, DiffValue> map = d.doDiff(original, revision);
		List<DiffValue> list = new ArrayList<DiffValue>();
		for (DiffValue _values : map.values()) {
			diffpatch(list, _values.getOldValue() ,_values.getNewValue());
		}
	}

	public static void diffpatch(List<DiffValue> list , String original, String revision) throws DiffException {
		Patch<String> patchs = DiffUtils.diffInline(original, revision);
		List<AbstractDelta<String>> deltas = patchs.getDeltas();
		for (AbstractDelta<String> delta : deltas) {
			if(delta.getType().equals(DeltaType.CHANGE)){
				DiffValue diffValues = new DiffValue();
				diffValues.setOldValue(delta.getSource().getLines().get(0));
				diffValues.setNewValue(delta.getTarget().getLines().get(0));
				list.add(diffValues);
			}
			if(delta.getType().equals(DeltaType.INSERT)) {
				list.get(list.size()-1).setNewValue(list.get(list.size()-1).getNewValue().concat(" ").concat(delta.getTarget().getLines().get(0)));
			}
			
		}
	}
	
	
	public static void patch(String original, String revision) throws DiffException {
		DiffRowGenerator generator = DiffRowGenerator.create().showInlineDiffs(true).inlineDiffByWord(true)
				.oldTag(f -> "~").newTag(f -> "**").build();
		List<DiffRow> rows = generator.generateDiffRows(Arrays.asList(original.split("\\r?\\n")),
				Arrays.asList(revision.split("\\r?\\n")));
		for (DiffRow row : rows) {
			System.out.println("|" + row.getOldLine() + "|" + row.getNewLine() + "|");
		}
	}

	public static String identifier() throws Exception {
		String folder = "/home/alecindro/Documents/drcontabil/docs/comprovantes/bradesco.pdf";
		File initialFile = new File(folder);
		PDDocument document;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream first = null;
		try {
			InputStream stream = new FileInputStream(initialFile);
			stream.transferTo(baos);
			first = new ByteArrayInputStream(baos.toByteArray());
			document = PDDocument.load(first);
			Splitter splitter = new Splitter();
			List<PDDocument> pages = splitter.split(document);
			PDFTextStripper stripper = new PdfReaderPreserveSpace();
			String text = stripper.getText(pages.get(0));
			return text;
		} catch (Exception e) {
			throw (e);
		}
	}

}
