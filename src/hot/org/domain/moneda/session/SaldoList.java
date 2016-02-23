package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("saldoList")
public class SaldoList extends EntityQuery<Saldo> {

	private static final String EJBQL = "select saldo from Saldo saldo ";
	
	private String nombre;

	private static final String[] RESTRICTIONS = {
			"year( saldo.id.fecha ) = year( current_date() )",
			"lower(saldo.personal.nombre) like lower(concat(#{saldoList.nombre},'%'))",
			"lower(saldo.usuariomod) like lower(concat(#{saldoList.saldo.usuariomod},'%'))"};

	private Saldo saldo;

	public SaldoList() {
		saldo = new Saldo();
		saldo.setId(new SaldoId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		System.out.println(">>>" + this.getEjbql());
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
	
	
}
