package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("franquiciaestablecimientoHome")
public class FranquiciaestablecimientoHome extends
		EntityHome<Franquiciaestablecimiento> {

	@In(create = true)
	FranquiciaHome franquiciaHome;
	@In(create = true)
	EstablecimientoHome establecimientoHome;

	public void setFranquiciaestablecimientoId(FranquiciaestablecimientoId id) {
		setId(id);
	}

	public FranquiciaestablecimientoId getFranquiciaestablecimientoId() {
		return (FranquiciaestablecimientoId) getId();
	}

	public FranquiciaestablecimientoHome() {
		setFranquiciaestablecimientoId(new FranquiciaestablecimientoId());
	}

	@Override
	public boolean isIdDefined() {
		if (getFranquiciaestablecimientoId().getFranquicia() == null
				|| "".equals(getFranquiciaestablecimientoId().getFranquicia()))
			return false;
		if (getFranquiciaestablecimientoId().getEstablecimiento() == null
				|| "".equals(getFranquiciaestablecimientoId()
						.getEstablecimiento()))
			return false;
		return true;
	}

	@Override
	protected Franquiciaestablecimiento createInstance() {
		Franquiciaestablecimiento franquiciaestablecimiento = new Franquiciaestablecimiento();
		franquiciaestablecimiento.setId(new FranquiciaestablecimientoId());
		return franquiciaestablecimiento;
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
		Establecimiento establecimiento = establecimientoHome
				.getDefinedInstance();
		if (establecimiento != null) {
			getInstance().setEstablecimiento(establecimiento);
		}
	}

	public boolean isWired() {
		if (getInstance().getFranquicia() == null)
			return false;
		if (getInstance().getEstablecimiento() == null)
			return false;
		return true;
	}

	public Franquiciaestablecimiento getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
