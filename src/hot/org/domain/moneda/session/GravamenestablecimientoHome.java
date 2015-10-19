package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("gravamenestablecimientoHome")
public class GravamenestablecimientoHome extends
		EntityHome<Gravamenestablecimiento> {

	@In(create = true)
	EstablecimientoHome establecimientoHome;
	@In(create = true)
	GravamenHome gravamenHome;

	public void setGravamenestablecimientoId(GravamenestablecimientoId id) {
		setId(id);
	}

	public GravamenestablecimientoId getGravamenestablecimientoId() {
		return (GravamenestablecimientoId) getId();
	}

	public GravamenestablecimientoHome() {
		setGravamenestablecimientoId(new GravamenestablecimientoId());
	}

	@Override
	public boolean isIdDefined() {
		if (getGravamenestablecimientoId().getCodigounico() == null
				|| "".equals(getGravamenestablecimientoId().getCodigounico()))
			return false;
		if (getGravamenestablecimientoId().getGravamen() == null
				|| "".equals(getGravamenestablecimientoId().getGravamen()))
			return false;
		return true;
	}

	@Override
	protected Gravamenestablecimiento createInstance() {
		Gravamenestablecimiento gravamenestablecimiento = new Gravamenestablecimiento();
		gravamenestablecimiento.setId(new GravamenestablecimientoId());
		return gravamenestablecimiento;
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
		Gravamen gravamen = gravamenHome.getDefinedInstance();
		if (gravamen != null) {
			getInstance().setGravamen(gravamen);
		}
	}

	public boolean isWired() {
		if (getInstance().getEstablecimiento() == null)
			return false;
		if (getInstance().getGravamen() == null)
			return false;
		return true;
	}

	public Gravamenestablecimiento getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
