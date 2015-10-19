package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("tasadolarbancopaisList")
public class TasadolarbancopaisList extends EntityQuery<Tasadolarbancopais> {

	private static final String EJBQL = "select tasadolarbancopais from Tasadolarbancopais tasadolarbancopais";

	private static final String[] RESTRICTIONS = {
			"lower(tasadolarbancopais.id.codbanco) like lower(concat(#{tasadolarbancopaisList.tasadolarbancopais.id.codbanco},'%'))",
			"lower(tasadolarbancopais.id.codigopais) like lower(concat(#{tasadolarbancopaisList.tasadolarbancopais.id.codigopais},'%'))", };

	private Tasadolarbancopais tasadolarbancopais;

	public TasadolarbancopaisList() {
		tasadolarbancopais = new Tasadolarbancopais();
		tasadolarbancopais.setId(new TasadolarbancopaisId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Tasadolarbancopais getTasadolarbancopais() {
		return tasadolarbancopais;
	}
}
