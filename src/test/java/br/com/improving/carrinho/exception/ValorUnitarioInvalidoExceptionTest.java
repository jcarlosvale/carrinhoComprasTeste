package br.com.improving.carrinho.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ValorUnitarioInvalidoExceptionTest {

	@Test
	void constructor() {
		//GIVEN
		//WHEN
		ValorUnitarioInvalidoException exception = new ValorUnitarioInvalidoException();

		//THEN
		assertThat(exception).isNotNull();
	}

}