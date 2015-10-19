package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("agenciaHome")
public class AgenciaHome extends EntityHome<Agencia> {

	public void setAgenciaNombre(String id) {
		setId(id);
	}

	public String getAgenciaNombre() {
		return (String) getId();
	}

	@Override
	protected Agencia createInstance() {
		Agencia agencia = new Agencia();
		return agencia;
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

	public Agencia getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
