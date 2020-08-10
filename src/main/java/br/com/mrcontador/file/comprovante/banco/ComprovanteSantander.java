package br.com.mrcontador.file.comprovante.banco;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.difflib.algorithm.DiffException;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.MrContadorException;

public class ComprovanteSantander extends ComprovanteBanco{


	@Override
	public List<Comprovante> parse(String comprovante,Agenciabancaria agenciabancaria, Parceiro parceiro) throws DiffException {
		return super.parse(comprovante,getPattern(comprovante),agenciabancaria,parceiro);
	}
	
	private String getPattern(String comprovante) {
		String[] _lines = comprovante.split("\\r?\\n");
		if (_lines == null || _lines.length < 6) {
			return null;
		}
		if (StringUtils.normalizeSpace(_lines[3].trim()).equals("COMPROVANTE DE PAGAMENTO")) {
			return COMPROVANTE;
		}
		if (StringUtils.normalizeSpace(_lines[3].trim())
				.equals("COMPROVANTE DE RECOLHIMENTO - FGTS RESCISORIO")) {
			return FGTS_RESC;
		}
		if (StringUtils.normalizeSpace(_lines[3].trim())
				.equals("COMPROVANTE DE PAGAMENTO RECOLHIMENTO - FGTS GRF")) {
			return FGTS_GRF;
		}
		if (StringUtils.normalizeSpace(_lines[3].trim())
				.equals("COMPROVANTE DE PAGAMENTO DE DAS")) {
			return DAS;
		}
		if (StringUtils.normalizeSpace(_lines[1].trim())
				.equals("Títulos > 2ª via de Comprovante")) {
			return TITULOS;
		}
		
		throw new MrContadorException("comprovante.patternnotfound", StringUtils.normalizeSpace(_lines[0].trim()));
	}
	
	public static final String COMPROVANTE = "                                                           \n" + 
			"                       $1                                     Agência: $ag              Conta    Corrente: $conta\n" + 
			"                       Pagamento        com    codigo    de  barras    >  2ª  via de  comprovante\n" + 
			"                                                                                 COMPROVANTE         DE   PAGAMENTO\n" + 
			"                        \n" + 
			"                       Empresa:                       $2\n" + 
			"                       Convenio     de\n" + 
			"                       Arrecadacao:\n" + 
			"                                                      00330067001004016510\n" + 
			"                       Codigo    de  Barras:          84620000001-2         01940004000-2        04468260140-4        30997316600-7\n" + 
			"                       Data   de  Pagamento:          $pagto\n" + 
			"                       Valor:                         R$   $valor_pag\n" + 
			"                       Data   da  Transacao:          24/03/2020\n" + 
			"                       Hora   da  Transacao:          13:20:59\n" + 
			"                       Canal:                         INTERNET      BANKING\n" + 
			"                       Autenticacao:                  $doc\n" + 
			"                        \n" + 
			"                       Pagamento      efetuado    com   base   nas  informacoes     do  codigo   de  barras.\n" + 
			"                       Guarde    este  recibo   junto  com   o  documento      original  para  eventual    comprovacao       do  pagamento.\n" + 
			"                                                                                                        SAC   - Atendimento      24h   por  dia,  todos  os  dias.\n" + 
			"                                                                                                        0800   762   7777\n" + 
			"                          Central    de  Atendimento        $3      Empresarial                  0800   771   0401   (Pessoas    com   deficiência   auditiva   ou  de\n" + 
			"                          4004-2125     (Regiões    Metropolitanas)                                     fala)\n" + 
			"                          0800   726   2125   (Demais    Localidades)                                   Ouvidoria     -  Das  9h  às  18h,  de  segunda     a sexta-feira,\n" + 
			"                          0800   723   5007   (Pessoas    com   deficiência   auditiva   ou  de         exceto   feriado.\n" + 
			"                          fala)                                                                         0800   726   0322\n" + 
			"                                                                                                        0800   771   0301   (Pessoas    com   deficiência   auditiva   ou  de\n" + 
			"                                                                                                        fala)\n" + 
			"\n" + 
			"";
	
	private static final String FGTS_RESC = "                                                           \n" + 
	        "                       LMC    PROFISSOES         E IDIOMAS       LTDA                                     Agência: $ag               Conta    Corrente: $conta\n" + 
			"                       Pagamento        com    codigo    de  barras    >  2ª  via de  comprovante\n" + 
			"                                                                 COMPROVANTE          DE  RECOLHIMENTO         -  FGTS   RESCISORIO\n" + 
			"                        \n" + 
			"                       Codigo    de  Barras:          85890000001-8         14080239201-1        91223352926-9        51287711872-3\n" + 
			"                        \n" + 
			"                       Empresa:                       FGTS    GRRF    ELETRONICA       239\n" + 
			"                       Data   de  Validade:           23/12/2019\n" + 
			"                       Identificador:                 $doc\n" + 
			"                       Valor   Recolhido:             R$   $valor_pag\n" + 
			"                        \n" + 
			"                       Data   de  Pagamento:          23/12/2019\n" + 
			"                        \n" + 
			"                       Data   da  Transacao:          $pagto\n" + 
			"                       Hora   da  Transacao:          07:00:05\n" + 
			"                       Canal:                         INTERNET      BANKING\n" + 
			"                        \n" + 
			"                       Autenticacao:                  57373153235020544351000\n" + 
			"                        \n" + 
			"                       Convenio     de\n" + 
			"                       Arrecadacao:\n" + 
			"                                                      00336915000900000121\n" + 
			"                        \n" + 
			"                       Documento      pago   dentro    das  condicoes    definidas   no  oficio DIFUG/GEPAS        135/2003.\n" + 
			"                       Pagamento      efetuado    com   base   nas  informacoes     do  codigo   de  barras.\n" + 
			"                       Guarde    este  recibo   junto  com   o  documento      original  para  eventual    comprovacao       do  pagamento.\n" + 
			"                        \n" + 
			"                                                                                                        SAC   - Atendimento      24h   por  dia,  todos  os  dias.\n" + 
			"                                                                                                        0800   762   7777\n" + 
			"                          Central    de  Atendimento        Santander      Empresarial                  0800   771   0401   (Pessoas    com   deficiência   auditiva   ou  de\n" + 
			"                          4004-2125     (Regiões    Metropolitanas)                                     fala)\n" + 
			"                          0800   726   2125   (Demais    Localidades)                                   Ouvidoria     -  Das  9h  às  18h,  de  segunda     a sexta-feira,\n" + 
			"                          0800   723   5007   (Pessoas    com   deficiência   auditiva   ou  de         exceto   feriado.\n" + 
			"                          fala)                                                                         0800   726   0322\n" + 
			"                                                                                                        0800   771   0301   (Pessoas    com   deficiência   auditiva   ou  de\n" + 
			"                                                                                                        fala)\n";
	
	private static final String FGTS_GRF = "                                                           \n" + 
	        "                       LMC    PROFISSOES         E IDIOMAS       LTDA                                     Agência: $ag               Conta    Corrente: $conta     \n" + 
			"                       Pagamento        com    codigo    de  barras    >  2ª  via de  comprovante\n" + 
			"                                                               COMPROVANTE         DE  PAGAMENTO        RECOLHIMENTO         - FGTS    GRF\n" + 
			"                        \n" + 
			"                       Codigo    de  Barras:          85850000009-6         93150179191-1        22763505482-0        87711870001-3\n" + 
			"                       Empresa:                       $doc                             \n" + 
			"                       CNPJ:                          28.771.187/0001-46\n" + 
			"                       Data   de  Validade:           27/12/2019\n" + 
			"                       Competencia:                   11/2019\n" + 
			"                       Valor   Recolhido:             R$   $valor_pag\n" + 
			"                       Data   de  Pagamento:          $pagto    \n" + 
			"                       Data   da  Transacao:          27/12/2019\n" + 
			"                       Hora   da  Transacao:          11:28:25\n" + 
			"                       Canal:                         INTERNET      BANKING\n" + 
			"                       Autenticacao:                  05193611127590222112417\n" + 
			"                       Convenio     de\n" + 
			"                       Arrecadacao:\n" + 
			"                                                      00336915000950017913\n" + 
			"                        \n" + 
			"                       Documento      pago   dentro    das  condicoes    definidas   no  oficio DIFUG/GEPAS        135/2003.\n" + 
			"                       Pagamento      efetuado    com   base   nas  informacoes     do  codigo   de  barras.\n" + 
			"                       Guarde    este  recibo   junto  com   o  documento      original  para  eventual    comprovacao       do  pagamento.\n" + 
			"                                                                                                        SAC   - Atendimento      24h   por  dia,  todos  os  dias.\n" + 
			"                                                                                                        0800   762   7777\n" + 
			"                          Central    de  Atendimento        Santander      Empresarial                  0800   771   0401   (Pessoas    com   deficiência   auditiva   ou  de\n" + 
			"                          4004-2125     (Regiões    Metropolitanas)                                     fala)\n" + 
			"                          0800   726   2125   (Demais    Localidades)                                   Ouvidoria     -  Das  9h  às  18h,  de  segunda     a sexta-feira,\n" + 
			"                          0800   723   5007   (Pessoas    com   deficiência   auditiva   ou  de         exceto   feriado.\n" + 
			"                          fala)                                                                         0800   726   0322\n" + 
			"                                                                                                        0800   771   0301   (Pessoas    com   deficiência   auditiva   ou  de\n" + 
			"                                                                                                        fala)";

	private static final String DAS ="                                                           \n" + 
	        "                       LMC    PROFISSOES         E IDIOMAS       LTDA                                     Agência: $ag               Conta    Corrente: $conta    \n" + 
			"                       Pagamento        com    codigo    de  barras    >  2ª  via de  comprovante\n" + 
			"                                                                            COMPROVANTE         DE  PAGAMENTO        DE   DAS\n" + 
			"                        \n" + 
			"                       Agente    Arrecadador:         CNC    033  BANCO     SANTANDER        (BRASIL)    S.A.\n" + 
			"                       Codigo    de  Barras:          85810000006-4         37720328200-8        30071820028-3        51230900880-3\n" + 
			"                       Data   de  Pagamento:          $pagto   \n" + 
			"                       Numero     do  Documento:      $doc                \n" + 
			"                       Valor   Total:                 R$   $valor_pag\n" + 
			"                       Autenticacao:                  05200291419140287945604\n" + 
			"                       Convenio     de\n" + 
			"                       Arrecadacao:\n" + 
			"                                                      00336916000900000070\n" + 
			"                       Empresa:                       SIMPLES     NACIONAL\n" + 
			"                       Data   de  Vencimento:         $data_venc\n" + 
			"                       Data   da  Transacao:          29/01/2020\n" + 
			"                       Hora   da  Transacao:          14:19:57\n" + 
			"                       Canal:                         INTERNET      BANKING\n" + 
			"                        \n" + 
			"                       Pagamento      efetuado    com   base   nas  informacoes     do  codigo   de  barras.\n" + 
			"                       Guarde    este  recibo   junto  com   o  documento      original  para  eventual    comprovacao       do  pagamento.\n" + 
			"                                                                                                        SAC   - Atendimento      24h   por  dia,  todos  os  dias.\n" + 
			"                                                                                                        0800   762   7777\n" + 
			"                          Central    de  Atendimento        Santander      Empresarial                  0800   771   0401   (Pessoas    com   deficiência   auditiva   ou  de\n" + 
			"                          4004-2125     (Regiões    Metropolitanas)                                     fala)\n" + 
			"                          0800   726   2125   (Demais    Localidades)                                   Ouvidoria     -  Das  9h  às  18h,  de  segunda     a sexta-feira,\n" + 
			"                          0800   723   5007   (Pessoas    com   deficiência   auditiva   ou  de         exceto   feriado.\n" + 
			"                          fala)                                                                         0800   726   0322\n" + 
			"                                                                                                        0800   771   0301   (Pessoas    com   deficiência   auditiva   ou  de\n" + 
			"                                                                                                        fala)\n";
	
	private static final String TITULOS = "                                                           \n" + 
			"                        Títulos   >  2ª  via de  Comprovante\n" + 
			"                        WTF    SUSHI    LTDA     ME                                                        Agência: $ag               Conta   Corrente: $conta    \n" + 
			"                        Código   de  Barras:                 0339982340             96500000011              73750301019                      4              80870000010060\n" + 
			"                        Nosso   Número:                   $doc      \n" + 
			"                        Instituição   Financeira\n" + 
			"                        Favorecida:\n" + 
			"                                                          033   - BANCO      SANTANDER\n" + 
			"                        Dados    do  Beneficiário      Original\n" + 
			"                        CNPJ:                              $cnpj_ben         \n" + 
			"                        Razão   Social:                    $2                                                   \n" + 
			"                        Nome    Fantasia:                  DESTERRO        COMERCIO        ATACADISTA         DE   ALIMENTOS        S/A\n" + 
			"                        Dados    do  Pagador      Original                                             Dados    do   Pagador     Efetivo\n" + 
			"                        CNPJ:                             24.408.746/0001-05                                        CNPJ:                       $cnpj\n" + 
			"                         Razão   Social:                   WTF    SUSHI     LTDA    ME\n" + 
			"                        Dados    do  Pagamento\n" + 
			"                        Data  de  Vencimento:             $data_venc\n" + 
			"                        Valor  Nominal:                   R$ $valor_doc\n" + 
			"                        Valor  Total  a Cobrar:R$         R$ $valor_pag\n" + 
			"                           Transação      exclusiva     para   pagamento        de  Título.   Pagamento        válido   somente      se  informados       corretamente        os\n" + 
			"                          dados    do  título.  A  veracidade      dessas     informações       é de   responsabilidade        do   Cliente/Pagador,         que   se  obriga    a\n" + 
			"                         apresentar      os  títulos   para   verificação     sempre      que   solicitado,    nos   termos     da  lei. Havendo      divergências       entre   a\n" + 
			"                        informação       ora   fornecida     e o  valor   efetivamente       devido,    será   facultado     ao  banco     efetuar    ou  não   o  pagamento,\n" + 
			"                        ficando,    no   caso   de  efetivação,     desde    já  autorizado      a debitar    ou   creditar    na  conta   corrente     do   Cliente/Pagador\n" + 
			"                                                                                     a  diferença     encontrada.\n" + 
			"                                                                            Data  da  Transação:        $pagto   \n" + 
			"                                                                  Número    de  Autenticação     da\n" + 
			"                                                          Instituição   Financeira    Favorecida:\n" + 
			"                                                                                                        A3E6B4786238A65A67BCB28\n" + 
			"                                                                                           Canal   :    Internet     Banking\n" + 
			"                                                                                                         SAC   - Atendimento      24h  por  dia,  todos   os dias.\n" + 
			"                                                                                                         0800   762  7777\n" + 
			"                          Central    de  Atendimento        Santander       Empresarial                  0800   771  0401    (Pessoas    com   deficiência   auditiva   ou  de\n" + 
			"                          4004-2125      (Regiões    Metropolitanas)                                     fala)\n" + 
			"                          0800   726   2125   (Demais    Localidades)                                    Ouvidoria     - Das   9h  às 18h,   de  segunda    a  sexta-feira,\n" + 
			"                          0800   723   5007   (Pessoas    com   deficiência   auditiva   ou  de          exceto   feriado.\n" + 
			"                          fala)                                                                          0800   726  0322\n" + 
			"                                                                                                         0800   771  0301    (Pessoas    com   deficiência   auditiva   ou  de\n" + 
			"                                                                                                         fala)\n";

}
