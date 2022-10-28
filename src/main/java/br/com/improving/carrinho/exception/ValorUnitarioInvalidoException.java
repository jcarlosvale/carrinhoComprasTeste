package br.com.improving.carrinho.exception;

public class ValorUnitarioInvalidoException extends RuntimeException {

	public ValorUnitarioInvalidoException() {
		super("Valor unitario nao pode ser null ou menor que zero");
	}

}
