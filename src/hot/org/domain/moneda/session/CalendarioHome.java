package org.domain.moneda.session;

import org.domain.moneda.entity.*;

import java.util.Date;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("calendarioHome")
public class CalendarioHome extends EntityHome<Calendario> {

	public void setCalendarioFecha(Date id) {
		setId(id);
	}

	public Date getCalendarioFecha() {
		return (Date) getId();
	}

	@Override
	protected Calendario createInstance() {
		Calendario calendario = new Calendario();
		return calendario;
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

	public Calendario getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
