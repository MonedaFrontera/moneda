package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("saldodiariopromotorHome")
public class SaldodiariopromotorHome extends EntityHome<Saldodiariopromotor> {

	@In(create = true)
	PromotorHome promotorHome;

	public void setSaldodiariopromotorId(SaldodiariopromotorId id) {
		setId(id);
	}

	public SaldodiariopromotorId getSaldodiariopromotorId() {
		return (SaldodiariopromotorId) getId();
	}

	public SaldodiariopromotorHome() {
		setSaldodiariopromotorId(new SaldodiariopromotorId());
	}

	@Override
	public boolean isIdDefined() {
		if (getSaldodiariopromotorId().getDocumento() == null
				|| "".equals(getSaldodiariopromotorId().getDocumento()))
			return false;
		if (getSaldodiariopromotorId().getFecha() == null)
			return false;
		return true;
	}

	@Override
	protected Saldodiariopromotor createInstance() {
		Saldodiariopromotor saldodiariopromotor = new Saldodiariopromotor();
		saldodiariopromotor.setId(new SaldodiariopromotorId());
		return saldodiariopromotor;
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
	}

	public boolean isWired() {
		if (getInstance().getPromotor() == null)
			return false;
		return true;
	}

	public Saldodiariopromotor getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
