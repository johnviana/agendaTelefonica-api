package com.api.telefonico.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.api.telefonico.domain.model.Contato;
import com.api.telefonico.domain.repository.ContatoRepository;
import com.api.telefonico.exception.ContatoNaoEncontradoException;
import com.api.telefonico.exception.NegocioException;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;


@Service
public class ContatoService {
	
	@Autowired
	private ContatoRepository contatoRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	public List<Contato> listarContato(){
		return contatoRepository.findAll();
	}
	
	public Optional<Contato> burcarContato(Long id) {
		 return contatoRepository.findById(id);
		}
	
	@Transactional
	public Contato salvarContato(Contato contato) {
		entityManager.detach(contato);
		Optional<Contato> emailExistente = contatoRepository.findByEmail(contato.getEmail());
        Optional<Contato> contatoExistente = contatoRepository.findByCelular(contato.getCelular());
        
        if (contatoExistente.isPresent()) {
        	throw new NegocioException(
					String.format("Já existe um numero celular cadastrado no banco de dados %s", contato.getCelular()));
        }
        
        if (emailExistente.isPresent()) {
        	throw new NegocioException(
					String.format("Já existe um Email no banco de dados %s", contato.getEmail()));
        }
		return contatoRepository.save(contato);
	}
	
	@Transactional
	public void excluirContato(Long id) {
		try {
			contatoRepository.deleteById(id);
			contatoRepository.flush();
			
		} catch (EmptyResultDataAccessException e) {
			throw new ContatoNaoEncontradoException(
					String.format("Codigo de Contato não Encontrado", id));
			} 
		
	}
	
	
	public Contato buscarOuFalhar(Long id) {
		return contatoRepository.findById(id)
				.orElseThrow(() -> new ContatoNaoEncontradoException(
						String.format("Codigo de Contato não Encontrado", id)));
		
	}
	
	@Transactional
	public void ativar(Long id) {
		Contato contatoAtual = buscarOuFalhar(id);
		contatoAtual.ativar();
	}

	@Transactional
	public void inativar(Long id) {
		Contato contatoAtual = buscarOuFalhar(id);
		contatoAtual.inativar();
	}
	
	public List<Contato> listarContatosFavoritos() {
        return contatoRepository.findByFavoritoTrue();
    }
	
	public void marcarContatoComoFavorito(Long contatoId) {
        Optional<Contato> contatoOptional = contatoRepository.findById(contatoId);
        if (contatoOptional.isPresent()) {
            Contato contato = contatoOptional.get();
            contato.setFavorito(true);
            contatoRepository.save(contato);
        } else {
            throw new ContatoNaoEncontradoException("Contato não encontrado.");
        }
    }
	
	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		Contato contatoSalva = buscarOuFalhar(codigo);
		contatoSalva.setAtivo(ativo);
		contatoRepository.save(contatoSalva);
	}
	
	public void atualizarFavoritoAtivo(Long codigo, Boolean ativo) {
		Contato contatoSalva = buscarOuFalhar(codigo);
		contatoSalva.setFavorito(ativo);
		contatoRepository.save(contatoSalva);
	}


}
