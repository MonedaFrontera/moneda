package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("reportesHome")
public class ReportesHome extends EntityHome<Reportes> {

	public void setReportesCodreporte(String id) {
		setId(id);
	}

	public String getReportesCodreporte() {
		return (String) getId();
	}

	@Override
	protected Reportes createInstance() {
		Reportes reportes = new Reportes();
		return reportes;
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

	public Reportes getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
