package br.com.mrcontador.file.comprovante.banco;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.difflib.algorithm.DiffException;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.ComprovanteException;
import br.com.mrcontador.file.comprovante.DiffValue;

public class ComprovanteUnicred extends ComprovanteBanco {

	@Override
	public List<Comprovante> parse(String comprovante, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws DiffException, ComprovanteException {
		String[] _lines = comprovante.split("\\r?\\n");
		if (_lines == null || _lines.length < 7) {
			throw new ComprovanteException("Comprovante está sem informação");
		}
		String line = StringUtils.normalizeSpace(_lines[8].trim());
		if (line.equals("Comprovante de Pagamento de Titulo")) {
			return parseTitulo(_lines, agenciabancaria, parceiro);
		}
		if (line.equals("Comprovante de Pagamento de Convênio")) {
			return parsePagtoConvenio(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[4]).contains("ARRECADACAO DE DARF")) {
			return parseDarf(_lines, agenciabancaria, parceiro);
		}
		if (line.contains("Comprovante de Pagamento de Tributos")) {
			return parsePagtoTributos(_lines, agenciabancaria, parceiro);
		}
		if (StringUtils.normalizeSpace(_lines[4]).contains("ARRECADACAO DE GPS")) {
			return parseGPS(_lines, agenciabancaria, parceiro);
		}

		throw new ComprovanteException("Comprovante não identificado");
	}

	private List<Comprovante> parseTitulo(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Conta:")) {
				String value = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "Conta:").trim(), "Usuário:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Razão Social:")) {
				String value = StringUtils.substringAfter(line, "Razão Social:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(FORNECEDOR);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CNPJ/CPF:")) {
				if (lines[i - 6].contains(" Beneficiário")) {
					String value = StringUtils.substringAfter(line, "CNPJ/CPF:").trim();
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(CNPJ_BEN);
					diffValue.setNewValue(value);
					diffValue.setLine(i);
					list.add(diffValue);
				}
			}
			if (line.contains("CNPJ/CPF:")) {
				if (lines[i - 4].contains(" Pagador")) {
					String value = StringUtils.substringAfter(line, "CNPJ/CPF:").trim();
					DiffValue diffValue = new DiffValue();
					diffValue.setOldValue(CNPJ_BEN);
					diffValue.setNewValue(value);
					diffValue.setLine(i);
					list.add(diffValue);
				}
			}
			if (line.contains("Data de Vencimento:")) {
				String value = StringUtils.substringAfter(line, "Data de Vencimento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Data do Pagamento:")) {
				String value = StringUtils.substringAfter(line, "Data do Pagamento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Valor Nominal:")) {
				String value = StringUtils.substringAfter(line, "Valor Nominal:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Valor Pago:")) {
				String value = StringUtils.substringAfter(line, "Valor Pago:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("ID do Documento:")) {
				String value = StringUtils.substringAfter(line, "ID do Documento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("Comprovante de Pagamento de Titulo");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}

	private List<Comprovante> parseDarf(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Data do Pagamento:")) {
				String value = StringUtils
						.substringAfterLast(StringUtils.substringAfter(line, "Data do Pagamento:"), ".").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("CPF ou CNPJ")) {
				String value = StringUtils.substringAfterLast(StringUtils.substringAfter(line, "CPF ou CNPJ"), ".")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CNPJ_PAG);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data Vencimento")) {
				String value = StringUtils.substringAfterLast(StringUtils.substringAfter(line, "Data Vencimento"), ".")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor Principal")) {
				String value = StringUtils.substringAfterLast(StringUtils.substringAfter(line, "Valor Principal"), ".")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor Total:")) {
				String value = StringUtils.substringAfterLast(StringUtils.substringAfter(line, "Valor Total:"), ".")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("N. DA AUTENTICACAO:")) {
				String value = StringUtils
						.substringAfterLast(StringUtils.substringAfter(line, "N. DA AUTENTICACAO:"), ".").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("ARRECADACAO DE DARF");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}

	private List<Comprovante> parsePagtoConvenio(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Conta:")) {
				String value = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "Conta:").trim(), "Usuário:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data do Pagamento:")) {
				String value = StringUtils.substringAfter(line, "Data do Pagamento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Valor Nominal:")) {
				String value = StringUtils.substringAfter(line, "Valor Nominal:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Valor Total:")) {
				String value = StringUtils.substringAfter(line, "Valor Total:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Id. do Documento:")) {
				String value = StringUtils.substringAfter(line, "Id. do Documento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("Comprovante de Pagamento de Convênio");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}
	
	private List<Comprovante> parsePagtoTributos(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Conta:")) {
				String value = StringUtils
						.substringBefore(StringUtils.substringAfter(line, "Conta:").trim(), "Usuário:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(CONTA);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data do Pagamento:")) {
				String value = StringUtils.substringAfter(line, "Data do Pagamento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Valor Nominal:")) {
				String value = StringUtils.substringAfter(line, "Valor Nominal:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);

			}
			if (line.contains("Valor Total:")) {
				String value = StringUtils.substringAfter(line, "Valor Total:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Id. do Documento:")) {
				String value = StringUtils.substringAfter(line, "Id. do Documento:").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			i = i + 1;
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("Comprovante de Pagamento de Tributos");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}
	
	private List<Comprovante> parseGPS(String[] lines, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws ComprovanteException {
		List<DiffValue> list = new ArrayList<DiffValue>();
		int i = 0;
		for (String line : lines) {
			line = StringUtils.normalizeSpace(line.trim());
			if (line.contains("Data do Pagamento:")) {
				String value = StringUtils
						.substringAfterLast(StringUtils.substringAfter(line, "Data do Pagamento:"), ".").trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Data de Vencimento:")) {
				String value = StringUtils.substringAfterLast(StringUtils.substringAfter(line, "Data de Vencimento:"), ".")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DATA_VCTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Identificador:")) {
				String value = StringUtils.substringAfterLast(StringUtils.substringAfter(line, "Identificador:"), ".")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(DOCUMENTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor INSS:")) {
				String value = StringUtils.substringAfterLast(StringUtils.substringAfter(line, "Valor INSS:"), ".")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_DOC);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
			if (line.contains("Valor Total:")) {
				String value = StringUtils.substringAfterLast(StringUtils.substringAfter(line, "Valor Total:"), ".")
						.trim();
				DiffValue diffValue = new DiffValue();
				diffValue.setOldValue(VALOR_PGTO);
				diffValue.setNewValue(value);
				diffValue.setLine(i);
				list.add(diffValue);
			}
		}
		DiffValue diffValue = new DiffValue();
		diffValue.setOldValue(OBS);
		diffValue.setNewValue("ARRECADACAO DE GPS");
		diffValue.setLine(i);
		list.add(diffValue);
		List<Comprovante> comprovantes = new ArrayList<>();
		comprovantes.add(toEntity(list, agenciabancaria, parceiro));
		return comprovantes;
	}

	private static final String pattern = "                                                          17/05/2020                                                            19:37:59\n"
			+ "\n"
			+ "                                                                               $3         FLORIANOPOLIS\n"
			+ "\n"
			+ "                                                                     OUVIDORIA           UNICRED        0800-940-0602\n"
			+ "\n" + "\n" + "\n"
			+ "                                                                   Comprovante             de    Pagamento          de    Titulo\n"
			+ "\n" + "\n" + "\n"
			+ "                                                          Conta:       $conta                                Usuário:          FERNANDO\n"
			+ "\n"
			+ "                                                          --------------------------------------------\n"
			+ "\n"
			+ "                                                          Código       de    Barras:               23791.60902            90000.053745\n"
			+ "\n"
			+ "                                                                                       28000.001108             4   81200000041173\n"
			+ "\n"
			+ "                                                          ID   do    Documento:                                                   sao     joao\n"
			+ "\n"
			+ "                                                          Instituição            Emissora:                  BANCO       BRADESCO          S.A.\n"
			+ "\n" + "\n" + "                                                          Beneficiário\n" + "\n"
			+ "                                                          Nome     Fantasia:                              $2\n"
			+ "\n"
			+ "                                                          Razão      Social:                              FRIGORIFICO             SAO     JOAO\n"
			+ "\n"
			+ "                                                          CNPJ/CPF:                                           $cnpj_ben\n"
			+ "\n" + "\n" + "                                                          Sacador/Avalista\n" + "\n"
			+ "                                                          Razão      Social:\n" + "\n"
			+ "                                                          CNPJ/CPF:\n" + "\n" + "\n"
			+ "                                                          Pagador\n" + "\n"
			+ "                                                          Nome:                                    $1\n"
			+ "\n"
			+ "                                                          CNPJ/CPF:                                           $cnpj\n"
			+ "\n" + "\n" + "                                                          Pagador        Final\n" + "\n"
			+ "                                                          CNPJ/CPF:                                           15.584.243/0001-91\n"
			+ "\n" + "\n"
			+ "                                                          Data     de    Vencimento:                                          $data_venc\n"
			+ "\n"
			+ "                                                          Data     do    Pagamento:                                           $pagto\n"
			+ "\n"
			+ "                                                          Valor      Nominal:                                                   R$    $valor_doc\n"
			+ "\n"
			+ "                                                          Encargos:                                                                 R$    0,00\n"
			+ "\n"
			+ "                                                          Descontos:                                                                R$    0,00\n"
			+ "\n"
			+ "                                                          Valor      Pago:                                                      R$    $valor_pag\n"
			+ "\n"
			+ "                                                          --------------------------------------------\n"
			+ "\n" + "                                                          Autenticação             Documento:\n"
			+ "\n"
			+ "                                                                                                      $doc\n"
			+ "\n"
			+ "                                                          --------------------------------------------\n"
			+ "\n"
			+ "                                                                 Cooperado           Unicred,         utilize         o   Débito\n"
			+ "\n"
			+ "                                                              Automático           para      pagamento          de    suas      contas\n"
			+ "\n" + "\n" + "\n" + "\n"
			+ "                                                                                                                                                                       Página   1 de  282\n"
			+ "\n" + "";

}
