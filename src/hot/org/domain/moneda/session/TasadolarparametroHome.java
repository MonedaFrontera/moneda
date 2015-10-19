package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("tasadolarparametroHome")
public class TasadolarparametroHome extends EntityHome<Tasadolarparametro> {

	@In(create = true)
	FranquiciaHome franquiciaHome;
	@In(create = true)
	PaisHome paisHome;
	@In(create = true)
	EstablecimientoHome establecimientoHome;
	@In(create = true)
	BancoHome bancoHome;

	public void setTasadolarparametroConsecutivo(Integer id) {
		setId(id);
	}

	public Integer getTasadolarparametroConsecutivo() {
		return (Integer) getId();
	}

	@Override
	protected Tasadolarparametro createInstance() {
		Tasadolarparametro tasadolarparametro = new Tasadolarparametro();
		return tasadolarparametro;
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

	public Tasadolarparametro getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
