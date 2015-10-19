package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("bancobinHome")
public class BancobinHome extends EntityHome<Bancobin> {

	public void setBancobinBin(String id) {
		setId(id);
	}

	public String getBancobinBin() {
		return (String) getId();
	}

	@Override
	protected Bancobin createInstance() {
		Bancobin bancobin = new Bancobin();
		return bancobin;
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

	public Bancobin getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
