package br.com.improving.carrinho;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.sun.tools.javac.util.List;

import br.com.improving.carrinho.exception.NullProdutoException;
import br.com.improving.carrinho.exception.QuantidadeInvalidaException;
import br.com.improving.carrinho.exception.ValorUnitarioInvalidoException;

class CarrinhoComprasTest {

	@Test
	void adicionarItemCarrinhoVazio() {
		//GIVEN
		Produto produto = Mockito.mock(Produto.class);
		BigDecimal valorUnitario = BigDecimal.ONE;
		int quantidade = 10;
		Item expectedItem = new Item(produto, valorUnitario, quantidade);
		BigDecimal expectedValorTotal = new BigDecimal("10.00");

		CarrinhoCompras carrinhoCompras = new CarrinhoCompras();

		//WHEN
		carrinhoCompras.adicionarItem(produto, valorUnitario, quantidade);

		//THEN
		assertThat(carrinhoCompras.getItens()).containsExactly(expectedItem);
		assertThat(carrinhoCompras.getValorTotal()).isEqualTo(expectedValorTotal);
	}

	@Test
	void adicionarMaisDeUmItemDiferenteNoCarrinho() {
		//GIVEN
		Produto produtoOne = Mockito.mock(Produto.class);
		Produto produtoTwo = Mockito.mock(Produto.class);
		Item expectedItemOne = new Item(produtoOne, BigDecimal.ONE, 10);
		Item expectedItemTwo = new Item(produtoTwo, new BigDecimal("2.5"), 2);
		BigDecimal expectedValorTotal = new BigDecimal("15.00");

		CarrinhoCompras carrinhoCompras = new CarrinhoCompras();

		//WHEN
		carrinhoCompras.adicionarItem(produtoOne, BigDecimal.ONE, 10);
		carrinhoCompras.adicionarItem(produtoTwo, new BigDecimal("2.5"), 2);

		//THEN
		assertThat(carrinhoCompras.getItens()).containsExactly(expectedItemOne, expectedItemTwo);
		assertThat(carrinhoCompras.getValorTotal()).isEqualTo(expectedValorTotal);
	}

	@Test
	void adicionarMesmoProdutoCarrinho() {
		//GIVEN
		Produto produtoOne = Mockito.mock(Produto.class);
		Item expectedItemOne = new Item(produtoOne, BigDecimal.TEN, 12);
		BigDecimal expectedValorTotal = new BigDecimal("30.00");

		CarrinhoCompras carrinhoCompras = new CarrinhoCompras();

		//WHEN
		carrinhoCompras.adicionarItem(produtoOne, BigDecimal.ONE, 10);
		carrinhoCompras.adicionarItem(produtoOne, BigDecimal.TEN, 2);

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
				arguments(Mockito.mock(Produto.class), null, 0, ValorUnitarioInvalidoException.class),
				arguments(Mockito.mock(Produto.class), new BigDecimal(" -0.01"), 0, ValorUnitarioInvalidoException.class),
				arguments(Mockito.mock(Produto.class), BigDecimal.ZERO, -1, QuantidadeInvalidaException.class)
		);
	}

	@ParameterizedTest
	@MethodSource("removerItemsPorProduto")
	void removerItemsDoCarrinhoorProduto(CarrinhoCompras carrinhoDeCompras, Produto produto,
										 boolean expectedResult, BigDecimal expectedValorTotal,
										 Item ... expectedItems) {
		//GIVEN
		CarrinhoCompras carrinho = carrinhoDeCompras;

		//WHEN
		boolean actualResult = carrinho.removerItem(produto);

		//THEN
		assertThat(actualResult).isEqualTo(expectedResult);
		assertThat(carrinho.getValorTotal()).isEqualTo(expectedValorTotal);
		assertThat(carrinho.getItens()).containsExactly(expectedItems);
	}

	static Stream<Arguments> removerItemsPorProduto() {
		Produto produtoOne = Mockito.mock(Produto.class);
		Produto produtoTwo = Mockito.mock(Produto.class);

		CarrinhoCompras carrinhoVazio = new CarrinhoCompras();

		CarrinhoCompras carrinhoUmItem = new CarrinhoCompras();
		carrinhoUmItem.adicionarItem(produtoOne, BigDecimal.ONE, 1);

		CarrinhoCompras carrinhoDoisItens = new CarrinhoCompras();
		carrinhoDoisItens.adicionarItem(produtoOne, BigDecimal.ONE, 1);
		carrinhoDoisItens.adicionarItem(produtoTwo, BigDecimal.TEN, 2);

		Item itemOne = new Item(produtoOne, BigDecimal.ONE, 1);
		Item itemTwo = new Item(produtoTwo, BigDecimal.TEN, 2);

		return Stream.of(
				arguments(carrinhoVazio, produtoOne, false, BigDecimal.ZERO, new ArrayList<>()),
				arguments(carrinhoUmItem, produtoOne, false, BigDecimal.ZERO, new ArrayList<>()),
				arguments(carrinhoDoisItens, produtoOne, true, new BigDecimal("20.00"), List.of(itemTwo)),
				arguments(carrinhoDoisItens, produtoTwo, true, BigDecimal.ONE, List.of(itemOne))
		);
	}

	@ParameterizedTest
	@MethodSource("removerItemsPorPosicao")
	void removerItemsDoCarrinhoorProduto(CarrinhoCompras carrinhoDeCompras, int posicao,
										 boolean expectedResult, BigDecimal expectedValorTotal,
										 Item ... expectedItems) {
		//GIVEN
		CarrinhoCompras carrinho = carrinhoDeCompras;

		//WHEN
		boolean actualResult = carrinho.removerItem(posicao);

		//THEN
		assertThat(actualResult).isEqualTo(expectedResult);
		assertThat(carrinho.getValorTotal()).isEqualTo(expectedValorTotal);
		assertThat(carrinho.getItens()).containsExactly(expectedItems);
	}

	static Stream<Arguments> removerItemsPorPosicao() {
		Produto produtoOne = Mockito.mock(Produto.class);
		Produto produtoTwo = Mockito.mock(Produto.class);

		CarrinhoCompras carrinhoVazio = new CarrinhoCompras();

		CarrinhoCompras carrinhoUmItem = new CarrinhoCompras();
		carrinhoUmItem.adicionarItem(produtoOne, BigDecimal.ONE, 1);

		CarrinhoCompras carrinhoDoisItens = new CarrinhoCompras();
		carrinhoDoisItens.adicionarItem(produtoOne, BigDecimal.ONE, 1);
		carrinhoDoisItens.adicionarItem(produtoTwo, BigDecimal.TEN, 2);

		Item itemOne = new Item(produtoOne, BigDecimal.ONE, 1);
		Item itemTwo = new Item(produtoTwo, BigDecimal.TEN, 2);

		return Stream.of(
				arguments(carrinhoVazio, 0, false, BigDecimal.ZERO, new ArrayList<>()),
				arguments(carrinhoUmItem, 0, false, BigDecimal.ZERO, new ArrayList<>()),
				arguments(carrinhoDoisItens, 2, false, new BigDecimal("21.00"), List.of(itemOne, itemTwo)),
				arguments(carrinhoDoisItens, 0, true, new BigDecimal("20.00"), List.of(itemTwo)),
				arguments(carrinhoDoisItens, 1, true, BigDecimal.ONE, List.of(itemOne))
		);
	}
}