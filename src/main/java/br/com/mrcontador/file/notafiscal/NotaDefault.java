package br.com.mrcontador.file.notafiscal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import com.fincatto.documentofiscal.DFBase;
import com.fincatto.documentofiscal.nfe310.classes.NFProtocolo;

@Root(name = "nfeProc")
@Namespace(reference = "http://www.portalfiscal.inf.br/nfe")
public class NotaDefault extends DFBase {

	

	 
	    private static final long serialVersionUID = -6327121382813587248L;

	   
	    @Attribute(name = "versao")
	    private String versao;


		public String getVersao() {
			return versao;
		}


		public void setVersao(String versao) {
			this.versao = versao;
		}


	

		
	    
	    
}
