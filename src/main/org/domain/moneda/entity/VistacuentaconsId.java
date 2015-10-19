package org.domain.moneda.entity;

// Generated 2/03/2013 03:06:19 PM by Hibernate Tools 3.2.4.GA

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.hibernate.validator.Length;

/**
 * VistacuentaconsId generated by hbm2java
 */
@Embeddable
public class VistacuentaconsId implements java.io.Serializable {

	private String numcuenta;
	private String nombre;
	private Date fecha;
	private BigDecimal creditos;
	private BigDecimal debitos;
	private String detalle;
	private String nombrebanco;
	private String codbanco;
	private BigDecimal saldo;
	private Long item;

	public VistacuentaconsId() {
	}

	public VistacuentaconsId(String numcuenta, String nombre, Date fecha,
			BigDecimal creditos, BigDecimal debitos, String detalle,
			String nombrebanco, String codbanco, BigDecimal saldo, Long item) {
		this.numcuenta = numcuenta;
		this.nombre = nombre;
		this.fecha = fecha;
		this.creditos = creditos;
		this.debitos = debitos;
		this.detalle = detalle;
		this.nombrebanco = nombrebanco;
		this.codbanco = codbanco;
		this.saldo = saldo;
		this.item = item;
	}

	@Column(name = "numcuenta", length = 25)
	@Length(max = 25)
	public String getNumcuenta() {
		return this.numcuenta;
	}

	public void setNumcuenta(String numcuenta) {
		this.numcuenta = numcuenta;
	}

	@Column(name = "nombre", length = 100)
	@Length(max = 100)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "fecha", length = 29)
	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Column(name = "creditos", precision = 131089, scale = 0)
	public BigDecimal getCreditos() {
		return this.creditos;
	}

	public void setCreditos(BigDecimal creditos) {
		this.creditos = creditos;
	}

	@Column(name = "debitos", precision = 131089, scale = 0)
	public BigDecimal getDebitos() {
		return this.debitos;
	}

	public void setDebitos(BigDecimal debitos) {
		this.debitos = debitos;
	}

	@Column(name = "detalle")
	public String getDetalle() {
		return this.detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	@Column(name = "nombrebanco", length = 50)
	@Length(max = 50)
	public String getNombrebanco() {
		return this.nombrebanco;
	}

	public void setNombrebanco(String nombrebanco) {
		this.nombrebanco = nombrebanco;
	}

	@Column(name = "codbanco", length = 3)
	@Length(max = 3)
	public String getCodbanco() {
		return this.codbanco;
	}

	public void setCodbanco(String codbanco) {
		this.codbanco = codbanco;
	}

	@Column(name = "saldo", precision = 131089, scale = 0)
	public BigDecimal getSaldo() {
		return this.saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	@Column(name = "item")
	public Long getItem() {
		return this.item;
	}

	public void setItem(Long item) {
		this.item = item;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof VistacuentaconsId))
			return false;
		VistacuentaconsId castOther = (VistacuentaconsId) other;

		return ((this.getNumcuenta() == castOther.getNumcuenta()) || (this
				.getNumcuenta() != null
				&& castOther.getNumcuenta() != null && this.getNumcuenta()
				.equals(castOther.getNumcuenta())))
				&& ((this.getNombre() == castOther.getNombre()) || (this
						.getNombre() != null
						&& castOther.getNombre() != null && this.getNombre()
						.equals(castOther.getNombre())))
				&& ((this.getFecha() == castOther.getFecha()) || (this
						.getFecha() != null
						&& castOther.getFecha() != null && this.getFecha()
						.equals(castOther.getFecha())))
				&& ((this.getCreditos() == castOther.getCreditos()) || (this
						.getCreditos() != null
						&& castOther.getCreditos() != null && this
						.getCreditos().equals(castOther.getCreditos())))
				&& ((this.getDebitos() == castOther.getDebitos()) || (this
						.getDebitos() != null
						&& castOther.getDebitos() != null && this.getDebitos()
						.equals(castOther.getDebitos())))
				&& ((this.getDetalle() == castOther.getDetalle()) || (this
						.getDetalle() != null
						&& castOther.getDetalle() != null && this.getDetalle()
						.equals(castOther.getDetalle())))
				&& ((this.getNombrebanco() == castOther.getNombrebanco()) || (this
						.getNombrebanco() != null
						&& castOther.getNombrebanco() != null && this
						.getNombrebanco().equals(castOther.getNombrebanco())))
				&& ((this.getCodbanco() == castOther.getCodbanco()) || (this
						.getCodbanco() != null
						&& castOther.getCodbanco() != null && this
						.getCodbanco().equals(castOther.getCodbanco())))
				&& ((this.getSaldo() == castOther.getSaldo()) || (this
						.getSaldo() != null
						&& castOther.getSaldo() != null && this.getSaldo()
						.equals(castOther.getSaldo())))
				&& ((this.getItem() == castOther.getItem()) || (this.getItem() != null
						&& castOther.getItem() != null && this.getItem()
						.equals(castOther.getItem())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getNumcuenta() == null ? 0 : this.getNumcuenta().hashCode());
		result = 37 * result
				+ (getNombre() == null ? 0 : this.getNombre().hashCode());
		result = 37 * result
				+ (getFecha() == null ? 0 : this.getFecha().hashCode());
		result = 37 * result
				+ (getCreditos() == null ? 0 : this.getCreditos().hashCode());
		result = 37 * result
				+ (getDebitos() == null ? 0 : this.getDebitos().hashCode());
		result = 37 * result
				+ (getDetalle() == null ? 0 : this.getDetalle().hashCode());
		result = 37
				* result
				+ (getNombrebanco() == null ? 0 : this.getNombrebanco()
						.hashCode());
		result = 37 * result
				+ (getCodbanco() == null ? 0 : this.getCodbanco().hashCode());
		result = 37 * result
				+ (getSaldo() == null ? 0 : this.getSaldo().hashCode());
		result = 37 * result
				+ (getItem() == null ? 0 : this.getItem().hashCode());
		return result;
	}

}
