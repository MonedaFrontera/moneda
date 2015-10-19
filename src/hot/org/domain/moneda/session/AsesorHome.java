package org.domain.moneda.session;

import java.util.ArrayList;
import java.util.List;

import org.domain.moneda.entity.*;
import org.domain.moneda.session.PersonalHome;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("asesorHome")
public class AsesorHome extends EntityHome<Asesor> {

	@In(create = true)
	PersonalHome personalHome;

	public void setAsesorDocumento(String id) {
		setId(id);
	}

	public String getAsesorDocumento() {
		return (String) getId();
	}

	@Override
	protected Asesor createInstance() {
		Asesor asesor = new Asesor();
		return asesor;
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

	public Asesor getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Promotor> getPromotors() {
		return getInstance() == null ? null : new ArrayList<Promotor>(
				getInstance().getPromotors());
	}
	
	public List<Envios> getEnvioses() {
		return getInstance() == null ? null : new ArrayList<Envios>(
				getInstance().getEnvioses());
	}

}
