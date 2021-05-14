package br.com.mrcontador.file.planoconta;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mrcontador.config.tenant.TenantContext;
import br.com.mrcontador.domain.Conta;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.dto.PlanoConta;
import br.com.mrcontador.service.ArquivoService;
import br.com.mrcontador.service.ContaService;
import br.com.mrcontador.service.ParceiroService;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.file.S3Service;
import br.com.mrcontador.service.mapper.PlanoContaMapper;
import br.com.mrcontador.web.rest.errors.CnpjAlreadyExistException;

@Service
public class PdfParserPlanoConta {

	@Autowired
	private S3Service s3Service;
	@Autowired
	private ContaService contaService;
	@Autowired
	private ParceiroService parceiroService;
	@Autowired
	private ArquivoService arquivoService;


	public void process(FileDTO dto, SistemaPlanoConta sistemaPlanoConta) {
		PDDocument document = null;
		InputStream first = null;
		try {

			first = new ByteArrayInputStream(dto.getOutputStream().toByteArray());
			document = PDDocument.load(first);
			PlanoConta planoConta = parsePlanoConta(sistemaPlanoConta, document);
			validatePlano(dto.getParceiro(), planoConta.getCnpjCliente());
			PlanoContaMapper mapper = new PlanoContaMapper();
			s3Service.uploadPlanoConta(dto,arquivoService,TenantContext.getTenantSchema());
			List<Conta> contas = mapper.toEntity(planoConta.getPlanoContaDetails(), dto.getParceiro());
			contaService.save(contas);			
		
		} catch (CnpjAlreadyExistException e) {
			throw e;
		} catch (MrContadorException e) {
			throw e;
		} catch (Exception e) {
			s3Service.uploadErro(dto);
			throw new MrContadorException("planodeconta.parse.error", e.getMessage(), e);
		} finally {
			try {
				if(document != null) {
				document.close();
				}
				if (first != null) {
					first.close();
				}
			} catch (IOException e) {
			}
		}
	}
	
	private void validatePlano(Parceiro parceiro, String cnpj) {
		Optional<Parceiro> oParceiro = parceiroService.findByParCnpjcpf(cnpj);
		if(oParceiro.isEmpty()) {
			throw new MrContadorException("parceiro.notexists",cnpj);
		}
		
		if (!oParceiro.get().getId().equals(parceiro.getId())) {
			throw new MrContadorException("plano.notparceiro", cnpj);
		}
	}

	public void update(FileDTO dto, SistemaPlanoConta sistemaPlanoConta) {
		PDDocument document = null;
		InputStream first = null;
		try {
			first = new ByteArrayInputStream(dto.getOutputStream().toByteArray());
			document = PDDocument.load(first);
			PlanoConta planoConta = parsePlanoConta(sistemaPlanoConta, document);
			s3Service.uploadPlanoConta(dto,arquivoService,TenantContext.getTenantSchema());
			PlanoContaMapper mapper = new PlanoContaMapper();
			List<Conta> contas = mapper.toEntity(planoConta.getPlanoContaDetails(), dto.getParceiro());
			contaService.update(contas, dto.getUsuario(), dto.getParceiro());
		} catch (MrContadorException e) {
			throw e;
		} catch (Exception e) {
			s3Service.uploadErro(dto);
			throw new MrContadorException("planodeconta.parse.error", e.getMessage(), e);
		} finally {
			try {
				if (document != null) {
				document.close();
				}
				if (first != null) {
					first.close();
				}
			} catch (IOException e) {

			}
		}
	}

	private PlanoConta parsePlanoConta(SistemaPlanoConta sistemaPlanoConta, PDDocument document) throws IOException {
		PdfReader reader = getIdentifier(sistemaPlanoConta);
		Splitter splitter = new Splitter();
		List<PDDocument> pages = splitter.split(document);
		return reader.process(pages);
	}

	private PdfReader getIdentifier(SistemaPlanoConta sistemaPlanoConta) throws IOException {
		switch (sistemaPlanoConta) {
		case DOMINIO_SISTEMAS:
			return new PdfPlanoContaDominio2();

		default:
			throw new RuntimeException("pdf.systemnotimplemented");
		}
	}

}
