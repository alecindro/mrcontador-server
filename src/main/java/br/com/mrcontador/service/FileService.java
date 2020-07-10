package br.com.mrcontador.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.dto.ParserDTO;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
public class FileService {

	@Autowired
	S3Client s3Client;
	@Value("${}")
	String bucketName;
	@Value("${}")
	String planoContaFolder;
	@Value("${}")
	String urlS3;
//		"https://mrcontador-files.s3.amazonaws.com/planoconta/plano_de_contas_mercado_dassoler.pdf"

	private final Logger log = LoggerFactory.getLogger(FileService.class);

	@Deprecated
	public void save(InputStream stream) throws IOException, SAXException, TikaException {
		ParserDTO parserDTO = this.process(stream);
		log.info(parserDTO.getContent());
	}

	@Deprecated
	private ParserDTO process(InputStream stream) throws IOException, SAXException, TikaException {
		Metadata metadata = new Metadata();
		ParseContext context = new ParseContext();
		Parser parser = new AutoDetectParser();
		ContentHandler contentHandler = new BodyContentHandler(-1);
		parser.parse(stream, contentHandler, metadata, context);
		return new ParserDTO(contentHandler.toString(), metadata);
	}

	public FileDTO uploadPlanoConta(String ds, FileDTO dto) {
		String dir = ds + "/" + planoContaFolder;
		String eTag = "";
		if (dto.getSize() != null && dto.getInputStream() != null) {
			eTag = uploadS3Stream(dto.getName(), dir, dto.getSize(), dto.getInputStream());
		} else {
			eTag = uploadS3Bytes(dto.getName(), dir, dto.getBytes());
		}
		dto.setBucket(bucketName);
		dto.setS3Dir(dir);
		dto.setS3Url(dir + "/" + dto.getName());
		dto.seteTag(eTag);
		dto.setUrl(urlS3 + "/" + dir + "/" + dto.getName());
		return dto;
	}

	private String uploadS3Bytes(String filename, String dir, byte[] bytes) {
		PutObjectResponse response = s3Client.putObject(
				PutObjectRequest.builder().bucket(bucketName).key(dir + "/" + filename).build(),
				RequestBody.fromBytes(bytes));
		return response.eTag();
	}

	private String uploadS3Stream(String filename, String dir, Long size, InputStream stream) {
		PutObjectResponse response = s3Client.putObject(
				PutObjectRequest.builder().bucket(bucketName).key(dir + "/" + filename).build(),
				RequestBody.fromInputStream(stream, size));
		return response.eTag();
	}

	public byte[] downloadByteArray(String s3Url) {
		return downloadS3(s3Url).asByteArray();
	}

	public ByteBuffer downloadByteBuffer(String s3Url) {
		return downloadS3(s3Url).asByteBuffer();
	}

	public InputStream downloadStream(String s3Url) {
		return downloadS3(s3Url).asInputStream();
	}

	private ResponseBytes<GetObjectResponse> downloadS3(String s3Url) {
		GetObjectRequest objectRequest = GetObjectRequest.builder().key(s3Url).bucket(bucketName).build();
		return s3Client.getObjectAsBytes(objectRequest);
	}

}
