package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("baucherHome")
public class BaucherHome extends EntityHome<Baucher> {

	@In(create = true)
	TransaccionHome transaccionHome;

	public void setBaucherId(BaucherId id) {
		setId(id);
	}

	public BaucherId getBaucherId() {
		return (BaucherId) getId();
	}

	public BaucherHome() {
		setBaucherId(new BaucherId());
	}

	@Override
	public boolean isIdDefined() {
		if (getBaucherId().getConsecutivo() == 0)
			return false;
		if (getBaucherId().getNumautorizacion() == null
				|| "".equals(getBaucherId().getNumautorizacion()))
			return false;
		return true;
	}

	@Override
	protected Baucher createInstance() {
		Baucher baucher = new Baucher();
		baucher.setId(new BaucherId());
		return baucher;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Transaccion transaccion = transaccionHome.getDefinedInstance();
		if (transaccion != null) {
			getInstance().setTransaccion(transaccion);
		}
	}

	public boolean isWired() {
		if (getInstance().getTransaccion() == null)
			return false;
		return true;
	}

	public Baucher getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
