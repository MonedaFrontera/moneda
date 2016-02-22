package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("saldoList")
public class SaldoList extends EntityQuery<Saldo> {

	private static final String EJBQL = "select saldo from Saldo saldo ";

	private static final String[] RESTRICTIONS = {
			"lower(saldo.personal.nombre) like lower(concat(#{saldoList.nombre},'%'))",
			"lower(saldo.personal.apellido) like lower(concat(#{saldoList.apellido},'%'))", };

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

	
	
}