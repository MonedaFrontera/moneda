package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("dolartodayList")
public class DolartodayList extends EntityQuery<Dolartoday> {

	private static final String EJBQL = "select dolartoday from Dolartoday dolartoday";

	private static final String[] RESTRICTIONS = {};

	private Dolartoday dolartoday = new Dolartoday();

	public DolartodayList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Dolartoday getDolartoday() {
		return dolartoday;
	}
}
