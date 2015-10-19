package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("vistacuentaList")
public class VistacuentaList extends EntityQuery<Vistacuenta> {

	private static final String EJBQL = "select vistacuenta from Vistacuenta vistacuenta";

	private static final String[] RESTRICTIONS = { "lower(vistacuenta.id.numcuenta) like lower(concat(#{vistacuentaList.vistacuenta.id.numcuenta},'%'))", };

	private Vistacuenta vistacuenta;

	public VistacuentaList() {
		vistacuenta = new Vistacuenta();
		vistacuenta.setId(new VistacuentaId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Vistacuenta getVistacuenta() {
		return vistacuenta;
	}
}
