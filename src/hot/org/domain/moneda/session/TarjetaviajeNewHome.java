package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("tarjetaviajeNewHome")
public class TarjetaviajeNewHome extends EntityHome<TarjetaviajeNew> {

	public void setTarjetaviajeNewId(TarjetaviajeNewId id) {
		setId(id);
	}

	public TarjetaviajeNewId getTarjetaviajeNewId() {
		return (TarjetaviajeNewId) getId();
	}

	public TarjetaviajeNewHome() {
		setTarjetaviajeNewId(new TarjetaviajeNewId());
	}

	@Override
	public boolean isIdDefined() {
		if (getTarjetaviajeNewId().getNumerotarjeta() == null
				|| "".equals(getTarjetaviajeNewId().getNumerotarjeta()))
			return false;
		if (getTarjetaviajeNewId().getConsecutivoviaje() == 0)
			return false;
		return true;
	}

	@Override
	protected TarjetaviajeNew createInstance() {
		TarjetaviajeNew tarjetaviajeNew = new TarjetaviajeNew();
		tarjetaviajeNew.setId(new TarjetaviajeNewId());
		return tarjetaviajeNew;
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

	public TarjetaviajeNew getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
