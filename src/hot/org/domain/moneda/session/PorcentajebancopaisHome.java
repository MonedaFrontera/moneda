package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("porcentajebancopaisHome")
public class PorcentajebancopaisHome extends EntityHome<Porcentajebancopais> {

	@In(create = true)
	PaisHome paisHome;
	@In(create = true)
	BancoHome bancoHome;

	public void setPorcentajebancopaisId(PorcentajebancopaisId id) {
		setId(id);
	}

	public PorcentajebancopaisId getPorcentajebancopaisId() {
		return (PorcentajebancopaisId) getId();
	}

	public PorcentajebancopaisHome() {
		setPorcentajebancopaisId(new PorcentajebancopaisId());
	}

	@Override
	public boolean isIdDefined() {
		if (getPorcentajebancopaisId().getCodbanco() == null
				|| "".equals(getPorcentajebancopaisId().getCodbanco()))
			return false;
		if (getPorcentajebancopaisId().getCodigopais() == null
				|| "".equals(getPorcentajebancopaisId().getCodigopais()))
			return false;
		if (getPorcentajebancopaisId().getFecha() == null)
			return false;
		return true;
	}

	@Override
	protected Porcentajebancopais createInstance() {
		Porcentajebancopais porcentajebancopais = new Porcentajebancopais();
		porcentajebancopais.setId(new PorcentajebancopaisId());
		return porcentajebancopais;
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
		Banco banco = bancoHome.getDefinedInstance();
		if (banco != null) {
			getInstance().setBanco(banco);
		}
	}

	public boolean isWired() {
		if (getInstance().getPais() == null)
			return false;
		if (getInstance().getBanco() == null)
			return false;
		return true;
	}

	public Porcentajebancopais getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
