package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("solicitudtarjetaHome")
public class SolicitudtarjetaHome extends EntityHome<Solicitudtarjeta> {

	@In(create = true)
	SolicitudtransferenciaHome solicitudtransferenciaHome;
	@In(create = true)
	TarjetaHome tarjetaHome;

	public void setSolicitudtarjetaId(SolicitudtarjetaId id) {
		setId(id);
	}

	public SolicitudtarjetaId getSolicitudtarjetaId() {
		return (SolicitudtarjetaId) getId();
	}

	public SolicitudtarjetaHome() {
		setSolicitudtarjetaId(new SolicitudtarjetaId());
	}

	@Override
	public boolean isIdDefined() {
		if (getSolicitudtarjetaId().getConsecutivo() == 0)
			return false;
		if (getSolicitudtarjetaId().getNumerotarjeta() == null
				|| "".equals(getSolicitudtarjetaId().getNumerotarjeta()))
			return false;
		return true;
	}

	@Override
	protected Solicitudtarjeta createInstance() {
		Solicitudtarjeta solicitudtarjeta = new Solicitudtarjeta();
		solicitudtarjeta.setId(new SolicitudtarjetaId());
		return solicitudtarjeta;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Solicitudtransferencia solicitudtransferencia = solicitudtransferenciaHome
				.getDefinedInstance();
		if (solicitudtransferencia != null) {
			getInstance().setSolicitudtransferencia(solicitudtransferencia);
		}
		Tarjeta tarjeta = tarjetaHome.getDefinedInstance();
		if (tarjeta != null) {
			getInstance().setTarjeta(tarjeta);
		}
	}

	public boolean isWired() {
		if (getInstance().getSolicitudtransferencia() == null)
			return false;
		if (getInstance().getTarjeta() == null)
			return false;
		return true;
	}

	public Solicitudtarjeta getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
