package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("baucherList")
public class BaucherList extends EntityQuery<Baucher> {

	private static final String EJBQL = "select baucher from Baucher baucher";

	private static final String[] RESTRICTIONS = { "lower(baucher.id.numautorizacion) like lower(concat(#{baucherList.baucher.id.numautorizacion},'%'))", };

	private Baucher baucher;

	public BaucherList() {
		baucher = new Baucher();
		baucher.setId(new BaucherId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Baucher getBaucher() {
		return baucher;
	}
}
