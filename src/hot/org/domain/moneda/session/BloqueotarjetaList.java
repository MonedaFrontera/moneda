package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("bloqueotarjetaList")
public class BloqueotarjetaList extends EntityQuery<Bloqueotarjeta> {

	private static final String EJBQL = "select bloqueotarjeta from Bloqueotarjeta bloqueotarjeta";

	private static final String[] RESTRICTIONS = {
			"lower(bloqueotarjeta.numerotarjeta) like lower(concat(#{bloqueotarjetaList.bloqueotarjeta.numerotarjeta},'%'))",
			"lower(bloqueotarjeta.observacion) like lower(concat(#{bloqueotarjetaList.bloqueotarjeta.observacion},'%'))", };

	private Bloqueotarjeta bloqueotarjeta = new Bloqueotarjeta();

	public BloqueotarjetaList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Bloqueotarjeta getBloqueotarjeta() {
		return bloqueotarjeta;
	}
}
