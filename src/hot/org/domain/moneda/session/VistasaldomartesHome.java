package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.math.BigDecimal;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("vistasaldomartesHome")
public class VistasaldomartesHome extends EntityHome<Vistasaldomartes> {

	public void setVistasaldomartesId(VistasaldomartesId id) {
		setId(id);
	}

	public VistasaldomartesId getVistasaldomartesId() {
		return (VistasaldomartesId) getId();
	}

	public VistasaldomartesHome() {
		setVistasaldomartesId(new VistasaldomartesId());
	}

	@Override
	public boolean isIdDefined() {
		if (getVistasaldomartesId().getDocupromo() == null
				|| "".equals(getVistasaldomartesId().getDocupromo()))
			return false;
		if (getVistasaldomartesId().getNombrepromo() == null
				|| "".equals(getVistasaldomartesId().getNombrepromo()))
			return false;
		if (getVistasaldomartesId().getAsesora() == null
				|| "".equals(getVistasaldomartesId().getAsesora()))
			return false;
		if (getVistasaldomartesId().getSaldoCliente() == null)
			return false;
		return true;
	}

	@Override
	protected Vistasaldomartes createInstance() {
		Vistasaldomartes vistasaldomartes = new Vistasaldomartes();
		vistasaldomartes.setId(new VistasaldomartesId());
		return vistasaldomartes;
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

	public Vistasaldomartes getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
