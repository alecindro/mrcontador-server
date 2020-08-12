package br.com.mrcontador.file.comprovante.banco;

import java.util.List;

import com.github.difflib.algorithm.DiffException;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.domain.Comprovante;
import br.com.mrcontador.domain.Parceiro;
import br.com.mrcontador.erros.ComprovanteException;

public class ComprovanteCredCrea extends ComprovanteBanco{

	@Override
	public List<Comprovante> parse(String comprovante,Agenciabancaria agenciabancaria, Parceiro parceiro) throws DiffException, ComprovanteException {
		return super.parse(comprovante,pattern,agenciabancaria,parceiro);
	}
	
	
	private static final String pattern = "                                                                                                                                               Emitido     em   17/05/2020       - 19:58:34\n" + 
			"                                                                       COMPROVANTE                 DE   PAGAMENTO\n" + 
			"        DADOS      DO    PAGADOR\n" + 
			"        Banco                                                                                                                                                                             $3\n" + 
			"        Agência                                                                                                                                                                         $ag\n" + 
			"        Conta/DV                                                                                                   $conta     -$1\n" + 
			"        CPF/CNPJ                                                                                                                                                        $cnpj\n" + 
			"        DADOS      DO    BENEFICIÁRIO\n" + 
			"        Beneficiário                                                                                                                                                               $2\n" + 
			"        CPF/CNPJ                                                                                                                                                  $cnpj_ben\n" + 
			"        Banco                                                                                                                                                  ITAU    UNIBANCO         S.A.\n" + 
			"        DADOS      DO    PAGAMENTO\n" + 
			"        Data/Hora       Transação                                                                                                                                16/05/2020       17:29:32\n" + 
			"        Data   Do   Vencimento                                                                                                                                               $data_venc\n" + 
			"        Valor   Título                                                                                                                                                              $valor_doc\n" + 
			"        Encargos                                                                                                                                                                        0,00\n" + 
			"        Descontos                                                                                                                                                                       0,00\n" + 
			"        Sequência      De   Autenticação                                                                                                                                                546\n" + 
			"        Data   Do   Pagamento                                                                                                                                                $pagto\n" + 
			"        Valor                                                                                                                                                                       $valor_pag\n" + 
			"        Linha    Digitável                                                                             34191.75512        46091.662521         50451.630003        8  00000000000000\n" + 
			"        Protocolo                                                                                                                              $doc\n" + 
			"                                                   SAC    0800   647   2200    - Atendimento        todos    os  dias   das   06:00   às  22:00\n" + 
			"                                              OUVIDORIA        0800    644   1100   - Atendimento         todos    os  dias   das  08:00    às  17:00";



}
