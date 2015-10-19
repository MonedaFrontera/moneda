package org.domain.moneda.entity;

// Generated 28/03/2013 11:43:43 PM by Hibernate Tools 3.2.4.GA

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.validator.Length;

/**
 * Activacion generated by hbm2java
 */
@Entity
@Table(name = "activacion")
public class Activacion implements java.io.Serializable {

	private int consecutivo;
	private Gestor gestor;
	private Actestado actestado;
	private Banco banco;
	private String cedula;
	private String nombre;
	private Date fechareg;
	private String usuarioreg;
	private String correo;
	private String clave;
	private String usuario;
	private String llave;
	private Integer ano;
	private Promotor promotor;
	private String usuariomod;
	private Date fechamod;
	private Date fechaact;
	private Date fechacita;
	private Set<Estadoactivacion> estadoactivacions = new HashSet<Estadoactivacion>(
			0);
	private Set<Cita> citas = new HashSet<Cita>(0);
	private Set<Activagestor> activagestors = new HashSet<Activagestor>(0);
	private Set<Observacion> observacions = new HashSet<Observacion>(0);
	private Set<Viaje> viajes = new HashSet<Viaje>(0);
	private Boolean ingresocadivi;
	private Boolean documentoscompletos;
	private Boolean citaasignada;
	private String agencia;
	private String tipoviaje;
	private Date fechaInicioViaje;
	private Date fechaFinViaje;
	private String clavemercantil;
	private Boolean coordenadas;
	private String provinet;
	private String claveprovinet;
	private Boolean rusad;
	private Boolean noingresacadivi;
	private Boolean rif;
	private String deposito;
	private Date fechadeposito;
	private BigDecimal valordeposito;
	private Date fechadevolucion;
	private BigDecimal valordevolucion;
	private Boolean erroringresocadivi;
	private Integer cupoaprobado;
	private String usuariobanco;
	private Boolean ingresobancalinea;
	private Boolean noingresobancalinea;
	private Integer destino;
	
	public Activacion() {
	}

	public Activacion(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	
	public Activacion(int consecutivo, Gestor gestor, Actestado actestado,
			Banco banco, String cedula, String nombre, Date fechareg,
			String usuarioreg, String correo, String clave, String usuario,
			String llave, Integer ano, Promotor promotor, String usuariomod,
			Date fechamod, Date fechaact, Date fechacita,
			Set<Estadoactivacion> estadoactivacions, Set<Cita> citas,
			Set<Activagestor> activagestors, Set<Observacion> observacions,
			Set<Viaje> viajes, Boolean ingresocadivi,
			Boolean documentoscompletos, Boolean citaasignada, String agencia,
			String tipoviaje, Date fechaInicioViaje, Date fechaFinViaje,
			String clavemercantil, Boolean coordenadas, String provinet,
			String claveprovinet, Boolean rusad, Boolean noingresacadivi,
			Boolean rif, String deposito, Date fechadeposito,
			BigDecimal valordeposito, Date fechadevolucion,
			BigDecimal valordevolucion, Boolean erroringresocadivi,
			Integer cupoaprobado, String usuariobanco,
			Boolean ingresobancalinea, Boolean noingresobancalinea, Integer destino) {
		super();
		this.consecutivo = consecutivo;
		this.gestor = gestor;
		this.actestado = actestado;
		this.banco = banco;
		this.cedula = cedula;
		this.nombre = nombre;
		this.fechareg = fechareg;
		this.usuarioreg = usuarioreg;
		this.correo = correo;
		this.clave = clave;
		this.usuario = usuario;
		this.llave = llave;
		this.ano = ano;
		this.promotor = promotor;
		this.usuariomod = usuariomod;
		this.fechamod = fechamod;
		this.fechaact = fechaact;
		this.fechacita = fechacita;
		this.estadoactivacions = estadoactivacions;
		this.citas = citas;
		this.activagestors = activagestors;
		this.observacions = observacions;
		this.viajes = viajes;
		this.ingresocadivi = ingresocadivi;
		this.documentoscompletos = documentoscompletos;
		this.citaasignada = citaasignada;
		this.agencia = agencia;
		this.tipoviaje = tipoviaje;
		this.fechaInicioViaje = fechaInicioViaje;
		this.fechaFinViaje = fechaFinViaje;
		this.clavemercantil = clavemercantil;
		this.coordenadas = coordenadas;
		this.provinet = provinet;
		this.claveprovinet = claveprovinet;
		this.rusad = rusad;
		this.noingresacadivi = noingresacadivi;
		this.rif = rif;
		this.deposito = deposito;
		this.fechadeposito = fechadeposito;
		this.valordeposito = valordeposito;
		this.fechadevolucion = fechadevolucion;
		this.valordevolucion = valordevolucion;
		this.erroringresocadivi = erroringresocadivi;
		this.cupoaprobado = cupoaprobado;
		this.usuariobanco = usuariobanco;
		this.ingresobancalinea = ingresobancalinea;
		this.noingresobancalinea = noingresobancalinea;
		this.destino = destino;
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
	@JoinColumn(name = "gestor")
	public Gestor getGestor() {
		return this.gestor;
	}

	public void setGestor(Gestor gestor) {
		this.gestor = gestor;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "estado")
	public Actestado getActestado() {
		return this.actestado;
	}

	public void setActestado(Actestado actestado) {
		this.actestado = actestado;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codbanco")
	public Banco getBanco() {
		return this.banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	@Column(name = "cedula", length = 15)
	@Length(max = 15)
	public String getCedula() {
		return this.cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	@Column(name = "nombre", length = 50)
	@Length(max = 100)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fechareg", length = 29)
	public Date getFechareg() {
		return this.fechareg;
	}

	public void setFechareg(Date fechareg) {
		this.fechareg = fechareg;
	}

	@Column(name = "usuarioreg", length = 20)
	@Length(max = 20)
	public String getUsuarioreg() {
		return this.usuarioreg;
	}

	public void setUsuarioreg(String usuarioreg) {
		this.usuarioreg = usuarioreg;
	}

	@Column(name = "correo", length = 50)
	@Length(max = 100)
	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	@Column(name = "clave", length = 50)
	@Length(max = 50)
	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	@Column(name = "usuario", length = 50)
	@Length(max = 50)
	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	@Column(name = "llave", length = 50)
	@Length(max = 50)
	public String getLlave() {
		return this.llave;
	}

	public void setLlave(String llave) {
		this.llave = llave;
	}

	@Column(name = "ano")
	public Integer getAno() {
		return this.ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "promotor")
	public Promotor getPromotor() {
		return this.promotor;
	}

	public void setPromotor(Promotor promotor) {
		this.promotor = promotor;
	}

	@Column(name = "usuariomod", length = 20)
	@Length(max = 20)
	public String getUsuariomod() {
		return this.usuariomod;
	}

	public void setUsuariomod(String usuariomod) {
		this.usuariomod = usuariomod;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fechamod", length = 15)
	public Date getFechamod() {
		return this.fechamod;
	}

	public void setFechamod(Date fechamod) {
		this.fechamod = fechamod;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fechaact", length = 15)
	public Date getFechaact() {
		return this.fechaact;
	}

	public void setFechaact(Date fechaact) {
		this.fechaact = fechaact;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fechacita", length = 29)
	public Date getFechacita() {
		return this.fechacita;
	}

	public void setFechacita(Date fechacita) {
		this.fechacita = fechacita;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "activacion")
	public Set<Estadoactivacion> getEstadoactivacions() {
		return this.estadoactivacions;
	}

	public void setEstadoactivacions(Set<Estadoactivacion> estadoactivacions) {
		this.estadoactivacions = estadoactivacions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "activacion")
	public Set<Cita> getCitas() {
		return this.citas;
	}

	public void setCitas(Set<Cita> citas) {
		this.citas = citas;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "activacion")
	public Set<Activagestor> getActivagestors() {
		return this.activagestors;
	}

	public void setActivagestors(Set<Activagestor> activagestors) {
		this.activagestors = activagestors;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "activacion")
	public Set<Observacion> getObservacions() {
		return this.observacions;
	}

	public void setObservacions(Set<Observacion> observacions) {
		this.observacions = observacions;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "activaviaje", schema = "public", joinColumns = { @JoinColumn(name = "consactivacion", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "consviaje", nullable = false, updatable = false) })
	public Set<Viaje> getViajes() {
		return this.viajes;
	}

	public void setViajes(Set<Viaje> viajes) {
		this.viajes = viajes;
	}
	
	@Column(name = "ingresocadivi")
	public Boolean getIngresocadivi() {
		return this.ingresocadivi;
	}

	public void setIngresocadivi(Boolean ingresocadivi) {
		this.ingresocadivi = ingresocadivi;
	}

	@Column(name = "documentoscompletos")
	public Boolean getDocumentoscompletos() {
		return this.documentoscompletos;
	}

	public void setDocumentoscompletos(Boolean documentoscompletos) {
		this.documentoscompletos = documentoscompletos;
	}

	@Column(name = "citaasignada")
	public Boolean getCitaasignada() {
		return this.citaasignada;
	}

	public void setCitaasignada(Boolean citaasignada) {
		this.citaasignada = citaasignada;
	}
	
	@Column(name = "agencia", length = 100)
	@Length(max = 100)
	public String getAgencia() {
		return this.agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}
	
	@Column(name = "tipoviaje", length = 1)
	@Length(max = 1)
	public String getTipoviaje() {
		return tipoviaje;
	}

	public void setTipoviaje(String tipoviaje) {
		this.tipoviaje = tipoviaje;
	}

	public Date getFechaInicioViaje() {
		return fechaInicioViaje;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fechainicioviaje", length = 15)
	public void setFechaInicioViaje(Date fechainicioviaje) {
		this.fechaInicioViaje = fechainicioviaje;
	}

	public Date getFechaFinViaje() {
		return fechaFinViaje;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fechafinviaje", length = 15)
	public void setFechaFinViaje(Date fechafinviaje) {
		this.fechaFinViaje = fechafinviaje;
	}

	@Column(name = "clavemercantil", length = 50)
	@Length(max = 50)
	public String getClavemercantil() {
		return clavemercantil;
	}

	public void setClavemercantil(String clavemercantil) {
		this.clavemercantil = clavemercantil;
	}

	@Column(name = "coordenadas")
	public Boolean getCoordenadas() {
		return coordenadas;
	}

	public void setCoordenadas(Boolean coordenadas) {
		this.coordenadas = coordenadas;
	}

	@Column(name = "provinet", length = 50)
	@Length(max = 50)
	public String getProvinet() {
		return provinet;
	}

	public void setProvinet(String provinet) {
		this.provinet = provinet;
	}

	@Column(name = "claveprovinet", length = 50)
	@Length(max = 50)
	public String getClaveprovinet() {
		return claveprovinet;
	}

	public void setClaveprovinet(String claveprovinet) {
		this.claveprovinet = claveprovinet;
	}

	@Column(name = "rusad")
	public Boolean getRusad() {
		return rusad;
	}

	public void setRusad(Boolean rusad) {
		this.rusad = rusad;
	}

	@Column(name = "noingresacadivi")
	public Boolean getNoingresacadivi() {
		return noingresacadivi;
	}

	public void setNoingresacadivi(Boolean noingresacadivi) {
		this.noingresacadivi = noingresacadivi;
	}

	@Column(name = "rif")
	public Boolean getRif() {
		return rif;
	}

	public void setRif(Boolean rif) {		
		this.rif = rif;
	}
	
	@Column(name = "deposito", length = 50)
	@Length(max = 50)
	public String getDeposito() {
		return this.deposito;
	}

	public void setDeposito(String deposito) {
		this.deposito = deposito;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fechadeposito", length = 22)
	public Date getFechadeposito() {
		return this.fechadeposito;
	}

	public void setFechadeposito(Date fechadeposito) {
		this.fechadeposito = fechadeposito;
	}

	@Column(name = "valordeposito", precision = 6)
	public BigDecimal getValordeposito() {
		return this.valordeposito;
	}

	public void setValordeposito(BigDecimal valordeposito) {
		this.valordeposito = valordeposito;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fechadevolucion", length = 22)
	public Date getFechadevolucion() {
		return this.fechadevolucion;
	}

	public void setFechadevolucion(Date fechadevolucion) {
		this.fechadevolucion = fechadevolucion;
	}

	@Column(name = "valordevolucion", precision = 6)
	public BigDecimal getValordevolucion() {
		return this.valordevolucion;
	}

	public void setValordevolucion(BigDecimal valordevolucion) {
		this.valordevolucion = valordevolucion;
	}
	
	@Column(name = "erroringresocadivi")
	public Boolean getErroringresocadivi() {
		return this.erroringresocadivi;
	}

	public void setErroringresocadivi(Boolean erroringresocadivi) {
		this.erroringresocadivi = erroringresocadivi;
	}
	
	@Column(name = "cupoaprobado", precision = 8, scale = 0)
	public Integer getCupoaprobado() {
		return this.cupoaprobado;
	}

	public void setCupoaprobado(Integer cupoaprobado) {
		this.cupoaprobado = cupoaprobado;
	}
	
	@Column(name = "usuariobanco", length = 20)
	@Length(max = 20)
	public String getUsuariobanco() {
		return this.usuariobanco;
	}

	public void setUsuariobanco(String usuariobanco) {
		this.usuariobanco = usuariobanco;
	}

	@Column(name = "ingresobancalinea")
	public Boolean getIngresobancalinea() {
		return ingresobancalinea;
	}

	public void setIngresobancalinea(Boolean ingresobancalinea) {
		this.ingresobancalinea = ingresobancalinea;
	}

	@Column(name = "noingresobancalinea")
	public Boolean getNoingresobancalinea() {
		return noingresobancalinea;
	}

	public void setNoingresobancalinea(Boolean noingresobancalinea) {
		this.noingresobancalinea = noingresobancalinea;
	}
	
	@Column(name = "destino")
	public Integer getDestino() {
		return this.destino;
	}

	public void setDestino(Integer destino) {
		this.destino = destino;
	}
	
	
		
}
