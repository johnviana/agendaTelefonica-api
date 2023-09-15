package com.api.telefonico.exception;

public class ContatoExistenteException extends RuntimeException {
private static final long serialVersionUID = 1L;
	
	public ContatoExistenteException(String mensagem) {
		super(mensagem);
	}
}
