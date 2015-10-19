package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("monedaList")
public class MonedaList extends EntityQuery<Moneda> {

	private static final String EJBQL = "select moneda from Moneda moneda";

	private static final String[] RESTRICTIONS = {
			"lower(moneda.codigomoneda) like lower(concat(#{monedaList.moneda.codigomoneda},'%'))",
			"lower(moneda.nombreMoneda) like lower(concat(#{monedaList.moneda.nombreMoneda},'%'))", };

	private Moneda moneda = new Moneda();

	public MonedaList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Moneda getMoneda() {
		return moneda;
	}
}
