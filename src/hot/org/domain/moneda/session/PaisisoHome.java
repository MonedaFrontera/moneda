package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("paisisoHome")
public class PaisisoHome extends EntityHome<Paisiso> {

	public void setPaisisoCodigopais(String id) {
		setId(id);
	}

	public String getPaisisoCodigopais() {
		return (String) getId();
	}

	@Override
	protected Paisiso createInstance() {
		Paisiso paisiso = new Paisiso();
		return paisiso;
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

	public Paisiso getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	

	public List<Pais> getPaises() {
		return getInstance() == null ? null : new ArrayList<Pais>(getInstance()
				.getPaises());
	}

}
