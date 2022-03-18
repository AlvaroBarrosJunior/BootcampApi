package com.bootcamp.api.exception;

public class TokenException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public TokenException() {
		
	}
	
	public TokenException(String mensagem) {
		super(mensagem);
	}
	
	public TokenException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
}
