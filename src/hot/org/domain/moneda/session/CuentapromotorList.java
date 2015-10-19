package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("cuentapromotorList")
public class CuentapromotorList extends EntityQuery<Cuentapromotor> {

	private static final String EJBQL = "select cuentapromotor from Cuentapromotor cuentapromotor";

	private static final String[] RESTRICTIONS = {
			"lower(cuentapromotor.id.numcuenta) like lower(concat(#{cuentapromotorList.cuentapromotor.id.numcuenta},'%'))",
			"lower(cuentapromotor.id.documento) like lower(concat(#{cuentapromotorList.cuentapromotor.id.documento},'%'))",
			"lower(cuentapromotor.nombre) like lower(concat(#{cuentapromotorList.cuentapromotor.nombre},'%'))", };

	private Cuentapromotor cuentapromotor;

	public CuentapromotorList() {
		cuentapromotor = new Cuentapromotor();
		cuentapromotor.setId(new CuentapromotorId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Cuentapromotor getCuentapromotor() {
		return cuentapromotor;
	}
}
