package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("empresaHome")
public class EmpresaHome extends EntityHome<Empresa> {

	public void setEmpresaNit(String id) {
		setId(id);
	}

	public String getEmpresaNit() {
		return (String) getId();
	}

	@Override
	protected Empresa createInstance() {
		Empresa empresa = new Empresa();
		return empresa;
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

	public Empresa getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Establecimiento> getEstablecimientos() {
		return getInstance() == null ? null : new ArrayList<Establecimiento>(
				getInstance().getEstablecimientos());
	}

}
