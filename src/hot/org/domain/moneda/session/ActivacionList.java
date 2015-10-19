package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("activacionList")
public class ActivacionList extends EntityQuery<Activacion> {

	private static final String EJBQL = "select activacion from Activacion activacion";

	private static final String[] RESTRICTIONS = {
			"lower(activacion.cedula) like lower(concat(#{activacionList.activacion.cedula},'%'))",
			"lower(activacion.nombre) like lower(concat(#{activacionList.activacion.nombre},'%'))",
			"lower(activacion.usuarioreg) like lower(concat(#{activacionList.activacion.usuarioreg},'%'))",
			"lower(activacion.correo) like lower(concat(#{activacionList.activacion.correo},'%'))",
			"lower(activacion.clave) like lower(concat(#{activacionList.activacion.clave},'%'))",
			"lower(activacion.usuario) like lower(concat(#{activacionList.activacion.usuario},'%'))",
			"lower(activacion.llave) like lower(concat(#{activacionList.activacion.llave},'%'))",
			"lower(activacion.promotor) like lower(concat(#{activacionList.activacion.promotor},'%'))",
			"lower(activacion.usuariomod) like lower(concat(#{activacionList.activacion.usuariomod},'%'))", };

	private Activacion activacion = new Activacion();

	public ActivacionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Activacion getActivacion() {
		return activacion;
	}
	
	public void setActivacion(Activacion activacion){
		this.activacion = activacion;
		
	}
}
