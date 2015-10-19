package org.domain.moneda.session;

import org.domain.moneda.entity.*;

import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("franquiciaHome")
public class FranquiciaHome extends EntityHome<Franquicia> {

	public void setFranquiciaCodfranquicia(String id) {
		setId(id);
	}

	public String getFranquiciaCodfranquicia() {
		return (String) getId();
	}

	@Override
	protected Franquicia createInstance() {
		Franquicia franquicia = new Franquicia();
		return franquicia;
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

	public Franquicia getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Tarjeta> getTarjetas() {
		return getInstance() == null ? null : new ArrayList<Tarjeta>(
				getInstance().getTarjetas());
	}
	
	public List<Promotorfranquicia> getPromotorfranquicias() {
		return getInstance() == null ? null
				: new ArrayList<Promotorfranquicia>(getInstance()
						.getPromotorfranquicias());
	}

}
