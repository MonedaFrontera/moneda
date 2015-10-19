package org.domain.moneda.session;

import org.domain.moneda.entity.*;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("cnedireccionList")
public class CnedireccionList extends EntityQuery<Cnedireccion> {

	private static final String EJBQL = "select cnedireccion from Cnedireccion cnedireccion";

	private static final String[] RESTRICTIONS = {
			"lower(cnedireccion.id.codCentro) like lower(concat(#{cnedireccionList.cnedireccion.id.codCentro},'%'))",
			"lower(cnedireccion.id.tipo) like lower(concat(#{cnedireccionList.cnedireccion.id.tipo},'%'))",
			"lower(cnedireccion.id.codEstado) like lower(concat(#{cnedireccionList.cnedireccion.id.codEstado},'%'))",
			"lower(cnedireccion.id.codMunicipio) like lower(concat(#{cnedireccionList.cnedireccion.id.codMunicipio},'%'))",
			"lower(cnedireccion.id.codParroquia) like lower(concat(#{cnedireccionList.cnedireccion.id.codParroquia},'%'))",
			"lower(cnedireccion.id.nombreCentro) like lower(concat(#{cnedireccionList.cnedireccion.id.nombreCentro},'%'))",
			"lower(cnedireccion.id.direccionCentro) like lower(concat(#{cnedireccionList.cnedireccion.id.direccionCentro},'%'))",
			"lower(cnedireccion.id.centroNuevo) like lower(concat(#{cnedireccionList.cnedireccion.id.centroNuevo},'%'))", };

	private Cnedireccion cnedireccion;

	public CnedireccionList() {
		cnedireccion = new Cnedireccion();
		cnedireccion.setId(new CnedireccionId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Cnedireccion getCnedireccion() {
		return cnedireccion;
	}
}
