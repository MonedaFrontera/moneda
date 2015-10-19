package org.domain.moneda.session;

import org.domain.moneda.entity.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("transaccionHome")
public class TransaccionHome extends EntityHome<Transaccion> {

	@In(create = true)
	EstablecimientoHome establecimientoHome;
	@In(create = true)
	TarjetaHome tarjetaHome;

	public void setTransaccionConsecutivo(Integer id) {
		setId(id);
	}

	public Integer getTransaccionConsecutivo() {
		return (Integer) getId();
	}

	@Override
	protected Transaccion createInstance() {
		Transaccion transaccion = new Transaccion();
		return transaccion;
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

	public Transaccion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public List<Baucher> getBauchers() {
		return getInstance() == null ? null : new ArrayList<Baucher>(
				getInstance().getBauchers());
	}
	
	public List<Factura> getFacturas() {
		return getInstance() == null ? null : new ArrayList<Factura>(
				getInstance().getFacturas());
	}
	
}
