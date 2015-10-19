package org.domain.moneda.entity;

// Generated 30/10/2013 10:52:03 AM by Hibernate Tools 3.2.4.GA

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * Bancobin generated by hbm2java
 */
@Entity
@Table(name = "bancobin")
public class Bancobin implements java.io.Serializable {

	private String bin;
	private String codbanco;

	public Bancobin() {
	}

	public Bancobin(String bin) {
		this.bin = bin;
	}

	public Bancobin(String bin, String codbanco) {
		this.bin = bin;
		this.codbanco = codbanco;
	}

	@Id
	@Column(name = "bin", unique = true, nullable = false, length = 8)
	@NotNull
	@Length(max = 8)
	public String getBin() {
		return this.bin;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}

	@Column(name = "codbanco", length = 3)
	@Length(max = 3)
	public String getCodbanco() {
		return this.codbanco;
	}

	public void setCodbanco(String codbanco) {
		this.codbanco = codbanco;
	}

}
