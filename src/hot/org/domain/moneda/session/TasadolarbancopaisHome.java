package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("tasadolarbancopaisHome")
public class TasadolarbancopaisHome extends EntityHome<Tasadolarbancopais> {

	@In(create = true)
	PaisHome paisHome;
	@In(create = true)
	BancoHome bancoHome;

	public void setTasadolarbancopaisId(TasadolarbancopaisId id) {
		setId(id);
	}

	public TasadolarbancopaisId getTasadolarbancopaisId() {
		return (TasadolarbancopaisId) getId();
	}

	public TasadolarbancopaisHome() {
		setTasadolarbancopaisId(new TasadolarbancopaisId());
	}

	@Override
	public boolean isIdDefined() {
		if (getTasadolarbancopaisId().getCodbanco() == null
				|| "".equals(getTasadolarbancopaisId().getCodbanco()))
			return false;
		if (getTasadolarbancopaisId().getCodigopais() == null
				|| "".equals(getTasadolarbancopaisId().getCodigopais()))
			return false;
		if (getTasadolarbancopaisId().getFecha() == null)
			return false;
		return true;
	}

	@Override
	protected Tasadolarbancopais createInstance() {
		Tasadolarbancopais tasadolarbancopais = new Tasadolarbancopais();
		tasadolarbancopais.setId(new TasadolarbancopaisId());
		return tasadolarbancopais;
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

	public Tasadolarbancopais getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
