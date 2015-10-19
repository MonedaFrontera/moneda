package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("activacionHome")
public class ActivacionHome extends EntityHome<Activacion> {

	@In(create = true)
	GestorHome gestorHome;
	@In(create = true)
	PromotorHome promotorHome;
	@In(create = true)
	ActestadoHome actestadoHome;
	@In(create = true)
	BancoHome bancoHome;

	public void setActivacionConsecutivo(Integer id) {
		setId(id);
	}

	public Integer getActivacionConsecutivo() {
		return (Integer) getId();
	}

	@Override
	protected Activacion createInstance() {
		Activacion activacion = new Activacion();
		return activacion;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Gestor gestor = gestorHome.getDefinedInstance();
		if (gestor != null) {
			getInstance().setGestor(gestor);
		}
		Promotor promotor = promotorHome.getDefinedInstance();
		if (promotor != null) {
			getInstance().setPromotor(promotor);
		}
		Actestado actestado = actestadoHome.getDefinedInstance();
		if (actestado != null) {
			getInstance().setActestado(actestado);
		}
		Banco banco = bancoHome.getDefinedInstance();
		if (banco != null) {
			getInstance().setBanco(banco);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Activacion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Estadoactivacion> getEstadoactivacions() {
		return getInstance() == null ? null : new ArrayList<Estadoactivacion>(
				getInstance().getEstadoactivacions());
	}

	public List<Cita> getCitas() {
		return getInstance() == null ? null : new ArrayList<Cita>(getInstance()
				.getCitas());
	}

	public List<Activagestor> getActivagestors() {
		return getInstance() == null ? null : new ArrayList<Activagestor>(
				getInstance().getActivagestors());
	}

	public List<Observacion> getObservacions() {
		return getInstance() == null ? null : new ArrayList<Observacion>(
				getInstance().getObservacions());
	}

	

}
