package br.com.improving.carrinho;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import br.com.improving.carrinho.exception.NullProdutoException;
import br.com.improving.carrinho.exception.QuantidadeInvalidaException;
import br.com.improving.carrinho.exception.ValorUnitarioInvalidoException;

/**
 * Classe que representa o carrinho de compras de um cliente.
 */
public class CarrinhoCompras {

	private final List<Produto> produtos;
	private final Map<Produto, Item> itemMap;

	public CarrinhoCompras() {
		this.itemMap = new HashMap<>();
		this.produtos = new LinkedList<>();
	}

	/**
     * Permite a adição de um novo item no carrinho de compras.
     *
     * Caso o item já exista no carrinho para este mesmo produto, as seguintes regras deverão ser seguidas:
     * - A quantidade do item deverá ser a soma da quantidade atual com a quantidade passada como parâmetro.
     * - Se o valor unitário informado for diferente do valor unitário atual do item, o novo valor unitário do item deverá ser
     * o passado como parâmetro.
     *
     * Devem ser lançadas subclasses de RuntimeException caso não seja possível adicionar o item ao carrinho de compras.
     *
     * @param produto
     * @param valorUnitario
     * @param quantidade
     */
    public void adicionarItem(Produto produto, BigDecimal valorUnitario, int quantidade) {

		validateProduto(produto);
		validateValorUnitario(valorUnitario);
		validateQuantidade(quantidade);

		Item newItem = new Item(produto, valorUnitario, quantidade);

		if (!itemMap.containsKey(produto)) {
			produtos.add(produto);
		}

		itemMap.merge(produto, newItem,
				(item, item2) -> new Item(produto, valorUnitario, item.getQuantidade() + item2.getQuantidade()));
    }

	private void validateQuantidade(final int quantidade) {
		if (quantidade < 0) {
			throw new QuantidadeInvalidaException();
		}
	}

	private void validateValorUnitario(final BigDecimal valorUnitario) {
		if (Objects.isNull(valorUnitario) || (valorUnitario.compareTo(BigDecimal.ZERO) < 0)) {
			throw new ValorUnitarioInvalidoException();
		}
	}

	private void validateProduto(final Produto produto) {
		if (Objects.isNull(produto)) {
			throw new NullProdutoException();
		}
	}

	/**
     * Permite a remoção do item que representa este produto do carrinho de compras.
     *
     * @param produto
     * @return Retorna um boolean, tendo o valor true caso o produto exista no carrinho de compras e false
     * caso o produto não exista no carrinho.
     */
    public boolean removerItem(Produto produto) {
		if (Objects.nonNull(itemMap.remove(produto))) {
			return produtos.remove(produto);
		}
		return false;
    }

    /**
     * Permite a remoção do item de acordo com a posição.
     * Essa posição deve ser determinada pela ordem de inclusão do produto na 
     * coleção, em que zero representa o primeiro item.
     *
     * @param posicaoItem
     * @return Retorna um boolean, tendo o valor true caso o produto exista no carrinho de compras e false
     * caso o produto não exista no carrinho.
     */
    public boolean removerItem(int posicaoItem) {
		if (posicaoItem < produtos.size()) {
			return removerItem(produtos.get(posicaoItem));
		}
		return false;
    }

    /**
     * Retorna o valor total do carrinho de compras, que deve ser a soma dos valores totais
     * de todos os itens que compõem o carrinho.
     *
     * @return BigDecimal
     */
    public BigDecimal getValorTotal() {
		return itemMap
				.values()
				.stream()
				.map(Item::getValorTotal)
				.reduce(BigDecimal.ZERO, BigDecimal::add)
				.setScale(2, RoundingMode.HALF_EVEN);
    }

    /**
     * Retorna a lista de itens do carrinho de compras.
     *
     * @return itens
     */
    public Collection<Item> getItens() {
		return produtos.stream()
				.map(itemMap::get)
				.collect(Collectors.toList());
    }
}