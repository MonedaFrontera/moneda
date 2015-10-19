package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("cneHome")
public class CneHome extends EntityHome<Cne> {

	public void setCneId(CneId id) {
		setId(id);
	}

	public CneId getCneId() {
		return (CneId) getId();
	}

	public CneHome() {
		setCneId(new CneId());
	}

	@Override
	public boolean isIdDefined() {
		if (getCneId().getNacionalidad() == null
				|| "".equals(getCneId().getNacionalidad()))
			return false;
		if (getCneId().getCedula() == null || "".equals(getCneId().getCedula()))
			return false;
		if (getCneId().getPrimerApellido() == null
				|| "".equals(getCneId().getPrimerApellido()))
			return false;
		if (getCneId().getSegundoApellido() == null
				|| "".equals(getCneId().getSegundoApellido()))
			return false;
		if (getCneId().getPrimerNombre() == null
				|| "".equals(getCneId().getPrimerNombre()))
			return false;
		if (getCneId().getSegundoNombre() == null
				|| "".equals(getCneId().getSegundoNombre()))
			return false;
		if (getCneId().getEstado() == null || "".equals(getCneId().getEstado()))
			return false;
		if (getCneId().getCodCentro() == null
				|| "".equals(getCneId().getCodCentro()))
			return false;
		return true;
	}

	@Override
	protected Cne createInstance() {
		Cne cne = new Cne();
		cne.setId(new CneId());
		return cne;
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

	public Cne getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
