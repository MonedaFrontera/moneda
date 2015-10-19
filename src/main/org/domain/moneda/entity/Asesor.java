package org.domain.moneda.entity;

// Generated 23/12/2011 07:58:00 AM by Hibernate Tools 3.2.4.GA

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.domain.moneda.entity.Personal;
import org.domain.moneda.entity.Promotor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * Asesor generated by hbm2java
 */
@Entity
@Table(name = "asesor", schema = "public")
public class Asesor implements java.io.Serializable {

	private String documento;
	private Personal personal;
	private BigDecimal comision;
	private Integer extension;
	private Set<Promotor> promotors = new HashSet<Promotor>(0);
	private Set<Envios> envioses = new HashSet<Envios>(0);

	public Asesor() {
	}

	public Asesor(Personal personal) {
		this.personal = personal;
	}

	public Asesor(Personal personal, BigDecimal comision, Integer extension,
			Set<Promotor> promotors, Set<Envios> envioses) {
		this.personal = personal;
		this.comision = comision;
		this.extension = extension;
		this.promotors = promotors;
		this.envioses = envioses;
	}

	//@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "personal"))
	@Id
	//@GeneratedValue(generator = "generator")
	@Column(name = "documento", unique = true, nullable = false, length = 15)
	@Length(max = 15)
	public String getDocumento() {
		return this.documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	@NotNull
	//@Length(max = 15)
	public Personal getPersonal() {
		return this.personal;
	}

	public void setPersonal(Personal personal) {
		this.personal = personal;
	}

	@Column(name = "comision", precision = 4, scale = 3)
	public BigDecimal getComision() {
		return this.comision;
	}

	public void setComision(BigDecimal comision) {
		this.comision = comision;
	}
	
	@Column(name = "extension", precision = 6, scale = 0)
	public Integer getExtension() {
		return this.extension;
	}

	public void setExtension(Integer extension) {
		this.extension = extension;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "asesor")
	public Set<Promotor> getPromotors() {
		return this.promotors;
	}

	public void setPromotors(Set<Promotor> promotors) {
		this.promotors = promotors;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "asesor")
	public Set<Envios> getEnvioses() {
		return this.envioses;
	}

	public void setEnvioses(Set<Envios> envioses) {
		this.envioses = envioses;
	}

}
