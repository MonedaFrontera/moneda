package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("dolartodayHome")
public class DolartodayHome extends EntityHome<Dolartoday> {

	public void setDolartodayEpoch(Long id) {
		setId(id);
	}

	public Long getDolartodayEpoch() {
		return (Long) getId();
	}

	@Override
	protected Dolartoday createInstance() {
		Dolartoday dolartoday = new Dolartoday();
		return dolartoday;
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

	public Dolartoday getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
