package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("activagestorHome")
public class ActivagestorHome extends EntityHome<Activagestor> {

	@In(create = true)
	GestorHome gestorHome;
	@In(create = true)
	ActivacionHome activacionHome;

	public void setActivagestorId(ActivagestorId id) {
		setId(id);
	}

	public ActivagestorId getActivagestorId() {
		return (ActivagestorId) getId();
	}

	public ActivagestorHome() {
		setActivagestorId(new ActivagestorId());
	}

	@Override
	public boolean isIdDefined() {
		if (getActivagestorId().getConsecutivo() == 0)
			return false;
		if (getActivagestorId().getDocumento() == null
				|| "".equals(getActivagestorId().getDocumento()))
			return false;
		if (getActivagestorId().getFecha() == null)
			return false;
		return true;
	}

	@Override
	protected Activagestor createInstance() {
		Activagestor activagestor = new Activagestor();
		activagestor.setId(new ActivagestorId());
		return activagestor;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Gestor gestor = gestorHome.getDefinedInstance();
		if (gestor != null) {
			getInstance().setGestor(gestor);
		}
		Activacion activacion = activacionHome.getDefinedInstance();
		if (activacion != null) {
			getInstance().setActivacion(activacion);
		}
	}

	public boolean isWired() {
		if (getInstance().getGestor() == null)
			return false;
		if (getInstance().getActivacion() == null)
			return false;
		return true;
	}

	public Activagestor getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
