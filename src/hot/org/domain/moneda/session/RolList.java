package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("rolList")
public class RolList extends EntityQuery<Rol> {

	private static final String EJBQL = "select rol from Rol rol";

	private static final String[] RESTRICTIONS = { "lower(rol.descripcion) like lower(concat(#{rolList.rol.descripcion},'%'))", };

	private Rol rol = new Rol();

	public RolList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Rol getRol() {
		return rol;
	}
}
