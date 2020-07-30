package br.com.mrcontador.file.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mrcontador.config.tenant.TenantContext;
import br.com.mrcontador.domain.Arquivo;
import br.com.mrcontador.domain.Conta;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.SistemaPlanoConta;
import br.com.mrcontador.file.dto.PlanoConta;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.ContaService;
import br.com.mrcontador.service.ParceiroService;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.file.S3Service;
import br.com.mrcontador.service.mapper.PlanoContaMapper;
import br.com.mrcontador.util.MrContadorUtil;
import br.com.mrcontador.web.rest.errors.CnpjAlreadyExistException;

@Service
public class PdfParserPlanoConta {

	@Autowired
	private S3Service s3Service;
	@Autowired
	private ContaService contaService;
	@Autowired
	private ParceiroService parceiroService;

	public Parceiro process(FileDTO dto, SistemaPlanoConta sistemaPlanoConta, String parceiroCnpj) {
		PDDocument document;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream first = null;
		InputStream second = null;
		try {
			dto.getInputStream().transferTo(baos);
			second = new ByteArrayInputStream(baos.toByteArray());
			first = new ByteArrayInputStream(baos.toByteArray());
			document = PDDocument.load(first);
			PdfReader reader = getIdentifier(sistemaPlanoConta);
			Splitter splitter = new Splitter();
			List<PDDocument> pages = splitter.split(document);
			PlanoConta planoConta = reader.process(pages);
			if (parceiroCnpj != null) {
				if (!MrContadorUtil.onlyNumbers(parceiroCnpj)
						.equals(MrContadorUtil.onlyNumbers(planoConta.getCnpjCliente()))) {
					throw new MrContadorException("file.notparceiro", parceiroCnpj);
				}
			}
			Parceiro parceiro = parceiroService.saveByServiceCnpj(planoConta.getCnpjCliente(), contaService);
			dto.setParceiro(parceiro);
			PlanoContaMapper mapper = new PlanoContaMapper();
			List<Conta> contas = mapper.toEntity(planoConta.getPlanoContaDetails(), parceiro, planoConta.getArquivo());
			contaService.save(contas);
			dto.setInputStream(second);
			Arquivo arquivo = s3Service.uploadPlanoConta(dto);
			contas.forEach(conta -> conta.setArquivo(arquivo));
			contaService.save(contas);
			return parceiro;
		} catch (CnpjAlreadyExistException e) {
			throw e;
		} catch (MrContadorException e) {
			throw e;
		} catch (Exception e) {
			TenantContext.setTenantSchema(SecurityUtils.DEFAULT_TENANT);
			dto.setInputStream(new ByteArrayInputStream(baos.toByteArray()));
			s3Service.uploadErro(dto);
			throw new MrContadorException("planodeconta.parse.error", e.getMessage(), e);
		} finally {
			try {
				baos.close();
				if (first != null) {
					first.close();
				}
			} catch (IOException e) {

			}
		}

	}

	private PdfReader getIdentifier(SistemaPlanoConta sistemaPlanoConta) throws IOException {
		switch (sistemaPlanoConta) {
		case DOMINIO_SISTEMAS:
			return new PdfPlanoContaDominio();

		default:
			throw new RuntimeException("pdf.systemnotimplemented");
		}
	}

}
