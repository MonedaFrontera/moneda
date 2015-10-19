package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("transaccionerrorList")
public class TransaccionerrorList extends EntityQuery<Transaccionerror> {

	private static final String EJBQL = "select transaccionerror from Transaccionerror transaccionerror";

	private static final String[] RESTRICTIONS = {
			"lower(transaccionerror.id.numerotarjeta) like lower(concat(#{transaccionerrorList.transaccionerror.id.numerotarjeta},'%'))",
			"lower(transaccionerror.id.codigounico) like lower(concat(#{transaccionerrorList.transaccionerror.id.codigounico},'%'))",
			"lower(transaccionerror.id.tipotx) like lower(concat(#{transaccionerrorList.transaccionerror.id.tipotx},'%'))",
			"lower(transaccionerror.id.numfactura) like lower(concat(#{transaccionerrorList.transaccionerror.id.numfactura},'%'))",
			"lower(transaccionerror.id.usuariomod) like lower(concat(#{transaccionerrorList.transaccionerror.id.usuariomod},'%'))",
			"lower(transaccionerror.id.asesor) like lower(concat(#{transaccionerrorList.transaccionerror.id.asesor},'%'))",
			"lower(transaccionerror.id.promotor) like lower(concat(#{transaccionerrorList.transaccionerror.id.promotor},'%'))",
			"lower(transaccionerror.id.digitador) like lower(concat(#{transaccionerrorList.transaccionerror.id.digitador},'%'))",
			"lower(transaccionerror.id.error) like lower(concat(#{transaccionerrorList.transaccionerror.id.error},'%'))", };

	private Transaccionerror transaccionerror;

	public TransaccionerrorList() {
		transaccionerror = new Transaccionerror();
		transaccionerror.setId(new TransaccionerrorId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Transaccionerror getTransaccionerror() {
		return transaccionerror;
	}
}
