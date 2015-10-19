package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("vistaactobservacionHome")
public class VistaactobservacionHome extends EntityHome<Vistaactobservacion> {

	public void setVistaactobservacionId(VistaactobservacionId id) {
		setId(id);
	}

	public VistaactobservacionId getVistaactobservacionId() {
		return (VistaactobservacionId) getId();
	}

	public VistaactobservacionHome() {
		setVistaactobservacionId(new VistaactobservacionId());
	}

	@Override
	public boolean isIdDefined() {
		if (getVistaactobservacionId().getTipo() == null
				|| "".equals(getVistaactobservacionId().getTipo()))
			return false;
		if (getVistaactobservacionId().getConsecutivo() == null)
			return false;
		if (getVistaactobservacionId().getFecha() == null)
			return false;
		if (getVistaactobservacionId().getObservacion() == null
				|| "".equals(getVistaactobservacionId().getObservacion()))
			return false;
		if (getVistaactobservacionId().getDato() == null
				|| "".equals(getVistaactobservacionId().getDato()))
			return false;
		return true;
	}

	@Override
	protected Vistaactobservacion createInstance() {
		Vistaactobservacion vistaactobservacion = new Vistaactobservacion();
		vistaactobservacion.setId(new VistaactobservacionId());
		return vistaactobservacion;
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

	public Vistaactobservacion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
