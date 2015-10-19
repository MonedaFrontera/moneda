package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("tasadolarparametrosHome")
public class TasadolarparametrosHome extends EntityHome<Tasadolarparametros> {

	@In(create = true)
	FranquiciaHome franquiciaHome;
	@In(create = true)
	PaisHome paisHome;
	@In(create = true)
	BancoHome bancoHome;

	public void setTasadolarparametrosId(TasadolarparametrosId id) {
		setId(id);
	}

	public TasadolarparametrosId getTasadolarparametrosId() {
		return (TasadolarparametrosId) getId();
	}

	public TasadolarparametrosHome() {
		setTasadolarparametrosId(new TasadolarparametrosId());
	}

	@Override
	public boolean isIdDefined() {
		if (getTasadolarparametrosId().getCodbanco() == null
				|| "".equals(getTasadolarparametrosId().getCodbanco()))
			return false;
		if (getTasadolarparametrosId().getCodfranquicia() == null
				|| "".equals(getTasadolarparametrosId().getCodfranquicia()))
			return false;
		if (getTasadolarparametrosId().getCodpais() == null
				|| "".equals(getTasadolarparametrosId().getCodpais()))
			return false;
		if (getTasadolarparametrosId().getFechainicio() == null)
			return false;
		return true;
	}

	@Override
	protected Tasadolarparametros createInstance() {
		Tasadolarparametros tasadolarparametros = new Tasadolarparametros();
		tasadolarparametros.setId(new TasadolarparametrosId());
		return tasadolarparametros;
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

	public Tasadolarparametros getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
