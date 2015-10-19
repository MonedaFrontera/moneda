package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("promotortasaHome")
public class PromotortasaHome extends EntityHome<Promotortasa> {

	@In(create = true)
	PromotorHome promotorHome;
	@In(create = true)
	PaisHome paisHome;

	public void setPromotortasaId(PromotortasaId id) {
		setId(id);
	}

	public PromotortasaId getPromotortasaId() {
		return (PromotortasaId) getId();
	}

	public PromotortasaHome() {
		setPromotortasaId(new PromotortasaId());
	}

	@Override
	public boolean isIdDefined() {
		if (getPromotortasaId().getDocumento() == null
				|| "".equals(getPromotortasaId().getDocumento()))
			return false;
		if (getPromotortasaId().getCodigopais() == null
				|| "".equals(getPromotortasaId().getCodigopais()))
			return false;
		if (getPromotortasaId().getFecha() == null)
			return false;
		return true;
	}

	@Override
	protected Promotortasa createInstance() {
		Promotortasa promotortasa = new Promotortasa();
		promotortasa.setId(new PromotortasaId());
		return promotortasa;
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
		Pais pais = paisHome.getDefinedInstance();
		if (pais != null) {
			getInstance().setPais(pais);
		}
	}

	public boolean isWired() {
		if (getInstance().getPromotor() == null)
			return false;
		if (getInstance().getPais() == null)
			return false;
		return true;
	}

	public Promotortasa getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
