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
import org.mockito.Mockito;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class ItemTest {

	private static final Produto PRODUTO = Mockito.mock(Produto.class);
	private static final int QUANTIDADE = 10;


	@ParameterizedTest
	@MethodSource("camposValidos")
	void constructorBuilder(final BigDecimal valorUnitario,
							final BigDecimal expectedValorUnitario) {
		//GIVEN
		//WHEN
		Item item = new Item(PRODUTO, valorUnitario, QUANTIDADE);

		//THEN
		assertThat(item.getProduto()).isEqualTo(PRODUTO);
		assertThat(item.getValorUnitario()).isEqualTo(expectedValorUnitario);
		assertThat(item.getQuantidade()).isEqualTo(QUANTIDADE);
	}

	static Stream<Arguments> camposValidos() {
		return Stream.of(
				arguments(BigDecimal.ZERO, new BigDecimal("0.00")),
				arguments(new BigDecimal("9.999999"), new BigDecimal("9.99"))
		);
	}

	@ParameterizedTest
	@MethodSource("camposInvalidos")
	void constructorBuilder(final Produto produto,
							final BigDecimal valorUnitario,
							final int quantidade,
							final String expectedMessage) {
		//GIVEN
		//WHEN
		//THEN
		assertThatThrownBy(() -> new Item(produto, valorUnitario, quantidade))
				.isInstanceOf(RuntimeException.class)
				.hasMessage(expectedMessage);
	}

	static Stream<Arguments> camposInvalidos() {
		return Stream.of(
				arguments(null, BigDecimal.ZERO, QUANTIDADE, "Produto nao deve ser null"),
				arguments(PRODUTO, null, QUANTIDADE, "Valor unitario nao deve ser null ou negativo"),
				arguments(PRODUTO, BigDecimal.ZERO, -1, "Quantidade deve ser maior ou igual a zero"),
				arguments(PRODUTO, new BigDecimal("-0.01"), QUANTIDADE, "Valor unitario nao deve ser null ou negativo")
		);
	}

	@ParameterizedTest
	@MethodSource("valorTotal")
	void constructorBuilder(final int quantidade,
						    final BigDecimal valorUnitario,
							final BigDecimal expectedValorTotal) {
		//GIVEN
		//WHEN
		Item item = new Item(PRODUTO, valorUnitario, quantidade);

		//THEN
		assertThat(item.getValorTotal()).isEqualTo(expectedValorTotal);
	}

	static Stream<Arguments> valorTotal() {
		return Stream.of(
				arguments(9, new BigDecimal("1.11"), new BigDecimal("9.99")),
				arguments(5, new BigDecimal("0.99"), new BigDecimal("4.95"))
		);
	}

	@Test
	void equalsAndHashcodeContract() {
		EqualsVerifier.simple().forClass(Item.class).suppress(Warning.BIGDECIMAL_EQUALITY)
				.verify();
	}

}