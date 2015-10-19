package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("vistacuentadetalleList")
public class VistacuentadetalleList extends EntityQuery<Vistacuentadetalle> {

	private static final String EJBQL = "select vistacuentadetalle from Vistacuentadetalle vistacuentadetalle";

	private static final String[] RESTRICTIONS = {
			"lower(vistacuentadetalle.id.numcuenta) like lower(concat(#{vistacuentadetalleList.vistacuentadetalle.id.numcuenta},'%'))",
			"lower(vistacuentadetalle.id.nombre) like lower(concat(#{vistacuentadetalleList.vistacuentadetalle.id.nombre},'%'))",
			"lower(vistacuentadetalle.id.detalle) like lower(concat(#{vistacuentadetalleList.vistacuentadetalle.id.detalle},'%'))",
			"lower(vistacuentadetalle.id.nombrebanco) like lower(concat(#{vistacuentadetalleList.vistacuentadetalle.id.nombrebanco},'%'))",
			"lower(vistacuentadetalle.id.codbanco) like lower(concat(#{vistacuentadetalleList.vistacuentadetalle.id.codbanco},'%'))", };

	private Vistacuentadetalle vistacuentadetalle;

	public VistacuentadetalleList() {
		vistacuentadetalle = new Vistacuentadetalle();
		vistacuentadetalle.setId(new VistacuentadetalleId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Vistacuentadetalle getVistacuentadetalle() {
		return vistacuentadetalle;
	}
}
