package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("actestadoHome")
public class ActestadoHome extends EntityHome<Actestado> {

	public void setActestadoCodestado(String id) {
		setId(id);
	}

	public String getActestadoCodestado() {
		return (String) getId();
	}

	@Override
	protected Actestado createInstance() {
		Actestado actestado = new Actestado();
		return actestado;
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

	public Actestado getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Estadoactivacion> getEstadoactivacions() {
		return getInstance() == null ? null : new ArrayList<Estadoactivacion>(
				getInstance().getEstadoactivacions());
	}

	public List<Activacion> getActivacions() {
		return getInstance() == null ? null : new ArrayList<Activacion>(
				getInstance().getActivacions());
	}

}
