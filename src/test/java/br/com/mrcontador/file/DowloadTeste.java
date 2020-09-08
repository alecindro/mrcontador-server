package br.com.mrcontador.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.mrcontador.MrcontadorServerApp;
import br.com.mrcontador.service.file.S3Service;

@SpringBootTest(classes = MrcontadorServerApp.class)
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
public class DowloadTeste {
	
	@Autowired
	private S3Service s3Service;
	
	@Test
	public void teste() throws IOException {
	String s3Url = "teste/2/comprovantes/Comprovante_parc_2_data_2020_9_7_13_13_57_page_.pdf";	
	byte[] buffer = s3Service.downloadByteArray(s3Url);
	File targetFile = new File("/home/alecindro/Documents/drcontabil/upload/targetFile.pdf");
    OutputStream outStream = new FileOutputStream(targetFile);
    outStream.write(buffer);
    outStream.close();
	}

}
