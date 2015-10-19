package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("promotorcomisiontxHome")
public class PromotorcomisiontxHome extends EntityHome<Promotorcomisiontx> {

	@In(create = true)
	PromotorHome promotorHome;
	@In(create = true)
	PaisHome paisHome;

	public void setPromotorcomisiontxId(PromotorcomisiontxId id) {
		setId(id);
	}

	public PromotorcomisiontxId getPromotorcomisiontxId() {
		return (PromotorcomisiontxId) getId();
	}

	public PromotorcomisiontxHome() {
		setPromotorcomisiontxId(new PromotorcomisiontxId());
	}

	@Override
	public boolean isIdDefined() {
		if (getPromotorcomisiontxId().getCodpais() == null
				|| "".equals(getPromotorcomisiontxId().getCodpais()))
			return false;
		if (getPromotorcomisiontxId().getFechainicio() == null)
			return false;
		if (getPromotorcomisiontxId().getDocumento() == null
				|| "".equals(getPromotorcomisiontxId().getDocumento()))
			return false;
		return true;
	}

	@Override
	protected Promotorcomisiontx createInstance() {
		Promotorcomisiontx promotorcomisiontx = new Promotorcomisiontx();
		promotorcomisiontx.setId(new PromotorcomisiontxId());
		return promotorcomisiontx;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Promotor promotor = promotorHome.getDefinedInstance();
		if (promotor != null) {
			getInstance().setPromotor(promotor);
		}
		Pais pais = paisHome.getDefinedInstance();
		if (pais != null) {
			getInstance().setPais(pais);
		}
	}

	public boolean isWired() {
		if (getInstance().getPromotor() == null)
			return false;
		if (getInstance().getPais() == null)
			return false;
		return true;
	}

	public Promotorcomisiontx getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
