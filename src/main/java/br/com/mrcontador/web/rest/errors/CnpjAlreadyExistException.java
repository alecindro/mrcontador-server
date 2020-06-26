package br.com.mrcontador.web.rest.errors;

public class CnpjAlreadyExistException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public CnpjAlreadyExistException() {
        super(ErrorConstants.CNPJ_OR_ALREADY_USED_TYPE, "Cnpj or Crc is already in use!", "contadorResource", "cpnjcrcexists");
    }
}
