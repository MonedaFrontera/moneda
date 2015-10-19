package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("auditTarjetaviajeList")
public class AuditTarjetaviajeList extends EntityQuery<AuditTarjetaviaje> {

	private static final String EJBQL = "select auditTarjetaviaje from AuditTarjetaviaje auditTarjetaviaje";

	private static final String[] RESTRICTIONS = {
			"lower(auditTarjetaviaje.id.numerotarjeta) like lower(concat(#{auditTarjetaviajeList.auditTarjetaviaje.id.numerotarjeta},'%'))",
			"lower(auditTarjetaviaje.id.usuariomod) like lower(concat(#{auditTarjetaviajeList.auditTarjetaviaje.id.usuariomod},'%'))", };

	private AuditTarjetaviaje auditTarjetaviaje;

	public AuditTarjetaviajeList() {
		auditTarjetaviaje = new AuditTarjetaviaje();
		auditTarjetaviaje.setId(new AuditTarjetaviajeId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public AuditTarjetaviaje getAuditTarjetaviaje() {
		return auditTarjetaviaje;
	}
}
