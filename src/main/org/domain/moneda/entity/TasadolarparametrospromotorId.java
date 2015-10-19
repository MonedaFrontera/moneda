package org.domain.moneda.entity;

// Generated 7/10/2015 05:14:39 PM by Hibernate Tools 3.2.4.GA

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * TasadolarparametrospromotorId generated by hbm2java
 */
@Embeddable
public class TasadolarparametrospromotorId implements java.io.Serializable {

	private String codbanco;
	private String codfranquicia;
	private String codpais;
	private String documento;
	private Date fechainicio;

	public TasadolarparametrospromotorId() {
	}

	public TasadolarparametrospromotorId(String codbanco, String codfranquicia,
			String codpais, String documento, Date fechainicio) {
		this.codbanco = codbanco;
		this.codfranquicia = codfranquicia;
		this.codpais = codpais;
		this.documento = documento;
		this.fechainicio = fechainicio;
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

	@Column(name = "codfranquicia", nullable = false, length = 1)
	@NotNull
	@Length(max = 1)
	public String getCodfranquicia() {
		return this.codfranquicia;
	}

	public void setCodfranquicia(String codfranquicia) {
		this.codfranquicia = codfranquicia;
	}

	@Column(name = "codpais", nullable = false, length = 5)
	@NotNull
	@Length(max = 5)
	public String getCodpais() {
		return this.codpais;
	}

	public void setCodpais(String codpais) {
		this.codpais = codpais;
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

	@Column(name = "fechainicio", nullable = false, length = 29)
	@NotNull
	public Date getFechainicio() {
		return this.fechainicio;
	}

	public void setFechainicio(Date fechainicio) {
		this.fechainicio = fechainicio;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TasadolarparametrospromotorId))
			return false;
		TasadolarparametrospromotorId castOther = (TasadolarparametrospromotorId) other;

		return ((this.getCodbanco() == castOther.getCodbanco()) || (this
				.getCodbanco() != null
				&& castOther.getCodbanco() != null && this.getCodbanco()
				.equals(castOther.getCodbanco())))
				&& ((this.getCodfranquicia() == castOther.getCodfranquicia()) || (this
						.getCodfranquicia() != null
						&& castOther.getCodfranquicia() != null && this
						.getCodfranquicia()
						.equals(castOther.getCodfranquicia())))
				&& ((this.getCodpais() == castOther.getCodpais()) || (this
						.getCodpais() != null
						&& castOther.getCodpais() != null && this.getCodpais()
						.equals(castOther.getCodpais())))
				&& ((this.getDocumento() == castOther.getDocumento()) || (this
						.getDocumento() != null
						&& castOther.getDocumento() != null && this
						.getDocumento().equals(castOther.getDocumento())))
				&& ((this.getFechainicio() == castOther.getFechainicio()) || (this
						.getFechainicio() != null
						&& castOther.getFechainicio() != null && this
						.getFechainicio().equals(castOther.getFechainicio())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getCodbanco() == null ? 0 : this.getCodbanco().hashCode());
		result = 37
				* result
				+ (getCodfranquicia() == null ? 0 : this.getCodfranquicia()
						.hashCode());
		result = 37 * result
				+ (getCodpais() == null ? 0 : this.getCodpais().hashCode());
		result = 37 * result
				+ (getDocumento() == null ? 0 : this.getDocumento().hashCode());
		result = 37
				* result
				+ (getFechainicio() == null ? 0 : this.getFechainicio()
						.hashCode());
		return result;
	}

}
