package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.math.BigDecimal;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("vistasaldolunesHome")
public class VistasaldolunesHome extends EntityHome<Vistasaldolunes> {

	public void setVistasaldolunesId(VistasaldolunesId id) {
		setId(id);
	}

	public VistasaldolunesId getVistasaldolunesId() {
		return (VistasaldolunesId) getId();
	}

	public VistasaldolunesHome() {
		setVistasaldolunesId(new VistasaldolunesId());
	}

	@Override
	public boolean isIdDefined() {
		if (getVistasaldolunesId().getDocupromo() == null
				|| "".equals(getVistasaldolunesId().getDocupromo()))
			return false;
		if (getVistasaldolunesId().getNombrepromo() == null
				|| "".equals(getVistasaldolunesId().getNombrepromo()))
			return false;
		if (getVistasaldolunesId().getAsesora() == null
				|| "".equals(getVistasaldolunesId().getAsesora()))
			return false;
		if (getVistasaldolunesId().getSaldoCliente() == null)
			return false;
		return true;
	}

	@Override
	protected Vistasaldolunes createInstance() {
		Vistasaldolunes vistasaldolunes = new Vistasaldolunes();
		vistasaldolunes.setId(new VistasaldolunesId());
		return vistasaldolunes;
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

	public Vistasaldolunes getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
