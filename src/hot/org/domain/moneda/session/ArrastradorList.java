package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("arrastradorList")
public class ArrastradorList extends EntityQuery<Arrastrador> {

	private static final String EJBQL = "select arrastrador from Arrastrador arrastrador";

	private static final String[] RESTRICTIONS = { "lower(arrastrador.documento) like lower(concat(#{arrastradorList.arrastrador.documento},'%'))", };

	private Arrastrador arrastrador = new Arrastrador();

	public ArrastradorList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Arrastrador getArrastrador() {
		return arrastrador;
	}
}
