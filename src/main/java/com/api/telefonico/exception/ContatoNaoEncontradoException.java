package com.api.telefonico.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ContatoNaoEncontradoException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public ContatoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}

}
