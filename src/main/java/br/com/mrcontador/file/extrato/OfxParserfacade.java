package br.com.mrcontador.file.extrato;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.BancoCodigoBancario;
import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.erros.ExtratoException;
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
import br.com.mrcontador.service.dto.FileDTO;
import br.com.mrcontador.util.MrContadorUtil;

@Service
public class OfxParserfacade extends ExtratoFacade {

	@Autowired
	AggregateUnmarshaller<ResponseEnvelope> unmarshaller;

	private static Logger log = LoggerFactory.getLogger(OfxParserfacade.class);

	public String process(FileDTO fileDTO, Agenciabancaria agenciaBancaria) {
		List<BankStatementResponseTransaction> responses;
		try {
			ListOfxDto listOfxDto = new ListOfxDto();
			listOfxDto.setFileDTO(fileDTO);
			BankingResponseMessageSet bankingResponseMessageSet = init(fileDTO);
			responses = bankingResponseMessageSet.getStatementResponses();
			if (responses.isEmpty()) {
				throw new MrContadorException("ofx.empty");
			}
			process(responses.get(0), listOfxDto, agenciaBancaria);
		

		} catch (MrContadorException e) {
			log.error(e.getMessage(), e);
			s3Service.uploadErro(fileDTO);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			s3Service.uploadErro(fileDTO);
			throw new MrContadorException("ofx.process", e.getMessage());
		}
		return "";
	}

	private String process(BankStatementResponseTransaction transaction, ListOfxDto listOfxDto,
			Agenciabancaria agenciaBancaria) throws IOException, OFXParseException {
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
			throw new MrContadorException("ofx.banknotimplemented", bancoDetails.getBankId());
		}
		InputStream stream = null;
		try {
			stream = new ByteArrayInputStream(listOfxDto.getFileDTO().getOutputStream().toByteArray());
			parserBanco.validate(bancoDetails.getBankId(), bancoDetails.getBranchId(), bancoDetails.getAccountNumber(),
					listOfxDto.getFileDTO().getParceiro(), agenciaBancaria);
			parserBanco.process(listOfxDto, unmarshaller, stream);
			List<Extrato> extratos = new ArrayList<Extrato>();
			listOfxDto.getOfxDTOs().forEach(ofxDto -> {
				extratos.addAll(save(listOfxDto.getFileDTO(), ofxDto, agenciaBancaria));
			});
			parserBanco.callExtrato(extratoService, extratos, listOfxDto.getFileDTO().getParceiro().getId());
			Set<String> periodos = extratos.stream().map(e -> e.getPeriodo()).collect(Collectors.toSet());
			return periodos.stream().findFirst().get();
		} catch (Exception e) {
			throw new ExtratoException("extrato.parser.error", listOfxDto.getFileDTO().getOriginalFilename());
		} finally {
			stream.close();
		}
	}

	private BankingResponseMessageSet init(FileDTO fileDTO) {
		InputStream stream = null;
		try {
			stream = new ByteArrayInputStream(fileDTO.getOutputStream().toByteArray());
			ResponseEnvelope envelope = unmarshaller.unmarshal(stream);
			return (BankingResponseMessageSet) envelope.getMessageSet(MessageSetType.banking);
		} catch (IOException | OFXParseException e) {
			throw new ExtratoException("extrato.parser.error", fileDTO.getOriginalFilename());
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
