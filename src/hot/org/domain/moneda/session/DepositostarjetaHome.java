package org.domain.moneda.session;

import org.domain.moneda.entity.*;

import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("depositostarjetaHome")
public class DepositostarjetaHome extends EntityHome<Depositostarjeta> {

	@In(create = true)
	TarjetaHome tarjetaHome;
	
	@In(create = true)
	CuentaHome cuentaHome;

	public void setDepositostarjetaConsecutivo(Integer id) {
		setId(id);
	}

	public Integer getDepositostarjetaConsecutivo() {
		return (Integer) getId();
	}

	@Override
	protected Depositostarjeta createInstance() {
		Depositostarjeta depositostarjeta = new Depositostarjeta();
		return depositostarjeta;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Tarjeta tarjeta = tarjetaHome.getDefinedInstance();
		if (tarjeta != null) {
			getInstance().setTarjeta(tarjeta);
		}
		Cuenta cuenta = cuentaHome.getDefinedInstance();
		if (cuenta != null) {
			getInstance().setCuenta(cuenta);
		}
	}

	public boolean isWired() {
		if (getInstance().getTarjeta() == null)
			return false;
		return true;
	}

	public Depositostarjeta getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
