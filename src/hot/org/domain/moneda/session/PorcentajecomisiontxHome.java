package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("porcentajecomisiontxHome")
public class PorcentajecomisiontxHome extends EntityHome<Porcentajecomisiontx> {

	@In(create = true)
	PaisHome paisHome;

	public void setPorcentajecomisiontxId(PorcentajecomisiontxId id) {
		setId(id);
	}

	public PorcentajecomisiontxId getPorcentajecomisiontxId() {
		return (PorcentajecomisiontxId) getId();
	}

	public PorcentajecomisiontxHome() {
		setPorcentajecomisiontxId(new PorcentajecomisiontxId());
	}

	@Override
	public boolean isIdDefined() {
		if (getPorcentajecomisiontxId().getCodpais() == null
				|| "".equals(getPorcentajecomisiontxId().getCodpais()))
			return false;
		if (getPorcentajecomisiontxId().getFechainicio() == null)
			return false;
		return true;
	}

	@Override
	protected Porcentajecomisiontx createInstance() {
		Porcentajecomisiontx porcentajecomisiontx = new Porcentajecomisiontx();
		porcentajecomisiontx.setId(new PorcentajecomisiontxId());
		return porcentajecomisiontx;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Pais pais = paisHome.getDefinedInstance();
		if (pais != null) {
			getInstance().setPais(pais);
		}
	}

	public boolean isWired() {
		if (getInstance().getPais() == null)
			return false;
		return true;
	}

	public Porcentajecomisiontx getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
