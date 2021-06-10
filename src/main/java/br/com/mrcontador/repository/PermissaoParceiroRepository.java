package br.com.mrcontador.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.mrcontador.domain.PermissaoParceiro;

public interface PermissaoParceiroRepository extends JpaRepository<PermissaoParceiro, Long>, JpaSpecificationExecutor<PermissaoParceiro> {

	public List<PermissaoParceiro> findByUsuario(String usuario);
}
