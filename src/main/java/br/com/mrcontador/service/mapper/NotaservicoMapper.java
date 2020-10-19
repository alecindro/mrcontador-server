package br.com.mrcontador.service.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.mrcontador.domain.Notaservico;
import br.com.mrcontador.service.dto.NotaservicoDTO;

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
