package org.domain.moneda.entity;

// Generated 21/06/2012 01:37:44 PM by Hibernate Tools 3.2.4.GA

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.validator.Length;

/**
 * Solicitudtransferencia generated by hbm2java
 */
@Entity
@Table(name = "solicitudtransferencia", schema = "public")
public class Solicitudtransferencia implements java.io.Serializable {

	private int consecutivo;
	private String documento;
	private Date fecha;
	private Integer valor;
	private Integer depositos;
	private Integer saldoanterior;
	private Set<Solicitudtarjeta> solicitudtarjetas = new HashSet<Solicitudtarjeta>(
			0);

	public Solicitudtransferencia() {
	}

	public Solicitudtransferencia(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	public Solicitudtransferencia(int consecutivo, String documento,
			Date fecha, Integer valor, Integer depositos,
			Integer saldoanterior, Set<Solicitudtarjeta> solicitudtarjetas) {
		this.consecutivo = consecutivo;
		this.documento = documento;
		this.fecha = fecha;
		this.valor = valor;
		this.depositos = depositos;
		this.saldoanterior = saldoanterior;
		this.solicitudtarjetas = solicitudtarjetas;
	}

	@Id
	@Column(name = "consecutivo", unique = true, nullable = false)
	public int getConsecutivo() {
		return this.consecutivo;
	}

	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	@Column(name = "documento", length = 15)
	@Length(max = 15)
	public String getDocumento() {
		return this.documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha", length = 29)
	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Column(name = "valor", precision = 9, scale = 0)
	public Integer getValor() {
		return this.valor;
	}

	public void setValor(Integer valor) {
		this.valor = valor;
	}

	@Column(name = "depositos", precision = 9, scale = 0)
	public Integer getDepositos() {
		return this.depositos;
	}

	public void setDepositos(Integer depositos) {
		this.depositos = depositos;
	}

	@Column(name = "saldoanterior", precision = 9, scale = 0)
	public Integer getSaldoanterior() {
		return this.saldoanterior;
	}

	public void setSaldoanterior(Integer saldoanterior) {
		this.saldoanterior = saldoanterior;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "solicitudtransferencia")
	public Set<Solicitudtarjeta> getSolicitudtarjetas() {
		return this.solicitudtarjetas;
	}

	public void setSolicitudtarjetas(Set<Solicitudtarjeta> solicitudtarjetas) {
		this.solicitudtarjetas = solicitudtarjetas;
	}

}
