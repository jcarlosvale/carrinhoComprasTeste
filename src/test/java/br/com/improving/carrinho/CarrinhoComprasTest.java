package br.com.improving.carrinho;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import br.com.improving.carrinho.exception.NullProdutoException;
import br.com.improving.carrinho.exception.QuantidadeInvalidaException;
import br.com.improving.carrinho.exception.ValorUnitarioInvalidoException;

class CarrinhoComprasTest {

	private static final Produto PRODUTO_ONE = new Produto(1L, "produto1");
	private static final Produto PRODUTO_TWO = new Produto(2L, "produto2");
	@Test
	void adicionarItemCarrinhoVazio() {
		//GIVEN
		BigDecimal valorUnitario = BigDecimal.ONE;
		int quantidade = 10;
		Item expectedItem = new Item(PRODUTO_ONE, valorUnitario, quantidade);
		BigDecimal expectedValorTotal = new BigDecimal("10.00");

		CarrinhoCompras carrinhoCompras = new CarrinhoCompras();

		//WHEN
		carrinhoCompras.adicionarItem(PRODUTO_ONE, valorUnitario, quantidade);

		//THEN
		assertThat(carrinhoCompras.getItens()).containsExactly(expectedItem);
		assertThat(carrinhoCompras.getValorTotal()).isEqualTo(expectedValorTotal);
	}

	@Test
	void adicionarMaisDeUmItemDiferenteNoCarrinho() {
		//GIVEN
		Item expectedItemOne = new Item(PRODUTO_ONE, BigDecimal.ONE, 10);
		Item expectedItemTwo = new Item(PRODUTO_TWO, new BigDecimal("2.5"), 2);
		BigDecimal expectedValorTotal = new BigDecimal("15.00");

		CarrinhoCompras carrinhoCompras = new CarrinhoCompras();

		//WHEN
		carrinhoCompras.adicionarItem(PRODUTO_ONE, BigDecimal.ONE, 10);
		carrinhoCompras.adicionarItem(PRODUTO_TWO, new BigDecimal("2.5"), 2);

		//THEN
		assertThat(carrinhoCompras.getItens()).containsExactly(expectedItemOne, expectedItemTwo);
		assertThat(carrinhoCompras.getValorTotal()).isEqualTo(expectedValorTotal);
	}

	@Test
	void adicionarMesmoProdutoCarrinho() {
		//GIVEN
		Item expectedItemOne = new Item(PRODUTO_ONE, BigDecimal.TEN, 12);
		BigDecimal expectedValorTotal = new BigDecimal("120.00");

		CarrinhoCompras carrinhoCompras = new CarrinhoCompras();

		//WHEN
		carrinhoCompras.adicionarItem(PRODUTO_ONE, BigDecimal.ONE, 10);
		carrinhoCompras.adicionarItem(PRODUTO_ONE, BigDecimal.TEN, 2);

		//THEN
		assertThat(carrinhoCompras.getItens()).containsExactly(expectedItemOne);
		assertThat(carrinhoCompras.getValorTotal()).isEqualTo(expectedValorTotal);
	}

	@ParameterizedTest
	@MethodSource("camposInvalidos")
	void adicionarValoresInvalidosCarrinho(final Produto produto, final BigDecimal valorUnitario,
										   final int quantidade, final Class<?> expectedClass) {
		//GIVEN
		CarrinhoCompras carrinhoCompras = new CarrinhoCompras();

		//WHEN
		//THEN
		assertThatThrownBy(() -> carrinhoCompras.adicionarItem(produto, valorUnitario, quantidade))
				.isInstanceOf(expectedClass);
	}

	static Stream<Arguments> camposInvalidos() {
		return Stream.of(
				arguments(null, BigDecimal.ZERO, 1, NullProdutoException.class),
				arguments(PRODUTO_ONE, null, 0, ValorUnitarioInvalidoException.class),
				arguments(PRODUTO_ONE, new BigDecimal("-0.01"), 0, ValorUnitarioInvalidoException.class),
				arguments(PRODUTO_ONE, BigDecimal.ZERO, -1, QuantidadeInvalidaException.class)
		);
	}

	@ParameterizedTest
	@MethodSource("removerItemsPorProduto")
	void removerItemsDoCarrinhoorProduto(CarrinhoCompras carrinhoDeCompras, Produto produto,
										 boolean expectedResult, BigDecimal expectedValorTotal) {
		//GIVEN
		CarrinhoCompras carrinho = carrinhoDeCompras;

		//WHEN
		boolean actualResult = carrinho.removerItem(produto);

		//THEN
		assertThat(actualResult).isEqualTo(expectedResult);
		assertThat(carrinho.getValorTotal()).isEqualTo(expectedValorTotal);
	}

	static Stream<Arguments> removerItemsPorProduto() {

		CarrinhoCompras carrinhoVazio = new CarrinhoCompras();

		CarrinhoCompras carrinhoUmItem = new CarrinhoCompras();
		carrinhoUmItem.adicionarItem(PRODUTO_ONE, BigDecimal.ONE, 1);

		CarrinhoCompras carrinhoDoisItensRemoveFirst = new CarrinhoCompras();
		carrinhoDoisItensRemoveFirst.adicionarItem(PRODUTO_ONE, BigDecimal.ONE, 1);
		carrinhoDoisItensRemoveFirst.adicionarItem(PRODUTO_TWO, BigDecimal.TEN, 2);

		CarrinhoCompras carrinhoDoisItensRemoveSecond = new CarrinhoCompras();
		carrinhoDoisItensRemoveSecond.adicionarItem(PRODUTO_ONE, BigDecimal.ONE, 1);
		carrinhoDoisItensRemoveSecond.adicionarItem(PRODUTO_TWO, BigDecimal.TEN, 2);

		return Stream.of(
				arguments(carrinhoVazio, PRODUTO_ONE, false, new BigDecimal("0.00")),
				arguments(carrinhoUmItem, PRODUTO_ONE, true, new BigDecimal("0.00")),
				arguments(carrinhoDoisItensRemoveFirst, PRODUTO_ONE, true, new BigDecimal("20.00")),
				arguments(carrinhoDoisItensRemoveSecond, PRODUTO_TWO, true, new BigDecimal("1.00"))
		);
	}

	@ParameterizedTest
	@MethodSource("removerItemsPorPosicao")
	void removerItemsDoCarrinhoorProduto(CarrinhoCompras carrinhoDeCompras, int posicao,
										 boolean expectedResult, BigDecimal expectedValorTotal) {
		//GIVEN
		CarrinhoCompras carrinho = carrinhoDeCompras;

		//WHEN
		boolean actualResult = carrinho.removerItem(posicao);

		//THEN
		assertThat(actualResult).isEqualTo(expectedResult);
		assertThat(carrinho.getValorTotal()).isEqualTo(expectedValorTotal);
	}

	static Stream<Arguments> removerItemsPorPosicao() {
		CarrinhoCompras carrinhoVazio = new CarrinhoCompras();

		CarrinhoCompras carrinhoUmItem = new CarrinhoCompras();
		carrinhoUmItem.adicionarItem(PRODUTO_ONE, BigDecimal.ONE, 1);

		CarrinhoCompras carrinhoDoisItensRemoveFirst = new CarrinhoCompras();
		carrinhoDoisItensRemoveFirst.adicionarItem(PRODUTO_ONE, BigDecimal.ONE, 1);
		carrinhoDoisItensRemoveFirst.adicionarItem(PRODUTO_TWO, BigDecimal.TEN, 2);

		CarrinhoCompras carrinhoDoisItensRemoveSecond = new CarrinhoCompras();
		carrinhoDoisItensRemoveSecond.adicionarItem(PRODUTO_ONE, BigDecimal.ONE, 1);
		carrinhoDoisItensRemoveSecond.adicionarItem(PRODUTO_TWO, BigDecimal.TEN, 2);

		return Stream.of(
				arguments(carrinhoVazio, 0, false, new BigDecimal("0.00")),
				arguments(carrinhoUmItem, 0, true, new BigDecimal("0.00")),
				arguments(carrinhoDoisItensRemoveFirst, 2, false, new BigDecimal("21.00")),
				arguments(carrinhoDoisItensRemoveFirst, 0, true, new BigDecimal("20.00")),
				arguments(carrinhoDoisItensRemoveSecond, 1, true, new BigDecimal("1.00"))
		);
	}
}