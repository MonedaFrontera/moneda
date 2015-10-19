package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("transaccionList")
public class TransaccionList extends EntityQuery<Transaccion> {

	private static final String EJBQL = "select transaccion from Transaccion transaccion";

	private static final String[] RESTRICTIONS = {
			"lower(transaccion.tipotx) like lower(concat(#{transaccionList.transaccion.tipotx},'%'))",
			"transaccion.fechatx >= #{transaccionList.transaccion.fechatx}",
			"transaccion.fechatx <= #{transaccionList.transaccion.fechatx}",
			"lower(transaccion.numfactura) like lower(concat('%',#{transaccionList.transaccion.numfactura}))", };
 
	private Transaccion transaccion = new Transaccion();

	public TransaccionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Transaccion getTransaccion() {
		return transaccion;
	}
	
	private String numbaucher = new String();

	public String getNumbaucher() {
		return numbaucher;
	}

	public void setNumbaucher(String numbaucher) {
		this.numbaucher = numbaucher;
	}
	
	
}
