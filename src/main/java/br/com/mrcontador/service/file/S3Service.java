package br.com.mrcontador.service.file;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.xml.sax.ContentHandler;
//import org.xml.sax.SAXException;

import br.com.mrcontador.config.S3Properties;
import br.com.mrcontador.domain.Arquivo;
import br.com.mrcontador.domain.ArquivoErro;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.TipoDocumento;
import br.com.mrcontador.service.ArquivoErroService;
import br.com.mrcontador.service.ArquivoService;
import br.com.mrcontador.service.ComprovanteService;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.dto.FileS3;
import br.com.mrcontador.service.mapper.ArquivoErroMapper;
import br.com.mrcontador.service.mapper.ArquivoMapper;
import br.com.mrcontador.util.MrContadorUtil;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
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
	private S3AsyncClient s3AsyncClient;
	@Autowired
	private ComprovanteService comprovanteService;
	@Autowired
	private S3Properties properties;
	@Autowired 
	private ArquivoService arquivoService; 
	@Autowired
	private ArquivoErroService arquivoErroService;

	private final Logger log = LoggerFactory.getLogger(S3Service.class);

	public ArquivoErro uploadErro(FileDTO dto) {
		String dir = properties.getErrorFolder();
		String filename = MrContadorUtil.genErroFileName(dto.getContador(), dto.getContentType());
		log.info("Carregando arquivo: {}",filename);
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
		log.info("Salvando arquivo: {}",dto.getName());
		ArquivoMapper mapper = new ArquivoMapper();
		Arquivo arquivo = mapper.toEntity(dto);
		arquivo = arquivoService.save(arquivo);
		return arquivo;
	}
	
	private ArquivoErro saveArquivoErro(FileDTO dto) {
		log.info("Salvando arquivo: {}",dto.getName());
		ArquivoErroMapper mapper = new ArquivoErroMapper();
		ArquivoErro arquivoErro = mapper.toEntity(dto);
		arquivoErro = arquivoErroService.save(arquivoErro);
		return arquivoErro;
	}
	
	public Arquivo uploadNota(FileDTO dto) {
		dto.setTipoDocumento(TipoDocumento.NOTA);
		upload(properties.getNotaFolder(), dto);
		ArquivoMapper mapper = new ArquivoMapper();
		return mapper.toEntity(dto);
	}
	
	public Arquivo uploadExtrato(FileDTO dto) {
		dto.setTipoDocumento(TipoDocumento.EXTRATO);
		upload(properties.getExtratoFolder(), dto);
		ArquivoMapper mapper = new ArquivoMapper();
		return mapper.toEntity(dto);
	}

	public Arquivo uploadComprovante(FileDTO dto) {
		dto.setTipoDocumento(TipoDocumento.COMPROVANTE);
		upload(properties.getComprovanteFolder(), dto);
		return saveArquivo(dto);
	}
	
	public void uploadComporvante(List<FileS3> files) {
		for(FileS3 fileS3 : files) {
			String dir = MrContadorUtil.getFolder(fileS3.getFileDTO().getContador(), String.valueOf(fileS3.getFileDTO().getParceiro().getId()), properties.getComprovanteFolder());
			for(Comprovante comprovante : fileS3.getComprovantes()) {
			String filename = MrContadorUtil.genFileName(fileS3.getTipoDocumento(),fileS3.getFileDTO().getParceiro().getId(),fileS3.getFileDTO().getContentType(),fileS3.getPage());
			uploadAsyncStream(filename, dir, fileS3, comprovante);
			}
		}
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
		log.info("Carregando arquivo: {}",filename);
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
			throw new MrContadorException("upload.aws.error",e);
		}
	}

	private String uploadS3Stream(String filename, String dir, Long size, InputStream stream) {
		try {
		PutObjectResponse response = s3Client.putObject(
				PutObjectRequest.builder().bucket(properties.getBucketName()).key(dir + "/" + filename).build(),
				RequestBody.fromInputStream(stream, size));
		return response.eTag();
		}catch(Exception e) {
			throw new MrContadorException("upload.aws.error",e);
		}
	}
	
	private void uploadAsyncStream(String filename, String dir, FileS3 fileS3, Comprovante comprovante) {
		CompletableFuture<PutObjectResponse> future = s3AsyncClient.putObject(
                PutObjectRequest.builder()
                                .bucket(properties.getBucketName())
                                .key(dir + "/" + filename)
                                .build(),
                AsyncRequestBody.fromBytes(fileS3.getOutputStream().toByteArray())
        );
        future.whenComplete((resp, err) -> {
                if (resp != null) {
                	fileS3.getFileDTO().setName(filename);
                	fileS3.getFileDTO().setBucket(properties.getBucketName());
                	fileS3.getFileDTO().setS3Dir(dir);
                	fileS3.getFileDTO().setS3Url(MrContadorUtil.getS3Url(dir, properties.getUrlS3(), filename));
                	fileS3.getFileDTO().seteTag(resp.eTag());
                	fileS3.getFileDTO().setUrl(MrContadorUtil.getS3Url(dir, properties.getUrlS3(), filename));
                	Arquivo arquivo = saveArquivo(fileS3.getFileDTO());
                	comprovante.setArquivo(arquivo);
                	comprovanteService.save(comprovante);
                } else {
                   log.error(err.getMessage(),err);
                }
           
        });

        future.join();
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
