package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.math.BigDecimal;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("vistasaldojuevesHome")
public class VistasaldojuevesHome extends EntityHome<Vistasaldojueves> {

	public void setVistasaldojuevesId(VistasaldojuevesId id) {
		setId(id);
	}

	public VistasaldojuevesId getVistasaldojuevesId() {
		return (VistasaldojuevesId) getId();
	}

	public VistasaldojuevesHome() {
		setVistasaldojuevesId(new VistasaldojuevesId());
	}

	@Override
	public boolean isIdDefined() {
		if (getVistasaldojuevesId().getDocupromo() == null
				|| "".equals(getVistasaldojuevesId().getDocupromo()))
			return false;
		if (getVistasaldojuevesId().getNombrepromo() == null
				|| "".equals(getVistasaldojuevesId().getNombrepromo()))
			return false;
		if (getVistasaldojuevesId().getAsesora() == null
				|| "".equals(getVistasaldojuevesId().getAsesora()))
			return false;
		if (getVistasaldojuevesId().getSaldoCliente() == null)
			return false;
		return true;
	}

	@Override
	protected Vistasaldojueves createInstance() {
		Vistasaldojueves vistasaldojueves = new Vistasaldojueves();
		vistasaldojueves.setId(new VistasaldojuevesId());
		return vistasaldojueves;
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

	public Vistasaldojueves getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
