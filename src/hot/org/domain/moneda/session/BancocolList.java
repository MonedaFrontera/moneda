package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("bancocolList")
public class BancocolList extends EntityQuery<Bancocol> {

	private static final String EJBQL = "select bancocol from Bancocol bancocol";

	private static final String[] RESTRICTIONS = {
			"lower(bancocol.codbanco) like lower(concat(#{bancocolList.bancocol.codbanco},'%'))",
			"lower(bancocol.nombre) like lower(concat(#{bancocolList.bancocol.nombre},'%'))", };

	private Bancocol bancocol = new Bancocol();

	public BancocolList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrder("nombre"); 
		setMaxResults(25);
	}

	public Bancocol getBancocol() {
		return bancocol;
	}
}
