package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("solicitudtransferenciaList")
public class SolicitudtransferenciaList extends
		EntityQuery<Solicitudtransferencia> {

	private static final String EJBQL = "select solicitudtransferencia from Solicitudtransferencia solicitudtransferencia";

	private static final String[] RESTRICTIONS = { "lower(solicitudtransferencia.documento) like lower(concat(#{solicitudtransferenciaList.solicitudtransferencia.documento},'%'))", };

	private Solicitudtransferencia solicitudtransferencia = new Solicitudtransferencia();

	public SolicitudtransferenciaList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Solicitudtransferencia getSolicitudtransferencia() {
		return solicitudtransferencia;
	}
}
