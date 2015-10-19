package org.domain.moneda.entity;

// Generated 2/03/2013 03:06:19 PM by Hibernate Tools 3.2.4.GA

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * TransaccionerrorId generated by hbm2java
 */
@Embeddable
public class TransaccionerrorId implements java.io.Serializable {

	private String numerotarjeta;
	private String codigounico;
	private Date fechatx;
	private String tipotx;
	private String numfactura;
	private Date fechamod;
	private String usuariomod;
	private String asesor;
	private String promotor;
	private String digitador;
	private String error;

	public TransaccionerrorId() {
	}

	public TransaccionerrorId(String numerotarjeta, String codigounico,
			Date fechatx) {
		this.numerotarjeta = numerotarjeta;
		this.codigounico = codigounico;
		this.fechatx = fechatx;
	}

	public TransaccionerrorId(String numerotarjeta, String codigounico,
			Date fechatx, String tipotx, String numfactura, Date fechamod,
			String usuariomod, String asesor, String promotor,
			String digitador, String error) {
		this.numerotarjeta = numerotarjeta;
		this.codigounico = codigounico;
		this.fechatx = fechatx;
		this.tipotx = tipotx;
		this.numfactura = numfactura;
		this.fechamod = fechamod;
		this.usuariomod = usuariomod;
		this.asesor = asesor;
		this.promotor = promotor;
		this.digitador = digitador;
		this.error = error;
	}

	@Column(name = "numerotarjeta", nullable = false, length = 16)
	@NotNull
	@Length(max = 16)
	public String getNumerotarjeta() {
		return this.numerotarjeta;
	}

	public void setNumerotarjeta(String numerotarjeta) {
		this.numerotarjeta = numerotarjeta;
	}

	@Column(name = "codigounico", nullable = false, length = 8)
	@NotNull
	@Length(max = 8)
	public String getCodigounico() {
		return this.codigounico;
	}

	public void setCodigounico(String codigounico) {
		this.codigounico = codigounico;
	}

	@Column(name = "fechatx", nullable = false, length = 29)
	@NotNull
	public Date getFechatx() {
		return this.fechatx;
	}

	public void setFechatx(Date fechatx) {
		this.fechatx = fechatx;
	}

	@Column(name = "tipotx", length = 1)
	@Length(max = 1)
	public String getTipotx() {
		return this.tipotx;
	}

	public void setTipotx(String tipotx) {
		this.tipotx = tipotx;
	}

	@Column(name = "numfactura", length = 10)
	@Length(max = 10)
	public String getNumfactura() {
		return this.numfactura;
	}

	public void setNumfactura(String numfactura) {
		this.numfactura = numfactura;
	}

	@Column(name = "fechamod", length = 29)
	public Date getFechamod() {
		return this.fechamod;
	}

	public void setFechamod(Date fechamod) {
		this.fechamod = fechamod;
	}

	@Column(name = "usuariomod", length = 15)
	@Length(max = 15)
	public String getUsuariomod() {
		return this.usuariomod;
	}

	public void setUsuariomod(String usuariomod) {
		this.usuariomod = usuariomod;
	}

	@Column(name = "asesor", length = 15)
	@Length(max = 15)
	public String getAsesor() {
		return this.asesor;
	}

	public void setAsesor(String asesor) {
		this.asesor = asesor;
	}

	@Column(name = "promotor", length = 15)
	@Length(max = 15)
	public String getPromotor() {
		return this.promotor;
	}

	public void setPromotor(String promotor) {
		this.promotor = promotor;
	}

	@Column(name = "digitador", length = 15)
	@Length(max = 15)
	public String getDigitador() {
		return this.digitador;
	}

	public void setDigitador(String digitador) {
		this.digitador = digitador;
	}

	@Column(name = "error")
	public String getError() {
		return this.error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TransaccionerrorId))
			return false;
		TransaccionerrorId castOther = (TransaccionerrorId) other;

		return ((this.getNumerotarjeta() == castOther.getNumerotarjeta()) || (this
				.getNumerotarjeta() != null
				&& castOther.getNumerotarjeta() != null && this
				.getNumerotarjeta().equals(castOther.getNumerotarjeta())))
				&& ((this.getCodigounico() == castOther.getCodigounico()) || (this
						.getCodigounico() != null
						&& castOther.getCodigounico() != null && this
						.getCodigounico().equals(castOther.getCodigounico())))
				&& ((this.getFechatx() == castOther.getFechatx()) || (this
						.getFechatx() != null
						&& castOther.getFechatx() != null && this.getFechatx()
						.equals(castOther.getFechatx())))
				&& ((this.getTipotx() == castOther.getTipotx()) || (this
						.getTipotx() != null
						&& castOther.getTipotx() != null && this.getTipotx()
						.equals(castOther.getTipotx())))
				&& ((this.getNumfactura() == castOther.getNumfactura()) || (this
						.getNumfactura() != null
						&& castOther.getNumfactura() != null && this
						.getNumfactura().equals(castOther.getNumfactura())))
				&& ((this.getFechamod() == castOther.getFechamod()) || (this
						.getFechamod() != null
						&& castOther.getFechamod() != null && this
						.getFechamod().equals(castOther.getFechamod())))
				&& ((this.getUsuariomod() == castOther.getUsuariomod()) || (this
						.getUsuariomod() != null
						&& castOther.getUsuariomod() != null && this
						.getUsuariomod().equals(castOther.getUsuariomod())))
				&& ((this.getAsesor() == castOther.getAsesor()) || (this
						.getAsesor() != null
						&& castOther.getAsesor() != null && this.getAsesor()
						.equals(castOther.getAsesor())))
				&& ((this.getPromotor() == castOther.getPromotor()) || (this
						.getPromotor() != null
						&& castOther.getPromotor() != null && this
						.getPromotor().equals(castOther.getPromotor())))
				&& ((this.getDigitador() == castOther.getDigitador()) || (this
						.getDigitador() != null
						&& castOther.getDigitador() != null && this
						.getDigitador().equals(castOther.getDigitador())))
				&& ((this.getError() == castOther.getError()) || (this
						.getError() != null
						&& castOther.getError() != null && this.getError()
						.equals(castOther.getError())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getNumerotarjeta() == null ? 0 : this.getNumerotarjeta()
						.hashCode());
		result = 37
				* result
				+ (getCodigounico() == null ? 0 : this.getCodigounico()
						.hashCode());
		result = 37 * result
				+ (getFechatx() == null ? 0 : this.getFechatx().hashCode());
		result = 37 * result
				+ (getTipotx() == null ? 0 : this.getTipotx().hashCode());
		result = 37
				* result
				+ (getNumfactura() == null ? 0 : this.getNumfactura()
						.hashCode());
		result = 37 * result
				+ (getFechamod() == null ? 0 : this.getFechamod().hashCode());
		result = 37
				* result
				+ (getUsuariomod() == null ? 0 : this.getUsuariomod()
						.hashCode());
		result = 37 * result
				+ (getAsesor() == null ? 0 : this.getAsesor().hashCode());
		result = 37 * result
				+ (getPromotor() == null ? 0 : this.getPromotor().hashCode());
		result = 37 * result
				+ (getDigitador() == null ? 0 : this.getDigitador().hashCode());
		result = 37 * result
				+ (getError() == null ? 0 : this.getError().hashCode());
		return result;
	}

}
