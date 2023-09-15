package com.api.telefonico.api.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.telefonico.domain.model.Contato;
import com.api.telefonico.domain.repository.ContatoRepository;
import com.api.telefonico.domain.service.ContatoService;
import com.api.telefonico.exception.ContatoNaoEncontradoException;
import com.api.telefonico.exception.NegocioException;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/contatos")
public class ContatoController {

	@Autowired
	private ContatoService contatoService;

	@Autowired
	private ContatoRepository contatoRepository;

	@GetMapping
	public List<Contato> listar() {
		return contatoService.listarContato();
	}
	
	@GetMapping("/{id}")
	public Contato buscarContato(@PathVariable Long id) {
		Contato contato = contatoService.buscarOuFalhar(id);
		return contato;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Contato salvar(@Valid @RequestBody Contato contato) {
		return contatoService.salvarContato(contato);
	}

	@PutMapping("/{id}")
	public Contato atualizar(@PathVariable Long id, @RequestBody Contato contato) {
		try {
			Contato contatoAtual = contatoService.buscarOuFalhar(id);

			BeanUtils.copyProperties(contato, contatoAtual, "id");

			return contatoService.salvarContato(contatoAtual);
		} catch (ContatoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover(@PathVariable Long id) {
		if (!contatoRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		contatoRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	
	@PostMapping("/{contatoId}/favorito")
    public ResponseEntity<Void> marcarContatoComoFavorito(@PathVariable Long contatoId) {
        contatoService.marcarContatoComoFavorito(contatoId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
//	@GetMapping("/favoritos")
//    public ResponseEntity<List<Contato>> listarContatosFavoritos() {
//        List<Contato> contatos = contatoService.listarContatosFavoritos();
//        return new ResponseEntity<>(contatos, HttpStatus.OK);
//    }
	@GetMapping("/nome")
	public List<Contato> contatoPorNome(String nome){
		return contatoRepository.findByNomeContaining(nome);
	}
	
	@PutMapping("/{codigo}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarPropriedadeAtivo(@PathVariable Long codigo, @RequestBody Boolean ativo) {
		contatoService.atualizarPropriedadeAtivo(codigo, ativo);
	}
	
	@PutMapping("/{codigo}/ativo/favorito")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarFavoritoAtivo(@PathVariable Long codigo, @RequestBody Boolean ativo) {
		contatoService.atualizarFavoritoAtivo(codigo, ativo);
	}
}