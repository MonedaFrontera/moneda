package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.math.BigDecimal;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("vistasaldomiercolesHome")
public class VistasaldomiercolesHome extends EntityHome<Vistasaldomiercoles> {

	public void setVistasaldomiercolesId(VistasaldomiercolesId id) {
		setId(id);
	}

	public VistasaldomiercolesId getVistasaldomiercolesId() {
		return (VistasaldomiercolesId) getId();
	}

	public VistasaldomiercolesHome() {
		setVistasaldomiercolesId(new VistasaldomiercolesId());
	}

	@Override
	public boolean isIdDefined() {
		if (getVistasaldomiercolesId().getDocupromo() == null
				|| "".equals(getVistasaldomiercolesId().getDocupromo()))
			return false;
		if (getVistasaldomiercolesId().getNombrepromo() == null
				|| "".equals(getVistasaldomiercolesId().getNombrepromo()))
			return false;
		if (getVistasaldomiercolesId().getAsesora() == null
				|| "".equals(getVistasaldomiercolesId().getAsesora()))
			return false;
		if (getVistasaldomiercolesId().getSaldoCliente() == null)
			return false;
		return true;
	}

	@Override
	protected Vistasaldomiercoles createInstance() {
		Vistasaldomiercoles vistasaldomiercoles = new Vistasaldomiercoles();
		vistasaldomiercoles.setId(new VistasaldomiercolesId());
		return vistasaldomiercoles;
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

	public Vistasaldomiercoles getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
