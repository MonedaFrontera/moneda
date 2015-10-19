package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("deduccionesList")
public class DeduccionesList extends EntityQuery<Deducciones> {

	private static final String EJBQL = "select deducciones from Deducciones deducciones";

	private static final String[] RESTRICTIONS = { "lower(deducciones.descripcion) like lower(concat(#{deduccionesList.deducciones.descripcion},'%'))", };

	private Deducciones deducciones;

	public DeduccionesList() {
		deducciones = new Deducciones();
		deducciones.setId(new DeduccionesId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Deducciones getDeducciones() {
		return deducciones;
	}
}
