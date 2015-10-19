package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("gravamenList")
public class GravamenList extends EntityQuery<Gravamen> {

	private static final String EJBQL = "select gravamen from Gravamen gravamen";

	private static final String[] RESTRICTIONS = {
			"lower(gravamen.codigo) like lower(concat(#{gravamenList.gravamen.codigo},'%'))",
			"lower(gravamen.nombre) like lower(concat(#{gravamenList.gravamen.nombre},'%'))", };

	private Gravamen gravamen = new Gravamen();

	public GravamenList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Gravamen getGravamen() {
		return gravamen;
	}
}
