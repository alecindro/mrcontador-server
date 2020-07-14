package br.com.mrcontador.service.mapper;


import br.com.mrcontador.domain.*;
import br.com.mrcontador.service.dto.ComprovanteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Comprovante} and its DTO {@link ComprovanteDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ComprovanteMapper extends EntityMapper<ComprovanteDTO, Comprovante> {

    default Comprovante fromId(Long id) {
        if (id == null) {
            return null;
        }
        Comprovante comprovante = new Comprovante();
        comprovante.setId(id);
        return comprovante;
    }
}
