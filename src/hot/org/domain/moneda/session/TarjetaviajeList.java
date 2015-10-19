package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("tarjetaviajeList")
public class TarjetaviajeList extends EntityQuery<Tarjetaviaje> {

	private static final String EJBQL = "select tarjetaviaje from Tarjetaviaje tarjetaviaje";

	private static final String[] RESTRICTIONS = { "lower(tarjetaviaje.id.numerotarjeta) like lower(concat(#{tarjetaviajeList.tarjetaviaje.id.numerotarjeta},'%'))", };

	private Tarjetaviaje tarjetaviaje;

	public TarjetaviajeList() {
		tarjetaviaje = new Tarjetaviaje();
		tarjetaviaje.setId(new TarjetaviajeId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Tarjetaviaje getTarjetaviaje() {
		return tarjetaviaje;
	}
}
