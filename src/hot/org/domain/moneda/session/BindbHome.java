package org.domain.moneda.session;

import org.domain.moneda.entity.*;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("bindbHome")
public class BindbHome extends EntityHome<Bindb> {

	public void setBindbBin(String id) {
		setId(id);
	}

	public String getBindbBin() {
		return (String) getId();
	}

	@Override
	protected Bindb createInstance() {
		Bindb bindb = new Bindb();
		return bindb;
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

	public Bindb getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
