package br.com.mrcontador.file.comprovante.banco;

import java.util.List;

import com.github.difflib.algorithm.DiffException;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.domain.Parceiro;

public class ComprovanteSantander extends ComprovanteBanco{


	@Override
	public List<Comprovante> parse(String comprovante,Agenciabancaria agenciabancaria, Parceiro parceiro) throws DiffException {
		return super.parse(comprovante,pattern,agenciabancaria,parceiro);
	}
	
	
	
	public static final String pattern = "                                                           \n" + 
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


}
