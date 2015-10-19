package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("porcentcomisiontxparampromoList")
public class PorcentcomisiontxparampromoList extends
		EntityQuery<Porcentcomisiontxparampromo> {

	private static final String EJBQL = "select porcentcomisiontxparampromo from Porcentcomisiontxparampromo porcentcomisiontxparampromo";

	private static final String[] RESTRICTIONS = { "lower(porcentcomisiontxparampromo.tipocupo) like lower(concat(#{porcentcomisiontxparampromoList.porcentcomisiontxparampromo.tipocupo},'%'))", };

	private Porcentcomisiontxparampromo porcentcomisiontxparampromo = new Porcentcomisiontxparampromo();

	public PorcentcomisiontxparampromoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Porcentcomisiontxparampromo getPorcentcomisiontxparampromo() {
		return porcentcomisiontxparampromo;
	}
}
