package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("promotortasaList")
public class PromotortasaList extends EntityQuery<Promotortasa> {

	private static final String EJBQL = "select promotortasa from Promotortasa promotortasa";

	private static final String[] RESTRICTIONS = {
			"lower(promotortasa.id.documento) like lower(concat(#{promotortasaList.promotortasa.id.documento},'%'))",
			"lower(promotortasa.id.codigopais) like lower(concat(#{promotortasaList.promotortasa.id.codigopais},'%'))", };

	private Promotortasa promotortasa;

	public PromotortasaList() {
		promotortasa = new Promotortasa();
		promotortasa.setId(new PromotortasaId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Promotortasa getPromotortasa() {
		return promotortasa;
	}
}
