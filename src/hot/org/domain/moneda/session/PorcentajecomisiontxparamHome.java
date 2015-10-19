package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("porcentajecomisiontxparamHome")
public class PorcentajecomisiontxparamHome extends
		EntityHome<Porcentajecomisiontxparam> {

	@In(create = true)
	FranquiciaHome franquiciaHome;
	@In(create = true)
	PaisHome paisHome;
	@In(create = true)
	EstablecimientoHome establecimientoHome;
	@In(create = true)
	BancoHome bancoHome;

	public void setPorcentajecomisiontxparamConsecutivo(Integer id) {
		setId(id);
	}

	public Integer getPorcentajecomisiontxparamConsecutivo() {
		return (Integer) getId();
	}

	@Override
	protected Porcentajecomisiontxparam createInstance() {
		Porcentajecomisiontxparam porcentajecomisiontxparam = new Porcentajecomisiontxparam();
		return porcentajecomisiontxparam;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Franquicia franquicia = franquiciaHome.getDefinedInstance();
		if (franquicia != null) {
			getInstance().setFranquicia(franquicia);
		}
		Pais pais = paisHome.getDefinedInstance();
		if (pais != null) {
			getInstance().setPais(pais);
		}
		Establecimiento establecimiento = establecimientoHome
				.getDefinedInstance();
		if (establecimiento != null) {
			getInstance().setEstablecimiento(establecimiento);
		}
		Banco banco = bancoHome.getDefinedInstance();
		if (banco != null) {
			getInstance().setBanco(banco);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Porcentajecomisiontxparam getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
