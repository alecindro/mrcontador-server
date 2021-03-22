package br.com.mrcontador.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.mrcontador.domain.Sieg;

@Repository
public interface SiegRepository extends JpaRepository<Sieg, Long>, JpaSpecificationExecutor<Sieg>{
	
	@EntityGraph(attributePaths = {"contador"})
	List<Sieg> findByActive(boolean active);
}
