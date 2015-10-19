package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.math.BigDecimal;
import java.util.Date;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("vistacuentaconsHome")
public class VistacuentaconsHome extends EntityHome<Vistacuentacons> {

	public void setVistacuentaconsId(VistacuentaconsId id) {
		setId(id);
	}

	public VistacuentaconsId getVistacuentaconsId() {
		return (VistacuentaconsId) getId();
	}

	public VistacuentaconsHome() {
		setVistacuentaconsId(new VistacuentaconsId());
	}

	@Override
	public boolean isIdDefined() {
		if (getVistacuentaconsId().getNumcuenta() == null
				|| "".equals(getVistacuentaconsId().getNumcuenta()))
			return false;
		if (getVistacuentaconsId().getNombre() == null
				|| "".equals(getVistacuentaconsId().getNombre()))
			return false;
		if (getVistacuentaconsId().getFecha() == null)
			return false;
		if (getVistacuentaconsId().getCreditos() == null)
			return false;
		if (getVistacuentaconsId().getDebitos() == null)
			return false;
		if (getVistacuentaconsId().getDetalle() == null
				|| "".equals(getVistacuentaconsId().getDetalle()))
			return false;
		if (getVistacuentaconsId().getNombrebanco() == null
				|| "".equals(getVistacuentaconsId().getNombrebanco()))
			return false;
		if (getVistacuentaconsId().getCodbanco() == null
				|| "".equals(getVistacuentaconsId().getCodbanco()))
			return false;
		if (getVistacuentaconsId().getSaldo() == null)
			return false;
		if (getVistacuentaconsId().getItem() == null)
			return false;
		return true;
	}

	@Override
	protected Vistacuentacons createInstance() {
		Vistacuentacons vistacuentacons = new Vistacuentacons();
		vistacuentacons.setId(new VistacuentaconsId());
		return vistacuentacons;
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

	public Vistacuentacons getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
