package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("detallefacturaList")
public class DetallefacturaList extends EntityQuery<Detallefactura> {

	private static final String EJBQL = "select detallefactura from Detallefactura detallefactura";

	private static final String[] RESTRICTIONS = {
			"lower(detallefactura.id.numfactura) like lower(concat(#{detallefacturaList.detallefactura.id.numfactura},'%'))",
			"lower(detallefactura.id.refproducto) like lower(concat(#{detallefacturaList.detallefactura.id.refproducto},'%'))",
			"lower(detallefactura.id.codigounico) like lower(concat(#{detallefacturaList.detallefactura.id.codigounico},'%'))", };

	private Detallefactura detallefactura;

	public DetallefacturaList() {
		detallefactura = new Detallefactura();
		detallefactura.setId(new DetallefacturaId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Detallefactura getDetallefactura() {
		return detallefactura;
	}
}
