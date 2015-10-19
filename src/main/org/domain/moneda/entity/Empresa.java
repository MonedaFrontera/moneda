package org.domain.moneda.entity;

// Generated 23/12/2011 07:58:00 AM by Hibernate Tools 3.2.4.GA

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * Empresa generated by hbm2java
 */
@Entity
@Table(name = "empresa", schema = "public")
public class Empresa implements java.io.Serializable {

	private String nit;
	private String nombrerazonsocial;
	private Set<Establecimiento> establecimientos = new HashSet<Establecimiento>(
			0);

	public Empresa() {
	}

	public Empresa(String nit) {
		this.nit = nit;
	}

	public Empresa(String nit, String nombrerazonsocial,
			Set<Establecimiento> establecimientos) {
		this.nit = nit;
		this.nombrerazonsocial = nombrerazonsocial;
		this.establecimientos = establecimientos;
	}

	@Id
	@Column(name = "nit", unique = true, nullable = false, length = 20)
	@NotNull
	@Length(max = 20)
	public String getNit() {
		return this.nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	@Column(name = "nombrerazonsocial", length = 70)
	@Length(max = 70)
	public String getNombrerazonsocial() {
		return this.nombrerazonsocial;
	}

	public void setNombrerazonsocial(String nombrerazonsocial) {
		this.nombrerazonsocial = nombrerazonsocial;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "empresa")
	public Set<Establecimiento> getEstablecimientos() {
		return this.establecimientos;
	}

	public void setEstablecimientos(Set<Establecimiento> establecimientos) {
		this.establecimientos = establecimientos;
	}

}
