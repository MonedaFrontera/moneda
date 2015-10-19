package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("vistacuentasvenezolanasList")
public class VistacuentasvenezolanasList extends
		EntityQuery<Vistacuentasvenezolanas> {

	private static final String EJBQL = "select vistacuentasvenezolanas from Vistacuentasvenezolanas vistacuentasvenezolanas";

	private static final String[] RESTRICTIONS = {
			"lower(vistacuentasvenezolanas.id.nombrebanco) like lower(concat(#{vistacuentasvenezolanasList.vistacuentasvenezolanas.id.nombrebanco},'%'))",
			"lower(vistacuentasvenezolanas.id.numcuenta) like lower(concat(#{vistacuentasvenezolanasList.vistacuentasvenezolanas.id.numcuenta},'%'))",
			"lower(vistacuentasvenezolanas.id.nombre) like lower(concat(#{vistacuentasvenezolanasList.vistacuentasvenezolanas.id.nombre},'%'))",
			"lower(vistacuentasvenezolanas.id.codbanco) like lower(concat(#{vistacuentasvenezolanasList.vistacuentasvenezolanas.id.codbanco},'%'))", };

	private Vistacuentasvenezolanas vistacuentasvenezolanas;

	public VistacuentasvenezolanasList() {
		vistacuentasvenezolanas = new Vistacuentasvenezolanas();
		vistacuentasvenezolanas.setId(new VistacuentasvenezolanasId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Vistacuentasvenezolanas getVistacuentasvenezolanas() {
		return vistacuentasvenezolanas;
	}
}
