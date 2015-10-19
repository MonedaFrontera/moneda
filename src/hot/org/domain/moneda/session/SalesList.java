package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("salesList")
public class SalesList extends EntityQuery<Sales> {

	private static final String EJBQL = "select sales from Sales sales";

	private static final String[] RESTRICTIONS = {};

	private Sales sales;

	public SalesList() {
		sales = new Sales();
		sales.setId(new SalesId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Sales getSales() {
		return sales;
	}
}
