package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("tasabolivartransferpaisHome")
public class TasabolivartransferpaisHome extends
		EntityHome<Tasabolivartransferpais> {

	@In(create = true)
	PaisisoHome paisisoHome;

	public void setTasabolivartransferpaisId(TasabolivartransferpaisId id) {
		setId(id);
	}

	public TasabolivartransferpaisId getTasabolivartransferpaisId() {
		return (TasabolivartransferpaisId) getId();
	}

	public TasabolivartransferpaisHome() {
		setTasabolivartransferpaisId(new TasabolivartransferpaisId());
	}

	@Override
	public boolean isIdDefined() {
		if (getTasabolivartransferpaisId().getFecha() == null)
			return false;
		if (getTasabolivartransferpaisId().getCodigopais() == null
				|| "".equals(getTasabolivartransferpaisId().getCodigopais()))
			return false;
		return true;
	}

	@Override
	protected Tasabolivartransferpais createInstance() {
		Tasabolivartransferpais tasabolivartransferpais = new Tasabolivartransferpais();
		tasabolivartransferpais.setId(new TasabolivartransferpaisId());
		return tasabolivartransferpais;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Paisiso paisiso = paisisoHome.getDefinedInstance();
		if (paisiso != null) {
			getInstance().setPaisiso(paisiso);
		}
	}

	public boolean isWired() {
		if (getInstance().getPaisiso() == null)
			return false;
		return true;
	}

	public Tasabolivartransferpais getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
