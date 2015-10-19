package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("tipoprocesoHome")
public class TipoprocesoHome extends EntityHome<Tipoproceso> {

	public void setTipoprocesoTipo(String id) {
		setId(id);
	}

	public String getTipoprocesoTipo() {
		return (String) getId();
	}

	@Override
	protected Tipoproceso createInstance() {
		Tipoproceso tipoproceso = new Tipoproceso();
		return tipoproceso;
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

	public Tipoproceso getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Personaltipoproceso> getPersonaltipoprocesos() {
		return getInstance() == null ? null
				: new ArrayList<Personaltipoproceso>(getInstance()
						.getPersonaltipoprocesos());
	}

}
