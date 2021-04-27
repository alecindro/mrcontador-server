package br.com.mrcontador.file.comprovante;

import br.com.mrcontador.domain.BancoCodigoBancario;
import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.comprovante.banco.ComprovanteBB;
import br.com.mrcontador.file.comprovante.banco.ComprovanteBradesco;
import br.com.mrcontador.file.comprovante.banco.ComprovanteCaixa;
import br.com.mrcontador.file.comprovante.banco.ComprovanteCredCrea;
import br.com.mrcontador.file.comprovante.banco.ComprovanteInter;
import br.com.mrcontador.file.comprovante.banco.ComprovanteItau;
import br.com.mrcontador.file.comprovante.banco.ComprovanteSantander;
import br.com.mrcontador.file.comprovante.banco.ComprovanteSicoob;
import br.com.mrcontador.file.comprovante.banco.ComprovanteSicredi;
import br.com.mrcontador.file.comprovante.banco.ComprovanteUnicred;
import br.com.mrcontador.util.MrContadorUtil;

public class ParserComprovanteFactory {
	
	public static ParserComprovante getParser(String codigoBancario) {
		codigoBancario = MrContadorUtil.removeZerosFromInital(codigoBancario);
		BancoCodigoBancario bcb = BancoCodigoBancario.find(codigoBancario);
		switch (bcb) {
		case BB:
			return new ComprovanteBB();
		case BRADESCO:
			return new ComprovanteBradesco();
		case CAIXA:
			return new ComprovanteCaixa();
		case CREDCREA:
			return new ComprovanteCredCrea();
		case ITAU:
			return new ComprovanteItau();
		case SANTANDER:
			return new ComprovanteSantander();
		case SICOOB:
			return new ComprovanteSicoob();
		case UNICRED:
			return new ComprovanteUnicred();
		case SICRED:
			return new ComprovanteSicredi();
		case INTER:
			return new ComprovanteInter();
		default:
			throw new MrContadorException("parsercomprovante.notfound", codigoBancario);

		}
	}

}
