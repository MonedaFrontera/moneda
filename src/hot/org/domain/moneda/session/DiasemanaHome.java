package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("diasemanaHome")
public class DiasemanaHome extends EntityHome<Diasemana> {

	public void setDiasemanaId(DiasemanaId id) {
		setId(id);
	}

	public DiasemanaId getDiasemanaId() {
		return (DiasemanaId) getId();
	}

	public DiasemanaHome() {
		setDiasemanaId(new DiasemanaId());
	}

	@Override
	public boolean isIdDefined() {
		if (getDiasemanaId().getCodigodia() == 0)
			return false;
		if (getDiasemanaId().getNombredia() == null
				|| "".equals(getDiasemanaId().getNombredia()))
			return false;
		return true;
	}

	@Override
	protected Diasemana createInstance() {
		Diasemana diasemana = new Diasemana();
		diasemana.setId(new DiasemanaId());
		return diasemana;
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

	public Diasemana getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
