package org.domain.moneda.session;

import org.domain.moneda.entity.*;

import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("personalHome")
public class PersonalHome extends EntityHome<Personal> {
 
	@In(create = true)
	CargoHome cargoHome;
	@In(create = true)
	ArrastradorHome arrastradorHome;
	@In(create = true)
	AdministrativoHome administrativoHome;
	@In(create = true)
	PromotorHome promotorHome;
	@In(create = true)
	AsesorHome asesorHome;
	@In(create = true)
	GestorHome gestorHome;

	public void setPersonalDocumento(String id) {
		setId(id);
	}

	public String getPersonalDocumento() {
		return (String) getId();
	}

	@Override
	protected Personal createInstance() {
		Personal personal = new Personal();
		return personal;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Cargo cargo = cargoHome.getDefinedInstance();
		if (cargo != null) {
			getInstance().setCargo(cargo);
		}
		Arrastrador arrastrador = arrastradorHome.getDefinedInstance();
		if (arrastrador != null) {
			getInstance().setArrastrador(arrastrador);
		}
		Administrativo administrativo = administrativoHome.getDefinedInstance();
		if (administrativo != null) {
			getInstance().setAdministrativo(administrativo);
		}
		Promotor promotor = promotorHome.getDefinedInstance();
		if (promotor != null) {
			getInstance().setPromotor(promotor);
		}
		Asesor asesor = asesorHome.getDefinedInstance();
		if (asesor != null) {
			getInstance().setAsesor(asesor);
		}
		
		Gestor gestor = gestorHome.getDefinedInstance();
		if (gestor != null) {
			getInstance().setGestor(gestor);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Personal getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	
	public List<Gastos> getGastoses() {
		return getInstance() == null ? null : new ArrayList<Gastos>(
				getInstance().getGastoses());
	}
	
	public List<Saldo> getSaldos() {
		return getInstance() == null ? null : new ArrayList<Saldo>(
				getInstance().getSaldos());
	}

}
