package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("franquiciaList")
public class FranquiciaList extends EntityQuery<Franquicia> {

	private static final String EJBQL = "select franquicia from Franquicia franquicia";

	private static final String[] RESTRICTIONS = {
			"lower(franquicia.codfranquicia) like lower(concat(#{franquiciaList.franquicia.codfranquicia},'%'))",
			"lower(franquicia.nombrefranquicia) like lower(concat(#{franquiciaList.franquicia.nombrefranquicia},'%'))", };

	private Franquicia franquicia = new Franquicia();

	public FranquiciaList() {
		setEjbql(EJBQL);
		this.setOrder("franquicia.nombrefranquicia");
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Franquicia getFranquicia() {
		return franquicia;
	}
}
