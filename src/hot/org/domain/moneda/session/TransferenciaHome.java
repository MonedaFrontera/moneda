package org.domain.moneda.session;

import java.util.Date;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.security.Identity;

@Name("transferenciaHome")
public class TransferenciaHome extends EntityHome<Transferencia> {

	@In(create = true)
	GastosHome gastosHome;
	@In(create = true)
	CuentaHome cuentaHome;

	@In Identity identity;
	
	public void setTransferenciaConsecutivo(Integer id) {
		setId(id);
	}

	public Integer getTransferenciaConsecutivo() {
		return (Integer) getId();
	}

	@Override
	protected Transferencia createInstance() {
		Transferencia transferencia = new Transferencia();
		return transferencia;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {		
		// Aca se establecen los datos para la auditoria como 
		// usuario que la modifico y la fecha de la modificacion o creacion
		getInstance().setUsuariomod(this.identity.getUsername());
		getInstance().setFechamod(new Date());
		
		getInstance();
		Gastos gastos = gastosHome.getDefinedInstance();
		if (gastos != null) {
			getInstance().setGastos(gastos);
		}
		Cuenta cuenta = cuentaHome.getDefinedInstance();
		if (cuenta != null) {
			getInstance().setCuenta(cuenta);
		}
	}

	public boolean isWired() {
		
		
		if (getInstance().getGastos() == null)
			return false;
		return true;
	}

	public Transferencia getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
