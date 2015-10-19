package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("comisionhistoricoHome")
public class ComisionhistoricoHome extends EntityHome<Comisionhistorico> {

	@In(create = true)
	PersonalHome personalHome;

	public void setComisionhistoricoId(ComisionhistoricoId id) {
		setId(id);
	}

	public ComisionhistoricoId getComisionhistoricoId() {
		return (ComisionhistoricoId) getId();
	}

	public ComisionhistoricoHome() {
		setComisionhistoricoId(new ComisionhistoricoId());
	}

	@Override
	public boolean isIdDefined() {
		if (getComisionhistoricoId().getDocumento() == null
				|| "".equals(getComisionhistoricoId().getDocumento()))
			return false;
		if (getComisionhistoricoId().getFechainicio() == null)
			return false;
		return true;
	}

	@Override
	protected Comisionhistorico createInstance() {
		Comisionhistorico comisionhistorico = new Comisionhistorico();
		comisionhistorico.setId(new ComisionhistoricoId());
		return comisionhistorico;
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

	public Comisionhistorico getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
