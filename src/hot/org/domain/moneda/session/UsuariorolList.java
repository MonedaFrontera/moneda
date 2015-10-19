package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("usuariorolList")
public class UsuariorolList extends EntityQuery<Usuariorol> {

	private static final String EJBQL = "select usuariorol from Usuariorol usuariorol";

	private static final String[] RESTRICTIONS = { "lower(usuariorol.id.usuario) like lower(concat(#{usuariorolList.usuariorol.id.usuario},'%'))", };

	private Usuariorol usuariorol;

	public UsuariorolList() {
		usuariorol = new Usuariorol();
		usuariorol.setId(new UsuariorolId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Usuariorol getUsuariorol() {
		return usuariorol;
	}
}
