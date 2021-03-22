package br.com.mrcontador.file.extrato;

import java.io.IOException;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.BancoCodigoBancario;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.FileException;
import br.com.mrcontador.file.extrato.banco.PdfBancoDoBrasil;
import br.com.mrcontador.file.extrato.banco.PdfBradesco;
import br.com.mrcontador.file.extrato.banco.PdfCef;
import br.com.mrcontador.file.extrato.banco.PdfCredicrea;
import br.com.mrcontador.file.extrato.banco.PdfItau;
import br.com.mrcontador.file.extrato.banco.PdfSafra;
import br.com.mrcontador.file.extrato.banco.PdfSantander;
import br.com.mrcontador.file.extrato.banco.PdfSicredi;
import br.com.mrcontador.file.extrato.banco.PdfUnicred;
import br.com.mrcontador.util.MrContadorUtil;

public class PdfParserExtratoFactory {

	public static PdfParserExtrato getParser(Agenciabancaria agenciaBancaria) {
		String codigoBancario = MrContadorUtil.removeZerosFromInital(agenciaBancaria.getBanCodigobancario());
		BancoCodigoBancario bancoCodigoBancario = BancoCodigoBancario.find(codigoBancario);
		try {
			switch (bancoCodigoBancario) {
			case BB:
				return new PdfBancoDoBrasil();
			case BRADESCO:
				return new PdfBradesco();
			case SANTANDER:
				return new PdfSantander();
			case UNICRED:
				return new PdfUnicred();
			case CAIXA:
				return new PdfCef();
			case CREDCREA:
				return new PdfCredicrea();
			case ITAU:
				return new PdfItau();
			case SAFRA:
				return new PdfSafra();
			case SICRED:
				return new PdfSicredi();
			default:
				throw new MrContadorException("extrato.pdf.banknotimplemented", agenciaBancaria.getBanCodigobancario());
			}
		} catch (IOException e) {
			throw new FileException("", "", e);
		}
	}

}
