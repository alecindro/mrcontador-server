package br.com.mrcontador.aws;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.sun.mail.iap.Response;

import br.com.mrcontador.MrcontadorServerApp;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@SpringBootTest(classes = MrcontadorServerApp.class)
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
public class ListBuckets {
	
	@Autowired
	S3Client s3Client;
	
	private String bucketName = "mrcontador-files";

	
	public void test() {
		ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().build();
		ListBucketsResponse listBucketsResponse = s3Client.listBuckets(listBucketsRequest);
		listBucketsResponse.buckets().stream().forEach(x -> System.out.println(x.name()));
		
		upload();
	}
	
	@Test
	public void upload() {
		String folder = "/home/alecindro/Documents/drcontabil/docs/Plano de Contas - mercado dassoler.pdf";
		File initialFile = new File(folder);
		Long size = initialFile.length();
	    try {
			InputStream stream = new FileInputStream(initialFile);
	   
		PutObjectResponse response = s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucketName).key("ds_demo/planocontas/6/planoconta_2020_7_11_20_10_10.pdf")
                .build(),
        RequestBody.fromInputStream(stream, size));
		System.out.println(response.eTag());
		System.out.println(response.toString());
	    }catch(Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	
}
