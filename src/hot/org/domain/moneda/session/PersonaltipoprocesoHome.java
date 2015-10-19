package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("personaltipoprocesoHome")
public class PersonaltipoprocesoHome extends EntityHome<Personaltipoproceso> {

	@In(create = true)
	PersonalHome personalHome;
	@In(create = true)
	PuntodeventaHome puntodeventaHome;
	@In(create = true)
	TipoprocesoHome tipoprocesoHome;

	public void setPersonaltipoprocesoId(PersonaltipoprocesoId id) {
		setId(id);
	}

	public PersonaltipoprocesoId getPersonaltipoprocesoId() {
		return (PersonaltipoprocesoId) getId();
	}

	public PersonaltipoprocesoHome() {
		setPersonaltipoprocesoId(new PersonaltipoprocesoId());
	}

	@Override
	public boolean isIdDefined() {
		if (getPersonaltipoprocesoId().getTipoproceso() == null
				|| "".equals(getPersonaltipoprocesoId().getTipoproceso()))
			return false;
		if (getPersonaltipoprocesoId().getDocumento() == null
				|| "".equals(getPersonaltipoprocesoId().getDocumento()))
			return false;
		if (getPersonaltipoprocesoId().getFechainicio() == null)
			return false;
		if (getPersonaltipoprocesoId().getPuntodeventa() == null
				|| "".equals(getPersonaltipoprocesoId().getPuntodeventa()))
			return false;
		return true;
	}

	@Override
	protected Personaltipoproceso createInstance() {
		Personaltipoproceso personaltipoproceso = new Personaltipoproceso();
		personaltipoproceso.setId(new PersonaltipoprocesoId());
		return personaltipoproceso;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Personal personal = personalHome.getDefinedInstance();
		if (personal != null) {
			getInstance().setPersonal(personal);
		}
		Puntodeventa puntodeventa = puntodeventaHome.getDefinedInstance();
		if (puntodeventa != null) {
			getInstance().setPuntodeventa(puntodeventa);
		}
		Tipoproceso tipoproceso = tipoprocesoHome.getDefinedInstance();
		if (tipoproceso != null) {
			getInstance().setTipoproceso(tipoproceso);
		}
	}

	public boolean isWired() {
		if (getInstance().getPersonal() == null)
			return false;
		if (getInstance().getPuntodeventa() == null)
			return false;
		if (getInstance().getTipoproceso() == null)
			return false;
		return true;
	}

	public Personaltipoproceso getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
