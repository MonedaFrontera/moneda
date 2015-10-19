package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.math.BigDecimal;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("vistasaldosabadoHome")
public class VistasaldosabadoHome extends EntityHome<Vistasaldosabado> {

	public void setVistasaldosabadoId(VistasaldosabadoId id) {
		setId(id);
	}

	public VistasaldosabadoId getVistasaldosabadoId() {
		return (VistasaldosabadoId) getId();
	}

	public VistasaldosabadoHome() {
		setVistasaldosabadoId(new VistasaldosabadoId());
	}

	@Override
	public boolean isIdDefined() {
		if (getVistasaldosabadoId().getDocupromo() == null
				|| "".equals(getVistasaldosabadoId().getDocupromo()))
			return false;
		if (getVistasaldosabadoId().getNombrepromo() == null
				|| "".equals(getVistasaldosabadoId().getNombrepromo()))
			return false;
		if (getVistasaldosabadoId().getAsesora() == null
				|| "".equals(getVistasaldosabadoId().getAsesora()))
			return false;
		if (getVistasaldosabadoId().getSaldoCliente() == null)
			return false;
		return true;
	}

	@Override
	protected Vistasaldosabado createInstance() {
		Vistasaldosabado vistasaldosabado = new Vistasaldosabado();
		vistasaldosabado.setId(new VistasaldosabadoId());
		return vistasaldosabado;
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

	public Vistasaldosabado getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
