package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("tasabolivarnegociadoHome")
public class TasabolivarnegociadoHome extends EntityHome<Tasabolivarnegociado> {

	@In(create = true)
	PromotorHome promotorHome;

	public void setTasabolivarnegociadoId(TasabolivarnegociadoId id) {
		setId(id);
	}

	public TasabolivarnegociadoId getTasabolivarnegociadoId() {
		return (TasabolivarnegociadoId) getId();
	}

	public TasabolivarnegociadoHome() {
		setTasabolivarnegociadoId(new TasabolivarnegociadoId());
	}

	@Override
	public boolean isIdDefined() {
		if (getTasabolivarnegociadoId().getFecha() == null)
			return false;
		if (getTasabolivarnegociadoId().getDocumento() == null
				|| "".equals(getTasabolivarnegociadoId().getDocumento()))
			return false;
		if (getTasabolivarnegociadoId().getTipo() == null
				|| "".equals(getTasabolivarnegociadoId().getTipo()))
			return false;
		return true;
	}

	@Override
	protected Tasabolivarnegociado createInstance() {
		Tasabolivarnegociado tasabolivarnegociado = new Tasabolivarnegociado();
		tasabolivarnegociado.setId(new TasabolivarnegociadoId());
		return tasabolivarnegociado;
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
		Promotor promotor = promotorHome.getDefinedInstance();
		if (promotor != null) {
			getInstance().setPromotor(promotor);
		}
	}

	public boolean isWired() {
		if (getInstance().getPromotor() == null)
			return false;
		return true;
	}

	public Tasabolivarnegociado getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
