package br.com.mrcontador.service.mapper;


import br.com.mrcontador.domain.*;
import br.com.mrcontador.service.dto.BancoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Banco} and its DTO {@link BancoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BancoMapper extends EntityMapper<BancoDTO, Banco> {



    default Banco fromId(Long id) {
        if (id == null) {
            return null;
        }
        Banco banco = new Banco();
        banco.setId(id);
        return banco;
    }
}
