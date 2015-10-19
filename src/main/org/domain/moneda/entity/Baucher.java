package org.domain.moneda.entity;

// Generated 10/01/2012 06:26:13 PM by Hibernate Tools 3.2.4.GA

import java.math.BigDecimal;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.validator.NotNull;

/**
 * Baucher generated by hbm2java
 */
@Entity
@Table(name = "baucher")
public class Baucher implements java.io.Serializable {

	private BaucherId id;
	private Transaccion transaccion;
	private BigDecimal valor;

	public Baucher() {
	}

	public Baucher(BaucherId id, Transaccion transaccion) {
		this.id = id;
		this.transaccion = transaccion;
	}

	public Baucher(BaucherId id, Transaccion transaccion, BigDecimal valor) {
		this.id = id;
		this.transaccion = transaccion;
		this.valor = valor;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "consecutivo", column = @Column(name = "consecutivo", nullable = false)),
			@AttributeOverride(name = "numautorizacion", column = @Column(name = "numautorizacion", nullable = false, length = 8)) })
	@NotNull
	public BaucherId getId() {
		return this.id;
	}

	public void setId(BaucherId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "consecutivo", nullable = false, insertable = false, updatable = false)
	@NotNull
	public Transaccion getTransaccion() {
		return this.transaccion;
	}

	public void setTransaccion(Transaccion transaccion) {
		this.transaccion = transaccion;
	}

	@Column(name = "valor", precision = 10)
	public BigDecimal getValor() {
		return this.valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

}
