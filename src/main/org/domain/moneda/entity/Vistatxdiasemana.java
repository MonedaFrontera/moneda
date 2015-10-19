package org.domain.moneda.entity;

// Generated 27/04/2015 11:47:31 AM by Hibernate Tools 3.2.4.GA

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Vistatxdiasemana generated by hbm2java
 */
@Entity
@Table(name = "vistatxdiasemana")
public class Vistatxdiasemana implements java.io.Serializable {

	private VistatxdiasemanaId id;

	public Vistatxdiasemana() {
	}

	public Vistatxdiasemana(VistatxdiasemanaId id) {
		this.id = id;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "codigodia", column = @Column(name = "codigodia")),
			@AttributeOverride(name = "codigomes", column = @Column(name = "codigomes")),
			@AttributeOverride(name = "fechatx", column = @Column(name = "fechatx", length = 29)),
			@AttributeOverride(name = "valortxpesos", column = @Column(name = "valortxpesos", precision = 10)),
			@AttributeOverride(name = "promotor", column = @Column(name = "promotor", length = 15)),
			@AttributeOverride(name = "asesor", column = @Column(name = "asesor", length = 15)) })
	public VistatxdiasemanaId getId() {
		return this.id;
	}

	public void setId(VistatxdiasemanaId id) {
		this.id = id;
	}

}
