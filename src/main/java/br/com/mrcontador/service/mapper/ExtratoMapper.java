package br.com.mrcontador.service.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.mrcontador.domain.Extrato;
import br.com.mrcontador.service.dto.ExtratoDTO;

/**
 * Mapper for the entity {@link Extrato} and its DTO {@link ExtratoDTO}.
 */
@Mapper(componentModel = "spring", uses = {ParceiroMapper.class, AgenciabancariaMapper.class})
public interface ExtratoMapper extends EntityMapper<ExtratoDTO, Extrato> {

    @Mapping(source = "parceiro.id", target = "parceiroId")
    @Mapping(source = "agenciabancaria.id", target = "agenciabancariaId")
    ExtratoDTO toDto(Extrato extrato);

    @Mapping(source = "parceiroId", target = "parceiro")
    @Mapping(source = "agenciabancariaId", target = "agenciabancaria")
    Extrato toEntity(ExtratoDTO extratoDTO);

    default Extrato fromId(Long id) {
        if (id == null) {
            return null;
        }
        Extrato extrato = new Extrato();
        extrato.setId(id);
        return extrato;
    }
}
