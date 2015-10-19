package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.math.BigDecimal;
import java.util.Date;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("vistacuentasaldoHome")
public class VistacuentasaldoHome extends EntityHome<Vistacuentasaldo> {

	public void setVistacuentasaldoId(VistacuentasaldoId id) {
		setId(id);
	}

	public VistacuentasaldoId getVistacuentasaldoId() {
		return (VistacuentasaldoId) getId();
	}

	public VistacuentasaldoHome() {
		setVistacuentasaldoId(new VistacuentasaldoId());
	}

	@Override
	public boolean isIdDefined() {
		if (getVistacuentasaldoId().getNumcuenta() == null
				|| "".equals(getVistacuentasaldoId().getNumcuenta()))
			return false;
		if (getVistacuentasaldoId().getNombre() == null
				|| "".equals(getVistacuentasaldoId().getNombre()))
			return false;
		if (getVistacuentasaldoId().getFecha() == null)
			return false;
		if (getVistacuentasaldoId().getCreditos() == null)
			return false;
		if (getVistacuentasaldoId().getDebitos() == null)
			return false;
		if (getVistacuentasaldoId().getDetalle() == null
				|| "".equals(getVistacuentasaldoId().getDetalle()))
			return false;
		if (getVistacuentasaldoId().getNombrebanco() == null
				|| "".equals(getVistacuentasaldoId().getNombrebanco()))
			return false;
		if (getVistacuentasaldoId().getCodbanco() == null
				|| "".equals(getVistacuentasaldoId().getCodbanco()))
			return false;
		if (getVistacuentasaldoId().getSaldo() == null)
			return false;
		if (getVistacuentasaldoId().getItem() == null)
			return false;
		if (getVistacuentasaldoId().getSum() == null)
			return false;
		return true;
	}

	@Override
	protected Vistacuentasaldo createInstance() {
		Vistacuentasaldo vistacuentasaldo = new Vistacuentasaldo();
		vistacuentasaldo.setId(new VistacuentasaldoId());
		return vistacuentasaldo;
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

	public Vistacuentasaldo getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
