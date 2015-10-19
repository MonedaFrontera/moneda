package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("auditTarjetaList")
public class AuditTarjetaList extends EntityQuery<AuditTarjeta> {

	private static final String EJBQL = "select auditTarjeta from AuditTarjeta auditTarjeta";

	private static final String[] RESTRICTIONS = {
			"lower(auditTarjeta.id.numerotarjeta) like lower(concat(#{auditTarjetaList.auditTarjeta.id.numerotarjeta},'%'))",
			"lower(auditTarjeta.id.bancoemisor) like lower(concat(#{auditTarjetaList.auditTarjeta.id.bancoemisor},'%'))",
			"lower(auditTarjeta.id.franquicia) like lower(concat(#{auditTarjetaList.auditTarjeta.id.franquicia},'%'))",
			"lower(auditTarjeta.id.documento) like lower(concat(#{auditTarjetaList.auditTarjeta.id.documento},'%'))",
			"lower(auditTarjeta.id.fechavencimiento) like lower(concat(#{auditTarjetaList.auditTarjeta.id.fechavencimiento},'%'))",
			"lower(auditTarjeta.id.codseguridad) like lower(concat(#{auditTarjetaList.auditTarjeta.id.codseguridad},'%'))",
			"lower(auditTarjeta.id.tarjetahabiente) like lower(concat(#{auditTarjetaList.auditTarjeta.id.tarjetahabiente},'%'))",
			"lower(auditTarjeta.id.direcciontarjetahabiente) like lower(concat(#{auditTarjetaList.auditTarjeta.id.direcciontarjetahabiente},'%'))",
			"lower(auditTarjeta.id.cedulatarjetahabiente) like lower(concat(#{auditTarjetaList.auditTarjeta.id.cedulatarjetahabiente},'%'))",
			"lower(auditTarjeta.id.telefonotarjetahabiente) like lower(concat(#{auditTarjetaList.auditTarjeta.id.telefonotarjetahabiente},'%'))",
			"lower(auditTarjeta.id.usuariomod) like lower(concat(#{auditTarjetaList.auditTarjeta.id.usuariomod},'%'))", };

	private AuditTarjeta auditTarjeta;

	public AuditTarjetaList() {
		auditTarjeta = new AuditTarjeta();
		auditTarjeta.setId(new AuditTarjetaId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public AuditTarjeta getAuditTarjeta() {
		return auditTarjeta;
	}
}
