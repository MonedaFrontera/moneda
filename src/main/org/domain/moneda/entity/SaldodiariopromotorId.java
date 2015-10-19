package org.domain.moneda.entity;

// Generated 27/04/2015 11:47:31 AM by Hibernate Tools 3.2.4.GA

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * SaldodiariopromotorId generated by hbm2java
 */
@Embeddable
public class SaldodiariopromotorId implements java.io.Serializable {

	private String documento;
	private Date fecha;

	public SaldodiariopromotorId() {
	}

	public SaldodiariopromotorId(String documento, Date fecha) {
		this.documento = documento;
		this.fecha = fecha;
	}

	@Column(name = "documento", nullable = false, length = 15)
	@NotNull
	@Length(max = 15)
	public String getDocumento() {
		return this.documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	@Column(name = "fecha", nullable = false, length = 13)
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
		if (!(other instanceof SaldodiariopromotorId))
			return false;
		SaldodiariopromotorId castOther = (SaldodiariopromotorId) other;

		return ((this.getDocumento() == castOther.getDocumento()) || (this
				.getDocumento() != null
				&& castOther.getDocumento() != null && this.getDocumento()
				.equals(castOther.getDocumento())))
				&& ((this.getFecha() == castOther.getFecha()) || (this
						.getFecha() != null
						&& castOther.getFecha() != null && this.getFecha()
						.equals(castOther.getFecha())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getDocumento() == null ? 0 : this.getDocumento().hashCode());
		result = 37 * result
				+ (getFecha() == null ? 0 : this.getFecha().hashCode());
		return result;
	}

}
