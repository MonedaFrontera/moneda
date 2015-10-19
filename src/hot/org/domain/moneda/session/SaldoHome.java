package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("saldoHome")
public class SaldoHome extends EntityHome<Saldo> {

	@In(create = true)
	PersonalHome personalHome;

	public void setSaldoId(SaldoId id) {
		setId(id);
	}

	public SaldoId getSaldoId() {
		return (SaldoId) getId();
	}

	public SaldoHome() {
		setSaldoId(new SaldoId());
	}

	@Override
	public boolean isIdDefined() {
		if (getSaldoId().getDocumento() == null
				|| "".equals(getSaldoId().getDocumento()))
			return false;
		if (getSaldoId().getFecha() == null)
			return false;
		return true;
	}

	@Override
	protected Saldo createInstance() {
		Saldo saldo = new Saldo();
		saldo.setId(new SaldoId());
		return saldo;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Personal personal = personalHome.getDefinedInstance();
		if (personal != null) {
			getInstance().setPersonal(personal);
		}
	}

	public boolean isWired() {
		if (getInstance().getPersonal() == null)
			return false;
		return true;
	}

	public Saldo getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
