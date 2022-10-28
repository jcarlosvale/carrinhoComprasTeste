package br.com.improving.carrinho;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class CarrinhoComprasFactoryTest {

	@Test
	void criarCarrinhoComClienteNull() {
		//GIVEN
		CarrinhoComprasFactory factory = new CarrinhoComprasFactory();
		//WHEN
		//THEN
		assertThatThrownBy(() -> factory.criar(null))
				.isInstanceOf(RuntimeException.class);
	}

	@Test
	void criarCarrinhoNovoCliente() {
		//GIVEN
		String identificacao = "novo cliente";
		CarrinhoComprasFactory factory = new CarrinhoComprasFactory();

		//WHEN
		CarrinhoCompras carrinhoCompras = factory.criar(identificacao);

		//THEN
		assertThat(carrinhoCompras.getValorTotal()).isEqualTo(new BigDecimal("0.00"));
		assertThat(carrinhoCompras.getItens()).isEmpty();
	}

	@Test
	void criarCarrinhoClienteExistente() {
		//GIVEN
		String identificacao = "novo cliente";
		CarrinhoComprasFactory factory = new CarrinhoComprasFactory();
		CarrinhoCompras expected = factory.criar(identificacao);

		//WHEN
		CarrinhoCompras actual = factory.criar(identificacao);

		//THEN
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void getValorTicketMedioSemCarrinho() {
		//GIVEN
		CarrinhoComprasFactory factory = new CarrinhoComprasFactory();

		//WHEN
		BigDecimal valorMedio = factory.getValorTicketMedio();

		//THEN
		assertThat(valorMedio).isEqualTo(new BigDecimal("0.00"));
	}

	@Test
	void getValorTicketMedioUnicoCliente() {
		//GIVEN
		Produto produtoOne = new Produto(1L, "produto1");
		Produto produtoTwo = new Produto(2L, "produto2");

		CarrinhoComprasFactory factory = new CarrinhoComprasFactory();

		CarrinhoCompras carrinhoCompras = factory.criar("cliente");
		carrinhoCompras.adicionarItem(produtoOne, new BigDecimal("5"), 2);
		carrinhoCompras.adicionarItem(produtoTwo, new BigDecimal("2.5"), 2);

		//WHEN
		BigDecimal valorMedio = factory.getValorTicketMedio();

		//THEN
		assertThat(valorMedio).isEqualTo(new BigDecimal("15.00"));
	}

	@Test
	void getValorTicketMedioMultiplosClientes() {
		//GIVEN
		Produto produtoOne = new Produto(1L, "produto1");

		CarrinhoComprasFactory factory = new CarrinhoComprasFactory();

		CarrinhoCompras carrinhoComprasOne = factory.criar("cliente1");
		carrinhoComprasOne.adicionarItem(produtoOne, BigDecimal.ONE, 2);

		CarrinhoCompras carrinhoComprasTwo = factory.criar("cliente2");
		carrinhoComprasTwo.adicionarItem(produtoOne, BigDecimal.ONE, 2);

		CarrinhoCompras carrinhoComprasThree = factory.criar("cliente3");
		carrinhoComprasThree.adicionarItem(produtoOne, BigDecimal.ONE, 1);

		//WHEN
		BigDecimal valorMedio = factory.getValorTicketMedio();

		//THEN
		assertThat(valorMedio).isEqualTo(new BigDecimal("1.67"));
	}

	@Test
	void invalidarCarrinhoComClienteNull() {
		//GIVEN
		CarrinhoComprasFactory factory = new CarrinhoComprasFactory();
		//WHEN
		//THEN
		assertThatThrownBy(() -> factory.invalidar(null))
				.isInstanceOf(RuntimeException.class);
	}

	@Test
	void invalidarCarrinhoClienteExistente() {
		//GIVEN
		String identificacao = "novo cliente";
		CarrinhoComprasFactory factory = new CarrinhoComprasFactory();
		factory.criar(identificacao);

		//WHEN
		boolean actual = factory.invalidar(identificacao);

		//THEN
		assertThat(actual).isTrue();
	}

	@Test
	void invalidarCarrinhoClienteInexistente() {
		//GIVEN
		String identificacao = "novo cliente";
		CarrinhoComprasFactory factory = new CarrinhoComprasFactory();
		factory.criar(identificacao);

		//WHEN
		boolean actual = factory.invalidar("outro cliente");

		//THEN
		assertThat(actual).isFalse();
	}
}