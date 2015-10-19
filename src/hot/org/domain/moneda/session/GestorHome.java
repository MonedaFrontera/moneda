package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("gestorHome")
public class GestorHome extends EntityHome<Gestor> {

	@In(create = true)
	PersonalHome personalHome;

	public void setGestorDocumento(String id) {
		setId(id);
	}

	public String getGestorDocumento() {
		return (String) getId();
	}

	@Override
	protected Gestor createInstance() {
		Gestor gestor = new Gestor();
		return gestor;
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

	public Gestor getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Activagestor> getActivagestors() {
		return getInstance() == null ? null : new ArrayList<Activagestor>(
				getInstance().getActivagestors());
	}

	public List<Activacion> getActivacions() {
		return getInstance() == null ? null : new ArrayList<Activacion>(
				getInstance().getActivacions());
	}

}
