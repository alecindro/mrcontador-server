package br.com.mrcontador.file.comprovante.banco;

import java.util.ArrayList;
import java.util.List;

import com.github.difflib.algorithm.DiffException;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.ComprovanteException;

public class ComprovanteSicoob extends ComprovanteBanco{

	@Override
	public List<Comprovante> parse(String comprovante,Agenciabancaria agenciabancaria, Parceiro parceiro) throws DiffException, ComprovanteException {
		List<Comprovante> comprovantes = new ArrayList<>();
		String[] comprovantesText = comprovante.split(quebra_linha);
		if(comprovantesText.length>2) {
			comprovantesText[1] =comprovantesText[1].replaceFirst("\\n", "");
		}
		for(String _comprovante : comprovantesText) {
			if(_comprovante.split("\\r?\\n").length>5) {
			comprovantes.addAll(super.parse(_comprovante,pattern,agenciabancaria,parceiro));
			}
		}
		return comprovantes;
	}
	

	private static final String quebra_linha = "OUVIDORIA       SICOOB:     \\d+";
	
	private static final String pattern = "                                                      SICOOB         - Sistema      de   Cooperativas          de   Crédito      do   Brasil\n" + 
			"                                                                SISBR      - Sistema       de   Informática        do   $3\n" + 
			"\n" + 
			"            Data:    17/06/2020                                     Comprovante           de   - Pagamento          de   Título                                    Hora:    10:07:53\n" + 
			"            Coop.:      $ag   /CC   MAXI   ALFA    LIVRE   ADMISSÃO       ASSOCIADOS\n" + 
			"            Conta:      $conta   /SILVA    & MACHADO        ELEVADORES        LTDA    - EPP\n" + 
			"            Linha  digitável:                                                    34191.09057     05389.630079      25717.550005      1 82430000053428\n" + 
			"            Nº  documento:                                                       $doc\n" + 
			"            Nosso   Número:                                                      00725717510905053896\n" + 
			"            No.  Agendamento:                                                    16.898.855\n" + 
			"            Instituição  Emissora:                                               341-ITAU    UNIBANCO       S.A.\n" + 
			"            Tipo  Documento:                                                     Título\n" + 
			"            Nome/Razão      Social  do  Beneficiário:                            $2\n" + 
			"            Nome    Fantasia   Beneficiário:                                     ELEVCOM       IE  C D  P E  A P  ELEVAD\n" + 
			"            CPF/CNPJ      Beneficiário:                                          $cnpj_ben\n" + 
			"            Nome/Razão      Social  do  Pagador:                                 $1\n" + 
			"            Nome    Fantasia   Pagador:                                          SILVA   E  MACHADO       ELEVADORES         LTD\n" + 
			"            CPF/CNPJ      Pagador:                                               $cnpj\n" + 
			"            Data   Agendamento:                                                  02/04/2020-17:00:51\n" + 
			"            Data   Pagamento:                                                    $pagto\n" + 
			"            Data   Vencimento:                                                   $data_venc\n" + 
			"            Valor  Documento:                                                    $valor_doc\n" + 
			"            (-) Desconto    /Abatimento:                                         0,00\n" + 
			"            (+) Outros   acréscimos:                                             0,00\n" + 
			"            Valor  Pago:                                                         $valor_pag\n" + 
			"            Situação:                                                            EFETIVADO\n" + 
			"            Autorizou   pagar   valor diferente  do  agendado:                   Não\n" + 
			"            Observação:                                                          $4\n" + 
			"            Autenticação:                                                        24862A51-5742-4E50-B3E5-2AE6D955B4FA\n";
}
