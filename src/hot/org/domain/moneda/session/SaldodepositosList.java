package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("saldodepositosList")
public class SaldodepositosList extends EntityQuery<Saldodepositos> {

	private static final String EJBQL = "select saldodepositos from Saldodepositos saldodepositos";

	private static final String[] RESTRICTIONS = {
			"lower(saldodepositos.id.numerotarjeta) like lower(concat(#{saldodepositosList.saldodepositos.id.numerotarjeta},'%'))",
			"lower(saldodepositos.usuariomod) like lower(concat(#{saldodepositosList.saldodepositos.usuariomod},'%'))", };

	private Saldodepositos saldodepositos;

	public SaldodepositosList() {
		saldodepositos = new Saldodepositos();
		saldodepositos.setId(new SaldodepositosId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Saldodepositos getSaldodepositos() {
		return saldodepositos;
	}
}
