package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("auditGastosList")
public class AuditGastosList extends EntityQuery<AuditGastos> {

	private static final String EJBQL = "select auditGastos from AuditGastos auditGastos";

	private static final String[] RESTRICTIONS = {
			"lower(auditGastos.id.documento) like lower(concat(#{auditGastosList.auditGastos.id.documento},'%'))",
			"lower(auditGastos.id.observacion) like lower(concat(#{auditGastosList.auditGastos.id.observacion},'%'))",
			"lower(auditGastos.id.tipogasto) like lower(concat(#{auditGastosList.auditGastos.id.tipogasto},'%'))",
			"lower(auditGastos.id.usuariomod) like lower(concat(#{auditGastosList.auditGastos.id.usuariomod},'%'))", };

	private AuditGastos auditGastos;

	public AuditGastosList() {
		auditGastos = new AuditGastos();
		auditGastos.setId(new AuditGastosId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public AuditGastos getAuditGastos() {
		return auditGastos;
	}
}
