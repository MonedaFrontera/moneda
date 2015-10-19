package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("bancocolHome")
public class BancocolHome extends EntityHome<Bancocol> {

	public void setBancocolCodbanco(String id) {
		setId(id);
	}

	public String getBancocolCodbanco() {
		return (String) getId();
	}

	@Override
	protected Bancocol createInstance() {
		Bancocol bancocol = new Bancocol();
		return bancocol;
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

	public Bancocol getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Establecimiento> getEstablecimientos() {
		return getInstance() == null ? null : new ArrayList<Establecimiento>(
				getInstance().getEstablecimientos());
	}

}
