package br.com.mrcontador.service.mapper;


import br.com.mrcontador.domain.*;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.dto.ContadorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contador} and its DTO {@link ContadorDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContadorMapper extends EntityMapper<ContadorDTO, Contador> {

//replace(/\D/g,'')

    default Contador fromId(Long id) {
        if (id == null) {
            return null;
        }
        Contador contador = new Contador();
        contador.setId(id);
        String datasource = SecurityUtils.DS_PREFIX.concat(contador.getCnpj().replaceAll("\\D", ""));
        contador.setDatasource(datasource);
        return contador;
    }
}
