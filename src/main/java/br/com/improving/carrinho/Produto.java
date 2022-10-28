package br.com.improving.carrinho;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

/**
 * Classe que representa um produto que pode ser adicionado
 * como item ao carrinho de compras.
 *
 * Importante: Dois produtos são considerados iguais quando ambos possuem o
 * mesmo código.
 */
public class Produto {

    private final Long codigo;
    private final String descricao;

    /**
     * Construtor da classe Produto.
     *
     * @param codigo
     * @param descricao
     */
    public Produto(Long codigo, String descricao) {

		this.codigo = checkNotNull(codigo, "Codigo nao deve ser null");
		this.descricao = checkNotNull(descricao, "Descricao nao deve ser null");
    }

    /**
     * Retorna o código da produto.
     *
     * @return Long
     */
    public Long getCodigo() {
		return this.codigo;
    }

    /**
     * Retorna a descrição do produto.
     *
     * @return String
     */
    public String getDescricao() {
		return this.descricao;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final Produto produto = (Produto) o;
		return Objects.equals(codigo, produto.codigo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigo);
	}
}