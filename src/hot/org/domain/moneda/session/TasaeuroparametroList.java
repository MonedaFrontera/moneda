package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("tasaeuroparametroList")
public class TasaeuroparametroList extends EntityQuery<Tasaeuroparametro> {

	private static final String EJBQL = "select tasaeuroparametro from Tasaeuroparametro tasaeuroparametro";

	private static final String[] RESTRICTIONS = { "lower(tasaeuroparametro.tipocupo) like lower(concat(#{tasaeuroparametroList.tasaeuroparametro.tipocupo},'%'))", };

	private Tasaeuroparametro tasaeuroparametro = new Tasaeuroparametro();

	public TasaeuroparametroList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Tasaeuroparametro getTasaeuroparametro() {
		return tasaeuroparametro;
	}
}
