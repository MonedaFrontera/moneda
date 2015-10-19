package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("promotorfranquiciaList")
public class PromotorfranquiciaList extends EntityQuery<Promotorfranquicia> {

	private static final String EJBQL = "select promotorfranquicia from Promotorfranquicia promotorfranquicia";

	private static final String[] RESTRICTIONS = {
			"lower(promotorfranquicia.id.codfranquicia) like lower(concat(#{promotorfranquiciaList.promotorfranquicia.id.codfranquicia},'%'))",
			"lower(promotorfranquicia.id.documento) like lower(concat(#{promotorfranquiciaList.promotorfranquicia.id.documento},'%'))", };

	private Promotorfranquicia promotorfranquicia;

	public PromotorfranquiciaList() {
		promotorfranquicia = new Promotorfranquicia();
		promotorfranquicia.setId(new PromotorfranquiciaId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Promotorfranquicia getPromotorfranquicia() {
		return promotorfranquicia;
	}
}
