package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("observacionList")
public class ObservacionList extends EntityQuery<Observacion> {

	private static final String EJBQL = "select observacion from Observacion observacion";

	private static final String[] RESTRICTIONS = { "lower(observacion.observacion) like lower(concat(#{observacionList.observacion.observacion},'%'))", };

	private Observacion observacion;

	public ObservacionList() {
		observacion = new Observacion();
		observacion.setId(new ObservacionId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Observacion getObservacion() {
		return observacion;
	}
}
