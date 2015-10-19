package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("promotorfranquiciaHome")
public class PromotorfranquiciaHome extends EntityHome<Promotorfranquicia> {

	@In(create = true)
	PromotorHome promotorHome;
	@In(create = true)
	FranquiciaHome franquiciaHome;

	public void setPromotorfranquiciaId(PromotorfranquiciaId id) {
		setId(id);
	}

	public PromotorfranquiciaId getPromotorfranquiciaId() {
		return (PromotorfranquiciaId) getId();
	}

	public PromotorfranquiciaHome() {
		setPromotorfranquiciaId(new PromotorfranquiciaId());
	}

	@Override
	public boolean isIdDefined() {
		if (getPromotorfranquiciaId().getCodfranquicia() == null
				|| "".equals(getPromotorfranquiciaId().getCodfranquicia()))
			return false;
		if (getPromotorfranquiciaId().getDocumento() == null
				|| "".equals(getPromotorfranquiciaId().getDocumento()))
			return false;
		return true;
	}

	@Override
	protected Promotorfranquicia createInstance() {
		Promotorfranquicia promotorfranquicia = new Promotorfranquicia();
		promotorfranquicia.setId(new PromotorfranquiciaId());
		return promotorfranquicia;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Promotor promotor = promotorHome.getDefinedInstance();
		if (promotor != null) {
			getInstance().setPromotor(promotor);
		}
		Franquicia franquicia = franquiciaHome.getDefinedInstance();
		if (franquicia != null) {
			getInstance().setFranquicia(franquicia);
		}
	}

	public boolean isWired() {
		if (getInstance().getPromotor() == null)
			return false;
		if (getInstance().getFranquicia() == null)
			return false;
		return true;
	}

	public Promotorfranquicia getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
