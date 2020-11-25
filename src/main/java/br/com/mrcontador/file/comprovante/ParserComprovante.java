package br.com.mrcontador.file.comprovante;

import java.util.List;

import com.github.difflib.algorithm.DiffException;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.ComprovanteException;
import br.com.mrcontador.service.ComprovanteService;

public interface ParserComprovante {
	
	List<Comprovante> parse(String comprovante,Agenciabancaria agenciabancaria, Parceiro parceiro) throws DiffException, ComprovanteException;
	Comprovante save(Comprovante comprovante, ComprovanteService service);
	void callFunction(List<Comprovante> comprovantes, ComprovanteService service);

}
