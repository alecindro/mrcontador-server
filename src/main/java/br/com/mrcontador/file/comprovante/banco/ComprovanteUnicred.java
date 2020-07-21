package br.com.mrcontador.file.comprovante.banco;

import java.util.List;

import com.github.difflib.algorithm.DiffException;

import br.com.mrcontador.file.comprovante.DiffValue;

public class ComprovanteUnicred extends ComprovanteBanco{


	@Override
	public List<DiffValue> parse(String comprovante) throws DiffException {
		return super.parse(comprovante, pattern);
	}
	
	private static final String pattern = "                                                          17/05/2020                                                            19:37:59\n" + 
			"\n" + 
			"                                                                               $3         FLORIANOPOLIS\n" + 
			"\n" + 
			"                                                                     OUVIDORIA           UNICRED        0800-940-0602\n" + 
			"\n" + 
			"\n" + 
			"\n" + 
			"                                                                   Comprovante             de    Pagamento          de    Titulo\n" + 
			"\n" + 
			"\n" + 
			"\n" + 
			"                                                          Conta:       $conta                                Usuário:          FERNANDO\n" + 
			"\n" + 
			"                                                          --------------------------------------------\n" + 
			"\n" + 
			"                                                          Código       de    Barras:               23791.60902            90000.053745\n" + 
			"\n" + 
			"                                                                                       28000.001108             4   81200000041173\n" + 
			"\n" + 
			"                                                          ID   do    Documento:                                                   sao     joao\n" + 
			"\n" + 
			"                                                          Instituição            Emissora:                  BANCO       BRADESCO          S.A.\n" + 
			"\n" + 
			"\n" + 
			"                                                          Beneficiário\n" + 
			"\n" + 
			"                                                          Nome     Fantasia:                              $2\n" + 
			"\n" + 
			"                                                          Razão      Social:                              FRIGORIFICO             SAO     JOAO\n" + 
			"\n" + 
			"                                                          CNPJ/CPF:                                           $cnpj_ben\n" + 
			"\n" + 
			"\n" + 
			"                                                          Sacador/Avalista\n" + 
			"\n" + 
			"                                                          Razão      Social:\n" + 
			"\n" + 
			"                                                          CNPJ/CPF:\n" + 
			"\n" + 
			"\n" + 
			"                                                          Pagador\n" + 
			"\n" + 
			"                                                          Nome:                                    $1\n" + 
			"\n" + 
			"                                                          CNPJ/CPF:                                           $cnpj\n" + 
			"\n" + 
			"\n" + 
			"                                                          Pagador        Final\n" + 
			"\n" + 
			"                                                          CNPJ/CPF:                                           15.584.243/0001-91\n" + 
			"\n" + 
			"\n" + 
			"                                                          Data     de    Vencimento:                                          $data_venc\n" + 
			"\n" + 
			"                                                          Data     do    Pagamento:                                           $pagto\n" + 
			"\n" + 
			"                                                          Valor      Nominal:                                                   R$    $valor_doc\n" + 
			"\n" + 
			"                                                          Encargos:                                                                 R$    0,00\n" + 
			"\n" + 
			"                                                          Descontos:                                                                R$    0,00\n" + 
			"\n" + 
			"                                                          Valor      Pago:                                                      R$    $valor_pag\n" + 
			"\n" + 
			"                                                          --------------------------------------------\n" + 
			"\n" + 
			"                                                          Autenticação             Documento:\n" + 
			"\n" + 
			"                                                                                                      $doc\n" + 
			"\n" + 
			"                                                          --------------------------------------------\n" + 
			"\n" + 
			"                                                                 Cooperado           Unicred,         utilize         o   Débito\n" + 
			"\n" + 
			"                                                              Automático           para      pagamento          de    suas      contas\n" + 
			"\n" + 
			"\n" + 
			"\n" + 
			"\n" + 
			"                                                                                                                                                                       Página   1 de  282\n" + 
			"\n" + 
			"";


}
