package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("cuentacreditoHome")
public class CuentacreditoHome extends EntityHome<Cuentacredito> {

	@In(create = true)
	CuentaHome cuentaHome;

	public void setCuentacreditoConsecutivo(Integer id) {
		setId(id);
	}

	public Integer getCuentacreditoConsecutivo() {
		return (Integer) getId();
	}

	@Override
	protected Cuentacredito createInstance() {
		Cuentacredito cuentacredito = new Cuentacredito();
		return cuentacredito;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Cuenta cuenta = cuentaHome.getDefinedInstance();
		if (cuenta != null) {
			getInstance().setCuenta(cuenta);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Cuentacredito getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
