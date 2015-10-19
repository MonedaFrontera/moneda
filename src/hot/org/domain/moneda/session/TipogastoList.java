package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("tipogastoList")
public class TipogastoList extends EntityQuery<Tipogasto> {

	private static final String EJBQL = "select tipogasto from Tipogasto tipogasto";

	private static final String[] RESTRICTIONS = {
			"lower(tipogasto.codtipogasto) like lower(concat(#{tipogastoList.tipogasto.codtipogasto},'%'))",
			"lower(tipogasto.descripcion) like lower(concat(#{tipogastoList.tipogasto.descripcion},'%'))", };

	private Tipogasto tipogasto = new Tipogasto();

	public TipogastoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(50);
		setOrder("descripcion");
	}

	public Tipogasto getTipogasto() {
		return tipogasto;
	}
}
