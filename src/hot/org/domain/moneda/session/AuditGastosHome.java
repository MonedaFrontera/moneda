package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.math.BigDecimal;
import java.util.Date;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("auditGastosHome")
public class AuditGastosHome extends EntityHome<AuditGastos> {

	public void setAuditGastosId(AuditGastosId id) {
		setId(id);
	}

	public AuditGastosId getAuditGastosId() {
		return (AuditGastosId) getId();
	}

	public AuditGastosHome() {
		setAuditGastosId(new AuditGastosId());
	}

	@Override
	public boolean isIdDefined() {
		if (getAuditGastosId().getDocumento() == null
				|| "".equals(getAuditGastosId().getDocumento()))
			return false;
		if (getAuditGastosId().getFecha() == null)
			return false;
		if (getAuditGastosId().getValor() == null)
			return false;
		if (getAuditGastosId().getObservacion() == null
				|| "".equals(getAuditGastosId().getObservacion()))
			return false;
		if (getAuditGastosId().getTipogasto() == null
				|| "".equals(getAuditGastosId().getTipogasto()))
			return false;
		if (getAuditGastosId().getConsecutivo() == 0)
			return false;
		if (getAuditGastosId().getValorbolivar() == null)
			return false;
		if (getAuditGastosId().getUsuariomod() == null
				|| "".equals(getAuditGastosId().getUsuariomod()))
			return false;
		if (getAuditGastosId().getFechamod() == null)
			return false;
		return true;
	}

	@Override
	protected AuditGastos createInstance() {
		AuditGastos auditGastos = new AuditGastos();
		auditGastos.setId(new AuditGastosId());
		return auditGastos;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public AuditGastos getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
