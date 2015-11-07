package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.domain.moneda.entity.Pais;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("paisList")
public class PaisList extends EntityQuery<Pais> {

	private static final String EJBQL = "select pais from Pais pais where pais.estado = 1";

	private static final String[] RESTRICTIONS = {
			"lower(pais.codigopais) like lower(concat(#{paisList.pais.codigopais},'%'))",
			"lower(pais.nombre) like lower(concat('%',concat(#{paisList.pais.nombre},'%')))",
			};

	private Pais pais = new Pais();

	public PaisList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(120);
		setOrder("nombre");
	}

	public Pais getPais() {
		return pais;
	}
}
