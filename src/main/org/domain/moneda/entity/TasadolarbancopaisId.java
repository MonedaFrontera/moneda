package org.domain.moneda.entity;

// Generated 7/10/2015 05:14:39 PM by Hibernate Tools 3.2.4.GA

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * TasadolarbancopaisId generated by hbm2java
 */
@Embeddable
public class TasadolarbancopaisId implements java.io.Serializable {

	private String codbanco;
	private String codigopais;
	private Date fecha;

	public TasadolarbancopaisId() {
	}

	public TasadolarbancopaisId(String codbanco, String codigopais, Date fecha) {
		this.codbanco = codbanco;
		this.codigopais = codigopais;
		this.fecha = fecha;
	}

	@Column(name = "codbanco", nullable = false, length = 3)
	@NotNull
	@Length(max = 3)
	public String getCodbanco() {
		return this.codbanco;
	}

	public void setCodbanco(String codbanco) {
		this.codbanco = codbanco;
	}

	@Column(name = "codigopais", nullable = false, length = 5)
	@NotNull
	@Length(max = 5)
	public String getCodigopais() {
		return this.codigopais;
	}

	public void setCodigopais(String codigopais) {
		this.codigopais = codigopais;
	}

	@Column(name = "fecha", unique = true, nullable = false, length = 29)
	@NotNull
	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TasadolarbancopaisId))
			return false;
		TasadolarbancopaisId castOther = (TasadolarbancopaisId) other;

		return ((this.getCodbanco() == castOther.getCodbanco()) || (this
				.getCodbanco() != null
				&& castOther.getCodbanco() != null && this.getCodbanco()
				.equals(castOther.getCodbanco())))
				&& ((this.getCodigopais() == castOther.getCodigopais()) || (this
						.getCodigopais() != null
						&& castOther.getCodigopais() != null && this
						.getCodigopais().equals(castOther.getCodigopais())))
				&& ((this.getFecha() == castOther.getFecha()) || (this
						.getFecha() != null
						&& castOther.getFecha() != null && this.getFecha()
						.equals(castOther.getFecha())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getCodbanco() == null ? 0 : this.getCodbanco().hashCode());
		result = 37
				* result
				+ (getCodigopais() == null ? 0 : this.getCodigopais()
						.hashCode());
		result = 37 * result
				+ (getFecha() == null ? 0 : this.getFecha().hashCode());
		return result;
	}

}
