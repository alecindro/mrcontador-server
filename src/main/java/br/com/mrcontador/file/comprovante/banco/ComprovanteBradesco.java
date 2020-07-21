package br.com.mrcontador.file.comprovante.banco;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.difflib.algorithm.DiffException;

import br.com.mrcontador.erros.MrContadorException;
import br.com.mrcontador.file.comprovante.DiffValue;

public class ComprovanteBradesco extends ComprovanteBanco{
	
	@Override
	public List<DiffValue> parse(String comprovante) throws DiffException {
		return super.parse(comprovante,getPattern(comprovante));
	}
	
	private String getPattern(String comprovante) {
		String[] _lines = comprovante.split("\\r?\\n");
		if(_lines == null || _lines.length<6) {
			return null;
		}
		if(StringUtils.normalizeSpace(_lines[1].trim()).equals("Boleto de Cobrança")) {
			return BOLETO_COBRANCA;
		}
		if(StringUtils.normalizeSpace(_lines[1].trim()).equals("Água, Luz, Telefone e Gás")) {
			return  AGUA_LUZ_TELEFONE;
		}
		if(StringUtils.normalizeSpace(_lines[1].trim()).equals("Transferência entre Contas Bradesco")) {
			return  TEC;
		}
		if(StringUtils.normalizeSpace(_lines[1].trim()).equals("IMPOSTO/TAXAS")) {
			return  IMPOSTO_TAXAS;
		}
		if(StringUtils.normalizeSpace(_lines[1].trim()).equals("Transferências Para Contas de Outros Bancos (TED)")) {
			return  TED;
		}
		if(StringUtils.normalizeSpace(_lines[1].trim()).equals("Transferências Para Contas de Outros Bancos (DOC)")) {
			return  DOC;
		}
		if(StringUtils.normalizeSpace(_lines[4].trim()).equals("COMPROVANTE DE PAGAMENTO DO SIMPLES NACIONAL")) {
			return  SIMPLES_NACIONAL;
		}
		if(StringUtils.normalizeSpace(_lines[5].trim()).equals("COMPROVANTE DE PAGAMENTO DARF")) {
			return  DARF;
		}
		System.out.println(comprovante);
		throw new MrContadorException("comprovante.patternnotfound", StringUtils.normalizeSpace(_lines[1].trim()));
	}

	private static final String BOLETO_COBRANCA = "                                                   Comprovante             de   Transação          Bancária\n" + 
			"                                                   Boleto   de  Cobrança\n" + 
			"                                                   Data   da  operação:    07/05/2020\n" + 
			"                                                   N°  de  controle:   752.221.588.152.371.615         |Documento:      $doc\n" + 
			"                       Conta   de  débito:    Agência:    $ag   | Conta:   $conta     | Tipo:  Conta-Corrente\n" + 
			"                               Empresa:       $1   | CNPJ:    $cnpj\n" + 
			"                     Código    de  barras:    23792   37403    59606   535488    19010   520005    6  82480000018961\n" + 
			"                    Banco   destinatário:     237  - BANCO      BRADESCO        S.A.\n" + 
			"                           Razao    Social    Não   informado\n" + 
			"                            Beneficiário:\n" + 
			"                        Nome     Fantasia     Não   informado\n" + 
			"                            Beneficiário:\n" + 
			"              CPF/CNPJ      Beneficiário:     Não   informado\n" + 
			"                Razao    Social  Sacador      Não   informado\n" + 
			"                                 Avalista:\n" + 
			"                   CPF/CNPJ      Sacador      Não   informado\n" + 
			"                                 Avalista:\n" + 
			"              Instituição  Recebedora:        237  - BANCO      BRADESCO        S.A.\n" + 
			"                    Nome    do  Pagador:      Não   informado\n" + 
			"              CPF/CNPJ      do  Pagador:      Não   informado\n" + 
			"                        Data   de  débito:    $pagto\n" + 
			"                  Data  de  vencimento:       $data_venc\n" + 
			"                                    Valor:    R$  $valor_doc\n" + 
			"                               Desconto:      R$  0,00\n" + 
			"                            Abatimento:       R$  0,00\n" + 
			"                            Bonificação:      R$  0,00\n" + 
			"                                    Multa:    R$  0,00\n" + 
			"                                    Juros:    R$  0,00\n" + 
			"                              Valor  total:   R$  $valor_pag\n" + 
			"                              Descrição:      $4\n" + 
			"                 A  transação   acima    foirealizada   por  meio   do  $3    COBRANCA        COM     DEBITO     AUTOMATICO\n" + 
			"                                                                                            Autenticação\n" + 
			"                                       TP?IBMT4        y2yKO3I2         z??Z2hqv        KIJuQgkK        I7fU5YK#        sVbyMHGo        ZtXbUjcw         6LkJ436y\n" + 
			"                                       sW7Zr*cv        mLbxr95Y         kmc*gvuy        e#5yHK@p        fhp97oij        NsC2vClX        lV4xLC#L         NKKu?fY@\n" + 
			"                                       5nleseRk        Ohxe73fi         vg?Q*Pzf        f4h?6fdA        RdHewaA2        2*cSPAAL        67173280         27749010\n" + 
			"                 SAC    -Serviço    de     Alô  Bradesco           Deficiente   Auditivo   ou  de Fala      Cancelamentos,     Reclamações     e                     Demais   telefones\n" + 
			"                  Apoio   ao  Cliente      0800   704   8383       0800   722  0099                         Informações.                                             consulte  o site\n" + 
			"                                                                                                            Atendimento    24 horas,  7 dias por  semana.            Fale Conosco.\n" + 
			"                Ouvidoria       0800   727   9933      Atendimento   de  segunda   a sexta-feira, das  8h às  18h, exceto  feriados.";
	
	private static final String AGUA_LUZ_TELEFONE = "Comprovante             de   Transação          Bancária\n" + 
			"                                                   Água,   Luz,  Telefone    e Gás\n" + 
			"                                                   Data   da  operação:    11/05/2020\n" + 
			"                                                   N°  de  controle:   752.221.588.152.371.615         |Autenticação     bancária:   $doc\n" + 
			"                       Conta   de  débito:    Agência:    $ag   | Conta:   $conta     | Tipo:  Conta-Corrente\n" + 
			"                               Empresa:       $1   | CNPJ:    $cnpj\n" + 
			"                     Código    de  barras:    826800000158       932800130001       000000010496       238120048075\n" + 
			"                          MATRICULA:          9238120\n" + 
			"                       Concessionária:        $2       (AGUA)\n" + 
			"                                    Valor:    R$  $valor_pag\n" + 
			"                        Data   de  débito:    $pagto\n" + 
			"                              Descrição:      $4\n" + 
			"                 A  transação   acima    foirealizada   por  meio   do  Bradesco    INTERNET       -PESSOA      JURIDIC\n" + 
			"                                                                                            Autenticação\n" + 
			"                                       xkijg*wQ        oCA@wrvu         pj8kAVoG        L*jYaO3c        JYqrxQHx        L4JvBygM        ?@sRDr9D         zdcGDbcU\n" + 
			"                                       QDBohuJ#        WAhIj4gD         BZTXirKK        fmYsY8YJ        hcfigECo        6z8C7XwX        ylP6mtqx         DceeWkeF\n" + 
			"                                       O8G3@D3D        sEorN2Jh         uZuw?7uB        D6D5guFV        RP8TRyiG        nM?NPvtL        00201120         00530093\n" + 
			"                 SAC    -Serviço    de     Alô  $3           Deficiente   Auditivo   ou  de Fala      Cancelamentos,     Reclamações     e                     Demais   telefones\n" + 
			"                  Apoio   ao  Cliente      0800   704   8383       0800   722  0099                         Informações.   Atendimento    24 horas,  7 dias          consulte  o site\n" + 
			"                                                                                                            por semana.                                              Fale Conosco\n" + 
			"                Ouvidoria       0800   727   9933      Atendimento   de  segunda   a sexta-feira, das  8h às  18h, exceto  feriados.";
	
	private static final String DARF = "                                Data   da  Transação:    18/03/2020\n" + 
			"                                                   N°  Controle:   752.221.588.152.371.615\n" + 
			"                                                   Empresa:      $1                CNPJ:     $cnpj\n" + 
			"                                                   Agência    de  débito:   $ag                Conta   de  débito:   $conta\n" + 
			"\n" + 
			"                                                               COMPROVANTE            DE  PAGAMENTO          DARF\n" + 
			"                                   Agente    arrecadador:      237   -Banco    Bradesco    S/A\n" + 
			"                                   Data   do  Pagamento:       $pagto\n" + 
			"                                  Período   de  Apuração:      29/02/2020\n" + 
			"                            Número     do  CPF   ou  CNPJ:     003.551.834/0001-83\n" + 
			"                                     Código    de  Receita:    056-1\n" + 
			"                                Número     de  Referencia:\n" + 
			"                                   Data  do  Vencimento:       $data_venc\n" + 
			"                                      Valor  do  Principal:    R$  $valor_doc\n" + 
			"                                          Valor  da  Multa:    R$  0,00\n" + 
			"                            Valor  dos   Juros/Encargos:       R$  0,00\n" + 
			"                                               Valor  Total:   R$  $valor_pag\n" + 
			"                                Autenticação     Bancária:     $doc\n" + 
			"                 Modelo    aprovado    pela  SRF   - ADE   Conjunto    Corat/Cotec     Nº  001  de  2006.\n" + 
			"                                                             A  transação    acima   foi realizada  por  meio   do  $3    Net  Empresa.\n" + 
			"                          $4\n" + 
			"                                                                                            Autenticação\n" + 
			"                                             kJc@3bHs      phMbRb9e       3nSMEkR3       @radVLj*       ptlRqGiZ       bojY3XRU       YDl9c37y       QmO7e2jG\n" + 
			"                                             RijQ@RwF      q*xx*vfd       ?u39m2F6       7VB3EG*@       5k@39y?9       SAKw5eps       p9O2mzb5       sI#ccows\n" + 
			"                                             @KqyAL2L      j58BLbgb       rRyYQMMJ       CGgOkyO6       FkI8qvBf       qQkjtQD8       01105660       62201773\n" + 
			"               SAC   - Serviço    de     Alô Bradesco           Deficiente    Auditivo  ou  de  Fala      Cancelamentos,    Reclamações     e                   Demais   telefones\n" + 
			"               Apoio    ao  Cliente      0800   704  8383       0800   722   0099                         Informações.   Atendimento   24  horas, 7  dias       consulte  o site\n" + 
			"                                                                                                          por semana.                                           Fale Conosco\n" + 
			"                       Ouvidoria        0800  727   9933      Atendimento   de  segunda   a sexta-feira, das  8h às  18h, exceto  feriados.";
	
	private static final String IMPOSTO_TAXAS = "                                                    Comprovante            de   Transação          Bancária\n" + 
			"                                                    IMPOSTO/TAXAS\n" + 
			"                                                    Data  da  operação:    14/04/2020\n" + 
			"                                                    Nº Controle:   752.221.588.152.371.615          |Autenticação     Bancária:   $doc\n" + 
			"                                            Conta   de  débito:    Agência:   $ag   |Conta:   $conta     |Tipo:  Conta-Corrente                                             Empresa:\n" + 
			"                 $1   |CNPJ:    $cnpj\n" + 
			"                                          Código    de  barras:    85800000007-0       33310270200-0       30355183400-9       01832020037-2\n" + 
			"                                           Empresa    / Órgão:     $2                                           Descrição:      $4\n" + 
			"                                          IDENTIFICADOR:           03551834000183                                        Data   de débito:    $pagto\n" + 
			"                                       Data   do  vencimento:      00/00/0000                                     Valor  principal:    R$  $valor_doc\n" + 
			"                                                    Desconto:      R$  0,00                                             Juros:    R$   0,00\n" + 
			"                                                         Multa:    R$  0,00                           Valor  do  pagamento:       R$   $valor_pag\n" + 
			"                 A  transação   acima    foirealizada   por  meio   do  INTERNET      - PESSOA      JURIDIC.\n" + 
			"                 O  Lançamento      do  valor consta   no  extrato  de  Conta-Corrente,      junto  a Agência    do débito   nº. 348  , da  data  de  pagamento     14/04/2020.\n" + 
			"                                                                                            Autenticação\n" + 
			"                                             PFDHiHEQ      t9X9EaR7       AoGv@Tg9       J#I7#DsI       AwHD7bJ5       ExiF5bJW       h33C@MLf       m74ZlEJl\n" + 
			"                                             ocOtYsux      9cHLRL3X       QNxHT6VN       x9FMqg#c       UIG45H9n       go*e5Y@a       VD95#NE*       nHM5RD7*\n" + 
			"                                             r?CB*qSN      sTVKKpIu       4hAaY4VE       VPQZz??g       #E?LuDdX       b??S4f6X       00501420       00730033\n" + 
			"               SAC   - Serviço    de     Alô $3            Deficiente    Auditivo  ou  de  Fala      Cancelamentos,    Reclamações     e                   Demais   telefones\n" + 
			"               Apoio    ao  Cliente      0800   704  8383       0800   722   0099                         Informações.   Atendimento   24  horas, 7  dias       consulte  o site\n" + 
			"                                                                                                          por semana.                                           Fale Conosco\n" + 
			"                       Ouvidoria        0800  727   9933      Atendimento   de  segunda   a sexta-feira, das  8h às  18h, exceto  feriados.";
	
	private static final String SIMPLES_NACIONAL = "                                                   Data   da  Transação:    20/03/2020\n" + 
			"                                                   Nº  Controle:   752.221.588.152.371.615\n" + 
			"                                                   Empresa:      $parceiro                CNPJ:     $cnpj\n" + 
			"                                                   Agência    de  Débito:   $ag                   Conta   de  Débito:   $conta\n" + 
			"                                                               COMPROVANTE            DE  PAGAMENTO          DO   SIMPLES      NACIONAL\n" + 
			"                                   Agente    arrecadador:      237   -Banco    Bradesco    S/A\n" + 
			"                                      Código    de  Barras:    858000000003       569503282005        800708200207       545784538567\n" + 
			"                                   Data   do  Pagamento:       $pagto\n" + 
			"                               Número     do  Documento:       $doc\n" + 
			"                                               Valor  Total:   R$  $valor_pag\n" + 
			"                                Autenticação     Bancária:     096.503.336\n" + 
			"\n" + 
			"                                                             A  transação    acima   foi realizada  por  meio   do  $3    Net  Empresa.\n" + 
			"                O  lançamento     consta   no extrato   de Conta-Corrente      do  cliente  FUZZ    CABELEIREIROS          LTDA    ME,  junto  à Agência    348,  na  data  de  pagamento.\n" + 
			"                                                                                            Autenticação\n" + 
			"                                             uymjB9Qs      FrApeYHw       lRef39HL       afnIqVz?       3lpY6kXD       aRM34JG?       IYQgfy9#       s7t6lyEX\n" + 
			"                                             ZMR2KQW?      RJLMVH4a       l7K5hIx5       NZ5W7C8n       XtTq6XhU       OKfVQGNt       byC@Svbs       @R44JcCk\n" + 
			"                                             LxBCnhxR      mPgv5Sq3       kA@e5kkD       ma6D#x4W       Co8Y9aSr       *FIUUPxv       00502020       00060056\n" + 
			"\n" + 
			"               SAC   - Serviço    de     Alô Bradesco           Deficiente    Auditivo  ou  de  Fala      Cancelamentos,    Reclamações     e                   Demais   telefones\n" + 
			"               Apoio    ao  Cliente      0800   704  8383       0800   722   0099                         Informações.   Atendimento   24  horas, 7  dias       consulte  o site\n" + 
			"                                                                                                          por semana.                                           Fale Conosco\n" + 
			"                       Ouvidoria        0800  727   9933      Atendimento   de  segunda   a sexta-feira, das  8h às  18h, exceto  feriados.\n" + 
			"";
	
	private static final String DOC ="                                                   Comprovante             de   Transação          Bancária\n" + 
			"                                                   Transferências     Para   Contas    de Outros    Bancos   (DOC)\n" + 
			"                                                   Data   da  operação:    10/06/2019     -14h21\n" + 
			"                                                   N°  de  controle:   752221588152371615          |Documento:      $doc\n" + 
			"                       Conta   de  débito:    Agência:    $ag   | Conta:   $conta     | Tipo:  Conta-Corrente\n" + 
			"                               Empresa:       $1   | CNPJ:    $cnpj\n" + 
			"                  Nome    do  favorecido:     $2\n" + 
			"                                     CPF:     $cnpj_ben\n" + 
			"                      Conta   de  crédito:    Banco:    001  - BANCO      DO   BRASIL     S.A.  |Agência:     1453   |Conta:    4815203\n" + 
			"                          Tipo  de  conta:    CONTA-CORRENTE             INDIV\n" + 
			"                              Finalidade:     01  -CREDITO      EM   CONTA      CORRENTE\n" + 
			"                                    Valor:    R$  $valor_doc\n" + 
			"                                   Tarifa:    R$  0,00\n" + 
			"                              Valor  total:   R$  $valor_pag\n" + 
			"                Tipo   de transferência:      DOC    -Titularidade     Diferente\n" + 
			"                                              Crédito   será   realizado   no  próximo     dia  útil da  data  de  débito\n" + 
			"                        Data   de  débito:    $pagto\n" + 
			"                               A  tarifa é cobrada    por  transferência   realizada   e  para  as  operações    agendadas      poderá   sofrer  alteração   de  acordo   com   os\n" + 
			"                               valores   vigentes   na  data  do  débito\n" + 
			"                 A  transação   acima    foirealizada   por  meio   do  Bradesco    Net  Empresa.\n" + 
			"                                                                                            Autenticação\n" + 
			"                                       Si#S26Fr        63@yzl@o         LIJ?Yp58        nYR6UM5t        VXu@6QbA        cJe3rTVW        lcE@K@Dl         b4fFdlB4\n" + 
			"                                       nS7335Al        4DAcRgf2         LaGKMQPM        U3JT?qNc        ikA?QxDa        O*MTtvs@        w#nrCkjU         MAIz4TIx\n" + 
			"                                       jy2jzBI7        OpfFriR@         wBIAOHNZ        I3zWHR@H        j8ZAujrC        Nc?NzgRd        23093420         01152301\n" + 
			"                 SAC    -Serviço    de     Alô  Bradesco           Deficiente   Auditivo   ou  de Fala      Cancelamentos,     Reclamações     e                     Demais   telefones\n" + 
			"                  Apoio   ao  Cliente      0800   704   8383       0800   722  0099                         Informações.                                             consulte  o site\n" + 
			"                                                                                                            Atendimento    24 horas,  7 dias por  semana.            Fale Conosco.\n" + 
			"                Ouvidoria       0800   727   9933      Atendimento   de  segunda   a sexta-feira, das  8h às  18h, exceto  feriados.";

	private static final String TED ="                                                   Comprovante             de   Transação          Bancária\n" + 
			"                                                   Transferências     Para   Contas    de Outros    Bancos   (TED)\n" + 
			"                                                   Data   da  operação:    04/12/2019     -07h15\n" + 
			"                                                   N°  de  controle:   752221588152371615          |Documento:      $doc\n" + 
			"                       Conta   de  débito:    Agência:    $ag   | Conta:   $conta     | Tipo:  Conta-Corrente\n" + 
			"                               Empresa:       $1   | CNPJ:    $cnpj\n" + 
			"                      Conta   de  crédito:    Banco:    341  - ITAU   UNIBANCO        S.A.  |Agência:     8560   |Conta:    60256\n" + 
			"                          Tipo  de  conta:    CONTA-CORRENTE             INDIV\n" + 
			"                              Finalidade:     $4\n" + 
			"                                    Valor:    R$  $valor_doc\n" + 
			"                                   Tarifa:    R$  10,45\n" + 
			"                              Valor  total:   R$  $valor_pag\n" + 
			"                Tipo   de transferência:      TED   - Mesma     Titularidade\n" + 
			"                                              Crédito   disponível     no  mesmo     dia  da  data  de  débito\n" + 
			"                        Data   de  débito:    $pagto\n" + 
			"                               A  tarifa é cobrada    por  transferência   realizada   e  para  as  operações    agendadas      poderá   sofrer  alteração   de  acordo   com   os\n" + 
			"                               valores   vigentes   na  data  do  débito\n" + 
			"                 A  transação   acima    foirealizada   por  meio   do  $3    Net  Empresa.\n" + 
			"                                                                                            Autenticação\n" + 
			"                                       l*SE*koo        SKtUSRQ5         4H*MTwZF        qvV9?c2?        KLQdia7e        jbiEIcvh        sFZwqNzi         hymlwSO7\n" + 
			"                                       dS2IANFM        WbkfmzT6         ixE#NFPU        vROVZNC*        S7KpucM?        VwIftgmA        Iagv*6Fy         Sd3euJI#\n" + 
			"                                       UsItkYqR        vHTSO*i*         NV*Ng6@S        QSE4PrTj        FlvKVfeM        5xoN?PxH        66493420         31862620\n" + 
			"                 SAC    -Serviço    de     Alô  Bradesco           Deficiente   Auditivo   ou  de Fala      Cancelamentos,     Reclamações     e                     Demais   telefones\n" + 
			"                  Apoio   ao  Cliente      0800   704   8383       0800   722  0099                         Informações.                                             consulte  o site\n" + 
			"                                                                                                            Atendimento    24 horas,  7 dias por  semana.            Fale Conosco.\n" + 
			"                Ouvidoria       0800   727   9933      Atendimento   de  segunda   a sexta-feira, das  8h às  18h, exceto  feriados.\n" + 
			"\n" + 
			"";
	
	private static final String TEC ="                                                   Comprovante             de   Transação          Bancária\n" + 
			"                                                   Transferência     entre  Contas    Bradesco\n" + 
			"                                                   Data   da  operação:    19/06/2019     -16h46\n" + 
			"                                                   N°  de  controle:   752221588152371615          |Documento:      $doc\n" + 
			"                       Conta   de  débito:    Agência:    $ag   | Conta:   $conta     | Tipo:  Conta-Corrente\n" + 
			"                               Empresa:       $1   | CNPJ:    $cnpj\n" + 
			"                      Conta   de  crédito:    Agência:    3750   | Conta:   0027821-1     | Tipo:  Conta-Corrente\n" + 
			"                  Nome    do  favorecido:     $2\n" + 
			"                                    Valor:    R$  $valor_pag\n" + 
			"                        Data   de  débito:    $pagto\n" + 
			"                              Descrição:      rateio\n" + 
			"                 A  transação   acima    foirealizada   por  meio   do  Bradesco    Net  Empresa.\n" + 
			"                                                                                            Autenticação\n" + 
			"                                       aGaZHhHt        l98Ct7?x         Sobz8qA9        bfVA*89F        3q2NLiVr        f35tZkL*        eI*@SA97         3TK9B@n?\n" + 
			"                                       GU7UGLUK        GcGsCfrn         gWmYVvSR        yinAp5I#        uj7yGpHR        FpQVwLEW        KOJ#D4JM         wizDZdz4\n" + 
			"                                       marwnRzV        RtMxEiua         j954euB7        G@7AD69e        FxFc8NtI        TBYe#ACF        84720005         12916190\n" + 
			"                 SAC    -Serviço    de     Alô  $3           Deficiente   Auditivo   ou  de Fala      Cancelamentos,     Reclamações     e                     Demais   telefones\n" + 
			"                  Apoio   ao  Cliente      0800   704   8383       0800   722  0099                         Informações.                                             consulte  o site\n" + 
			"                                                                                                            Atendimento    24 horas,  7 dias por  semana.            Fale Conosco.\n" + 
			"                Ouvidoria       0800   727   9933      Atendimento   de  segunda   a sexta-feira, das  8h às  18h, exceto  feriados.\n" + 
			"\n" + 
			"";
}
