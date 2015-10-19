package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("franquiciaestablecimientoList")
public class FranquiciaestablecimientoList extends
		EntityQuery<Franquiciaestablecimiento> {

	private static final String EJBQL = "select franquiciaestablecimiento from Franquiciaestablecimiento franquiciaestablecimiento";

	private static final String[] RESTRICTIONS = {
			"lower(franquiciaestablecimiento.id.franquicia) like lower(concat(#{franquiciaestablecimientoList.franquiciaestablecimiento.id.franquicia},'%'))",
			"lower(franquiciaestablecimiento.id.establecimiento) like lower(concat(#{franquiciaestablecimientoList.franquiciaestablecimiento.id.establecimiento},'%'))", };

	private Franquiciaestablecimiento franquiciaestablecimiento;

	public FranquiciaestablecimientoList() {
		franquiciaestablecimiento = new Franquiciaestablecimiento();
		franquiciaestablecimiento.setId(new FranquiciaestablecimientoId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Franquiciaestablecimiento getFranquiciaestablecimiento() {
		return franquiciaestablecimiento;
	}
}
