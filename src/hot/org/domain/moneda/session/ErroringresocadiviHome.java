package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("erroringresocadiviHome")
public class ErroringresocadiviHome extends EntityHome<Erroringresocadivi> {

	@In(create = true)
	ActivacionHome activacionHome;

	public void setErroringresocadiviId(ErroringresocadiviId id) {
		setId(id);
	}

	public ErroringresocadiviId getErroringresocadiviId() {
		return (ErroringresocadiviId) getId();
	}

	public ErroringresocadiviHome() {
		setErroringresocadiviId(new ErroringresocadiviId());
	}

	@Override
	public boolean isIdDefined() {
		if (getErroringresocadiviId().getConsecutivo() == 0)
			return false;
		if (getErroringresocadiviId().getFecha() == null)
			return false;
		return true;
	}

	@Override
	protected Erroringresocadivi createInstance() {
		Erroringresocadivi erroringresocadivi = new Erroringresocadivi();
		erroringresocadivi.setId(new ErroringresocadiviId());
		return erroringresocadivi;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Activacion activacion = activacionHome.getDefinedInstance();
		if (activacion != null) {
			getInstance().setActivacion(activacion);
		}
	}

	public boolean isWired() {
		if (getInstance().getActivacion() == null)
			return false;
		return true;
	}

	public Erroringresocadivi getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
