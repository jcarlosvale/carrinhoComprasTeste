package br.com.improving.carrinho;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class ProdutoTest {

	@Test
	void constructorBuilder() {
		//GIVEN
		final Long codigo = 1234L;
		final String descricao = "descricao";

		//WHEN
		Produto produto = new Produto(codigo, descricao);

		//THEN
		assertThat(produto.getCodigo()).isEqualTo(codigo);
		assertThat(produto.getDescricao()).isEqualTo(descricao);
	}

	@Test
	void codigoNullNotAllowed() {
		//GIVEN
		final String descricao = "descricao";

		//WHEN THEN
		assertThatThrownBy(() -> new Produto(null, descricao))
				.isInstanceOf(RuntimeException.class);
	}

	@Test
	void descricaoNullNotAllowed() {
		//GIVEN
		final Long codigo = 1234L;

		//WHEN THEN
		assertThatThrownBy(() -> new Produto(codigo, null))
				.isInstanceOf(RuntimeException.class);
	}

	@Test
	void equalsAndHashcodeContract() {
		EqualsVerifier.simple().forClass(Produto.class).withIgnoredFields("descricao")
				.verify();
	}
}