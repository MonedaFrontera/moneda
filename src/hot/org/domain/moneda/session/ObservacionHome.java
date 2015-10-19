package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("observacionHome")
public class ObservacionHome extends EntityHome<Observacion> {

	@In(create = true)
	ActivacionHome activacionHome;

	public void setObservacionId(ObservacionId id) {
		setId(id);
	}

	public ObservacionId getObservacionId() {
		return (ObservacionId) getId();
	}

	public ObservacionHome() {
		setObservacionId(new ObservacionId());
	}

	@Override
	public boolean isIdDefined() {
		if (getObservacionId().getConsecutivo() == 0)
			return false;
		if (getObservacionId().getFecha() == null)
			return false;
		return true;
	}

	@Override
	protected Observacion createInstance() {
		Observacion observacion = new Observacion();
		observacion.setId(new ObservacionId());
		return observacion;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Activacion activacion = activacionHome.getDefinedInstance();
		if (activacion != null) {
			getInstance().setActivacion(activacion);
		}
	}

	public boolean isWired() {
		if (getInstance().getActivacion() == null)
			return false;
		return true;
	}

	public Observacion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
