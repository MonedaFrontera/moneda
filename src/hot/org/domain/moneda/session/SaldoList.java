package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("saldoList")
public class SaldoList extends EntityQuery<Saldo> {

	private static  String EJBQL = "select saldo from Saldo saldo";
	
	private String nombre;
	private String apellido;

	private static final String[] RESTRICTIONS = {
			"lower(saldo.personal.nombre) like lower(concat(#{saldoList.nombre},'%'))",
			"lower(saldo.personal.apellido) like lower(concat(#{saldoList.apellido},'%'))"};

	private Saldo saldo;

	public SaldoList() {
		saldo = new Saldo();
		saldo.setId(new SaldoId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Saldo getSaldo() {
		return saldo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	
}
