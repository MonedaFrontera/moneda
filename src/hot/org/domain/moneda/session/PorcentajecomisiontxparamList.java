package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("porcentajecomisiontxparamList")
public class PorcentajecomisiontxparamList extends
		EntityQuery<Porcentajecomisiontxparam> {

	private static final String EJBQL = "select porcentajecomisiontxparam from Porcentajecomisiontxparam porcentajecomisiontxparam";

	private static final String[] RESTRICTIONS = { "lower(porcentajecomisiontxparam.tipocupo) like lower(concat(#{porcentajecomisiontxparamList.porcentajecomisiontxparam.tipocupo},'%'))", };

	private Porcentajecomisiontxparam porcentajecomisiontxparam = new Porcentajecomisiontxparam();

	public PorcentajecomisiontxparamList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Porcentajecomisiontxparam getPorcentajecomisiontxparam() {
		return porcentajecomisiontxparam;
	}
}
