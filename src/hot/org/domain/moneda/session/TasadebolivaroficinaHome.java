package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("tasadebolivaroficinaHome")
public class TasadebolivaroficinaHome extends EntityHome<Tasadebolivaroficina> {

	public void setTasadebolivaroficinaId(TasadebolivaroficinaId id) {
		setId(id);
	}

	public TasadebolivaroficinaId getTasadebolivaroficinaId() {
		return (TasadebolivaroficinaId) getId();
	}

	public TasadebolivaroficinaHome() {
		setTasadebolivaroficinaId(new TasadebolivaroficinaId());
	}

	@Override
	public boolean isIdDefined() {
		if (getTasadebolivaroficinaId().getFecha() == null)
			return false;
		if (getTasadebolivaroficinaId().getTipo() == null
				|| "".equals(getTasadebolivaroficinaId().getTipo()))
			return false;
		return true;
	}

	@Override
	protected Tasadebolivaroficina createInstance() {
		Tasadebolivaroficina tasadebolivaroficina = new Tasadebolivaroficina();
		tasadebolivaroficina.setId(new TasadebolivaroficinaId());
		return tasadebolivaroficina;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		if(getInstance().getId().getFecha()==null)
			getInstance().getId().setFecha(new Date());
	}

	public boolean isWired() {
		return true;
	}

	public Tasadebolivaroficina getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
