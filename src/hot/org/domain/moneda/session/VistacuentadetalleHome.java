package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.math.BigDecimal;
import java.util.Date;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("vistacuentadetalleHome")
public class VistacuentadetalleHome extends EntityHome<Vistacuentadetalle> {

	public void setVistacuentadetalleId(VistacuentadetalleId id) {
		setId(id);
	}

	public VistacuentadetalleId getVistacuentadetalleId() {
		return (VistacuentadetalleId) getId();
	}

	public VistacuentadetalleHome() {
		setVistacuentadetalleId(new VistacuentadetalleId());
	}

	@Override
	public boolean isIdDefined() {
		if (getVistacuentadetalleId().getNumcuenta() == null
				|| "".equals(getVistacuentadetalleId().getNumcuenta()))
			return false;
		if (getVistacuentadetalleId().getNombre() == null
				|| "".equals(getVistacuentadetalleId().getNombre()))
			return false;
		if (getVistacuentadetalleId().getFecha() == null)
			return false;
		if (getVistacuentadetalleId().getCreditos() == null)
			return false;
		if (getVistacuentadetalleId().getDebitos() == null)
			return false;
		if (getVistacuentadetalleId().getDetalle() == null
				|| "".equals(getVistacuentadetalleId().getDetalle()))
			return false;
		if (getVistacuentadetalleId().getNombrebanco() == null
				|| "".equals(getVistacuentadetalleId().getNombrebanco()))
			return false;
		if (getVistacuentadetalleId().getCodbanco() == null
				|| "".equals(getVistacuentadetalleId().getCodbanco()))
			return false;
		return true;
	}

	@Override
	protected Vistacuentadetalle createInstance() {
		Vistacuentadetalle vistacuentadetalle = new Vistacuentadetalle();
		vistacuentadetalle.setId(new VistacuentadetalleId());
		return vistacuentadetalle;
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

	public Vistacuentadetalle getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
