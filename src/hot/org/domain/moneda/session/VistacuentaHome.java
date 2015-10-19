package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("vistacuentaHome")
public class VistacuentaHome extends EntityHome<Vistacuenta> {

	public void setVistacuentaId(VistacuentaId id) {
		setId(id);
	}

	public VistacuentaId getVistacuentaId() {
		return (VistacuentaId) getId();
	}

	public VistacuentaHome() {
		setVistacuentaId(new VistacuentaId());
	}

	@Override
	public boolean isIdDefined() {
		if (getVistacuentaId().getNumcuenta() == null
				|| "".equals(getVistacuentaId().getNumcuenta()))
			return false;
		if (getVistacuentaId().getFecha() == null)
			return false;
		return true;
	}

	@Override
	protected Vistacuenta createInstance() {
		Vistacuenta vistacuenta = new Vistacuenta();
		vistacuenta.setId(new VistacuentaId());
		return vistacuenta;
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

	public Vistacuenta getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
