package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("puntodeventaList")
public class PuntodeventaList extends EntityQuery<Puntodeventa> {

	private static final String EJBQL = "select puntodeventa from Puntodeventa puntodeventa";

	private static final String[] RESTRICTIONS = {
			"lower(puntodeventa.codpuntoventa) like lower(concat(#{puntodeventaList.puntodeventa.codpuntoventa},'%'))",
			"lower(puntodeventa.nombre) like lower(concat(#{puntodeventaList.puntodeventa.nombre},'%'))", };

	private Puntodeventa puntodeventa = new Puntodeventa();

	public PuntodeventaList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Puntodeventa getPuntodeventa() {
		return puntodeventa;
	}
}
