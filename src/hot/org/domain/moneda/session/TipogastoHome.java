package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("tipogastoHome")
public class TipogastoHome extends EntityHome<Tipogasto> {

	public void setTipogastoCodtipogasto(String id) {
		setId(id);
	}

	public String getTipogastoCodtipogasto() {
		return (String) getId();
	}

	@Override
	protected Tipogasto createInstance() {
		Tipogasto tipogasto = new Tipogasto();
		return tipogasto;
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

	public Tipogasto getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
