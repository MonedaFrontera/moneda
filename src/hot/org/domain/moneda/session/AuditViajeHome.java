package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("auditViajeHome")
public class AuditViajeHome extends EntityHome<AuditViaje> {

	public void setAuditViajeId(AuditViajeId id) {
		setId(id);
	}

	public AuditViajeId getAuditViajeId() {
		return (AuditViajeId) getId();
	}

	public AuditViajeHome() {
		setAuditViajeId(new AuditViajeId());
	}

	@Override
	public boolean isIdDefined() {
		if (getAuditViajeId().getConsecutivo() == 0)
			return false;
		if (getAuditViajeId().getFechainicio() == null)
			return false;
		if (getAuditViajeId().getFechafin() == null)
			return false;
		if (getAuditViajeId().getCupoinicialviajero() == null)
			return false;
		if (getAuditViajeId().getCupoinicialinternet() == null)
			return false;
		if (getAuditViajeId().getCupoviajero() == null)
			return false;
		if (getAuditViajeId().getCupointernet() == null)
			return false;
		if (getAuditViajeId().getCedulatarjetahabiente() == null
				|| "".equals(getAuditViajeId().getCedulatarjetahabiente()))
			return false;
		if (getAuditViajeId().getFechamod() == null)
			return false;
		if (getAuditViajeId().getUsuariomod() == null
				|| "".equals(getAuditViajeId().getUsuariomod()))
			return false;
		return true;
	}

	@Override
	protected AuditViaje createInstance() {
		AuditViaje auditViaje = new AuditViaje();
		auditViaje.setId(new AuditViajeId());
		return auditViaje;
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

	public AuditViaje getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
