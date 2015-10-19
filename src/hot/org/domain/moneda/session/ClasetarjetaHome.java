package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("clasetarjetaHome")
public class ClasetarjetaHome extends EntityHome<Clasetarjeta> {

	public void setClasetarjetaId(ClasetarjetaId id) {
		setId(id);
	}

	public ClasetarjetaId getClasetarjetaId() {
		return (ClasetarjetaId) getId();
	}

	public ClasetarjetaHome() {
		setClasetarjetaId(new ClasetarjetaId());
	}

	@Override
	public boolean isIdDefined() {
		if (getClasetarjetaId().getCodbanco() == null
				|| "".equals(getClasetarjetaId().getCodbanco()))
			return false;
		if (getClasetarjetaId().getCodfranquicia() == null
				|| "".equals(getClasetarjetaId().getCodfranquicia()))
			return false;
		if (getClasetarjetaId().getClase() == null
				|| "".equals(getClasetarjetaId().getClase()))
			return false;
		return true;
	}

	@Override
	protected Clasetarjeta createInstance() {
		Clasetarjeta clasetarjeta = new Clasetarjeta();
		clasetarjeta.setId(new ClasetarjetaId());
		return clasetarjeta;
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

	public Clasetarjeta getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
