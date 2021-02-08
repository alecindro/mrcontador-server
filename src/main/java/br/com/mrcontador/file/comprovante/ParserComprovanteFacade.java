package br.com.mrcontador.file.comprovante;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mrcontador.config.S3Properties;
import br.com.mrcontador.config.tenant.TenantContext;
import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Arquivo;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.TipoDocumento;
import br.com.mrcontador.service.ArquivoService;
import br.com.mrcontador.service.ComprovanteService;
import br.com.mrcontador.service.ExtratoService;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.dto.FileS3;
import br.com.mrcontador.service.file.S3Service;
import br.com.mrcontador.service.mapper.ArquivoMapper;
import br.com.mrcontador.util.MrContadorUtil;

@Service
public class ParserComprovanteFacade {

	@Autowired
	private ComprovanteService service;
	@Autowired
	private ExtratoService extratoService;
	@Autowired
	private S3Service s3Service;
	@Autowired
	private ArquivoService arquivoService;
	@Autowired
	private S3Properties properties;
	private int page;

	private static Logger log = LoggerFactory.getLogger(ParserComprovanteFacade.class);

	public String process(FileDTO fileDTO, Agenciabancaria agencia) {
			log.info("parse comprovantes");
			page = 0;
			ParserComprovante parser = ParserComprovanteFactory.getParser(agencia.getBanCodigobancario());
			List<PPDocumentDTO> textComprovantes = parser.parseComprovante(fileDTO);			
			List<FileS3> erros = new ArrayList<FileS3>();
			ArquivoMapper arquivoMapper = new ArquivoMapper();
			List<Comprovante> salvos = new ArrayList<Comprovante>();
			boolean conflict = false;
			for (PPDocumentDTO pddComprovante : textComprovantes) {
				page = page +1;
				genFile(fileDTO, page);
				log.info("tenant " + TenantContext.getTenantSchema());
				Arquivo arquivo = arquivoMapper.toEntity(fileDTO);
				arquivoService.save(arquivo);
				
				try {
					List<Comprovante> _comprovantes = parser.parse(pddComprovante.getComprovante(), agencia, fileDTO.getParceiro());
					_comprovantes.forEach(c-> c.setPeriodo(MrContadorUtil.periodo(c.getComDatapagamento())));
					if (_comprovantes != null && !_comprovantes.isEmpty()) {
						for(Comprovante _comprovante : _comprovantes){
							try {
							_comprovante.setArquivo(arquivo);	
							_comprovante = parser.save(_comprovante, service);
							s3Service.uploadComprovante(_comprovante, pddComprovante.getOutstream());
							salvos.add(_comprovante);
							}catch(org.springframework.dao.DataIntegrityViolationException e) {
								conflict = true;
							}catch(Exception e ) {
								FileS3 fileS3 = new FileS3();
								fileS3.setFileDTO(fileDTO);
								fileS3.setOutputStream(pddComprovante.getOutstream());
								fileS3.setTipoDocumento(TipoDocumento.COMPROVANTE);
								fileS3.setPage(page);
								erros.add(fileS3);
								log.error(e.getMessage());
							}
						}
					}
				} catch (Exception e) {					
					FileS3 fileS3 = new FileS3();
					fileS3.setFileDTO(fileDTO);
					fileS3.setOutputStream(pddComprovante.getOutstream());
					fileS3.setTipoDocumento(TipoDocumento.COMPROVANTE);
					fileS3.setPage(page);
					erros.add(fileS3);
					log.error(e.getMessage());
				}
			}
			if(salvos.isEmpty() && conflict) {
				throw new MrContadorException("comprovante.imported");
			}
			if(!erros.isEmpty()) {
				s3Service.uploadErro(erros);
			}
			if(salvos.isEmpty()) {
				throw new MrContadorException("comprovante.notimported.all");
			}
			parser.callFunction(salvos, service, extratoService);
			if(conflict) {
				throw new MrContadorException("comprovante.notallimported");
			}
			log.info("Comprovantes salvos");
			return MrContadorUtil.periodo(salvos.stream().findFirst().get().getComDatapagamento());
			
	}
	
	private void genFile(FileDTO fileDTO, int page) {
		String dir = MrContadorUtil.getFolder(fileDTO.getContador(),
				String.valueOf(fileDTO.getParceiro().getId()), properties.getComprovanteFolder());
		String filename = MrContadorUtil.genFileName(TipoDocumento.COMPROVANTE,
				fileDTO.getParceiro().getId(), fileDTO.getContentType(), page);
		fileDTO.setName(filename);
		fileDTO.setBucket(properties.getBucketName());
		fileDTO.setS3Dir(dir);
		fileDTO.setS3Url(MrContadorUtil.getS3Url(dir, properties.getUrlS3(), filename));
		fileDTO.setUrl(MrContadorUtil.getS3Url(dir, properties.getUrlS3(), filename));
	}
	


	
}
