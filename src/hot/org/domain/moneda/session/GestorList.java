package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("gestorList")
public class GestorList extends EntityQuery<Gestor> {

	private static final String EJBQL = "select gestor from Gestor gestor";

	private static final String[] RESTRICTIONS = { "lower(gestor.documento) like lower(concat(#{gestorList.gestor.documento},'%'))", };

	private Gestor gestor = new Gestor();

	public GestorList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Gestor getGestor() {
		return gestor;
	}
}
