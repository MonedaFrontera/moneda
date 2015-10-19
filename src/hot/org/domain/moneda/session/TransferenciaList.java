package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("transferenciaList")
public class TransferenciaList extends EntityQuery<Transferencia> {

	private static final String EJBQL = "select transferencia from Transferencia transferencia";

	private static final String[] RESTRICTIONS = { "lower(transferencia.tipobolivar) like lower(concat(#{transferenciaList.transferencia.tipobolivar},'%'))", };

	private Transferencia transferencia = new Transferencia();

	public TransferenciaList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(80);
	}

	public Transferencia getTransferencia() {
		return transferencia;
	}
}
