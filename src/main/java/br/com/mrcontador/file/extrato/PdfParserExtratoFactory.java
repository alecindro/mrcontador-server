package br.com.mrcontador.file.extrato;

import java.io.IOException;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.BancoCodigoBancario;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.FileException;
import br.com.mrcontador.file.extrato.banco.PdfBancoDoBrasil;
import br.com.mrcontador.file.extrato.banco.PdfBradesco;
import br.com.mrcontador.file.extrato.banco.PdfSantander;
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
			default:
				throw new MrContadorException("extrato.pdf.banknotimplemented", agenciaBancaria.getBanCodigobancario());
			}
		} catch (IOException e) {
			throw new FileException("", "", e);
		}
	}

}
