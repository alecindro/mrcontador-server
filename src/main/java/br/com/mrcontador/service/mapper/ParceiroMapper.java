package br.com.mrcontador.service.mapper;


import br.com.mrcontador.domain.*;
import br.com.mrcontador.service.dto.ParceiroDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Parceiro} and its DTO {@link ParceiroDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ParceiroMapper extends EntityMapper<ParceiroDTO, Parceiro> {


    @Mapping(target = "atividades", ignore = true)
    @Mapping(target = "removeAtividade", ignore = true)
    @Mapping(target = "socios", ignore = true)
    @Mapping(target = "removeSocio", ignore = true)
    Parceiro toEntity(ParceiroDTO parceiroDTO);

    default Parceiro fromId(Long id) {
        if (id == null) {
            return null;
        }
        Parceiro parceiro = new Parceiro();
        parceiro.setId(id);
        return parceiro;
    }
}
