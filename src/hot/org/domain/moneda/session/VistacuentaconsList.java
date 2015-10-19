package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("vistacuentaconsList")
public class VistacuentaconsList extends EntityQuery<Vistacuentacons> {

	private static final String EJBQL = "select vistacuentacons from Vistacuentacons vistacuentacons";

	private static final String[] RESTRICTIONS = {
			"lower(vistacuentacons.id.numcuenta) like lower(concat(#{vistacuentaconsList.vistacuentacons.id.numcuenta},'%'))",
			"lower(vistacuentacons.id.nombre) like lower(concat(#{vistacuentaconsList.vistacuentacons.id.nombre},'%'))",
			"lower(vistacuentacons.id.detalle) like lower(concat(#{vistacuentaconsList.vistacuentacons.id.detalle},'%'))",
			"lower(vistacuentacons.id.nombrebanco) like lower(concat(#{vistacuentaconsList.vistacuentacons.id.nombrebanco},'%'))",
			"lower(vistacuentacons.id.codbanco) like lower(concat(#{vistacuentaconsList.vistacuentacons.id.codbanco},'%'))", };

	private Vistacuentacons vistacuentacons;

	public VistacuentaconsList() {
		vistacuentacons = new Vistacuentacons();
		vistacuentacons.setId(new VistacuentaconsId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Vistacuentacons getVistacuentacons() {
		return vistacuentacons;
	}
}
