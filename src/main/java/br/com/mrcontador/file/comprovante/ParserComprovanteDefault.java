package br.com.mrcontador.file.comprovante;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.file.FileException;
import br.com.mrcontador.file.TipoDocumento;
import br.com.mrcontador.file.planoconta.PdfReaderPreserveSpace;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.ComprovanteService;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.dto.FileS3;
import br.com.mrcontador.service.file.S3Service;
import br.com.mrcontador.util.MrContadorUtil;

@Service
public class ParserComprovanteDefault {

	@Autowired
	private ComprovanteService service;
	@Autowired
	private S3Service s3Service;
	private int page;

	private static Logger log = LoggerFactory.getLogger(ParserComprovanteDefault.class);

	public String process(FileDTO fileDTO, Agenciabancaria agencia) {
			log.info("parse comprovantes");
			page = 0;
			List<PPDocumentDTO> textComprovantes = parseComprovante(fileDTO);
			ParserComprovante parser = ParserComprovanteFactory.getParser(agencia.getBanCodigobancario());
			List<FileS3> erros = new ArrayList<FileS3>();
			List<FileS3> files = new ArrayList<>();
			List<Comprovante> salvos = new ArrayList<Comprovante>();			
			for (PPDocumentDTO pddComprovante : textComprovantes) {
				page = page +1;
				try {
					List<Comprovante> _comprovantes = parser.parse(pddComprovante.getComprovante(), agencia, fileDTO.getParceiro());
					if (_comprovantes != null && !_comprovantes.isEmpty()) {
						_comprovantes.forEach(_comprovante ->{
							try {
							_comprovante = parser.save(_comprovante, service);
							salvos.add(_comprovante);
							files.add(create(_comprovantes, fileDTO, pddComprovante, page));
							}catch(Exception e ) {
								log.error(e.getMessage());
							}
						});
					}
				} catch (Exception e) {
					Comprovante comprovanteErro = new Comprovante();
					comprovanteErro.setAgenciabancaria(agencia);
					FileS3 fileS3 = new FileS3();
					fileS3.getComprovantes().add(comprovanteErro);
					fileS3.setFileDTO(fileDTO);
					fileS3.setOutputStream(pddComprovante.getOutstream());
					fileS3.setTipoDocumento(TipoDocumento.COMPROVANTE);
					fileS3.setPage(page);
					erros.add(fileS3);
					log.error(e.getMessage());
				}
			}
			if(salvos.isEmpty()) {
				throw new org.springframework.dao.DataIntegrityViolationException("comprovantes já importado");
			}
			s3Service.uploadComprovante(files, SecurityUtils.getCurrentTenantHeader());
			if(!erros.isEmpty()) {
				s3Service.uploadErro(erros, SecurityUtils.DEFAULT_TENANT);
			}
			log.info("Comprovantes salvos");
			return MrContadorUtil.periodo(salvos.stream().findFirst().get().getComDatapagamento());
			
	}
	
	private FileS3 create(List<Comprovante> comprovantes,FileDTO fileDTO, PPDocumentDTO pddComprovante, int page  ) {
		FileS3 fileS3 = new FileS3();
		fileS3.setComprovantes(comprovantes);
		fileS3.setFileDTO(fileDTO);
		fileS3.setOutputStream(pddComprovante.getOutstream());
		fileS3.getFileDTO().setTipoDocumento(TipoDocumento.COMPROVANTE);
		fileS3.setTipoDocumento(TipoDocumento.COMPROVANTE);
		fileS3.setPage(page);
		return fileS3;
	}
	


	private List<PPDocumentDTO> parseComprovante(FileDTO fileDTO) {
		InputStream stream = null;
		try {
			stream = new ByteArrayInputStream(fileDTO.getOutputStream().toByteArray());
			PDDocument document = PDDocument.load(stream);
			List<PPDocumentDTO> list = new ArrayList<>();
			Splitter splitter = new Splitter();
			PDFTextStripper stripper = new PdfReaderPreserveSpace();
			for (PDDocument pdDocument : splitter.split(document)) {
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				String comprovante = stripper.getText(pdDocument);
				pdDocument.save(output);
				PPDocumentDTO pddocumentDTO = new PPDocumentDTO(comprovante, output);
				list.add(pddocumentDTO);
				pdDocument.close();
			}
			document.close();
			return list;
		} catch (IOException e1) {
			throw new FileException("parsecomprovante.error", fileDTO.getOriginalFilename(), e1);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		}
	}

}
