package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("auditViajeList")
public class AuditViajeList extends EntityQuery<AuditViaje> {

	private static final String EJBQL = "select auditViaje from AuditViaje auditViaje";

	private static final String[] RESTRICTIONS = {
			"lower(auditViaje.id.cedulatarjetahabiente) like lower(concat(#{auditViajeList.auditViaje.id.cedulatarjetahabiente},'%'))",
			"lower(auditViaje.id.usuariomod) like lower(concat(#{auditViajeList.auditViaje.id.usuariomod},'%'))", };

	private AuditViaje auditViaje;

	public AuditViajeList() {
		auditViaje = new AuditViaje();
		auditViaje.setId(new AuditViajeId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public AuditViaje getAuditViaje() {
		return auditViaje;
	}
}
