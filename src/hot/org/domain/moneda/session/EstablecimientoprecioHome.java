package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("establecimientoprecioHome")
public class EstablecimientoprecioHome extends
		EntityHome<Establecimientoprecio> {

	@In(create = true)
	EstablecimientoHome establecimientoHome;

	public void setEstablecimientoprecioId(EstablecimientoprecioId id) {
		setId(id);
	}

	public EstablecimientoprecioId getEstablecimientoprecioId() {
		return (EstablecimientoprecioId) getId();
	}

	public EstablecimientoprecioHome() {
		setEstablecimientoprecioId(new EstablecimientoprecioId());
	}

	@Override
	public boolean isIdDefined() {
		if (getEstablecimientoprecioId().getCodigounico() == null
				|| "".equals(getEstablecimientoprecioId().getCodigounico()))
			return false;
		if (getEstablecimientoprecioId().getFechainicio() == null)
			return false;
		return true;
	}

	@Override
	protected Establecimientoprecio createInstance() {
		Establecimientoprecio establecimientoprecio = new Establecimientoprecio();
		establecimientoprecio.setId(new EstablecimientoprecioId());
		return establecimientoprecio;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Establecimiento establecimiento = establecimientoHome
				.getDefinedInstance();
		if (establecimiento != null) {
			getInstance().setEstablecimiento(establecimiento);
		}
	}

	public boolean isWired() {
		if (getInstance().getEstablecimiento() == null)
			return false;
		return true;
	}

	public Establecimientoprecio getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
