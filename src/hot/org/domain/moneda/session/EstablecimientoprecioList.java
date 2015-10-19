package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("establecimientoprecioList")
public class EstablecimientoprecioList extends
		EntityQuery<Establecimientoprecio> {

	private static final String EJBQL = "select establecimientoprecio from Establecimientoprecio establecimientoprecio";

	private static final String[] RESTRICTIONS = { "lower(establecimientoprecio.id.codigounico) like lower(concat(#{establecimientoprecioList.establecimientoprecio.id.codigounico},'%'))", };

	private Establecimientoprecio establecimientoprecio;

	public EstablecimientoprecioList() {
		establecimientoprecio = new Establecimientoprecio();
		establecimientoprecio.setId(new EstablecimientoprecioId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Establecimientoprecio getEstablecimientoprecio() {
		return establecimientoprecio;
	}
}
