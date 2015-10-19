package org.domain.moneda.session;

import org.domain.moneda.entity.*;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("calendarioList")
public class CalendarioList extends EntityQuery<Calendario> {

	private static final String EJBQL = "select calendario from Calendario calendario";

	private static final String[] RESTRICTIONS = {};

	private Calendario calendario = new Calendario();

	public CalendarioList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Calendario getCalendario() {
		return calendario;
	}
}
