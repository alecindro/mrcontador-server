package br.com.mrcontador.file.comprovante.banco;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.difflib.algorithm.DiffException;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.MrContadorException;

public class ComprovanteItau extends ComprovanteBanco {

	@Override
	public List<Comprovante> parse(String comprovante, Agenciabancaria agenciabancaria, Parceiro parceiro)
			throws DiffException {
		 List<Comprovante> comprovantes = new ArrayList<Comprovante>();
		 String pattern = getPattern(comprovante);
		 if(pattern != null) {
			 comprovantes =  super.parse(comprovante, pattern, agenciabancaria, parceiro);
		 }
		 return comprovantes;
	}

	private String getPattern(String comprovante) {
		String[] _lines = comprovante.split("\\r?\\n");
		if (_lines == null || _lines.length < 6) {
			return null;
		}
		if (StringUtils.normalizeSpace(_lines[0].trim()).equals("Comprovante de pagamento de boleto") &&
				StringUtils.normalizeSpace(_lines[5].trim()).equals("Identificação no meu")) {
			return BOLETO2;
		}
		
		if (StringUtils.normalizeSpace(_lines[0].trim()).equals("Comprovante de pagamento de boleto") &&
				StringUtils.normalizeSpace(_lines[3].trim()).equals("Identificação no meu") && _lines.length == 29) {
			return BOLETO5;
		}
		if (StringUtils.normalizeSpace(_lines[0].trim()).equals("Comprovante de pagamento de boleto") &&
				StringUtils.normalizeSpace(_lines[3].trim()).equals("Identificação no meu") && _lines.length == 28) {
			return BOLETO4;
		}
		if (StringUtils.normalizeSpace(_lines[0].trim()).equals("Comprovante de pagamento de boleto") &&
				StringUtils.normalizeSpace(_lines[3].trim()).equals("Identificação no meu")) {
			return BOLETO3;
		}
		if (StringUtils.normalizeSpace(_lines[0].trim()).equals("Comprovante de pagamento de boleto")) {
			return BOLETO;
		}
		if (StringUtils.normalizeSpace(_lines[0].trim())
				.equals("Banco Itaú - Comprovante de Pagamento de concessionárias")) {
			return CONCESSIONARIA;
		}
		if (StringUtils.normalizeSpace(_lines[0].trim()).equals("Comprovante de Pagamento SIMPLES NACIONAL")) {
			return SIMPLES_NACIONAL;
		}
		if (StringUtils.normalizeSpace(_lines[0].trim()).equals("Comprovante de Pagamento de GPS - Guia da Previdência Social") &&
				_lines.length ==22) {
			return GPS2;
		}
		if (StringUtils.normalizeSpace(_lines[0].trim()).equals("Comprovante de Pagamento de GPS-Guia da Previdência Social")) {
			return GPS;
		}
		if (StringUtils.normalizeSpace(_lines[0].trim()).equals("Comprovante de pagamento DARF")) {
			return DARF;
		}
		if (StringUtils.normalizeSpace(_lines[0].trim()).equals("Banco Itaú - Comprovante de Transferência")) {
			return TRANSFERENCIA;
		}
		if(StringUtils.normalizeSpace(_lines[0].trim()).equals("Banco Itaú - Comprovante de Pagamento com código de barras"))
		{
			return PAGTO_COD_BARRAS;
		}
		if (StringUtils.normalizeSpace(_lines[0].trim()).equals("Banco Itaú - Comprovante de Pagamento") &&
				StringUtils.normalizeSpace(_lines[1].trim()).equals("FGTS- GRRF")) {
			return GRRF;
		}
		if (StringUtils.normalizeSpace(_lines[0].trim()).equals("Banco Itaú - Comprovante de Pagamento") &&
				StringUtils.normalizeSpace(_lines[1].trim()).equals("GRF - Guia de Recolhimento do FGTS")) {
			return GRF;
		}
		if (StringUtils.normalizeSpace(_lines[0].trim()).equals("Banco Itaú - Comprovante de Pagamento") &&
				StringUtils.normalizeSpace(_lines[1].trim()).equals("DOC C – outra titularidade")) {
			return DOC_C;
		}
		if (StringUtils.normalizeSpace(_lines[0].trim()).equals("Banco Itaú - Comprovante de Agendamento")) {
			return null;
		}
	
		throw new MrContadorException("comprovante.patternnotfound", StringUtils.normalizeSpace(_lines[0].trim()));
	}

	private static final String BOLETO2 = "         Comprovante                 de    pagamento               de    boleto\n"
			+ "         Dados         da    conta       debitada\n"
			+ "         Agência/conta:          $ag/$conta                          CNPJ:      08.892.611/0001-01                         Empresa:\n"
			+ "                                                                                                                                             MOTO        BOMBAS         F   LTDA\n"
			+ "                                                                                                                                             ME\n"
			+ "                   Identificação        no   meu\n" + "                               comprovante:\n"
			+ "                                                    jacuzzi\n" + "          Pagador   final:\n"
			+ "          Agência/Conta:         1575/0012906-7                                                                                      CPF/CNPJ:\n"
			+ "          Nome:                  MOTO     BOMBAS     F LTDA    ME                                                                    08.892.611/0001-01\n"
			+ "                                                                                  34191     12879      06508      040273      81290      020007       6  82120000055486\n"
			+ "          Beneficiário:  $2                                                              CPF/CNPJ      do beneficiário:              Data  de  vencimento:\n"
			+ "          Razão   Social: JACUZZI    DO   BRASIL    IND  COM.LTD                          $cnpj_ben                                   $data_venc\n"
			+ "                                                                                                                                     Valor  do documento     (R$);\n"
			+ "                                                                                                                                     $valor_doc\n"
			+ "                                                                                                                                     (-)Desconto    (R$):\n"
			+ "                                                                                                                                     0,00\n"
			+ "                                                                                                                                     (+)Juros/Mora/Multa     (R$):\n"
			+ "                                                                                                                                     0,00\n"
			+ "          Pagador:                                                                       CPF/CNPJ      do pagador:                   (=) Valor  do pagamento     (R$):\n"
			+ "          MOTO        BOMBAS          FLORIANOPOLIS                LTDA                   $cnpj                                       $valor_pag\n"
			+ "          Sacador   /Avalista:                                                           CPF/CNPJ      do sacador:                   Data  de  pagamento:\n"
			+ "                                                                                                                                      $pagto\n"
			+ "          Autenticação   mecânica:                                                                                                   Pagamento     realizado  em  espécie:\n"
			+ "          6F1B1B69BA2ACF435AA022D7C3D368083E56B6B2                                                                                    Não\n"
			+ "         Operação        efetuada       em    01/04/2020         às   19:31:09h       via   bankline,      CTRL      $doc.\n"
			+ "         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n"
			+ "         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n"
			+ "         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).\n"
			+ "                                                                                                                                                                                                 14\n"
			+ "\n";

	private static final String CONCESSIONARIA = "                                   Banco         Itaú    -  Comprovante                 de    Pagamento               de    concessionárias\n"
			+ "                                                                            $2                                  \n"
			+ "               Identificação       no   extrato:    tim\n"
			+ "         Dados      da   conta    debitada:\n"
			+ "                                         Nome:      MOTO        BOMBAS          F  LTDA      ME\n"
			+ "                                      Agência:      $ag               Conta:     $conta \n"
			+ "         Dados      do   pagamento:\n"
			+ "                        Código      de   barras:    846900000064             212401090114            004224010621            101392412850\n"
			+ "                   Valor     do  documento:         $valor_doc  \n"
			+ "         Operação        efetuada       em    $pagto             às   17:59:31h       via   EMPRESA           PLUS,      CTRL      $doc           .\n"
			+ "         Autenticação:\n" + "         9C04474D6B9FBD00673E32966BB55612D199EEB2\n"
			+ "         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n"
			+ "         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n"
			+ "         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).\n"
			+ "                                                                                                                                          ";

	private static final String SIMPLES_NACIONAL = "                                                        Comprovante           de  Pagamento          SIMPLES         NACIONAL\n"
			+ "                                                            Agente      arrecadador:         CNC:341        Banco      Itaú   S/A\n"
			+ "                                      Código      de   barras:     85810000011           66670328201            21071720115           79301010700\n"
			+ "                                  Data     do   pagamento:         $pagto    \n"
			+ "                              Número       do   documento:         $doc                 \n"
			+ "                                                  Valor     total  R$   $valor_pag\n"
			+ "         Autenticação:         01201594031006373900\n"
			+ "         Operação        efetuada       via  EMPRESA           PLUS,      CTRL       202004292063739.\n"
			+ "         Autenticação         Digital    Itaú:   B8867C193AA26337AB2E7B39E02F1C5E916D05AC\n"
			+ "         Dados      da   conta    debitada:\n"
			+ "                                          Agência/Conta:           $ag      $conta \n"
			+ "                                    Nome       da   empresa:       MOTO       BOMBAS          F  LTDA      ME\n"
			+ "          - Pagamento          efetuado      em    sábado,       domingo       ou   feriado,     será   quitado      no   próximo      dia   útil.\n"
			+ "         - O   cliente    assume       total   responsabilidade           por   eventuais       danos     decorrentes         de   inexatidão       ou  insuficiência        nas\n"
			+ "         informações         por   ele   inseridas.\n"
			+ "         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n"
			+ "         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n"
			+ "         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).\n"
			+ "                                                                                                                                                                                                  9\n"
			+ "\n";

	private static final String GPS = "                                             Comprovante           de   Pagamento          de  GPS-Guia         da   Previdência        Social\n"
			+ "                    Agente      arrecadador:\n" + "                                     CNC:341\n"
			+ "                                                    Banco      Itaú   S/A\n"
			+ "                    Data    do   pagamento:         $pagto   \n"
			+ "                              Competência:          03/2020\n"
			+ "                                Identificador:      $doc         \n"
			+ "                Código      de   pagamento:         2003\n"
			+ "                             Valor    do   INSS:    R$    $valor_doc\n"
			+ "                Valor    outras     entidades:      R$    0,00\n"
			+ "              Valor    atual.   mon/jur/mul:        R$    54,50\n"
			+ "                                   Valor    total:  R$    $valor_pag\n"
			+ "         Autenticação:         2044\n"
			+ "               Modelo      aprovado        pela   SRF-ADE          conjunto      CORAT/COTEC               nº001,     de   2006\n"
			+ "         Identificação        do  extrato:     GPS     03/2020\n"
			+ "         Nome      do   contribuinte:       MOTO        BOMBAS         FLORIANOPOLIS                 LTDA\n"
			+ "         Dados      da   conta    debitada:\n"
			+ "                                      Agência:      $ag            Conta:     $conta \n"
			+ "         Pagamento          efetuado      via   EMPRESA           PLUS,      CTRL      202004303852044\n"
			+ "         Autenticação         Digital    Itaú:   7BE842E05C2B40E092AFD79650D596941A1A9695\n"
			+ "         - As   informações         fornecidas       para    o  pagamento          são   de   inteira    responsabilidade           do   cliente.    Pagamentos\n"
			+ "         e/ou    dados     fornecidos       indevidamente           deverão       ser  regularizados         diretamente         com    a  delegacia       da   Receita\n"
			+ "         Federal.      Pagamentos          efetuados        em   sábado,       domingo       ou   feriado     terão    a  quitação      no   próximo      dia   útil\n"
			+ "         seguinte.\n"
			+ "         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n"
			+ "         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n"
			+ "         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).\n";

	private static final String GPS2 = "                                            Comprovante           de   Pagamento          de  GPS      - Guia    da   Previdência        Social\n" + 
			"                                                            Agente      arrecadador:         CNC:341        Banco      Itaú   S/A\n" + 
			"                                      Código      de   barras:     85800000006           56320270430            81013499740           03962020018\n" + 
			"                                  Data     do   pagamento:         $pagto\n" + 
			"                                             Competência:          01/2020\n" + 
			"                                              Identificador:       $doc          \n" + 
			"                               Código      de   pagamento:         4308\n" + 
			"                                                  Valor     total  R$   $valor_pag\n" + 
			"         Autenticação:         2853\n" + 
			"         MODELO          APROVADO             PELO      SRF-ADE         CONJUNTO             CORAT/COTEC               Nº001,      DE    2006\n" + 
			"         Operação        efetuada       via  EMPRESA           PLUS,      CTRL       202001316822853.\n" + 
			"         Autenticação         Digital    Itaú:   F73302E5BD14EDA2AFD51CCF89DB5F421ACA9F3C\n" + 
			"         Dados      da   conta    debitada:\n" + 
			"                                          Agência/Conta:           $ag      $conta \n" + 
			"                                    Nome       da   empresa:       MOTO       BOMBAS          F  LTDA      ME\n" + 
			"          - Pagamento          efetuado      em    sábado,       domingo       ou   feriado,     será   quitado      no   próximo      dia   útil.\n" + 
			"         - O   cliente    assume       total   responsabilidade           por   eventuais       danos     decorrentes         de   inexatidão       ou  insuficiência        nas\n" + 
			"         informações         por   ele   inseridas.\n" + 
			"         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n" + 
			"         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n" + 
			"         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).";
	
	private static final String DARF = "                                                                    Comprovante           de   pagamento         DARF\n"
			+ "\n"
			+ "         ___________________________________________________________________________________________________________\n"
			+ "                   Agente      arrecadador:         CNC:341        Banco      Itaú   S/A\n"
			+ "                                  Data     do   pagamento:         $pagto\n"
			+ "                                 Período      de   apuração:       31/01/2020\n"
			+ "                         Número       do   CPF     ou   CNPJ:      08892611000101\n"
			+ "                                      Código      da   receita:    $2\n"
			+ "                               Número        de   referência:      00000000000000000\n"
			+ "                                  Data    do   vencimento:         $data_venc\n"
			+ "                                           Valor    principal:     R$   $valor_doc\n"
			+ "                                           Valor    da   multa:    R$   0,00\n"
			+ "                           Valor    dos   juros/encargos:          R$   0,00\n"
			+ "                                                 Valor     total:  R$   $valor_pag\n" + "\n"
			+ "         ___________________________________________________________________________________________________________\n"
			+ "\n"
			+ "         ___________________________________________________________________________________________________________\n"
			+ "                 MODELO           APROVADO            PELA      SRF-ADE          CONJUNTO            CORAT/COTEC               Nº   001,    DE    2006\n"
			+ "\n"
			+ "         ___________________________________________________________________________________________________________\n"
			+ "                 Operação         efetuada      via   Itaú   Empresas        na   internet.     CTRL:      $doc\n"
			+ "                 Autenticação          Digital   Itaú:   2A79E1F8A13BA565C393942072377E6A2EFC1BE3\n"
			+ "\n"
			+ "         ___________________________________________________________________________________________________________\n"
			+ "                 Nome       do   contribuinte:       MOTOBOMBAS                FLORIANOPOLIS                LTDA\n"
			+ "\n"
			+ "         ___________________________________________________________________________________________________________\n"
			+ "                 Dados      da   conta     debitada:\n"
			+ "                 Agência/Conta:           $ag      $conta     - 7\n"
			+ "                 CNPJ:       08892611000101\n"
			+ "         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n"
			+ "         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n"
			+ "         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).\n";

	private static final String BOLETO = "         Comprovante                 de    pagamento               de    boleto\n" + 
			"         Dados         da    conta       debitada\n" + 
			"         Agência/conta:          $ag /$conta                           CNPJ:      $cnpj                                      Empresa:        MOTO        BOMBAS         F   LTDAME\n" + 
			"                                                                                  34191     09511      38587      700576      94898      400000       1  81480000034788\n" + 
			"          Beneficiário:                                                                                                              Data  de  vencimento:\n" + 
			"          MULTINACIONAL                 DIST    M   C   LTDA                                                                          $data_venc\n" + 
			"                                                                                                                                     Valor  do boleto  (R$);\n" + 
			"                                                                                                                                     $valor_doc\n" + 
			"                                                                                                                                     (-)Desconto    (R$):\n" + 
			"                                                                                                                                     0,00\n" + 
			"                                                                                                                                     (+)Mora/Multa    (R$):\n" + 
			"                                                                                                                                     0,00\n" + 
			"                                                                                                                                     (=) Valor  do pagamento     (R$):\n" + 
			"                                                                                                                                     $valor_pag\n" + 
			"                                                                                                                                     Data  de  pagamento:\n" + 
			"                                                                                                                                      $pagto\n" + 
			"          Autenticação   mecânica:\n" + 
			"          027EB3C4C4B1767BCA1C29718E823BB171B4FDB0\n" + 
			"         Operação        efetuada       em    28/01/2020         via   ,CTRL       $doc           .\n" + 
			"         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n" + 
			"         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n" + 
			"         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).";
	
	private static final String TRANSFERENCIA= "                                                    Banco         Itaú     - Comprovante                 de     Transferência\n" + 
			"                                                          de    conta        corrente         para      conta       corrente\n" + 
			"               Identificação       no   extrato:    TBI    5821.05215-5              C/C\n" + 
			"         Dados      da   conta    debitada:\n" + 
			"                      Nome      da   empresa:       MOTO        BOMBAS          F  LTDA      ME\n" + 
			"                                      Agência:      $ag                               Conta     corrente:     $conta     - 7\n" + 
			"         Dados      da   conta    creditada:\n" + 
			"                                         Nome:      $2                                              \n" + 
			"                                      Agência:      5821                              Conta     corrente:     05215      - 5\n" + 
			"                                           Valor:   R$    $valor_pag\n" + 
			"         Transferência         efetuada       em    $pagto             às   18:32:24      via   , CTRL      $doc     .\n" + 
			"         Autenticação:\n" + 
			"         8FA50B7BDEADECCD44C9B0B24BEB6BB1C2307B19\n" + 
			"         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n" + 
			"         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n" + 
			"         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).\n";
	
	private static final String BOLETO3 = "         Comprovante                 de    pagamento               de    boleto\n" + 
			"         Dados         da    conta       debitada\n" + 
			"         Agência/conta:          $ag /$conta                           CNPJ:      08.892.611/0001-01                         Empresa:        MOTO        BOMBAS         F   LTDAME\n" + 
			"                   Identificação        no   meu\n" + 
			"                               comprovante:         casas     da   agua\n" + 
			"          Pagador   final:\n" + 
			"          Agência/Conta:         1575/0012906-7                                                                                      CPF/CNPJ:\n" + 
			"          Nome:                  MOTO     BOMBAS     F LTDA    ME                                                                    08.892.611/0001-01\n" + 
			"                                                                                  00190     00009      03199      366000      00039      603170       4  81450000016920\n" + 
			"          Beneficiário:CASAS     DA  AGUA    MATERIAIS      PARA   CONSTRUCAO\n" + 
			"         LTDA                                                                            CPF/CNPJ      do beneficiário:              Data  de  vencimento:\n" + 
			"          Razão   Social:CASAS     DA  AGUA    MATERIAIS     PARA    CONSTRUCAO\n" + 
			"         LTDA                                                                             13.501.187/0001-59                          $data_venc\n" + 
			"                                                                                                                                     Valor  do documento     (R$);\n" + 
			"                                                                                                                                     $valor_doc\n" + 
			"                                                                                                                                     (-)Desconto    (R$):\n" + 
			"                                                                                                                                     0,00\n" + 
			"                                                                                                                                     (+)Juros/Mora/Multa     (R$):\n" + 
			"                                                                                                                                     1,72\n" + 
			"          Pagador:                                                                       CPF/CNPJ      do pagador:                   (=) Valor  do pagamento     (R$):\n" + 
			"          MOTO        BOMBAS          FLORIANOPOLIS                LTDA      ME           08.892.611/0001-01                          $valor_pag\n" + 
			"          Sacador   /Avalista:                                                           CPF/CNPJ      do sacador:                   Data  de  pagamento:\n" + 
			"                                                                                                                                      $pagto\n" + 
			"          Autenticação   mecânica:                                                                                                   Pagamento     realizado  em  espécie:\n" + 
			"          97EC97ABF6070179A5F1BD2FE034174714A21A64                                                                                    Não\n" + 
			"         Operação        efetuada       em    29/01/2020         às   18:24:02h       via   bankline,      CTRL      $doc .\n" + 
			"         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n" + 
			"         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n" + 
			"         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).\n";
	
	private static final String BOLETO4 = "         Comprovante                 de    pagamento               de    boleto\n" + 
			"         Dados         da    conta       debitada\n" + 
			"         Agência/conta:          $ag /$conta                           CNPJ:      08.892.611/0001-01                         Empresa:        MOTO        BOMBAS         F   LTDAME\n" + 
			"                   Identificação        no   meu\n" + 
			"                               comprovante:         LUIZ     CORREA\n" + 
			"          Pagador   final:\n" + 
			"          Agência/Conta:         1575/0012906-7                                                                                      CPF/CNPJ:\n" + 
			"          Nome:                  MOTO     BOMBAS     F LTDA    ME                                                                    08.892.611/0001-01\n" + 
			"                                                                                  23793     38128      60007      007366      90000      050808       8  81470000094900\n" + 
			"          Beneficiário:  IUGU   SERVICOS     NA   INTERNET     S.A                       CPF/CNPJ      do beneficiário:              Data  de  vencimento:\n" + 
			"          Razão   Social: IUGU   SERVICOS      NA  INTERNET      S.A                      15.111.975/0001-64                           $data_venc\n" + 
			"                                                                                                                                     Valor  do documento     (R$);\n" + 
			"                                                                                                                                     $valor_doc\n" + 
			"                                                                                                                                     (-)Desconto    (R$):\n" + 
			"                                                                                                                                     0,00\n" + 
			"                                                                                                                                     (+)Juros/Mora/Multa     (R$):\n" + 
			"                                                                                                                                     19,29\n" + 
			"          Pagador:                                                                       CPF/CNPJ      do pagador:                   (=) Valor  do pagamento     (R$):\n" + 
			"          MOTO        BOMBAS          FLORIANOPOLIS                LTDA      ME           08.892.611/0001-01                          $valor_pag\n" + 
			"          Sacador   /Avalista:                                                           CPF/CNPJ      do sacador:                   Data  de  pagamento:\n" + 
			"          CONTABILIDADE                LUIZ     CORREA          SS   EPP                  04.656.282/0001-30                          $pagto\n" + 
			"          Autenticação   mecânica:                                                                                                   Pagamento     realizado  em  espécie:\n" + 
			"          B6104FA71FFEB528D7D02A5B097A0BE0B1C2216D                                                                                    Não\n" + 
			"         Operação        efetuada       em    28/01/2020         às   18:31:22h       via   bankline,      CTRL      $doc8.\n" + 
			"         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n" + 
			"         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n" + 
			"         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).";
	
	private static final String BOLETO5 = "         Comprovante                 de    pagamento               de    boleto\n" + 
			"         Dados         da    conta       debitada\n" + 
			"         Agência/conta:          $ag /$conta                           CNPJ:      08.892.611/0001-01                         Empresa:        MOTO        BOMBAS         F   LTDAME\n" + 
			"                   Identificação        no   meu\n" + 
			"                               comprovante:         santa     clara\n" + 
			"          Pagador   final:\n" + 
			"          Agência/Conta:         1575/0012906-7                                                                                      CPF/CNPJ:\n" + 
			"          Nome:                  MOTO     BOMBAS     F LTDA    ME                                                                    08.892.611/0001-01\n" + 
			"                                                                                  00190     00009      03075      800007      82177      940176       8  81030000004500\n" + 
			"          Beneficiário:  IUGU   SERVICOS     NA   INTERNET     S.A                       CPF/CNPJ      do beneficiário:              Data  de  vencimento:\n" + 
			"          Razão   Social: IUGU   SERVICOS      NA  INTERNET      S.A                      15.111.975/0001-64                           $data_venc\n" + 
			"                                                                                                                                     Valor  do documento     (R$);\n" + 
			"                                                                                                                                     $valor_doc\n" + 
			"                                                                                                                                     (-)Desconto    (R$):\n" + 
			"                                                                                                                                     0,00\n" + 
			"                                                                                                                                     (+)Juros/Mora/Multa     (R$):\n" + 
			"                                                                                                                                     2,03\n" + 
			"          Pagador:                                                                       CPF/CNPJ      do pagador:                   (=) Valor  do pagamento     (R$):\n" + 
			"          MOTO        BOMBAS          FLORIANOPOLIS                LTDA                   08.892.611/0001-01                          $valor_pag\n" + 
			"          Sacador   /Avalista:                                                           CPF/CNPJ      do sacador:                   Data  de  pagamento:\n" + 
			"         BERNARDI           E   MACHADO           MEDICINA\n" + 
			"         OCUPACIONAL                                                                      07.879.569/0001-18                          $pagto\n" + 
			"          Autenticação   mecânica:                                                                                                   Pagamento     realizado  em  espécie:\n" + 
			"          C96545F962CA61BBDD3798589222D22FB2D686D5                                                                                    Não\n" + 
			"         Operação        efetuada       em    06/01/2020         às   18:45:11h       via   bankline,      CTRL      $doc .\n" + 
			"         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n" + 
			"         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n" + 
			"         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).\n";
	
	private static final String PAGTO_COD_BARRAS =  "                                Banco         Itaú     -  Comprovante                 de    Pagamento               com      código         de    barras\n" + 
			"                                                                            0024     - $obs         \n" + 
			"               Identificação       no   extrato:    ICMS      PARCELADO\n" + 
			"         Dados      da   conta    debitada:\n" + 
			"                                         Nome:      MOTO        BOMBAS          F  LTDA      ME\n" + 
			"                                      Agência:      $ag               Conta:     $conta \n" + 
			"         Dados      do   pagamento:\n" + 
			"                        Código      de   barras:    856600000009             486100242008            420001781442            500000977610\n" + 
			"                   Valor     do  documento:         R$    $valor_pag\n" + 
			"         Operação        efetuada       em    $pagto             às   18:31:24h       via   EMPRESA           PLUS,      CTRL      $doc          .\n" + 
			"         Autenticação:\n" + 
			"         FFA5A2C400E9AB69F74D1BDFFCD89E05571254DF\n" + 
			"         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n" + 
			"         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n" + 
			"         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).";
	
	private static final String GRRF = "                                                     Banco         Itaú     -  Comprovante                 de    Pagamento\n" + 
			"                                                                                  FGTS-         GRRF\n" + 
			"         Dados      da   conta    debitada:\n" + 
			"                                Agência:       $ag               Conta:     $conta      \n" + 
			"                                    Nome:      MOTO       BOMBAS          F  LTDA      ME\n" + 
			"         Dados      do   pagamento:\n" + 
			"                  Código      de   barras:     858200000180            770102392027            001220150590701889261124\n" + 
			"                  Código      Convênio:        0239\n" + 
			"                  Data    de   Validade:       22/01/2020\n" + 
			"                          Identificador:       505970188926112\n" + 
			"                    Valor    Recolhido:        R$   $valor_pag\n" + 
			"                          Descrição       do\n" + 
			"                           Pagamento:          multa    fgts\n" + 
			"             Código      da   Operação:        00039804353310435300895\n" + 
			"         Operação        efetuada       em    $pagto            às   14:53:28      via   EMPRESA           PLUS,      CTRL      $doc           .\n" + 
			"         Autenticação         Digital    Itaú:   C3BEE7EA528DBD60082BB439949AE8656D2142D0\n" + 
			"               Identificação       no   extrato:    $obs          \n" + 
			"         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n" + 
			"         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n" + 
			"         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).";
	
	private static final String GRF = "                                                      Banco         Itaú     -  Comprovante                 de    Pagamento\n" + 
			"                                                         GRF        - Guia       de    Recolhimento                 do    FGTS\n" + 
			"         Dados      da   conta    debitada:\n" + 
			"                                Agência:       $ag               Conta:     $conta      \n" + 
			"                                    Nome:      MOTO       BOMBAS          F  LTDA      ME\n" + 
			"         Dados      do   pagamento:\n" + 
			"                  Código      de   barras:     858000000305            116201792004            131636053802889261100010\n" + 
			"                                    CNPJ:      $cnpj           \n" + 
			"                  Código      Convênio:        0179\n" + 
			"                  Data    de   Validade:       31/01/2020\n" + 
			"                        Competência:           12/2019\n" + 
			"                    Valor    Recolhido:        R$   $valor_pag\n" + 
			"                          Descrição       do\n" + 
			"                           Pagamento:          FGTS\n" + 
			"         Operação        efetuada       em    $pagto            às   18:44:22      via   EMPRESA           PLUS,      CTRL      $doc           .\n" + 
			"         Autenticação         Digital    Itaú:   6C17F848896DF6C30D9D40A2DE3E95F61E7644C4\n" + 
			"               Identificação       no   extrato:    FGTS\n" + 
			"         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n" + 
			"         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n" + 
			"         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).";
	
	private static final String DOC_C = "                                                      Banco         Itaú     -  Comprovante                 de    Pagamento\n" + 
			"                                                                     DOC        C    –   outra      titularidade\n" + 
			"               Identificação       no   extrato:    DOC      INT    $doc  \n" + 
			"         Dados      da   conta    debitada:\n" + 
			"                                         Nome:      MOTO        BOMBAS          F  LTDA      ME\n" + 
			"                                      Agência:      $ag                            Conta     corrente:     $conta \n" + 
			"         Dados      do   DOC:\n" + 
			"                         Nome       do  favorecido:       MAQUINAS            HIDRA       HIDROSUL\n" + 
			"                                     CPF     / CNPJ:      $cnpj_ben     \n" + 
			"              Número        do  banco,      nome      ou\n" + 
			"                                                 ISPB:    237    - BANCO         BRA     - ISPB     ESCO       S.A\n" + 
			"                                            Agência:      3271     CANOAS-CTO\n" + 
			"                                 Conta      corrente:     00000000425-1\n" + 
			"                                   Valor    do   DOC:     R$    $valor_pag\n" + 
			"                                        Finalidade:       01-CREDITO            EM    CONTA        CORRENTE\n" + 
			"         DOC      solicitado     em    $pagto             às   19:01:54       via  bankline.\n" + 
			"         Autenticação:\n" + 
			"         DFDF6970CC18A493EFF20D2E3C8F49F78CDCE8A4\n" + 
			"         Dúvidas,  sugestões    e reclamações:    na sua  agência.  Se  preferir, ligue para o  SAC   Itaú:0800   728 0728   (todos  os dias, 24h)  ou acesse   o Fale  Conosco    no\n" + 
			"         www.itau.com.br.    Se  não  ficarsatisfeito com   a solução  apresentada,    ligue para  a Ouvidoria  Corporativa   Itaú: 0800  570  0011  (em  dias  úteis, das 9h  às\n" + 
			"         18h)  ou Caixa  Postal  67.600,  CEP   03162-971.    Deficientes  auditivos  ou de  fala:0800   722  1722  (todos  os dias, 24h).\n";
}
