package br.com.mrcontador.file.notafiscal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import com.fincatto.documentofiscal.DFBase;
import com.fincatto.documentofiscal.nfe310.classes.NFProtocolo;

@Root(name = "nfeProc")
@Namespace(reference = "http://www.portalfiscal.inf.br/nfe")
public class NotaDefault extends DFBase {

	

	 
	    private static final long serialVersionUID = -6327121382813587248L;

	   
	    @Element(name = "protNFe")
	    private NFProtocolo protocolo;


		public NFProtocolo getProtocolo() {
			return protocolo;
		}


		public void setProtocolo(NFProtocolo protocolo) {
			this.protocolo = protocolo;
		}

		
	    
	    
}
