package br.com.improving.carrinho.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NullProdutoExceptionTest {

	@Test
	void constructor() {
		//GIVEN
		//WHEN
		NullProdutoException exception = new NullProdutoException();

		//THEN
		assertThat(exception).isNotNull();
	}

}