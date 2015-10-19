package org.domain.moneda.entity;

// Generated 23/01/2012 04:00:30 PM by Hibernate Tools 3.2.4.GA

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
 * Envios generated by hbm2java
 */
@Entity
@Table(name = "envios", schema = "public")
public class Envios implements java.io.Serializable {

	private int consecutivo;
	private Asesor asesor;
	private Promotor promotor;
	private Date fecha;
	private String envia;
	private String recibe;
	private String oficina;
	private String ciudad;
	private String nrocupon;
	private Boolean enviado;
	private Boolean recibido;
	private Date fechaenvio;
	private Date fechamrw;

	public Envios() {
	}

	public Envios(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	public Envios(int consecutivo, Asesor asesor, Promotor promotor,
			Date fecha, String envia, String recibe, String oficina,
			String ciudad, String nrocupon, Boolean enviado, Boolean recibido,
			Date fechaenvio) {
		this.consecutivo = consecutivo;
		this.asesor = asesor;
		this.promotor = promotor;
		this.fecha = fecha;
		this.envia = envia;
		this.recibe = recibe;
		this.oficina = oficina;
		this.ciudad = ciudad;
		this.nrocupon = nrocupon;
		this.enviado = enviado;
		this.recibido = recibido;
		this.fechaenvio = fechaenvio;
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
	@JoinColumn(name = "asesor")
	public Asesor getAsesor() {
		return this.asesor;
	}

	public void setAsesor(Asesor asesor) {
		this.asesor = asesor;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "promotor")
	public Promotor getPromotor() {
		return this.promotor;
	}

	public void setPromotor(Promotor promotor) {
		this.promotor = promotor;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha", length = 13)
	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Column(name = "envia", length = 30)
	@Length(max = 30)
	public String getEnvia() {
		return this.envia;
	}

	public void setEnvia(String envia) {
		this.envia = envia;
	}

	@Column(name = "recibe", length = 30)
	@Length(max = 30)
	public String getRecibe() {
		return this.recibe;
	}

	public void setRecibe(String recibe) {
		this.recibe = recibe;
	}

	@Column(name = "oficina", length = 100)
	@Length(max = 100)
	public String getOficina() {
		return this.oficina;
	}

	public void setOficina(String oficina) {
		this.oficina = oficina;
	}

	@Column(name = "ciudad", length = 15)
	@Length(max = 15)
	public String getCiudad() {
		return this.ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	@Column(name = "nrocupon", length = 15)
	@Length(max = 15)
	public String getNrocupon() {
		return this.nrocupon;
	}

	public void setNrocupon(String nrocupon) {
		this.nrocupon = nrocupon;
	}

	@Column(name = "enviado")
	public Boolean getEnviado() {
		return this.enviado;
	}

	public void setEnviado(Boolean enviado) {
		this.enviado = enviado;
	}

	@Column(name = "recibido")
	public Boolean getRecibido() {
		return this.recibido;
	}

	public void setRecibido(Boolean recibido) {
		this.recibido = recibido;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fechaenvio", length = 13)
	public Date getFechaenvio() {
		return this.fechaenvio;
	}

	public void setFechaenvio(Date fechaenvio) {
		this.fechaenvio = fechaenvio;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "fechamrw", length = 13)
	public Date getFechamrw() {
		return this.fechamrw;
	}

	public void setFechamrw(Date fechamrw) {
		this.fechamrw = fechamrw;
	}

}
