package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("tasaeuropromotorparametroList")
public class TasaeuropromotorparametroList extends
		EntityQuery<Tasaeuropromotorparametro> {

	private static final String EJBQL = "select tasaeuropromotorparametro from Tasaeuropromotorparametro tasaeuropromotorparametro";

	private static final String[] RESTRICTIONS = { "lower(tasaeuropromotorparametro.tipocupo) like lower(concat(#{tasaeuropromotorparametroList.tasaeuropromotorparametro.tipocupo},'%'))", };

	private Tasaeuropromotorparametro tasaeuropromotorparametro = new Tasaeuropromotorparametro();

	public TasaeuropromotorparametroList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Tasaeuropromotorparametro getTasaeuropromotorparametro() {
		return tasaeuropromotorparametro;
	}
}
