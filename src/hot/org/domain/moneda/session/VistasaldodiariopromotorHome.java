package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.math.BigDecimal;
import java.util.Date;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("vistasaldodiariopromotorHome")
public class VistasaldodiariopromotorHome extends
		EntityHome<Vistasaldodiariopromotor> {

	public void setVistasaldodiariopromotorId(VistasaldodiariopromotorId id) {
		setId(id);
	}

	public VistasaldodiariopromotorId getVistasaldodiariopromotorId() {
		return (VistasaldodiariopromotorId) getId();
	}

	public VistasaldodiariopromotorHome() {
		setVistasaldodiariopromotorId(new VistasaldodiariopromotorId());
	}

	@Override
	public boolean isIdDefined() {
		if (getVistasaldodiariopromotorId().getFecha() == null)
			return false;
		if (getVistasaldodiariopromotorId().getDocumento() == null
				|| "".equals(getVistasaldodiariopromotorId().getDocumento()))
			return false;
		if (getVistasaldodiariopromotorId().getPromotor() == null
				|| "".equals(getVistasaldodiariopromotorId().getPromotor()))
			return false;
		if (getVistasaldodiariopromotorId().getSaldoaldia() == null)
			return false;
		return true;
	}

	@Override
	protected Vistasaldodiariopromotor createInstance() {
		Vistasaldodiariopromotor vistasaldodiariopromotor = new Vistasaldodiariopromotor();
		vistasaldodiariopromotor.setId(new VistasaldodiariopromotorId());
		return vistasaldodiariopromotor;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public Vistasaldodiariopromotor getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
