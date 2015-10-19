package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("transaccionerrorHome")
public class TransaccionerrorHome extends EntityHome<Transaccionerror> {

	public void setTransaccionerrorId(TransaccionerrorId id) {
		setId(id);
	}

	public TransaccionerrorId getTransaccionerrorId() {
		return (TransaccionerrorId) getId();
	}

	public TransaccionerrorHome() {
		setTransaccionerrorId(new TransaccionerrorId());
	}

	@Override
	public boolean isIdDefined() {
		if (getTransaccionerrorId().getNumerotarjeta() == null
				|| "".equals(getTransaccionerrorId().getNumerotarjeta()))
			return false;
		if (getTransaccionerrorId().getCodigounico() == null
				|| "".equals(getTransaccionerrorId().getCodigounico()))
			return false;
		if (getTransaccionerrorId().getFechatx() == null)
			return false;
		if (getTransaccionerrorId().getTipotx() == null
				|| "".equals(getTransaccionerrorId().getTipotx()))
			return false;
		if (getTransaccionerrorId().getNumfactura() == null
				|| "".equals(getTransaccionerrorId().getNumfactura()))
			return false;
		if (getTransaccionerrorId().getFechamod() == null)
			return false;
		if (getTransaccionerrorId().getUsuariomod() == null
				|| "".equals(getTransaccionerrorId().getUsuariomod()))
			return false;
		if (getTransaccionerrorId().getAsesor() == null
				|| "".equals(getTransaccionerrorId().getAsesor()))
			return false;
		if (getTransaccionerrorId().getPromotor() == null
				|| "".equals(getTransaccionerrorId().getPromotor()))
			return false;
		if (getTransaccionerrorId().getDigitador() == null
				|| "".equals(getTransaccionerrorId().getDigitador()))
			return false;
		if (getTransaccionerrorId().getError() == null
				|| "".equals(getTransaccionerrorId().getError()))
			return false;
		return true;
	}

	@Override
	protected Transaccionerror createInstance() {
		Transaccionerror transaccionerror = new Transaccionerror();
		transaccionerror.setId(new TransaccionerrorId());
		return transaccionerror;
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

	public Transaccionerror getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
