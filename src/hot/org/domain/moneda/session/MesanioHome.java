package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("mesanioHome")
public class MesanioHome extends EntityHome<Mesanio> {

	public void setMesanioCodigomes(Integer id) {
		setId(id);
	}

	public Integer getMesanioCodigomes() {
		return (Integer) getId();
	}

	@Override
	protected Mesanio createInstance() {
		Mesanio mesanio = new Mesanio();
		return mesanio;
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

	public Mesanio getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
