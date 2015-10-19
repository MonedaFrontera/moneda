package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("tipoprocesoList")
public class TipoprocesoList extends EntityQuery<Tipoproceso> {

	private static final String EJBQL = "select tipoproceso from Tipoproceso tipoproceso";

	private static final String[] RESTRICTIONS = {
			"lower(tipoproceso.tipo) like lower(concat(#{tipoprocesoList.tipoproceso.tipo},'%'))",
			"lower(tipoproceso.descripcion) like lower(concat(#{tipoprocesoList.tipoproceso.descripcion},'%'))", };

	private Tipoproceso tipoproceso = new Tipoproceso();

	public TipoprocesoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Tipoproceso getTipoproceso() {
		return tipoproceso;
	}
}
