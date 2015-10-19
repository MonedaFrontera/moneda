package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("cneList")
public class CneList extends EntityQuery<Cne> {

	private static final String EJBQL = "select cne from Cne cne";

	private static final String[] RESTRICTIONS = {
			"lower(cne.id.nacionalidad) like lower(concat(#{cneList.cne.id.nacionalidad},'%'))",
			"lower(cne.id.cedula) like lower(concat(#{cneList.cne.id.cedula},'%'))",
			"lower(cne.id.primerApellido) like lower(concat(#{cneList.cne.id.primerApellido},'%'))",
			"lower(cne.id.segundoApellido) like lower(concat(#{cneList.cne.id.segundoApellido},'%'))",
			"lower(cne.id.primerNombre) like lower(concat(#{cneList.cne.id.primerNombre},'%'))",
			"lower(cne.id.segundoNombre) like lower(concat(#{cneList.cne.id.segundoNombre},'%'))",
			"lower(cne.id.estado) like lower(concat(#{cneList.cne.id.estado},'%'))",
			"lower(cne.id.codCentro) like lower(concat(#{cneList.cne.id.codCentro},'%'))", };

	private Cne cne;

	public CneList() {
		cne = new Cne();
		cne.setId(new CneId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Cne getCne() {
		return cne;
	}
}
