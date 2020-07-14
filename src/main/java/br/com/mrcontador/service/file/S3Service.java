package br.com.mrcontador.service.file;

import java.io.InputStream;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.xml.sax.ContentHandler;
//import org.xml.sax.SAXException;

import br.com.mrcontador.config.S3Properties;
import br.com.mrcontador.domain.Arquivo;
import br.com.mrcontador.domain.ArquivoErro;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.TipoDocumento;
import br.com.mrcontador.service.ArquivoErroService;
import br.com.mrcontador.service.ArquivoService;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.mapper.ArquivoErroMapper;
import br.com.mrcontador.service.mapper.ArquivoMapper;
import br.com.mrcontador.util.MrContadorUtil;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
public class S3Service {

	@Autowired
	private S3Client s3Client;
	@Autowired
	private S3Properties properties;
	@Autowired 
	private ArquivoService arquivoService; 
	@Autowired
	private ArquivoErroService arquivoErroService;

	private final Logger log = LoggerFactory.getLogger(S3Service.class);
/*
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
	*/
	public ArquivoErro uploadErro(FileDTO dto) {
		String dir = properties.getErrorFolder();
		String filename = MrContadorUtil.genErroFileName(dto.getContador(), dto.getContentType());
		log.info("Carregando arquivo: {0}",filename);
		String eTag = "";
		if (dto.getSize() != null && dto.getInputStream() != null) {
			eTag = uploadS3Stream(filename, dir, dto.getSize(), dto.getInputStream());
		} else {
			eTag = uploadS3Bytes(filename, dir, dto.getBytes());
		}
		dto.setBucket(properties.getBucketName());
		dto.setS3Dir(dir);
		dto.setS3Url(MrContadorUtil.getS3Url(dir, properties.getUrlS3(), filename));
		dto.seteTag(eTag);
		dto.setUrl(MrContadorUtil.getS3Url(dir, properties.getUrlS3(), filename));
		dto.setName(filename);
		return saveArquivoErro(dto);
	}
	
	private Arquivo saveArquivo(FileDTO dto) {
		log.info("Salvando arquivo: {0}",dto.getName());
		ArquivoMapper mapper = new ArquivoMapper();
		Arquivo arquivo = mapper.toEntity(dto);
		arquivo = arquivoService.save(arquivo);
		return arquivo;
	}
	
	private ArquivoErro saveArquivoErro(FileDTO dto) {
		log.info("Salvando arquivo: {0}",dto.getName());
		ArquivoErroMapper mapper = new ArquivoErroMapper();
		ArquivoErro arquivoErro = mapper.toEntity(dto);
		arquivoErro = arquivoErroService.save(arquivoErro);
		return arquivoErro;
	}
	
	public Arquivo uploadNota(FileDTO dto) {
		dto.setTipoDocumento(TipoDocumento.NOTA);
		upload(properties.getNotaFolder(), dto);
		return saveArquivo(dto);
	}

	public Arquivo uploadComprovante(FileDTO dto) {
		dto.setTipoDocumento(TipoDocumento.COMPROVANTE);
		upload(properties.getComprovanteFolder(), dto);
		return saveArquivo(dto);
	}

	public Arquivo uploadPlanoConta(FileDTO dto) {
		dto.setTipoDocumento(TipoDocumento.PLANO_DE_CONTA);
		upload(properties.getPlanoContaFolder(), dto);
		return saveArquivo(dto);
	}

	private FileDTO upload(String type, FileDTO dto) {
		String dir = MrContadorUtil.getFolder(dto.getContador(), String.valueOf(dto.getParceiro().getId()), type);
		String eTag = "";
		String filename = MrContadorUtil.genFileName(dto.getTipoDocumento(), dto.getContentType());
		log.info("Carregando arquivo: {0}",filename);
		if (dto.getSize() != null && dto.getInputStream() != null) {
			eTag = uploadS3Stream(filename, dir, dto.getSize(), dto.getInputStream());
		} else {
			eTag = uploadS3Bytes(filename, dir, dto.getBytes());
		}
		dto.setName(filename);
		dto.setBucket(properties.getBucketName());
		dto.setS3Dir(dir);
		dto.setS3Url(MrContadorUtil.getS3Url(dir, properties.getUrlS3(), filename));
		dto.seteTag(eTag);
		dto.setUrl(MrContadorUtil.getS3Url(dir, properties.getUrlS3(), filename));
		return dto;
	}

	private String uploadS3Bytes(String filename, String dir, byte[] bytes) {
		try {
		PutObjectResponse response = s3Client.putObject(
				PutObjectRequest.builder().bucket(properties.getBucketName()).key(dir + "/" + filename).build(),
				RequestBody.fromBytes(bytes));
		return response.eTag();
		}catch(Exception e) {
			throw new MrContadorException("error.upload.aws",e);
		}
	}

	private String uploadS3Stream(String filename, String dir, Long size, InputStream stream) {
		try {
		PutObjectResponse response = s3Client.putObject(
				PutObjectRequest.builder().bucket(properties.getBucketName()).key(dir + "/" + filename).build(),
				RequestBody.fromInputStream(stream, size));
		return response.eTag();
		}catch(Exception e) {
			throw new MrContadorException("error.upload.aws",e);
		}
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
		GetObjectRequest objectRequest = GetObjectRequest.builder().key(s3Url).bucket(properties.getBucketName())
				.build();
		return s3Client.getObjectAsBytes(objectRequest);
	}

}
