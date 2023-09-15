package com.api.telefonico.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.telefonico.domain.model.Contato;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long> {
	
    Optional<Contato> findByCelular(String celular);
    
    List<Contato> findByFavoritoTrue(); 
    
    List<Contato> findByNomeContaining(String nome);
    
	Optional<Contato> findByEmail(String email);




}
