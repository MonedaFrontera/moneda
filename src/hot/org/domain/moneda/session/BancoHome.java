package org.domain.moneda.session;

import org.domain.moneda.entity.*;

import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("bancoHome")
public class BancoHome extends EntityHome<Banco> {

	public void setBancoCodbanco(String id) {
		setId(id);
	}

	public String getBancoCodbanco() {
		return (String) getId();
	}

	@Override
	protected Banco createInstance() {
		Banco banco = new Banco();
		return banco;
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

	public Banco getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Tarjeta> getTarjetas() {
		return getInstance() == null ? null : new ArrayList<Tarjeta>(
				getInstance().getTarjetas());
	}
	
	public List<Cuenta> getCuentas() {
		return getInstance() == null ? null : new ArrayList<Cuenta>(
				getInstance().getCuentas());
	}
	
	public List<Cuentapromotor> getCuentapromotors() {
		return getInstance() == null ? null : new ArrayList<Cuentapromotor>(
				getInstance().getCuentapromotors());
	}
	
	public List<Activacion> getActivacions() {
		return getInstance() == null ? null : new ArrayList<Activacion>(
				getInstance().getActivacions());
	}

}
