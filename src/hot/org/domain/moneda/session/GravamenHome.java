package org.domain.moneda.session;

import java.util.ArrayList;
import java.util.List;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("gravamenHome")
public class GravamenHome extends EntityHome<Gravamen> {

	public void setGravamenCodigo(String id) {
		setId(id);
	}

	public String getGravamenCodigo() {
		return (String) getId();
	}

	@Override
	protected Gravamen createInstance() {
		Gravamen gravamen = new Gravamen();
		return gravamen;
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

	public Gravamen getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public List<Gravamenestablecimiento> getGravamenestablecimientos() {
		return getInstance() == null ? null
				: new ArrayList<Gravamenestablecimiento>(getInstance()
						.getGravamenestablecimientos());
	}

}
