package br.com.mrcontador.service.mapper;


import org.mapstruct.Mapper;

import br.com.mrcontador.domain.Regra;
import br.com.mrcontador.service.dto.RegraDTO;

/**
 * Mapper for the entity {@link Regra} and its DTO {@link RegraDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RegraMapper extends EntityMapper<RegraDTO, Regra> {



    default Regra fromId(Long id) {
        if (id == null) {
            return null;
        }
        Regra regra = new Regra();
        regra.setId(id);
        return regra;
    }
}
