package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("tasabolivarnegociadoList")
public class TasabolivarnegociadoList extends EntityQuery<Tasabolivarnegociado> {

	private static final String EJBQL = "select tasabolivarnegociado from Tasabolivarnegociado tasabolivarnegociado";

	private static final String[] RESTRICTIONS = {
			"lower(tasabolivarnegociado.id.documento) like lower(concat(#{tasabolivarnegociadoList.tasabolivarnegociado.id.documento},'%'))",
			"lower(tasabolivarnegociado.id.tipo) like lower(concat(#{tasabolivarnegociadoList.tasabolivarnegociado.id.tipo},'%'))", };

	private Tasabolivarnegociado tasabolivarnegociado;

	public TasabolivarnegociadoList() {
		tasabolivarnegociado = new Tasabolivarnegociado();
		tasabolivarnegociado.setId(new TasabolivarnegociadoId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Tasabolivarnegociado getTasabolivarnegociado() {
		return tasabolivarnegociado;
	}
}
