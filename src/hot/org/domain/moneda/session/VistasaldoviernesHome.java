package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.math.BigDecimal;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("vistasaldoviernesHome")
public class VistasaldoviernesHome extends EntityHome<Vistasaldoviernes> {

	public void setVistasaldoviernesId(VistasaldoviernesId id) {
		setId(id);
	}

	public VistasaldoviernesId getVistasaldoviernesId() {
		return (VistasaldoviernesId) getId();
	}

	public VistasaldoviernesHome() {
		setVistasaldoviernesId(new VistasaldoviernesId());
	}

	@Override
	public boolean isIdDefined() {
		if (getVistasaldoviernesId().getDocupromo() == null
				|| "".equals(getVistasaldoviernesId().getDocupromo()))
			return false;
		if (getVistasaldoviernesId().getNombrepromo() == null
				|| "".equals(getVistasaldoviernesId().getNombrepromo()))
			return false;
		if (getVistasaldoviernesId().getAsesora() == null
				|| "".equals(getVistasaldoviernesId().getAsesora()))
			return false;
		if (getVistasaldoviernesId().getSaldoCliente() == null)
			return false;
		return true;
	}

	@Override
	protected Vistasaldoviernes createInstance() {
		Vistasaldoviernes vistasaldoviernes = new Vistasaldoviernes();
		vistasaldoviernes.setId(new VistasaldoviernesId());
		return vistasaldoviernes;
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

	public Vistasaldoviernes getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
