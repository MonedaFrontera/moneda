package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("bloqueotarjetaHome")
public class BloqueotarjetaHome extends EntityHome<Bloqueotarjeta> {

	public void setBloqueotarjetaSecuencia(Integer id) {
		setId(id);
	}

	public Integer getBloqueotarjetaSecuencia() {
		return (Integer) getId();
	}

	@Override
	protected Bloqueotarjeta createInstance() {
		Bloqueotarjeta bloqueotarjeta = new Bloqueotarjeta();
		return bloqueotarjeta;
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

	public Bloqueotarjeta getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
