package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("porcentajeparametrosHome")
public class PorcentajeparametrosHome extends EntityHome<Porcentajeparametros> {

	@In(create = true)
	FranquiciaHome franquiciaHome;
	@In(create = true)
	PaisHome paisHome;
	@In(create = true)
	BancoHome bancoHome;

	public void setPorcentajeparametrosId(PorcentajeparametrosId id) {
		setId(id);
	}

	public PorcentajeparametrosId getPorcentajeparametrosId() {
		return (PorcentajeparametrosId) getId();
	}

	public PorcentajeparametrosHome() {
		setPorcentajeparametrosId(new PorcentajeparametrosId());
	}

	@Override
	public boolean isIdDefined() {
		if (getPorcentajeparametrosId().getCodbanco() == null
				|| "".equals(getPorcentajeparametrosId().getCodbanco()))
			return false;
		if (getPorcentajeparametrosId().getCodfranquicia() == null
				|| "".equals(getPorcentajeparametrosId().getCodfranquicia()))
			return false;
		if (getPorcentajeparametrosId().getCodpais() == null
				|| "".equals(getPorcentajeparametrosId().getCodpais()))
			return false;
		if (getPorcentajeparametrosId().getFechainicio() == null)
			return false;
		return true;
	}

	@Override
	protected Porcentajeparametros createInstance() {
		Porcentajeparametros porcentajeparametros = new Porcentajeparametros();
		porcentajeparametros.setId(new PorcentajeparametrosId());
		return porcentajeparametros;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Franquicia franquicia = franquiciaHome.getDefinedInstance();
		if (franquicia != null) {
			getInstance().setFranquicia(franquicia);
		}
		Pais pais = paisHome.getDefinedInstance();
		if (pais != null) {
			getInstance().setPais(pais);
		}
		Banco banco = bancoHome.getDefinedInstance();
		if (banco != null) {
			getInstance().setBanco(banco);
		}
	}

	public boolean isWired() {
		if (getInstance().getFranquicia() == null)
			return false;
		if (getInstance().getPais() == null)
			return false;
		if (getInstance().getBanco() == null)
			return false;
		return true;
	}

	public Porcentajeparametros getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
