package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("puntoestablecimientoList")
public class PuntoestablecimientoList extends EntityQuery<Puntoestablecimiento> {

	private static final String EJBQL = "select puntoestablecimiento from Puntoestablecimiento puntoestablecimiento";

	private static final String[] RESTRICTIONS = {
			"lower(puntoestablecimiento.id.codigounico) like lower(concat(#{puntoestablecimientoList.puntoestablecimiento.id.codigounico},'%'))",
			"lower(puntoestablecimiento.id.codpuntoventa) like lower(concat(#{puntoestablecimientoList.puntoestablecimiento.id.codpuntoventa},'%'))", };

	private Puntoestablecimiento puntoestablecimiento;

	public PuntoestablecimientoList() {
		puntoestablecimiento = new Puntoestablecimiento();
		puntoestablecimiento.setId(new PuntoestablecimientoId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Puntoestablecimiento getPuntoestablecimiento() {
		return puntoestablecimiento;
	}
}
