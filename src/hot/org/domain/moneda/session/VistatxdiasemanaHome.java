package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.math.BigDecimal;
import java.util.Date;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("vistatxdiasemanaHome")
public class VistatxdiasemanaHome extends EntityHome<Vistatxdiasemana> {

	public void setVistatxdiasemanaId(VistatxdiasemanaId id) {
		setId(id);
	}

	public VistatxdiasemanaId getVistatxdiasemanaId() {
		return (VistatxdiasemanaId) getId();
	}

	public VistatxdiasemanaHome() {
		setVistatxdiasemanaId(new VistatxdiasemanaId());
	}

	@Override
	public boolean isIdDefined() {
		if (getVistatxdiasemanaId().getCodigodia() == null)
			return false;
		if (getVistatxdiasemanaId().getCodigomes() == null)
			return false;
		if (getVistatxdiasemanaId().getFechatx() == null)
			return false;
		if (getVistatxdiasemanaId().getValortxpesos() == null)
			return false;
		if (getVistatxdiasemanaId().getPromotor() == null
				|| "".equals(getVistatxdiasemanaId().getPromotor()))
			return false;
		if (getVistatxdiasemanaId().getAsesor() == null
				|| "".equals(getVistatxdiasemanaId().getAsesor()))
			return false;
		return true;
	}

	@Override
	protected Vistatxdiasemana createInstance() {
		Vistatxdiasemana vistatxdiasemana = new Vistatxdiasemana();
		vistatxdiasemana.setId(new VistatxdiasemanaId());
		return vistatxdiasemana;
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

	public Vistatxdiasemana getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
