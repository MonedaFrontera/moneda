package org.domain.moneda.entity;

// Generated 7/10/2015 05:14:39 PM by Hibernate Tools 3.2.4.GA

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.validator.NotNull;

/**
 * Porcentajebancopais generated by hbm2java
 */
@Entity
@Table(name = "porcentajebancopais", uniqueConstraints = @UniqueConstraint(columnNames = "fecha"))
public class Porcentajebancopais implements java.io.Serializable {

	private PorcentajebancopaisId id;
	private Pais pais;
	private Banco banco;
	private Short porcentajeCliente;
	private Short porcentajeOficina;

	public Porcentajebancopais() {
	}

	public Porcentajebancopais(PorcentajebancopaisId id, Pais pais, Banco banco) {
		this.id = id;
		this.pais = pais;
		this.banco = banco;
	}

	public Porcentajebancopais(PorcentajebancopaisId id, Pais pais,
			Banco banco, Short porcentajeCliente, Short porcentajeOficina) {
		this.id = id;
		this.pais = pais;
		this.banco = banco;
		this.porcentajeCliente = porcentajeCliente;
		this.porcentajeOficina = porcentajeOficina;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "codbanco", column = @Column(name = "codbanco", nullable = false, length = 3)),
			@AttributeOverride(name = "codigopais", column = @Column(name = "codigopais", nullable = false, length = 5)),
			@AttributeOverride(name = "fecha", column = @Column(name = "fecha", unique = true, nullable = false, length = 29)) })
	@NotNull
	public PorcentajebancopaisId getId() {
		return this.id;
	}

	public void setId(PorcentajebancopaisId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigopais", nullable = false, insertable = false, updatable = false)
	@NotNull
	public Pais getPais() {
		return this.pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codbanco", nullable = false, insertable = false, updatable = false)
	@NotNull
	public Banco getBanco() {
		return this.banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	@Column(name = "porcentaje_cliente", precision = 4, scale = 0)
	public Short getPorcentajeCliente() {
		return this.porcentajeCliente;
	}

	public void setPorcentajeCliente(Short porcentajeCliente) {
		this.porcentajeCliente = porcentajeCliente;
	}

	@Column(name = "porcentaje_oficina", precision = 4, scale = 0)
	public Short getPorcentajeOficina() {
		return this.porcentajeOficina;
	}

	public void setPorcentajeOficina(Short porcentajeOficina) {
		this.porcentajeOficina = porcentajeOficina;
	}

}
