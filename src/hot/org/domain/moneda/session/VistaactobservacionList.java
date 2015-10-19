package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("vistaactobservacionList")
public class VistaactobservacionList extends EntityQuery<Vistaactobservacion> {

	private static final String EJBQL = "select vistaactobservacion from Vistaactobservacion vistaactobservacion";

	private static final String[] RESTRICTIONS = {
			"lower(vistaactobservacion.id.tipo) like lower(concat(#{vistaactobservacionList.vistaactobservacion.id.tipo},'%'))",
			"lower(vistaactobservacion.id.observacion) like lower(concat(#{vistaactobservacionList.vistaactobservacion.id.observacion},'%'))",
			"lower(vistaactobservacion.id.dato) like lower(concat(#{vistaactobservacionList.vistaactobservacion.id.dato},'%'))", };

	private Vistaactobservacion vistaactobservacion;

	public VistaactobservacionList() {
		vistaactobservacion = new Vistaactobservacion();
		vistaactobservacion.setId(new VistaactobservacionId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Vistaactobservacion getVistaactobservacion() {
		return vistaactobservacion;
	}
}
