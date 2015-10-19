package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("vistasemanasanioHome")
public class VistasemanasanioHome extends EntityHome<Vistasemanasanio> {

	public void setVistasemanasanioId(VistasemanasanioId id) {
		setId(id);
	}

	public VistasemanasanioId getVistasemanasanioId() {
		return (VistasemanasanioId) getId();
	}

	public VistasemanasanioHome() {
		setVistasemanasanioId(new VistasemanasanioId());
	}

	@Override
	public boolean isIdDefined() {
		if (getVistasemanasanioId().getNumsemana() == null
				|| "".equals(getVistasemanasanioId().getNumsemana()))
			return false;
		if (getVistasemanasanioId().getFechaini() == null)
			return false;
		if (getVistasemanasanioId().getFechafin() == null)
			return false;
		return true;
	}

	@Override
	protected Vistasemanasanio createInstance() {
		Vistasemanasanio vistasemanasanio = new Vistasemanasanio();
		vistasemanasanio.setId(new VistasemanasanioId());
		return vistasemanasanio;
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

	public Vistasemanasanio getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
