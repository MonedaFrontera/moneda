package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("auditTarjetaviajeHome")
public class AuditTarjetaviajeHome extends EntityHome<AuditTarjetaviaje> {

	@In(create = true)
	ViajeHome viajeHome;
	@In(create = true)
	TarjetaHome tarjetaHome;

	public void setAuditTarjetaviajeId(AuditTarjetaviajeId id) {
		setId(id);
	}

	public AuditTarjetaviajeId getAuditTarjetaviajeId() {
		return (AuditTarjetaviajeId) getId();
	}

	public AuditTarjetaviajeHome() {
		setAuditTarjetaviajeId(new AuditTarjetaviajeId());
	}

	@Override
	public boolean isIdDefined() {
		if (getAuditTarjetaviajeId().getNumerotarjeta() == null
				|| "".equals(getAuditTarjetaviajeId().getNumerotarjeta()))
			return false;
		if (getAuditTarjetaviajeId().getConsecutivoviaje() == 0)
			return false;
		if (getAuditTarjetaviajeId().getEstado() == null)
			return false;
		if (getAuditTarjetaviajeId().getFechamod() == null)
			return false;
		if (getAuditTarjetaviajeId().getUsuariomod() == null
				|| "".equals(getAuditTarjetaviajeId().getUsuariomod()))
			return false;
		return true;
	}

	@Override
	protected AuditTarjetaviaje createInstance() {
		AuditTarjetaviaje auditTarjetaviaje = new AuditTarjetaviaje();
		auditTarjetaviaje.setId(new AuditTarjetaviajeId());
		return auditTarjetaviaje;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Viaje viaje = viajeHome.getDefinedInstance();
		if (viaje != null) {
			getInstance().setViaje(viaje);
		}
		Tarjeta tarjeta = tarjetaHome.getDefinedInstance();
		if (tarjeta != null) {
			getInstance().setTarjeta(tarjeta);
		}
	}

	public boolean isWired() {
		if (getInstance().getViaje() == null)
			return false;
		if (getInstance().getTarjeta() == null)
			return false;
		return true;
	}

	public AuditTarjetaviaje getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
