package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.math.BigDecimal;
import java.util.Date;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("vistacuentasfechaHome")
public class VistacuentasfechaHome extends EntityHome<Vistacuentasfecha> {

	public void setVistacuentasfechaId(VistacuentasfechaId id) {
		setId(id);
	}

	public VistacuentasfechaId getVistacuentasfechaId() {
		return (VistacuentasfechaId) getId();
	}

	public VistacuentasfechaHome() {
		setVistacuentasfechaId(new VistacuentasfechaId());
	}

	@Override
	public boolean isIdDefined() {
		if (getVistacuentasfechaId().getNombrebanco() == null
				|| "".equals(getVistacuentasfechaId().getNombrebanco()))
			return false;
		if (getVistacuentasfechaId().getNumcuenta() == null
				|| "".equals(getVistacuentasfechaId().getNumcuenta()))
			return false;
		if (getVistacuentasfechaId().getNombre() == null
				|| "".equals(getVistacuentasfechaId().getNombre()))
			return false;
		if (getVistacuentasfechaId().getSaldo() == null)
			return false;
		if (getVistacuentasfechaId().getMovimientos() == null)
			return false;
		if (getVistacuentasfechaId().getDepositos() == null)
			return false;
		if (getVistacuentasfechaId().getTransferencias() == null)
			return false;
		if (getVistacuentasfechaId().getCodbanco() == null
				|| "".equals(getVistacuentasfechaId().getCodbanco()))
			return false;
		if (getVistacuentasfechaId().getFecha() == null)
			return false;
		return true;
	}

	@Override
	protected Vistacuentasfecha createInstance() {
		Vistacuentasfecha vistacuentasfecha = new Vistacuentasfecha();
		vistacuentasfecha.setId(new VistacuentasfechaId());
		return vistacuentasfecha;
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

	public Vistacuentasfecha getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
