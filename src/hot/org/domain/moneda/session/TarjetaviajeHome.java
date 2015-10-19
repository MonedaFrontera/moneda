package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("tarjetaviajeHome")
public class TarjetaviajeHome extends EntityHome<Tarjetaviaje> {

	@In(create = true)
	ViajeHome viajeHome;
	@In(create = true)
	TarjetaHome tarjetaHome;

	public void setTarjetaviajeId(TarjetaviajeId id) {
		setId(id);
	}

	public TarjetaviajeId getTarjetaviajeId() {
		return (TarjetaviajeId) getId();
	}

	public TarjetaviajeHome() {
		setTarjetaviajeId(new TarjetaviajeId());
	}

	@Override
	public boolean isIdDefined() {
		if (getTarjetaviajeId().getNumerotarjeta() == null
				|| "".equals(getTarjetaviajeId().getNumerotarjeta()))
			return false;
		if (getTarjetaviajeId().getConsecutivoviaje() == 0)
			return false;
		return true;
	}

	@Override
	protected Tarjetaviaje createInstance() {
		Tarjetaviaje tarjetaviaje = new Tarjetaviaje();
		tarjetaviaje.setId(new TarjetaviajeId());
		return tarjetaviaje;
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

	public Tarjetaviaje getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
