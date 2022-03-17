package com.bootcamp.api.exception;

public class BootcampException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public BootcampException() {
		
	}
	
	public BootcampException(String mensagem) {
		super(mensagem);
	}
	
	public BootcampException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}

}
