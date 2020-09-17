package br.com.mrcontador.service.mapper;


import br.com.mrcontador.domain.*;
import br.com.mrcontador.service.dto.ContaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Conta} and its DTO {@link ContaDTO}.
 */
@Mapper(componentModel = "spring", uses = {ParceiroMapper.class})
public interface ContaMapper extends EntityMapper<ContaDTO, Conta> {

    @Mapping(source = "parceiro.id", target = "parceiroId")
    ContaDTO toDto(Conta conta);

    @Mapping(source = "parceiroId", target = "parceiro")
    Conta toEntity(ContaDTO contaDTO);

    default Conta fromId(Long id) {
        if (id == null) {
            return null;
        }
        Conta conta = new Conta();
        conta.setId(id);
        return conta;
    }
}
