package br.com.improving.carrinho.exception;

public class QuantidadeInvalidaException extends RuntimeException {

	public QuantidadeInvalidaException() {
		super("Quantidade nao pode ser menor que zero");
	}

}
