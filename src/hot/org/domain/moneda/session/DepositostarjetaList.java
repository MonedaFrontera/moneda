package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("depositostarjetaList")
public class DepositostarjetaList extends EntityQuery<Depositostarjeta> {

	private static final String EJBQL = "select depositostarjeta from Depositostarjeta depositostarjeta";

	private static final String[] RESTRICTIONS = { "lower(depositostarjeta.tipodebolivar) like lower(concat(#{depositostarjetaList.depositostarjeta.tipodebolivar},'%'))", };

	private Depositostarjeta depositostarjeta = new Depositostarjeta();

	public DepositostarjetaList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Depositostarjeta getDepositostarjeta() {
		return depositostarjeta;
	}
}
