package br.com.mrcontador.file.ofx;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webcohesion.ofx4j.domain.data.MessageSetType;
import com.webcohesion.ofx4j.domain.data.ResponseEnvelope;
import com.webcohesion.ofx4j.domain.data.banking.BankAccountDetails;
import com.webcohesion.ofx4j.domain.data.banking.BankStatementResponse;
import com.webcohesion.ofx4j.domain.data.banking.BankStatementResponseTransaction;
import com.webcohesion.ofx4j.domain.data.banking.BankingResponseMessageSet;
import com.webcohesion.ofx4j.io.AggregateUnmarshaller;
import com.webcohesion.ofx4j.io.OFXParseException;

import br.com.mrcontador.config.tenant.TenantContext;
import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.ofx.banco.OfxBancoDoBrasil;
import br.com.mrcontador.file.ofx.banco.OfxBradesco;
import br.com.mrcontador.file.ofx.banco.OfxCef;
import br.com.mrcontador.file.ofx.banco.OfxCredCrea;
import br.com.mrcontador.file.ofx.banco.OfxItau;
import br.com.mrcontador.file.ofx.banco.OfxParserBanco;
import br.com.mrcontador.file.ofx.banco.OfxSantander;
import br.com.mrcontador.file.ofx.banco.OfxSicob;
import br.com.mrcontador.file.ofx.banco.OfxSicred;
import br.com.mrcontador.file.ofx.dto.ListOfxDto;
import br.com.mrcontador.file.ofx.dto.OfxDTO;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.ExtratoService;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.file.S3Service;

@Service
public class OfxParserDefault{
	
	@Autowired
	AggregateUnmarshaller<ResponseEnvelope> unmarshaller;
	@Autowired
	S3Service s3Service;
	@Autowired
	ExtratoService extratoService;
	
	private static Logger log = LoggerFactory.getLogger(OfxParserDefault.class);

	public void process(FileDTO fileDTO,  Agenciabancaria agenciaBancaria) {
		List<BankStatementResponseTransaction> responses;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();	
		InputStream first = null;
		InputStream second = null;
		InputStream third = null;
		try {
			fileDTO.getInputStream().transferTo(baos);
			third = new ByteArrayInputStream(baos.toByteArray());
			second = new ByteArrayInputStream(baos.toByteArray());
			first = new ByteArrayInputStream(baos.toByteArray());
			ListOfxDto listOfxDto = new ListOfxDto();
			listOfxDto.setFileDTO(fileDTO);
			BankingResponseMessageSet bankingResponseMessageSet = init(first);
			responses = bankingResponseMessageSet.getStatementResponses();
			for (BankStatementResponseTransaction response : responses) {
				OfxDTO dto = process(response);
				listOfxDto.add(dto);
			}
			fileDTO.setInputStream(second);
			extratoService.save(listOfxDto, agenciaBancaria);
		}catch(MrContadorException e) {
			TenantContext.setTenantSchema(SecurityUtils.DEFAULT_TENANT);
			log.error(e.getMessage(),e);
			fileDTO.setInputStream(third);
			s3Service.uploadErro(fileDTO);
			throw e;
		} catch (Exception e) {
			TenantContext.setTenantSchema(SecurityUtils.DEFAULT_TENANT);
			log.error(e.getMessage(),e);
			fileDTO.setInputStream(third);
			s3Service.uploadErro(fileDTO);
			throw new MrContadorException("ofx.process", e.getMessage());
		} finally {
			try {
				baos.close();
				if(first!= null) {
					first.close();
				}
				if(third!= null) {
					third.close();
				}
			} catch (IOException e) {
			}
		}
	}
	
	private OfxDTO process(BankStatementResponseTransaction transaction) {
		BankStatementResponse message = transaction.getMessage();
		BankAccountDetails bancoDetails = message.getAccount();
		OfxParserBanco parserBanco = null;
		switch (bancoDetails.getBankId()) {
		case "001":
			parserBanco = new OfxBancoDoBrasil();
			break;
		case "0237":
			parserBanco = new OfxBradesco();
			break;
		case "0104":
			parserBanco = new OfxCef();
			break;
		case "085":
			parserBanco = new OfxCredCrea();
			break;
		case "999999999":
			parserBanco = new OfxItau();
			break;
		case "033":
			parserBanco = new OfxSantander();
			break;
		case "756":
			parserBanco = new OfxSicob();
			break;
		case "748":
			parserBanco = new OfxSicred();
			break;

		default:
			throw new RuntimeException(bancoDetails.getBankId());
		}
		return parserBanco.process(message);
	}
	
	private BankingResponseMessageSet init(InputStream stream) throws IOException, OFXParseException {
		ResponseEnvelope envelope = unmarshaller.unmarshal(stream);
		return  (BankingResponseMessageSet) envelope
				.getMessageSet(MessageSetType.banking);
	}
	
}
