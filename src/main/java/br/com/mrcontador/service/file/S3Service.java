package br.com.mrcontador.service.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
//import org.xml.sax.ContentHandler;
//import org.xml.sax.SAXException;

import br.com.mrcontador.config.S3Properties;
import br.com.mrcontador.config.tenant.TenantContext;
import br.com.mrcontador.domain.Arquivo;
import br.com.mrcontador.domain.ArquivoErro;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.domain.Contador;
import br.com.mrcontador.domain.Notafiscal;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.TipoDocumento;
import br.com.mrcontador.file.notafiscal.pdf.NFDanfeReport;
import br.com.mrcontador.file.notafiscal.pdf.NFDanfeReport310;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.ArquivoErroService;
import br.com.mrcontador.service.ArquivoService;
import br.com.mrcontador.service.ContadorService;
import br.com.mrcontador.service.NotafiscalService;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.dto.FileS3;
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
	@Autowired
	private ContadorService contadorServive;

	private final Logger log = LoggerFactory.getLogger(S3Service.class);

	@Async("taskExecutor")
	public void uploadErro(FileDTO dto) {
		TenantContext.setTenantSchema(SecurityUtils.DEFAULT_TENANT);
		log.info("Pesquisando contador: "+dto.getContador());
		Optional<Contador> contador = contadorServive.findByDatasource(dto.getContador());
		String dir = properties.getErrorFolder();
		String filename = MrContadorUtil.genErroFileName(dto.getContador(), dto.getContentType());
		log.info("Carregando arquivo: {}", filename);
		
		if (dto.getSize() != null) {
			 uploadS3Stream(filename, dir, dto.getSize(),
					new ByteArrayInputStream(dto.getOutputStream().toByteArray()));
		} else {
			 uploadS3Bytes(filename, dir, dto.getBytes());
		}
		dto.setBucket(properties.getBucketName());
		dto.setS3Dir(dir);
		dto.setS3Url(MrContadorUtil.getS3Url(dir, properties.getUrlS3(), filename));		
		dto.setUrl(MrContadorUtil.getS3Url(dir, properties.getUrlS3(), filename));
		dto.setName(filename);
		saveArquivoErro(dto,contador);
	}

	@Async("taskExecutor")
	public void uploadErro(List<FileS3> files) {
		String dir = properties.getErrorFolder();
		TenantContext.setTenantSchema(SecurityUtils.DEFAULT_TENANT);
		String tenant = files.stream().findFirst().get().getFileDTO().getContador();
		Optional<Contador> contador = contadorServive.findByDatasource(tenant);
		files.forEach(fileS3 -> {
			FileDTO dto = fileS3.getFileDTO();
			String filename = MrContadorUtil.genErroFileName(dto.getContador(), dto.getContentType());
			uploadS3Bytes(filename, dir, fileS3.getOutputStream().toByteArray());
			dto.setBucket(properties.getBucketName());
			dto.setS3Dir(dir);
			dto.setS3Url(MrContadorUtil.getS3Url(dir, properties.getUrlS3(), filename));			
			dto.setUrl(MrContadorUtil.getS3Url(dir, properties.getUrlS3(), filename));
			dto.setName(filename);
			dto.setTipoDocumento(fileS3.getTipoDocumento());
			saveArquivoErro(dto,contador);
		});
	}
	private Arquivo saveArquivo(FileDTO dto) {
		log.info("Salvando arquivo: {}", dto.getName());
		ArquivoMapper mapper = new ArquivoMapper();
		Arquivo arquivo = mapper.toEntity(dto);
		arquivo = arquivoService.save(arquivo);
		return arquivo;
	}

	private ArquivoErro saveArquivoErro(FileDTO dto, Optional<Contador> contador) {
		log.info("Salvando arquivo: {}", dto.getName());
		ArquivoErroMapper mapper = new ArquivoErroMapper();
		ArquivoErro arquivoErro = mapper.toEntity(dto,contador);
		arquivoErro = arquivoErroService.save(arquivoErro);
		return arquivoErro;
	}

	//@Async("taskExecutor")
	public void uploadNota(NotafiscalService notafiscalService, Arquivo pdf, Arquivo xml,
			com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaProcessada nfNotaProcessada,
			ByteArrayOutputStream xmlByte, List<Notafiscal> notas) {
		NFDanfeReport nfDanfeReport = new NFDanfeReport(nfNotaProcessada);
		try {
			byte[] bytesArray = nfDanfeReport.gerarDanfeNFe(null);
			uploadS3Bytes(pdf.getNome(), pdf.gets3Dir(), bytesArray);
			uploadS3Bytes(xml.getNome(), xml.gets3Dir(), xmlByte.toByteArray());
		} catch (Exception e) {
			log.error("Arquivo não conseguiu processar: " + pdf.getNomeOriginal());
			log.error(e.getMessage(), e);
		}

		// TenantContext.setTenantSchema(tenant);
		// notas.forEach(nota -> notafiscalService.updateArquivo(nota.getId(),
		// xml.getId(), pdf.getId()));
	}

	//@Async("taskExecutor")
	public void uploadNota(NotafiscalService notafiscalService, Arquivo pdf, Arquivo xml,
			com.fincatto.documentofiscal.nfe310.classes.nota.NFNotaProcessada nfNotaProcessada,
			ByteArrayOutputStream xmlByte, List<Notafiscal> notas) {
		NFDanfeReport310 nfDanfeReport = new NFDanfeReport310(nfNotaProcessada);
		try {
			byte[] bytesArray = nfDanfeReport.gerarDanfeNFe(null);
			uploadS3Bytes(pdf.getNome(), pdf.gets3Dir(), bytesArray);
			uploadS3Bytes(xml.getNome(), xml.gets3Dir(), xmlByte.toByteArray());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		// TenantContext.setTenantSchema(tenant);
		// notas.forEach(nota -> notafiscalService.updateArquivo(nota.getId(),
		// xml.getId(), pdf.getId()));
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

/*	@Async("taskExecutor")
	public void uploadComprovante(List<FileS3> files, String tenant) {
		ArquivoMapper mapper = new ArquivoMapper();
		TenantContext.setTenantSchema(tenant);
		for (FileS3 fileS3 : files) {
			String dir = MrContadorUtil.getFolder(fileS3.getFileDTO().getContador(),
					String.valueOf(fileS3.getFileDTO().getParceiro().getId()), properties.getComprovanteFolder());
			String filename = MrContadorUtil.genFileName(fileS3.getTipoDocumento(),
					fileS3.getFileDTO().getParceiro().getId(), fileS3.getFileDTO().getContentType(), fileS3.getPage());
			String eTag = uploadS3Bytes(filename, dir, fileS3.getOutputStream().toByteArray());
			fileS3.getFileDTO().setName(filename);
			fileS3.getFileDTO().setBucket(properties.getBucketName());
			fileS3.getFileDTO().setS3Dir(dir);
			fileS3.getFileDTO().setS3Url(MrContadorUtil.getS3Url(dir, properties.getUrlS3(), filename));
			fileS3.getFileDTO().seteTag(eTag);
			fileS3.getFileDTO().setUrl(MrContadorUtil.getS3Url(dir, properties.getUrlS3(), filename));
			Arquivo arquivo = mapper.toEntity(fileS3.getFileDTO());
			arquivoService.save(arquivo);
			for (Comprovante comprovante : fileS3.getComprovantes()) {
				comprovanteService.updateArquivo(comprovante.getId(), arquivo.getId());
			}
		}
	}
	
	@Async("taskExecutor")
	public void uploadComprovante(List<Comprovante> comprovantes, ByteArrayOutputStream stream, String tenant) {
		TenantContext.setTenantSchema(tenant);
		for (Comprovante comprovante : comprovantes) {
			Arquivo arquivo = comprovante.getArquivo();
			String eTag = uploadS3Bytes(arquivo.getNome(), comprovante.getArquivo().gets3Dir(), stream.toByteArray());
			arquivo.setEtag(eTag);
			arquivoService.save(arquivo);
		}
		try {
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	*/
	//@Async("taskExecutor")
	public void uploadComprovante(Comprovante comprovante, ByteArrayOutputStream stream) {
		uploadS3Bytes(comprovante.getArquivo().getNome(), comprovante.getArquivo().gets3Dir(), stream.toByteArray());
		try {
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/*
	 * @Async("taskExecutor") public void uploadComprovante(FileS3 fileS3, String
	 * tenant) { ArquivoMapper mapper = new ArquivoMapper();
	 * TenantContext.setTenantSchema(tenant); String dir =
	 * MrContadorUtil.getFolder(fileS3.getFileDTO().getContador(),
	 * String.valueOf(fileS3.getFileDTO().getParceiro().getId()),
	 * properties.getComprovanteFolder()); String filename =
	 * MrContadorUtil.genFileName(fileS3.getTipoDocumento(),
	 * fileS3.getFileDTO().getParceiro().getId(),
	 * fileS3.getFileDTO().getContentType(), fileS3.getPage()); String eTag =
	 * uploadS3Bytes(filename, dir, fileS3.getOutputStream().toByteArray());
	 * fileS3.getFileDTO().setName(filename);
	 * fileS3.getFileDTO().setBucket(properties.getBucketName());
	 * fileS3.getFileDTO().setS3Dir(dir);
	 * fileS3.getFileDTO().setS3Url(MrContadorUtil.getS3Url(dir,
	 * properties.getUrlS3(), filename)); fileS3.getFileDTO().seteTag(eTag);
	 * fileS3.getFileDTO().setUrl(MrContadorUtil.getS3Url(dir,
	 * properties.getUrlS3(), filename)); Arquivo arquivo =
	 * mapper.toEntity(fileS3.getFileDTO()); arquivoService.save(arquivo);
	 * fileS3.getComprovantes().forEach(comprovante -> {
	 * comprovanteService.updateArquivo(comprovante.getId(), arquivo.getId()); });
	 * 
	 * }
	 */

	public Arquivo uploadPlanoConta(FileDTO dto) {
		dto.setTipoDocumento(TipoDocumento.PLANO_DE_CONTA);
		upload(properties.getPlanoContaFolder(), dto);
		return saveArquivo(dto);
	}

	private FileDTO upload(String type, FileDTO dto) {
		String dir = MrContadorUtil.getFolder(dto.getContador(), String.valueOf(dto.getParceiro().getId()), type);
		String eTag = "";
		String filename = MrContadorUtil.genFileName(dto.getTipoDocumento(), dto.getContentType());
		log.info("Carregando arquivo: {}", filename);
		if (dto.getSize() != null) {
			eTag = uploadS3Stream(filename, dir, dto.getSize(),
					new ByteArrayInputStream(dto.getOutputStream().toByteArray()));
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
		} catch (Exception e) {
			throw new MrContadorException("upload.aws.error", e);
		}
	}

	private String uploadS3Stream(String filename, String dir, Long size, InputStream stream) {
		try {
			PutObjectResponse response = s3Client.putObject(
					PutObjectRequest.builder().bucket(properties.getBucketName()).key(dir + "/" + filename).build(),
					RequestBody.fromInputStream(stream, size));
			return response.eTag();
		} catch (Exception e) {
			throw new MrContadorException("upload.aws.error", e);
		}
	}

	/*
	 * private CompletableFuture<PutObjectResponse> uploadAsyncStream(String
	 * filename, String dir, FileS3 fileS3, Comprovante comprovante) {
	 * CompletableFuture<PutObjectResponse> future = s3AsyncClient.putObject(
	 * PutObjectRequest.builder().bucket(properties.getBucketName()).key(dir + "/" +
	 * filename).build(),
	 * AsyncRequestBody.fromBytes(fileS3.getOutputStream().toByteArray()));
	 * future.join(); return future; }
	 */

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
