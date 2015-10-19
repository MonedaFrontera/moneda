package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("auditTarjetaHome")
public class AuditTarjetaHome extends EntityHome<AuditTarjeta> {

	public void setAuditTarjetaId(AuditTarjetaId id) {
		setId(id);
	}

	public AuditTarjetaId getAuditTarjetaId() {
		return (AuditTarjetaId) getId();
	}

	public AuditTarjetaHome() {
		setAuditTarjetaId(new AuditTarjetaId());
	}

	@Override
	public boolean isIdDefined() {
		if (getAuditTarjetaId().getNumerotarjeta() == null
				|| "".equals(getAuditTarjetaId().getNumerotarjeta()))
			return false;
		if (getAuditTarjetaId().getBancoemisor() == null
				|| "".equals(getAuditTarjetaId().getBancoemisor()))
			return false;
		if (getAuditTarjetaId().getFranquicia() == null
				|| "".equals(getAuditTarjetaId().getFranquicia()))
			return false;
		if (getAuditTarjetaId().getDocumento() == null
				|| "".equals(getAuditTarjetaId().getDocumento()))
			return false;
		if (getAuditTarjetaId().getLimite() == 0)
			return false;
		if (getAuditTarjetaId().getFechainicio() == null)
			return false;
		if (getAuditTarjetaId().getFechafin() == null)
			return false;
		if (getAuditTarjetaId().getFechavencimiento() == null
				|| "".equals(getAuditTarjetaId().getFechavencimiento()))
			return false;
		if (getAuditTarjetaId().getCodseguridad() == null
				|| "".equals(getAuditTarjetaId().getCodseguridad()))
			return false;
		if (getAuditTarjetaId().getTarjetahabiente() == null
				|| "".equals(getAuditTarjetaId().getTarjetahabiente()))
			return false;
		if (getAuditTarjetaId().getDirecciontarjetahabiente() == null
				|| "".equals(getAuditTarjetaId().getDirecciontarjetahabiente()))
			return false;
		if (getAuditTarjetaId().getCedulatarjetahabiente() == null
				|| "".equals(getAuditTarjetaId().getCedulatarjetahabiente()))
			return false;
		if (getAuditTarjetaId().getTelefonotarjetahabiente() == null
				|| "".equals(getAuditTarjetaId().getTelefonotarjetahabiente()))
			return false;
		if (getAuditTarjetaId().getFechamod() == null)
			return false;
		if (getAuditTarjetaId().getUsuariomod() == null
				|| "".equals(getAuditTarjetaId().getUsuariomod()))
			return false;
		return true;
	}

	@Override
	protected AuditTarjeta createInstance() {
		AuditTarjeta auditTarjeta = new AuditTarjeta();
		auditTarjeta.setId(new AuditTarjetaId());
		return auditTarjeta;
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

	public AuditTarjeta getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
