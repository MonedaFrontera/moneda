package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("porcentajeparametrosList")
public class PorcentajeparametrosList extends EntityQuery<Porcentajeparametros> {

	private static final String EJBQL = "select porcentajeparametros from Porcentajeparametros porcentajeparametros";

	private static final String[] RESTRICTIONS = {
			"lower(porcentajeparametros.id.codbanco) like lower(concat(#{porcentajeparametrosList.porcentajeparametros.id.codbanco},'%'))",
			"lower(porcentajeparametros.id.codfranquicia) like lower(concat(#{porcentajeparametrosList.porcentajeparametros.id.codfranquicia},'%'))",
			"lower(porcentajeparametros.id.codpais) like lower(concat(#{porcentajeparametrosList.porcentajeparametros.id.codpais},'%'))", };

	private Porcentajeparametros porcentajeparametros;

	public PorcentajeparametrosList() {
		porcentajeparametros = new Porcentajeparametros();
		porcentajeparametros.setId(new PorcentajeparametrosId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Porcentajeparametros getPorcentajeparametros() {
		return porcentajeparametros;
	}
}
