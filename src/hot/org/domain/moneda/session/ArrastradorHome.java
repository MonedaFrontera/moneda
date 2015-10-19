package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("arrastradorHome")
public class ArrastradorHome extends EntityHome<Arrastrador> {

	@In(create = true)
	PersonalHome personalHome;

	public void setArrastradorDocumento(String id) {
		setId(id);
	}

	public String getArrastradorDocumento() {
		return (String) getId();
	}

	@Override
	protected Arrastrador createInstance() {
		Arrastrador arrastrador = new Arrastrador();
		return arrastrador;
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

	public Arrastrador getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Promotor> getPromotors() {
		return getInstance() == null ? null : new ArrayList<Promotor>(
				getInstance().getPromotors());
	}

}
