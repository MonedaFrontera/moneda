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

	public List<Tasabolivartransferpais> getTasabolivartransferpaises() {
		return getInstance() == null ? null
				: new ArrayList<Tasabolivartransferpais>(getInstance()
						.getTasabolivartransferpaises());
	}

	public List<Pais> getPaises() {
		return getInstance() == null ? null : new ArrayList<Pais>(getInstance()
				.getPaises());
	}

	public List<Pais> getPaises_1() {
		return getInstance() == null ? null : new ArrayList<Pais>(getInstance()
				.getPaises_1());
	}

	public List<Tasabolivartransferpais> getTasabolivartransferpaises_1() {
		return getInstance() == null ? null
				: new ArrayList<Tasabolivartransferpais>(getInstance()
						.getTasabolivartransferpaises_1());
	}

}
