package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("puntoestablecimientoHome")
public class PuntoestablecimientoHome extends EntityHome<Puntoestablecimiento> {

	@In(create = true)
	PuntodeventaHome puntodeventaHome;
	@In(create = true)
	EstablecimientoHome establecimientoHome;

	public void setPuntoestablecimientoId(PuntoestablecimientoId id) {
		setId(id);
	}

	public PuntoestablecimientoId getPuntoestablecimientoId() {
		return (PuntoestablecimientoId) getId();
	}

	public PuntoestablecimientoHome() {
		setPuntoestablecimientoId(new PuntoestablecimientoId());
	}

	@Override
	public boolean isIdDefined() {
		if (getPuntoestablecimientoId().getCodigounico() == null
				|| "".equals(getPuntoestablecimientoId().getCodigounico()))
			return false;
		if (getPuntoestablecimientoId().getCodpuntoventa() == null
				|| "".equals(getPuntoestablecimientoId().getCodpuntoventa()))
			return false;
		if (getPuntoestablecimientoId().getFechainicio() == null)
			return false;
		return true;
	}

	@Override
	protected Puntoestablecimiento createInstance() {
		Puntoestablecimiento puntoestablecimiento = new Puntoestablecimiento();
		puntoestablecimiento.setId(new PuntoestablecimientoId());
		return puntoestablecimiento;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Puntodeventa puntodeventa = puntodeventaHome.getDefinedInstance();
		if (puntodeventa != null) {
			getInstance().setPuntodeventa(puntodeventa);
		}
		Establecimiento establecimiento = establecimientoHome
				.getDefinedInstance();
		if (establecimiento != null) {
			getInstance().setEstablecimiento(establecimiento);
		}
	}

	public boolean isWired() {
		if (getInstance().getPuntodeventa() == null)
			return false;
		if (getInstance().getEstablecimiento() == null)
			return false;
		return true;
	}

	public Puntoestablecimiento getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
