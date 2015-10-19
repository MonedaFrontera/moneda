package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("solicitudtarjetaList")
public class SolicitudtarjetaList extends EntityQuery<Solicitudtarjeta> {

	private static final String EJBQL = "select solicitudtarjeta from Solicitudtarjeta solicitudtarjeta";

	private static final String[] RESTRICTIONS = { "lower(solicitudtarjeta.id.numerotarjeta) like lower(concat(#{solicitudtarjetaList.solicitudtarjeta.id.numerotarjeta},'%'))", };

	private Solicitudtarjeta solicitudtarjeta;

	public SolicitudtarjetaList() {
		solicitudtarjeta = new Solicitudtarjeta();
		solicitudtarjeta.setId(new SolicitudtarjetaId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Solicitudtarjeta getSolicitudtarjeta() {
		return solicitudtarjeta;
	}
}
