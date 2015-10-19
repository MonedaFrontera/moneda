package org.domain.moneda.session;

import java.util.Date;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("autovozHome")
public class AutovozHome extends EntityHome<Autovoz> {

	@In(create = true)
	EstablecimientoHome establecimientoHome;
	@In(create = true)
	TarjetaHome tarjetaHome;

	public void setAutovozConsecutivo(Integer id) {
		setId(id);
	}

	public Integer getAutovozConsecutivo() {
		return (Integer) getId();
	}

	@Override
	protected Autovoz createInstance() {
		Autovoz autovoz = new Autovoz();
		return autovoz;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		if(getInstance().getFechatx()==null)
			getInstance().setFechatx(new Date());
		Establecimiento establecimiento = establecimientoHome
				.getDefinedInstance();
		if (establecimiento != null) {
			getInstance().setEstablecimiento(establecimiento);
		}
		Tarjeta tarjeta = tarjetaHome.getDefinedInstance();
		if (tarjeta != null) {
			getInstance().setTarjeta(tarjeta);
		}
	}

	public boolean isWired() {
		if (getInstance().getEstablecimiento() == null)
			return false;
		if (getInstance().getTarjeta() == null)
			return false;
		return true;
	}

	public Autovoz getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
