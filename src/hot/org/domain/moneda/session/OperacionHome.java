package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("operacionHome")
public class OperacionHome extends EntityHome<Operacion> {

	public void setOperacionCodoperacion(Integer id) {
		setId(id);
	}

	public Integer getOperacionCodoperacion() {
		return (Integer) getId();
	}

	@Override
	protected Operacion createInstance() {
		Operacion operacion = new Operacion();
		return operacion;
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

	public Operacion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
