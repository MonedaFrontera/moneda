package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("tasadolarparametrosList")
public class TasadolarparametrosList extends EntityQuery<Tasadolarparametros> {

	private static final String EJBQL = "select tasadolarparametros from Tasadolarparametros tasadolarparametros";

	private static final String[] RESTRICTIONS = {
			"lower(tasadolarparametros.id.codbanco) like lower(concat(#{tasadolarparametrosList.tasadolarparametros.id.codbanco},'%'))",
			"lower(tasadolarparametros.id.codfranquicia) like lower(concat(#{tasadolarparametrosList.tasadolarparametros.id.codfranquicia},'%'))",
			"lower(tasadolarparametros.id.codpais) like lower(concat(#{tasadolarparametrosList.tasadolarparametros.id.codpais},'%'))", };

	private Tasadolarparametros tasadolarparametros;

	public TasadolarparametrosList() {
		tasadolarparametros = new Tasadolarparametros();
		tasadolarparametros.setId(new TasadolarparametrosId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Tasadolarparametros getTasadolarparametros() {
		return tasadolarparametros;
	}
}
