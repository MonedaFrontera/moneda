package org.domain.moneda.session;

import org.domain.moneda.entity.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.security.Identity;

@Name("viajeHome")
public class ViajeHome extends EntityHome<Viaje> {

	@In Identity identity;
	
	public void setViajeConsecutivo(Integer id) {
		setId(id);
	}

	public Integer getViajeConsecutivo() {
		return (Integer) getId();
	}

	@Override
	protected Viaje createInstance() {
		Viaje viaje = new Viaje();
		return viaje;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		getInstance().setFechamod(new Date());
		getInstance().setUsuariomod(identity.getUsername());
	}

	public boolean isWired() {
		return true;
	}

	public Viaje getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Tarjetaviaje> getTarjetaviajes() {
		return getInstance() == null ? null : new ArrayList<Tarjetaviaje>(
				getInstance().getTarjetaviajes());
	}
	
	


}
