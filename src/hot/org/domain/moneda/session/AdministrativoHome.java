package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("administrativoHome")
public class AdministrativoHome extends EntityHome<Administrativo> {

	@In(create = true)
	PersonalHome personalHome;

	public void setAdministrativoDocumento(String id) {
		setId(id);
	}

	public String getAdministrativoDocumento() {
		return (String) getId();
	}

	@Override
	protected Administrativo createInstance() {
		Administrativo administrativo = new Administrativo();
		return administrativo;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Personal personal = personalHome.getDefinedInstance();
		if (personal != null) {
			getInstance().setPersonal(personal);
		}
	}

	public boolean isWired() {
		if (getInstance().getPersonal() == null)
			return false;
		return true;
	}

	public Administrativo getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
