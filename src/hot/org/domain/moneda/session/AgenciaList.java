package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("agenciaList")
public class AgenciaList extends EntityQuery<Agencia> {

	private static final String EJBQL = "select agencia from Agencia agencia";

	private static final String[] RESTRICTIONS = { "lower(agencia.nombre) like lower(concat(#{agenciaList.agencia.nombre},'%'))", };

	private Agencia agencia = new Agencia();

	public AgenciaList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Agencia getAgencia() {
		return agencia;
	}
}
