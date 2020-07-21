package br.com.mrcontador.file.comprovante.banco;

import java.util.List;

import com.github.difflib.algorithm.DiffException;

import br.com.mrcontador.file.comprovante.DiffValue;

public class ComprovanteItau extends ComprovanteBanco{

	@Override
	public List<DiffValue> parse(String comprovante) throws DiffException {
		return super.parse(comprovante, pattern);
	}


	
	private static final String pattern = "         Comprovante                 de    pagamento               de    boleto\n" + 
			"         Dados         da    conta       debitada\n" + 
			"         Agência/conta:          $ag/$conta                          CNPJ:      $cnpj                         Empresa:        $1\n" + 
			"                   Identificação        no   meu\n" + 
			"                               comprovante:         TRANSPORTADORA                     PLIMO\n" + 
			"          Pagador   final:\n" + 
			"          Agência/Conta:         1575/0012906-7                                                                                      CPF/CNPJ:\n" + 
			"          Nome:                  MOTO     BOMBAS     F LTDA    ME                                                                    08.892.611/0001-01\n" + 
			"                                                                                  00190     00009      01719      026005      00975      906173       1  81790000006512\n" + 
			"          Beneficiário:$parceiro\n" + 
			"         .                                                                               CPF/CNPJ      do beneficiário:              Data  de  vencimento:\n" + 
			"          Razão   Social: TRANSPORTADORA            PLIMOR    LTDA                        $cnpj_ben                          $data_venc\n" + 
			"                                                                                                                                     Valor  do documento     (R$);\n" + 
			"                                                                                                                                     $valor_doc\n" + 
			"                                                                                                                                     (-)Desconto    (R$):\n" + 
			"                                                                                                                                     0,00\n" + 
			"                                                                                                                                     (+)Juros/Mora/Multa     (R$):\n" + 
			"                                                                                                                                     0,00\n" + 
			"          Pagador:                                                                       CPF/CNPJ      do pagador:                   (=) Valor  do pagamento     (R$):\n" + 
			"          MOTO        BOMBAS          FLORIANOPOLIS                LTDA      ME           08.892.611/0001-01                          $valor_pag\n" + 
			"          Sacador   /Avalista:                                                           CPF/CNPJ      do sacador:                   Data  de  pagamento:\n" + 
			"                                                                                                                                      $pagto\n" + 
			"          Autenticação   mecânica:                                                                                                   Pagamento     realizado  em  espécie:\n" + 
			"          BD2FF20B9E32CE535F10AC60EB5F02FDA92A0643                                                                                    Não\n" + 
			"         Operação        efetuada       em    28/02/2020         às   18:32:33h       via   bankline,      CTRL      29239.\n" + 
			"         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   $3:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n" + 
			"         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n" + 
			"         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).";




}
