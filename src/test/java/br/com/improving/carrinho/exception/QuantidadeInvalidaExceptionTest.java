package br.com.improving.carrinho.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class QuantidadeInvalidaExceptionTest {

	@Test
	void constructor() {
		//GIVEN
		//WHEN
		QuantidadeInvalidaException exception = new QuantidadeInvalidaException();

		//THEN
		assertThat(exception).isNotNull();
	}

}