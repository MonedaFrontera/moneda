package org.domain.moneda.entity;

// Generated 7/10/2015 05:14:39 PM by Hibernate Tools 3.2.4.GA

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.validator.Length;

/**
 * Tasadolarparametro generated by hbm2java
 */
@Entity
@Table(name = "tasadolarparametro")
public class Tasadolarparametro implements java.io.Serializable {

	private int consecutivo;
	private Franquicia franquicia;
	private Pais pais;
	private Establecimiento establecimiento;
	private Banco banco;
	private String tipocupo;
	private BigDecimal tasadolar;
	private BigDecimal tasadolarTac;
	private Date fechainicio;
	private Date fechafin;

	public Tasadolarparametro() {
	}

	public Tasadolarparametro(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	public Tasadolarparametro(int consecutivo, Franquicia franquicia,
			Pais pais, Establecimiento establecimiento, Banco banco,
			String tipocupo, BigDecimal tasadolar, BigDecimal tasadolarTac,
			Date fechainicio, Date fechafin) {
		this.consecutivo = consecutivo;
		this.franquicia = franquicia;
		this.pais = pais;
		this.establecimiento = establecimiento;
		this.banco = banco;
		this.tipocupo = tipocupo;
		this.tasadolar = tasadolar;
		this.tasadolarTac = tasadolarTac;
		this.fechainicio = fechainicio;
		this.fechafin = fechafin;
	}

	@Id
	@Column(name = "consecutivo", unique = true, nullable = false)
	public int getConsecutivo() {
		return this.consecutivo;
	}

	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codfranquicia")
	public Franquicia getFranquicia() {
		return this.franquicia;
	}

	public void setFranquicia(Franquicia franquicia) {
		this.franquicia = franquicia;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codpais")
	public Pais getPais() {
		return this.pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigounico")
	public Establecimiento getEstablecimiento() {
		return this.establecimiento;
	}

	public void setEstablecimiento(Establecimiento establecimiento) {
		this.establecimiento = establecimiento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codbanco")
	public Banco getBanco() {
		return this.banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	@Column(name = "tipocupo", length = 1)
	@Length(max = 1)
	public String getTipocupo() {
		return this.tipocupo;
	}

	public void setTipocupo(String tipocupo) {
		this.tipocupo = tipocupo;
	}

	@Column(name = "tasadolar", precision = 6, scale = 3)
	public BigDecimal getTasadolar() {
		return this.tasadolar;
	}

	public void setTasadolar(BigDecimal tasadolar) {
		this.tasadolar = tasadolar;
	}

	@Column(name = "tasadolar_tac", precision = 6, scale = 3)
	public BigDecimal getTasadolarTac() {
		return this.tasadolarTac;
	}

	public void setTasadolarTac(BigDecimal tasadolarTac) {
		this.tasadolarTac = tasadolarTac;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fechainicio", length = 29)
	public Date getFechainicio() {
		return this.fechainicio;
	}

	public void setFechainicio(Date fechainicio) {
		this.fechainicio = fechainicio;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fechafin", length = 29)
	public Date getFechafin() {
		return this.fechafin;
	}

	public void setFechafin(Date fechafin) {
		this.fechafin = fechafin;
	}

}
