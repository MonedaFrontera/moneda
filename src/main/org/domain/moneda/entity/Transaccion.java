package org.domain.moneda.entity;

// Generated 23/12/2011 07:58:00 AM by Hibernate Tools 3.2.4.GA

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.domain.moneda.entity.Baucher;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * Transaccion generated by hbm2java
 */
@Entity
@Table(name = "transaccion")
public class Transaccion implements java.io.Serializable {

	private int consecutivo;
	private Establecimiento establecimiento;
	private Tarjeta tarjeta;
	private Date fechatx;
	private String tipotx;
	private BigDecimal valortxpesos;
	private BigDecimal valortxdolares;
	private String numfactura;
	private BigDecimal valorcomision;
	private BigDecimal asesorcomision;
	private BigDecimal arrastradorcomision;
	private BigDecimal establecimientocomision;
	private BigDecimal porcentajecomision;
	private Set<Deducciones> deduccioneses = new HashSet<Deducciones>(0);
	private Set<Baucher> bauchers = new HashSet<Baucher>(0);
	private Set<Factura> facturas = new HashSet<Factura>(0);
	private String asesor;
	private String promotor;
	private String arrastrador;
	private String digitador;
	private String usuariomod;
	private Date fechamod;
	
	public Transaccion() {
	}

	public Transaccion(int consecutivo, Establecimiento establecimiento,
			Tarjeta tarjeta, Date fechatx) {
		this.consecutivo = consecutivo;
		this.establecimiento = establecimiento;
		this.tarjeta = tarjeta;
		this.fechatx = fechatx;
	}

	public Transaccion(int consecutivo, Establecimiento establecimiento,
			Tarjeta tarjeta, Date fechatx, String tipotx,
			BigDecimal valortxpesos, BigDecimal valortxdolares,
			String numfactura, 
			BigDecimal valorcomision, BigDecimal asesorcomision,
			BigDecimal arrastradorcomision, BigDecimal establecimientocomision,
			Set<Deducciones> deduccioneses,
			BigDecimal porcentajecomision,
			Set<Baucher> bauchers, String promotor, 
			String arrastrador, Date fechamod, String usuariomod, 
			String digitador, String asesor, Set<Factura> facturas) {
		this.consecutivo = consecutivo;
		this.establecimiento = establecimiento;
		this.tarjeta = tarjeta;
		this.fechatx = fechatx;
		this.tipotx = tipotx;
		this.valortxpesos = valortxpesos;
		this.valortxdolares = valortxdolares;
		this.numfactura = numfactura;
		this.valorcomision = valorcomision;
		this.arrastradorcomision = arrastradorcomision;
		this.asesorcomision = asesorcomision;
		this.establecimientocomision = establecimientocomision;
		this.porcentajecomision = porcentajecomision;
		this.deduccioneses = deduccioneses;
		this.bauchers = bauchers;
		this.promotor = promotor;
		this.asesor = asesor;
		this.arrastrador = arrastrador;
		this.digitador = digitador;
		this.facturas = facturas;
		this.fechamod = fechamod;
		this.usuariomod = usuariomod;
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
	@JoinColumn(name = "codigounico", nullable = false)
	@NotNull
	public Establecimiento getEstablecimiento() {
		return this.establecimiento;
	}

	public void setEstablecimiento(Establecimiento establecimiento) {
		this.establecimiento = establecimiento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "numerotarjeta", nullable = false)
	@NotNull
	public Tarjeta getTarjeta() {
		return this.tarjeta;
	}

	public void setTarjeta(Tarjeta tarjeta) {
		this.tarjeta = tarjeta;
	}

	@Temporal(TemporalType.TIMESTAMP)
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

	@Column(name = "valortxpesos", precision = 10)
	public BigDecimal getValortxpesos() {
		return this.valortxpesos;
	}

	public void setValortxpesos(BigDecimal valortxpesos) {
		this.valortxpesos = valortxpesos;
	}

	@Column(name = "valortxdolares", precision = 10)
	public BigDecimal getValortxdolares() {
		return this.valortxdolares;
	}

	public void setValortxdolares(BigDecimal valortxdolares) {
		this.valortxdolares = valortxdolares;
	}

	@Column(name = "numfactura", length = 10)
	@Length(max = 10)
	public String getNumfactura() {
		return this.numfactura;
	}

	public void setNumfactura(String numfactura) {
		this.numfactura = numfactura;
	}
	
	public void setValorcomision(BigDecimal valorcomision) {
		this.valorcomision = valorcomision;
	}

	@Column(name = "valorcomision", precision = 10)
	public BigDecimal getValorcomision() {
		return this.valorcomision;
	}
	
	@Column(name = "asesorcomision", precision = 10)
	public BigDecimal getAsesorcomision() {
		return asesorcomision;
	}

	public void setAsesorcomision(BigDecimal asesorcomision) {
		this.asesorcomision = asesorcomision;
	}

	@Column(name = "arrastradorcomision", precision = 10)
	public BigDecimal getArrastradorcomision() {
		return arrastradorcomision;
	}

	public void setArrastradorcomision(BigDecimal arrastradorcomision) {
		this.arrastradorcomision = arrastradorcomision;
	}
	
	@Column(name = "establecimientocomision", precision = 10)
	public BigDecimal getEstablecimientocomision() {
		return establecimientocomision;
	}
	
	public void setEstablecimientocomision(BigDecimal establecimientocomision) {
		this.establecimientocomision = establecimientocomision;
	}
	
	@Column(name = "porcentajecomision", precision = 5, scale = 3)
	public BigDecimal getPorcentajecomision() {
		return this.porcentajecomision;
	}

	public void setPorcentajecomision(BigDecimal porcentajecomision) {
		this.porcentajecomision = porcentajecomision;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "transaccion")
	public Set<Deducciones> getDeduccioneses() {
		return this.deduccioneses;
	}

	public void setDeduccioneses(Set<Deducciones> deduccioneses) {
		this.deduccioneses = deduccioneses;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "transaccion")
	public Set<Baucher> getBauchers() {
		return this.bauchers;
	}

	public void setBauchers(Set<Baucher> bauchers) {
		this.bauchers = bauchers;
	}
	
	@Column(name = "asesor", length = 15)
	@Length(max = 15)
	public String getAsesor() {
		return asesor;
	}

	public void setAsesor(String asesor) {
		this.asesor = asesor;
	}
	
	@Column(name = "promotor", length = 15)
	@Length(max = 15)
	public String getPromotor() {
		return promotor;
	}

	public void setPromotor(String promotor) {
		this.promotor = promotor;
	}
	
	@Column(name = "arrastrador", length = 15)
	@Length(max = 15)
	public String getArrastrador() {
		return arrastrador;
	}

	public void setArrastrador(String arrastrador) {
		this.arrastrador = arrastrador;
	}
	
	
	@Length(max = 15)
	public String getDigitador() {
		return digitador;
	}

	public void setDigitador(String digitador) {
		this.digitador = digitador;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
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
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "transaccion")
	public Set<Factura> getFacturas() {
		return this.facturas;
	}

	public void setFacturas(Set<Factura> facturas) {
		this.facturas = facturas;
	}
	
	

}
