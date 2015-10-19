package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("operacionList")
public class OperacionList extends EntityQuery<Operacion> {

	private static final String EJBQL = "select operacion from Operacion operacion";

	private static final String[] RESTRICTIONS = { "lower(operacion.descripcion) like lower(concat(#{operacionList.operacion.descripcion},'%'))", };

	private Operacion operacion = new Operacion();

	public OperacionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Operacion getOperacion() {
		return operacion;
	}
}
