package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("saldoList")
public class SaldoList extends EntityQuery<Saldo> {

	private static final String EJBQL = "select saldo from Saldo saldo";

	private static final String[] RESTRICTIONS = {
			"lower(saldo.id.documento) like lower(concat(#{saldoList.saldo.id.documento},'%'))",
			"lower(saldo.usuariomod) like lower(concat(#{saldoList.saldo.usuariomod},'%'))", };

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
