package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("actestadoList")
public class ActestadoList extends EntityQuery<Actestado> {

	private static final String EJBQL = "select actestado from Actestado actestado";

	private static final String[] RESTRICTIONS = {
			"lower(actestado.codestado) like lower(concat(#{actestadoList.actestado.codestado},'%'))",
			"lower(actestado.descripcion) like lower(concat(#{actestadoList.actestado.descripcion},'%'))", };

	private Actestado actestado = new Actestado();

	public ActestadoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		this.setOrder("descripcion");
		setMaxResults(25);
	}

	public Actestado getActestado() {
		return actestado;
	}
}
