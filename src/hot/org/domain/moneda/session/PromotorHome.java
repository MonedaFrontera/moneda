package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.domain.moneda.session.AsesorHome;
import org.domain.moneda.session.PersonalHome;

import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("promotorHome")
public class PromotorHome extends EntityHome<Promotor> {

	@In(create = true)
	PersonalHome personalHome;
	@In(create = true)
	AsesorHome asesorHome;

	public void setPromotorDocumento(String id) {
		setId(id);
	}

	public String getPromotorDocumento() {
		return (String) getId();
	}

	@Override
	protected Promotor createInstance() {
		Promotor promotor = new Promotor();
		return promotor;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Personal personal = personalHome.getDefinedInstance();
		if (personal != null) {
			getInstance().setPersonal(personal);
		}
		Asesor asesor = asesorHome.getDefinedInstance();
		if (asesor != null) {
			getInstance().setAsesor(asesor);
		}
	}

	public boolean isWired() {
		if (getInstance().getPersonal() == null)
			return false;
		return true;
	}

	public Promotor getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Tasabolivarnegociado> getTasabolivarnegociados() {
		return getInstance() == null ? null
				: new ArrayList<Tasabolivarnegociado>(getInstance()
						.getTasabolivarnegociados());
	}

	public List<Tarjeta> getTarjetas() {
		return getInstance() == null ? null : new ArrayList<Tarjeta>(
				getInstance().getTarjetas());
	}
	
	public List<Envios> getEnvioses() {
		return getInstance() == null ? null : new ArrayList<Envios>(
				getInstance().getEnvioses());
	}
	
	public List<Cuentapromotor> getCuentapromotors() {
		return getInstance() == null ? null : new ArrayList<Cuentapromotor>(
				getInstance().getCuentapromotors());
	}
	
	public List<Promotorfranquicia> getPromotorfranquicias() {
		return getInstance() == null ? null
				: new ArrayList<Promotorfranquicia>(getInstance()
						.getPromotorfranquicias());
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
	
	public List<Activacion> getActivacions() {
		return getInstance() == null ? null : new ArrayList<Activacion>(
				getInstance().getActivacions());
	}

}
