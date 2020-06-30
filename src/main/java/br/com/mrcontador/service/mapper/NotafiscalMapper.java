package br.com.mrcontador.service.mapper;


import br.com.mrcontador.domain.*;
import br.com.mrcontador.service.dto.NotafiscalDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notafiscal} and its DTO {@link NotafiscalDTO}.
 */
@Mapper(componentModel = "spring", uses = {ParceiroMapper.class})
public interface NotafiscalMapper extends EntityMapper<NotafiscalDTO, Notafiscal> {

    @Mapping(source = "parceiro.id", target = "parceiroId")
    NotafiscalDTO toDto(Notafiscal notafiscal);

    @Mapping(source = "parceiroId", target = "parceiro")
    Notafiscal toEntity(NotafiscalDTO notafiscalDTO);

    default Notafiscal fromId(Long id) {
        if (id == null) {
            return null;
        }
        Notafiscal notafiscal = new Notafiscal();
        notafiscal.setId(id);
        return notafiscal;
    }
}
