package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("enviosHome")
public class EnviosHome extends EntityHome<Envios> {

	@In(create = true)
	AsesorHome asesorHome;
	@In(create = true)
	PromotorHome promotorHome;

	public void setEnviosConsecutivo(Integer id) {
		setId(id);
	}

	public Integer getEnviosConsecutivo() {
		return (Integer) getId();
	}

	@Override
	protected Envios createInstance() {
		Envios envios = new Envios();
		return envios;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Asesor asesor = asesorHome.getDefinedInstance();
		if (asesor != null) {
			getInstance().setAsesor(asesor);
		}
		Promotor promotor = promotorHome.getDefinedInstance();
		if (promotor != null) {
			getInstance().setPromotor(promotor);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Envios getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
