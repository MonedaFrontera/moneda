package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("saldodepositosHome")
public class SaldodepositosHome extends EntityHome<Saldodepositos> {

	@In(create = true)
	TarjetaHome tarjetaHome;

	public void setSaldodepositosId(SaldodepositosId id) {
		setId(id);
	}

	public SaldodepositosId getSaldodepositosId() {
		return (SaldodepositosId) getId();
	}

	public SaldodepositosHome() {
		setSaldodepositosId(new SaldodepositosId());
	}

	@Override
	public boolean isIdDefined() {
		if (getSaldodepositosId().getNumerotarjeta() == null
				|| "".equals(getSaldodepositosId().getNumerotarjeta()))
			return false;
		if (getSaldodepositosId().getFecha() == null)
			return false;
		return true;
	}

	@Override
	protected Saldodepositos createInstance() {
		Saldodepositos saldodepositos = new Saldodepositos();
		saldodepositos.setId(new SaldodepositosId());
		return saldodepositos;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Tarjeta tarjeta = tarjetaHome.getDefinedInstance();
		if (tarjeta != null) {
			getInstance().setTarjeta(tarjeta);
		}
	}

	public boolean isWired() {
		if (getInstance().getTarjeta() == null)
			return false;
		return true;
	}

	public Saldodepositos getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
