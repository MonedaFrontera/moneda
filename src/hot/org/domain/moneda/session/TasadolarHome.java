package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("tasadolarHome")
public class TasadolarHome extends EntityHome<Tasadolar> {

	@In(create = true)
	PaisHome paisHome;

	public void setTasadolarId(TasadolarId id) {
		setId(id);
	}

	public TasadolarId getTasadolarId() {
		return (TasadolarId) getId();
	}

	public TasadolarHome() {
		setTasadolarId(new TasadolarId());
	}

	@Override
	public boolean isIdDefined() {
		if (getTasadolarId().getCodigopais() == null
				|| "".equals(getTasadolarId().getCodigopais()))
			return false;
		if (getTasadolarId().getFecha() == null)
			return false;
		return true;
	}

	@Override
	protected Tasadolar createInstance() {
		Tasadolar tasadolar = new Tasadolar();
		tasadolar.setId(new TasadolarId());
		return tasadolar;
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

	public Tasadolar getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
