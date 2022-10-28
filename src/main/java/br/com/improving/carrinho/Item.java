package br.com.improving.carrinho;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Classe que representa um item no carrinho de compras.
 */
public class Item {

	private final Produto produto;
    private final BigDecimal valorUnitario;
    private final int quantidade;

    /**
     * Construtor da classe Item.
     * 
     * @param produto
     * @param valorUnitario
     * @param quantidade
     */
    public Item(Produto produto, BigDecimal valorUnitario, int quantidade) {
		checkArgument((valorUnitario != null) && (valorUnitario.compareTo(BigDecimal.ZERO) >= 0),
				"Valor unitario nao deve ser null ou negativo");
		checkArgument(quantidade >= 0, "Quantidade deve ser maior ou igual a zero");
		this.produto = checkNotNull(produto, "Produto nao deve ser null");
		this.valorUnitario = setScale(valorUnitario);
		this.quantidade = quantidade;
    }

	/**
     * Retorna o produto.
     *
     * @return Produto
     */
    public Produto getProduto() {
		return this.produto;
    }

    /**
     * Retorna o valor unitário do item.
     *
     * @return BigDecimal
     */
    public BigDecimal getValorUnitario() {
		return this.valorUnitario;
    }

    /**
     * Retorna a quantidade dos item.
     *
     * @return int
     */
    public int getQuantidade() {
		return this.quantidade;
    }

    /**
     * Retorna o valor total do item.
     *
     * @return BigDecimal
     */
    public BigDecimal getValorTotal() {
		return this.valorUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

	private BigDecimal setScale(BigDecimal valor) {
		return valor.setScale(2, RoundingMode.DOWN);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final Item item = (Item) o;
		return quantidade == item.quantidade && Objects.equals(produto, item.produto) && Objects.equals(valorUnitario, item.valorUnitario);
	}

	@Override
	public int hashCode() {
		return Objects.hash(produto, valorUnitario, quantidade);
	}
}
