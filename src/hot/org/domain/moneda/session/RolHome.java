package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("rolHome")
public class RolHome extends EntityHome<Rol> {

	public void setRolIdrol(Integer id) {
		setId(id);
	}

	public Integer getRolIdrol() {
		return (Integer) getId();
	}

	@Override
	protected Rol createInstance() {
		Rol rol = new Rol();
		return rol;
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

	public Rol getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
