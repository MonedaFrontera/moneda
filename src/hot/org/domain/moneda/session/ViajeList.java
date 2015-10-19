package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("viajeList")
public class ViajeList extends EntityQuery<Viaje> {

	private static final String EJBQL = "select viaje from Viaje viaje";

	private static final String[] RESTRICTIONS = {};

	private Viaje viaje = new Viaje();

	public ViajeList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Viaje getViaje() {
		return viaje;
	}
}
