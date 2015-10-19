package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.math.BigDecimal;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("vistacuentasvenezolanasHome")
public class VistacuentasvenezolanasHome extends
		EntityHome<Vistacuentasvenezolanas> {

	public void setVistacuentasvenezolanasId(VistacuentasvenezolanasId id) {
		setId(id);
	}

	public VistacuentasvenezolanasId getVistacuentasvenezolanasId() {
		return (VistacuentasvenezolanasId) getId();
	}

	public VistacuentasvenezolanasHome() {
		setVistacuentasvenezolanasId(new VistacuentasvenezolanasId());
	}

	@Override
	public boolean isIdDefined() {
		if (getVistacuentasvenezolanasId().getNombrebanco() == null
				|| "".equals(getVistacuentasvenezolanasId().getNombrebanco()))
			return false;
		if (getVistacuentasvenezolanasId().getNumcuenta() == null
				|| "".equals(getVistacuentasvenezolanasId().getNumcuenta()))
			return false;
		if (getVistacuentasvenezolanasId().getNombre() == null
				|| "".equals(getVistacuentasvenezolanasId().getNombre()))
			return false;
		if (getVistacuentasvenezolanasId().getSaldo() == null)
			return false;
		if (getVistacuentasvenezolanasId().getMovimientos() == null)
			return false;
		if (getVistacuentasvenezolanasId().getDepositos() == null)
			return false;
		if (getVistacuentasvenezolanasId().getTransferencias() == null)
			return false;
		if (getVistacuentasvenezolanasId().getCodbanco() == null
				|| "".equals(getVistacuentasvenezolanasId().getCodbanco()))
			return false;
		return true;
	}

	@Override
	protected Vistacuentasvenezolanas createInstance() {
		Vistacuentasvenezolanas vistacuentasvenezolanas = new Vistacuentasvenezolanas();
		vistacuentasvenezolanas.setId(new VistacuentasvenezolanasId());
		return vistacuentasvenezolanas;
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

	public Vistacuentasvenezolanas getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
