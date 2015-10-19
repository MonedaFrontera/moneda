package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("vistacuentasaldoList")
public class VistacuentasaldoList extends EntityQuery<Vistacuentasaldo> {

	private static final String EJBQL = "select vistacuentasaldo from Vistacuentasaldo vistacuentasaldo";

	private static final String[] RESTRICTIONS = {
			"lower(vistacuentasaldo.id.numcuenta) like lower(concat(#{vistacuentasaldoList.vistacuentasaldo.id.numcuenta},'%'))",
			"lower(vistacuentasaldo.id.nombre) like lower(concat(#{vistacuentasaldoList.vistacuentasaldo.id.nombre},'%'))",
			"lower(vistacuentasaldo.id.detalle) like lower(concat(#{vistacuentasaldoList.vistacuentasaldo.id.detalle},'%'))",
			"lower(vistacuentasaldo.id.nombrebanco) like lower(concat(#{vistacuentasaldoList.vistacuentasaldo.id.nombrebanco},'%'))",
			"lower(vistacuentasaldo.id.codbanco) like lower(concat(#{vistacuentasaldoList.vistacuentasaldo.id.codbanco},'%'))", };

	private Vistacuentasaldo vistacuentasaldo;

	public VistacuentasaldoList() {
		vistacuentasaldo = new Vistacuentasaldo();
		vistacuentasaldo.setId(new VistacuentasaldoId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Vistacuentasaldo getVistacuentasaldo() {
		return vistacuentasaldo;
	}
}
