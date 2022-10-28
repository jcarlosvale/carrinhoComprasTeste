package br.com.improving.carrinho.exception;

public class NullProdutoException extends RuntimeException {

	public NullProdutoException() {
		super("Produto nao pode ser null");
	}
}
