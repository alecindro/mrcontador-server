package br.com.mrcontador.file.extrato;

import java.io.ByteArrayInputStream;
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
import br.com.mrcontador.domain.BancoCodigoBancario;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.extrato.banco.OfxBancoDoBrasil;
import br.com.mrcontador.file.extrato.banco.OfxBradesco;
import br.com.mrcontador.file.extrato.banco.OfxCef;
import br.com.mrcontador.file.extrato.banco.OfxCredCrea;
import br.com.mrcontador.file.extrato.banco.OfxItau;
import br.com.mrcontador.file.extrato.banco.OfxParserBanco;
import br.com.mrcontador.file.extrato.banco.OfxSantander;
import br.com.mrcontador.file.extrato.banco.OfxSicob;
import br.com.mrcontador.file.extrato.banco.OfxSicred;
import br.com.mrcontador.file.extrato.banco.OfxUniCred;
import br.com.mrcontador.file.extrato.dto.ListOfxDto;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.ExtratoService;
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.service.file.S3Service;
import br.com.mrcontador.util.MrContadorUtil;

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
		InputStream first = null;
		InputStream second = null;
		try {
			first = new ByteArrayInputStream(fileDTO.getOutputStream().toByteArray());
			second = new ByteArrayInputStream(fileDTO.getOutputStream().toByteArray());
			ListOfxDto listOfxDto = new ListOfxDto();
			listOfxDto.setFileDTO(fileDTO);
			BankingResponseMessageSet bankingResponseMessageSet = init(first);
			responses = bankingResponseMessageSet.getStatementResponses();
			if(responses.isEmpty()) {
				throw new MrContadorException("ofx.empty");
			}
			process(responses.get(0),second,listOfxDto,agenciaBancaria);
			listOfxDto.getOfxDTOs().forEach(ofxDto ->{
				extratoService.save(fileDTO,ofxDto, agenciaBancaria);
			});
			
		}catch(MrContadorException e) {
			TenantContext.setTenantSchema(SecurityUtils.DEFAULT_TENANT);
			log.error(e.getMessage(),e);
			s3Service.uploadErro(fileDTO);
			throw e;
		} catch (Exception e) {
			TenantContext.setTenantSchema(SecurityUtils.DEFAULT_TENANT);
			log.error(e.getMessage(),e);
			s3Service.uploadErro(fileDTO);
			throw new MrContadorException("ofx.process", e.getMessage());
		} finally {
			try {
				if(first!= null) {
					first.close();
				}
				if(second!= null) {
					second.close();
				}
			} catch (IOException e) {
			}
		}
	}
	
	private void process(BankStatementResponseTransaction transaction,InputStream stream, ListOfxDto listOfxDto, Agenciabancaria agenciaBancaria) throws IOException, OFXParseException {
		BankStatementResponse message = transaction.getMessage();
		BankAccountDetails bancoDetails = message.getAccount();
		OfxParserBanco parserBanco = null;
		String codigoBancario = MrContadorUtil.removeZerosFromInital(bancoDetails.getBankId());
		BancoCodigoBancario bancoCodigoBancario = BancoCodigoBancario.find(codigoBancario);
		switch (bancoCodigoBancario) {
		case BB:
			parserBanco = new OfxBancoDoBrasil();
			break;
		case BRADESCO:
			parserBanco = new OfxBradesco();
			break;
		case CAIXA:
			parserBanco = new OfxCef();
			break;
		case CREDCREA:
			parserBanco = new OfxCredCrea();
			break;
		case ITAU2:
			parserBanco = new OfxItau();
			break;
		case ITAU:
			parserBanco = new OfxItau();
			break;
		case SANTANDER:
			parserBanco = new OfxSantander();
			break;
		case SICOOB:
			parserBanco = new OfxSicob();
			break;
		case SICRED:
			parserBanco = new OfxSicred();
			break;
		case UNICRED:
		   parserBanco = new OfxUniCred();
		   break;
		default:
			throw new MrContadorException("ofx.banknotimplemented",bancoDetails.getBankId());
		}
		parserBanco.validate(bancoDetails.getBankId(), bancoDetails.getBranchId(), bancoDetails.getAccountNumber(), listOfxDto.getFileDTO().getParceiro(), agenciaBancaria);
		parserBanco.process(listOfxDto, unmarshaller,stream);
	}
	
	private BankingResponseMessageSet init(InputStream stream) throws IOException, OFXParseException {
		ResponseEnvelope envelope = unmarshaller.unmarshal(stream);
		return  (BankingResponseMessageSet) envelope
				.getMessageSet(MessageSetType.banking);
	}
	
}
