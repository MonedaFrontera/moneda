package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("citaHome")
public class CitaHome extends EntityHome<Cita> {

	@In(create = true)
	ActivacionHome activacionHome;

	public void setCitaId(CitaId id) {
		setId(id);
	}

	public CitaId getCitaId() {
		return (CitaId) getId();
	}

	public CitaHome() {
		setCitaId(new CitaId());
	}

	@Override
	public boolean isIdDefined() {
		if (getCitaId().getConsecutivo() == 0)
			return false;
		if (getCitaId().getFecha() == null)
			return false;
		return true;
	}

	@Override
	protected Cita createInstance() {
		Cita cita = new Cita();
		cita.setId(new CitaId());
		return cita;
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

	public Cita getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
