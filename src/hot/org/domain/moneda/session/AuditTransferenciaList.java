package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("auditTransferenciaList")
public class AuditTransferenciaList extends EntityQuery<AuditTransferencia> {

	private static final String EJBQL = "select auditTransferencia from AuditTransferencia auditTransferencia";

	private static final String[] RESTRICTIONS = {
			"lower(auditTransferencia.id.tipobolivar) like lower(concat(#{auditTransferenciaList.auditTransferencia.id.tipobolivar},'%'))",
			"lower(auditTransferencia.id.numcuenta) like lower(concat(#{auditTransferenciaList.auditTransferencia.id.numcuenta},'%'))",
			"lower(auditTransferencia.id.numcuentapromotor) like lower(concat(#{auditTransferenciaList.auditTransferencia.id.numcuentapromotor},'%'))",
			"lower(auditTransferencia.id.documentopromotor) like lower(concat(#{auditTransferenciaList.auditTransferencia.id.documentopromotor},'%'))",
			"lower(auditTransferencia.id.usuariomod) like lower(concat(#{auditTransferenciaList.auditTransferencia.id.usuariomod},'%'))", };

	private AuditTransferencia auditTransferencia;

	public AuditTransferenciaList() {
		auditTransferencia = new AuditTransferencia();
		auditTransferencia.setId(new AuditTransferenciaId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public AuditTransferencia getAuditTransferencia() {
		return auditTransferencia;
	}
}
