package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("mesanioList")
public class MesanioList extends EntityQuery<Mesanio> {

	private static final String EJBQL = "select mesanio from Mesanio mesanio";

	private static final String[] RESTRICTIONS = { "lower(mesanio.nombremes) like lower(concat(#{mesanioList.mesanio.nombremes},'%'))", };

	private Mesanio mesanio = new Mesanio();

	public MesanioList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Mesanio getMesanio() {
		return mesanio;
	}
}
