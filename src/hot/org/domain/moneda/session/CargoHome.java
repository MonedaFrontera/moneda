package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("cargoHome")
public class CargoHome extends EntityHome<Cargo> {

	public void setCargoCodcargo(String id) {
		setId(id);
	}

	public String getCargoCodcargo() {
		return (String) getId();
	}

	@Override
	protected Cargo createInstance() {
		Cargo cargo = new Cargo();
		return cargo;
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

	public Cargo getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Personal> getPersonals() {
		return getInstance() == null ? null : new ArrayList<Personal>(
				getInstance().getPersonals());
	}

}
