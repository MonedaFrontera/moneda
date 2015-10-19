package org.domain.moneda.session;

import org.domain.moneda.entity.*;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("diasemanaList")
public class DiasemanaList extends EntityQuery<Diasemana> {

	private static final String EJBQL = "select diasemana from Diasemana diasemana";

	private static final String[] RESTRICTIONS = { "lower(diasemana.id.nombredia) like lower(concat(#{diasemanaList.diasemana.id.nombredia},'%'))", };

	private Diasemana diasemana;

	public DiasemanaList() {
		diasemana = new Diasemana();
		diasemana.setId(new DiasemanaId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Diasemana getDiasemana() {
		return diasemana;
	}
}
