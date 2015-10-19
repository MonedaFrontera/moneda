package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("vistacuentasfechaList")
public class VistacuentasfechaList extends EntityQuery<Vistacuentasfecha> {

	private static final String EJBQL = "select vistacuentasfecha from Vistacuentasfecha vistacuentasfecha";

	private static final String[] RESTRICTIONS = {
			"lower(vistacuentasfecha.id.nombrebanco) like lower(concat(#{vistacuentasfechaList.vistacuentasfecha.id.nombrebanco},'%'))",
			"lower(vistacuentasfecha.id.numcuenta) like lower(concat(#{vistacuentasfechaList.vistacuentasfecha.id.numcuenta},'%'))",
			"lower(vistacuentasfecha.id.nombre) like lower(concat(#{vistacuentasfechaList.vistacuentasfecha.id.nombre},'%'))",
			"lower(vistacuentasfecha.id.codbanco) like lower(concat(#{vistacuentasfechaList.vistacuentasfecha.id.codbanco},'%'))", };

	private Vistacuentasfecha vistacuentasfecha;

	public VistacuentasfechaList() {
		vistacuentasfecha = new Vistacuentasfecha();
		vistacuentasfecha.setId(new VistacuentasfechaId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Vistacuentasfecha getVistacuentasfecha() {
		return vistacuentasfecha;
	}
}
