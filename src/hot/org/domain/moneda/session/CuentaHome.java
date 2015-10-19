package org.domain.moneda.session;

import org.domain.moneda.entity.*;

import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("cuentaHome")
public class CuentaHome extends EntityHome<Cuenta> {

	@In(create = true)
	BancoHome bancoHome;

	public void setCuentaNumcuenta(String id) {
		setId(id);
	}

	public String getCuentaNumcuenta() {
		return (String) getId();
	}

	@Override
	protected Cuenta createInstance() {
		Cuenta cuenta = new Cuenta();
		return cuenta;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Banco banco = bancoHome.getDefinedInstance();
		if (banco != null) {
			getInstance().setBanco(banco);
		}
	}

	public boolean isWired() {
		if (getInstance().getBanco() == null)
			return false;
		return true;
	}

	public Cuenta getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Depositostarjeta> getDepositostarjetas() {
		return getInstance() == null ? null : new ArrayList<Depositostarjeta>(
				getInstance().getDepositostarjetas());
	}
	
	public List<Transferencia> getTransferencias() {
		return getInstance() == null ? null : new ArrayList<Transferencia>(
				getInstance().getTransferencias());
	}
	
	public List<Cuentacredito> getCuentacreditos() {
		return getInstance() == null ? null : new ArrayList<Cuentacredito>(
				getInstance().getCuentacreditos());
	}

}
