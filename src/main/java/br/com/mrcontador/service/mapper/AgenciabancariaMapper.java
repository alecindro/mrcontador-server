package br.com.mrcontador.service.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.mrcontador.domain.Agenciabancaria;
import br.com.mrcontador.service.dto.AgenciabancariaDTO;

/**
 * Mapper for the entity {@link Agenciabancaria} and its DTO {@link AgenciabancariaDTO}.
 */
@Mapper(componentModel = "spring", uses = {BancoMapper.class, ParceiroMapper.class, ContaMapper.class})
public interface AgenciabancariaMapper extends EntityMapper<AgenciabancariaDTO, Agenciabancaria> {

    @Mapping(source = "banco.id", target = "bancoId")
    @Mapping(source = "parceiro.id", target = "parceiroId")
    AgenciabancariaDTO toDto(Agenciabancaria agenciabancaria);

    @Mapping(source = "bancoId", target = "banco")
    @Mapping(source = "parceiroId", target = "parceiro")
    Agenciabancaria toEntity(AgenciabancariaDTO agenciabancariaDTO);

    default Agenciabancaria fromId(Long id) {
        if (id == null) {
            return null;
        }
        Agenciabancaria agenciabancaria = new Agenciabancaria();
        agenciabancaria.setId(id);
        return agenciabancaria;
    }
}
