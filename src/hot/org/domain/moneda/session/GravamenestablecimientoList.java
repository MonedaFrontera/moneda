package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("gravamenestablecimientoList")
public class GravamenestablecimientoList extends
		EntityQuery<Gravamenestablecimiento> {

	private static final String EJBQL = "select gravamenestablecimiento from Gravamenestablecimiento gravamenestablecimiento";

	private static final String[] RESTRICTIONS = {
			"lower(gravamenestablecimiento.id.codigounico) like lower(concat(#{gravamenestablecimientoList.gravamenestablecimiento.id.codigounico},'%'))",
			"lower(gravamenestablecimiento.id.gravamen) like lower(concat(#{gravamenestablecimientoList.gravamenestablecimiento.id.gravamen},'%'))", };

	private Gravamenestablecimiento gravamenestablecimiento;

	public GravamenestablecimientoList() {
		gravamenestablecimiento = new Gravamenestablecimiento();
		gravamenestablecimiento.setId(new GravamenestablecimientoId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Gravamenestablecimiento getGravamenestablecimiento() {
		return gravamenestablecimiento;
	}
}
