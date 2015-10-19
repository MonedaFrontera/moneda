package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("tasadolarpromotorparametroList")
public class TasadolarpromotorparametroList extends
		EntityQuery<Tasadolarpromotorparametro> {

	private static final String EJBQL = "select tasadolarpromotorparametro from Tasadolarpromotorparametro tasadolarpromotorparametro";

	private static final String[] RESTRICTIONS = { "lower(tasadolarpromotorparametro.tipocupo) like lower(concat(#{tasadolarpromotorparametroList.tasadolarpromotorparametro.tipocupo},'%'))", };

	private Tasadolarpromotorparametro tasadolarpromotorparametro = new Tasadolarpromotorparametro();

	public TasadolarpromotorparametroList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Tasadolarpromotorparametro getTasadolarpromotorparametro() {
		return tasadolarpromotorparametro;
	}
}
