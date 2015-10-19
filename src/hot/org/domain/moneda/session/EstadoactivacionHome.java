package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("estadoactivacionHome")
public class EstadoactivacionHome extends EntityHome<Estadoactivacion> {

	@In(create = true)
	ActivacionHome activacionHome;
	@In(create = true)
	ActestadoHome actestadoHome;

	public void setEstadoactivacionId(EstadoactivacionId id) {
		setId(id);
	}

	public EstadoactivacionId getEstadoactivacionId() {
		return (EstadoactivacionId) getId();
	}

	public EstadoactivacionHome() {
		setEstadoactivacionId(new EstadoactivacionId());
	}

	@Override
	public boolean isIdDefined() {
		if (getEstadoactivacionId().getConsecutivo() == 0)
			return false;
		if (getEstadoactivacionId().getEstado() == null
				|| "".equals(getEstadoactivacionId().getEstado()))
			return false;
		if (getEstadoactivacionId().getFecha() == null)
			return false;
		return true;
	}

	@Override
	protected Estadoactivacion createInstance() {
		Estadoactivacion estadoactivacion = new Estadoactivacion();
		estadoactivacion.setId(new EstadoactivacionId());
		return estadoactivacion;
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
		Actestado actestado = actestadoHome.getDefinedInstance();
		if (actestado != null) {
			getInstance().setActestado(actestado);
		}
	}

	public boolean isWired() {
		if (getInstance().getActivacion() == null)
			return false;
		if (getInstance().getActestado() == null)
			return false;
		return true;
	}

	public Estadoactivacion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
