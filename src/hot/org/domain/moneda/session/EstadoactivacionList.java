package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("estadoactivacionList")
public class EstadoactivacionList extends EntityQuery<Estadoactivacion> {

	private static final String EJBQL = "select estadoactivacion from Estadoactivacion estadoactivacion";

	private static final String[] RESTRICTIONS = {
			"lower(estadoactivacion.id.estado) like lower(concat(#{estadoactivacionList.estadoactivacion.id.estado},'%'))",
			"lower(estadoactivacion.observacion) like lower(concat(#{estadoactivacionList.estadoactivacion.observacion},'%'))", };

	private Estadoactivacion estadoactivacion;

	public EstadoactivacionList() {
		estadoactivacion = new Estadoactivacion();
		estadoactivacion.setId(new EstadoactivacionId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Estadoactivacion getEstadoactivacion() {
		return estadoactivacion;
	}
}
