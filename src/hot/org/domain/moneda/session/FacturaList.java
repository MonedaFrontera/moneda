package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("facturaList")
public class FacturaList extends EntityQuery<Factura> {

	private static final String EJBQL = "select factura from Factura factura";

	private static final String[] RESTRICTIONS = {
			"lower(factura.id.numfactura) like lower(concat(#{facturaList.factura.id.numfactura},'%'))",
			"lower(establecimiento.nombreestable) like lower(concat(#{facturaList.establecimiento.nombreestable},'%'))",
			"lower(factura.id.codigounico) like lower(concat(#{facturaList.factura.id.codigounico},'%'))", };

	private Factura factura; 
	
	private Establecimiento establecimiento;

	public FacturaList() {
		factura = new Factura();
		establecimiento = new Establecimiento();
		factura.setId(new FacturaId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Factura getFactura() {
		return factura;
	}
	
	public Establecimiento getEstablecimiento() {
		return establecimiento;
	}
}
