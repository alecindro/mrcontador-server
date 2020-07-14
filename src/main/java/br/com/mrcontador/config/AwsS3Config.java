package br.com.mrcontador.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsS3Config {

	@Bean
	public S3Client s3Client() {
		SystemPropertyCredentialsProvider credentials = SystemPropertyCredentialsProvider.create();
		S3Client client = S3Client.builder().credentialsProvider(credentials).region(Region.US_EAST_1).build();
		
		return client;
	}

}
