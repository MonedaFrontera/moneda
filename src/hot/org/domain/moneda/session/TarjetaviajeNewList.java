package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("tarjetaviajeNewList")
public class TarjetaviajeNewList extends EntityQuery<TarjetaviajeNew> {

	private static final String EJBQL = "select tarjetaviajeNew from TarjetaviajeNew tarjetaviajeNew";

	private static final String[] RESTRICTIONS = { "lower(tarjetaviajeNew.id.numerotarjeta) like lower(concat(#{tarjetaviajeNewList.tarjetaviajeNew.id.numerotarjeta},'%'))", };

	private TarjetaviajeNew tarjetaviajeNew;

	public TarjetaviajeNewList() {
		tarjetaviajeNew = new TarjetaviajeNew();
		tarjetaviajeNew.setId(new TarjetaviajeNewId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public TarjetaviajeNew getTarjetaviajeNew() {
		return tarjetaviajeNew;
	}
}
