package br.com.mrcontador.service.mapper;


import br.com.mrcontador.domain.*;
import br.com.mrcontador.service.dto.NotaservicoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notaservico} and its DTO {@link NotaservicoDTO}.
 */
@Mapper(componentModel = "spring", uses = {ParceiroMapper.class})
public interface NotaservicoMapper extends EntityMapper<NotaservicoDTO, Notaservico> {

    @Mapping(source = "parceiro.id", target = "parceiroId")
    NotaservicoDTO toDto(Notaservico notaservico);

    @Mapping(source = "parceiroId", target = "parceiro")
    Notaservico toEntity(NotaservicoDTO notaservicoDTO);

    default Notaservico fromId(Long id) {
        if (id == null) {
            return null;
        }
        Notaservico notaservico = new Notaservico();
        notaservico.setId(id);
        return notaservico;
    }
}
