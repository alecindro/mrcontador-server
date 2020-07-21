package br.com.mrcontador.file.comprovante;

import java.util.List;

import com.github.difflib.algorithm.DiffException;

public interface ParserComprovante {
	
	abstract List<DiffValue> parse(String comprovante) throws DiffException;

}
