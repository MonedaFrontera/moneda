package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("solicitudtransferenciaHome")
public class SolicitudtransferenciaHome extends
		EntityHome<Solicitudtransferencia> {

	public void setSolicitudtransferenciaConsecutivo(Integer id) {
		setId(id);
	}

	public Integer getSolicitudtransferenciaConsecutivo() {
		return (Integer) getId();
	}

	@Override
	protected Solicitudtransferencia createInstance() {
		Solicitudtransferencia solicitudtransferencia = new Solicitudtransferencia();
		return solicitudtransferencia;
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

	public Solicitudtransferencia getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Solicitudtarjeta> getSolicitudtarjetas() {
		return getInstance() == null ? null : new ArrayList<Solicitudtarjeta>(
				getInstance().getSolicitudtarjetas());
	}

}
