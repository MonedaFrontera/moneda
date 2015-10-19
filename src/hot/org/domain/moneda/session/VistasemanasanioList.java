package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("vistasemanasanioList")
public class VistasemanasanioList extends EntityQuery<Vistasemanasanio> {

	private static final String EJBQL = "select vistasemanasanio from Vistasemanasanio vistasemanasanio";

	private static final String[] RESTRICTIONS = { "lower(vistasemanasanio.id.numsemana) like lower(concat(#{vistasemanasanioList.vistasemanasanio.id.numsemana},'%'))", };

	private Vistasemanasanio vistasemanasanio;

	public VistasemanasanioList() {
		vistasemanasanio = new Vistasemanasanio();
		vistasemanasanio.setId(new VistasemanasanioId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Vistasemanasanio getVistasemanasanio() {
		return vistasemanasanio;
	}
}
