package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("comisionhistoricoList")
public class ComisionhistoricoList extends EntityQuery<Comisionhistorico> {

	private static final String EJBQL = "select comisionhistorico from Comisionhistorico comisionhistorico";

	private static final String[] RESTRICTIONS = { "lower(comisionhistorico.id.documento) like lower(concat(#{comisionhistoricoList.comisionhistorico.id.documento},'%'))", };

	private Comisionhistorico comisionhistorico;

	public ComisionhistoricoList() {
		comisionhistorico = new Comisionhistorico();
		comisionhistorico.setId(new ComisionhistoricoId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Comisionhistorico getComisionhistorico() {
		return comisionhistorico;
	}
}
