package org.domain.moneda.session;

import org.domain.moneda.entity.*;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("cnedireccionHome")
public class CnedireccionHome extends EntityHome<Cnedireccion> {

	public void setCnedireccionId(CnedireccionId id) {
		setId(id);
	}

	public CnedireccionId getCnedireccionId() {
		return (CnedireccionId) getId();
	}

	public CnedireccionHome() {
		setCnedireccionId(new CnedireccionId());
	}

	@Override
	public boolean isIdDefined() {
		if (getCnedireccionId().getCodCentro() == null
				|| "".equals(getCnedireccionId().getCodCentro()))
			return false;
		if (getCnedireccionId().getTipo() == null
				|| "".equals(getCnedireccionId().getTipo()))
			return false;
		if (getCnedireccionId().getCodEstado() == null
				|| "".equals(getCnedireccionId().getCodEstado()))
			return false;
		if (getCnedireccionId().getCodMunicipio() == null
				|| "".equals(getCnedireccionId().getCodMunicipio()))
			return false;
		if (getCnedireccionId().getCodParroquia() == null
				|| "".equals(getCnedireccionId().getCodParroquia()))
			return false;
		if (getCnedireccionId().getNombreCentro() == null
				|| "".equals(getCnedireccionId().getNombreCentro()))
			return false;
		if (getCnedireccionId().getDireccionCentro() == null
				|| "".equals(getCnedireccionId().getDireccionCentro()))
			return false;
		if (getCnedireccionId().getCentroNuevo() == null
				|| "".equals(getCnedireccionId().getCentroNuevo()))
			return false;
		return true;
	}

	@Override
	protected Cnedireccion createInstance() {
		Cnedireccion cnedireccion = new Cnedireccion();
		cnedireccion.setId(new CnedireccionId());
		return cnedireccion;
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

	public Cnedireccion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
