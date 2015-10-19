package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("porcentajecomisiontxList")
public class PorcentajecomisiontxList extends EntityQuery<Porcentajecomisiontx> {

	private static final String EJBQL = "select porcentajecomisiontx from Porcentajecomisiontx porcentajecomisiontx";

	private static final String[] RESTRICTIONS = { "lower(porcentajecomisiontx.id.codpais) like lower(concat(#{porcentajecomisiontxList.porcentajecomisiontx.id.codpais},'%'))", };

	private Porcentajecomisiontx porcentajecomisiontx;

	public PorcentajecomisiontxList() {
		porcentajecomisiontx = new Porcentajecomisiontx();
		porcentajecomisiontx.setId(new PorcentajecomisiontxId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Porcentajecomisiontx getPorcentajecomisiontx() {
		return porcentajecomisiontx;
	}
}
