package br.com.mrcontador.service.mapper;


import org.mapstruct.Mapper;

import br.com.mrcontador.domain.Contador;
import br.com.mrcontador.security.SecurityUtils;
import br.com.mrcontador.service.dto.ContadorDTO;
import br.com.mrcontador.util.MrContadorUtil;

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
        String datasource = SecurityUtils.DS_PREFIX.concat(MrContadorUtil.onlyNumbers(contador.getCnpj()));
        contador.setDatasource(datasource);
        return contador;
    }
}
