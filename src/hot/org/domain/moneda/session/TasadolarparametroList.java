package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("tasadolarparametroList")
public class TasadolarparametroList extends EntityQuery<Tasadolarparametro> {

	private static final String EJBQL = "select tasadolarparametro from Tasadolarparametro tasadolarparametro";

	private static final String[] RESTRICTIONS = { "lower(tasadolarparametro.tipocupo) like lower(concat(#{tasadolarparametroList.tasadolarparametro.tipocupo},'%'))", };

	private Tasadolarparametro tasadolarparametro = new Tasadolarparametro();

	public TasadolarparametroList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Tasadolarparametro getTasadolarparametro() {
		return tasadolarparametro;
	}
}
