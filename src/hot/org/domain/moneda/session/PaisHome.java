package org.domain.moneda.session;

import org.domain.moneda.entity.*;

import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("paisHome")
public class PaisHome extends EntityHome<Pais> {

	public void setPaisCodigopais(String id) {
		setId(id);
	}

	public String getPaisCodigopais() {
		return (String) getId();
	}

	@Override
	protected Pais createInstance() {
		Pais pais = new Pais();
		return pais;
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

	public Pais getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public List<Promotorcomisiontx> getPromotorcomisiontxes() {
		return getInstance() == null ? null
				: new ArrayList<Promotorcomisiontx>(getInstance()
						.getPromotorcomisiontxes());
	}

	public List<Promotortasa> getPromotortasas() {
		return getInstance() == null ? null : new ArrayList<Promotortasa>(
				getInstance().getPromotortasas());
	}

	public List<Porcentajecomisiontx> getPorcentajecomisiontxes() {
		return getInstance() == null ? null
				: new ArrayList<Porcentajecomisiontx>(getInstance()
						.getPorcentajecomisiontxes());
	}

	public List<Establecimiento> getEstablecimientos() {
		return getInstance() == null ? null : new ArrayList<Establecimiento>(
				getInstance().getEstablecimientos());
	}

	public List<Tasadolar> getTasadolars() {
		return getInstance() == null ? null : new ArrayList<Tasadolar>(
				getInstance().getTasadolars());
	}

}
