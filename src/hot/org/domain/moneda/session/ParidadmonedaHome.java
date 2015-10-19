package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("paridadmonedaHome")
public class ParidadmonedaHome extends EntityHome<Paridadmoneda> {

	@In(create = true)
	MonedaHome monedaHome;

	public void setParidadmonedaFecha(Date id) {
		setId(id);
	}

	public Date getParidadmonedaFecha() {
		return (Date) getId();
	}

	@Override
	protected Paridadmoneda createInstance() {
		Paridadmoneda paridadmoneda = new Paridadmoneda();
		return paridadmoneda;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Moneda monedaByCodmonedadestino = monedaHome.getDefinedInstance();
		if (monedaByCodmonedadestino != null) {
			getInstance().setMonedaByCodmonedadestino(monedaByCodmonedadestino);
		}
		Moneda monedaByCodmonedaorigen = monedaHome.getDefinedInstance();
		if (monedaByCodmonedaorigen != null) {
			getInstance().setMonedaByCodmonedaorigen(monedaByCodmonedaorigen);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Paridadmoneda getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
