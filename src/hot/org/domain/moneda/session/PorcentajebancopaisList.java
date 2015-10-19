package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("porcentajebancopaisList")
public class PorcentajebancopaisList extends EntityQuery<Porcentajebancopais> {

	private static final String EJBQL = "select porcentajebancopais from Porcentajebancopais porcentajebancopais";

	private static final String[] RESTRICTIONS = {
			"lower(porcentajebancopais.id.codbanco) like lower(concat(#{porcentajebancopaisList.porcentajebancopais.id.codbanco},'%'))",
			"lower(porcentajebancopais.id.codigopais) like lower(concat(#{porcentajebancopaisList.porcentajebancopais.id.codigopais},'%'))", };

	private Porcentajebancopais porcentajebancopais;

	public PorcentajebancopaisList() {
		porcentajebancopais = new Porcentajebancopais();
		porcentajebancopais.setId(new PorcentajebancopaisId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Porcentajebancopais getPorcentajebancopais() {
		return porcentajebancopais;
	}
}
