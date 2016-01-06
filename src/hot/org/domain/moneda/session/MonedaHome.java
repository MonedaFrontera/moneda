package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("monedaHome")
public class MonedaHome extends EntityHome<Moneda> {

	public void setMonedaCodigomoneda(String id) {
		setId(id);
	}

	public String getMonedaCodigomoneda() {
		return (String) getId();
	}

	@Override
	protected Moneda createInstance() {
		Moneda moneda = new Moneda();
		return moneda;
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

	public Moneda getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	
}
