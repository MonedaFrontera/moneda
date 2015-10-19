package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("auditUsuarioHome")
public class AuditUsuarioHome extends EntityHome<AuditUsuario> {

	public void setAuditUsuarioId(AuditUsuarioId id) {
		setId(id);
	}

	public AuditUsuarioId getAuditUsuarioId() {
		return (AuditUsuarioId) getId();
	}

	public AuditUsuarioHome() {
		setAuditUsuarioId(new AuditUsuarioId());
	}

	@Override
	public boolean isIdDefined() {
		if (getAuditUsuarioId().getUsuario() == null
				|| "".equals(getAuditUsuarioId().getUsuario()))
			return false;
		if (getAuditUsuarioId().getFecha() == null)
			return false;
		if (getAuditUsuarioId().getOperacion() == null)
			return false;
		if (getAuditUsuarioId().getDescripcion() == null
				|| "".equals(getAuditUsuarioId().getDescripcion()))
			return false;
		return true;
	}

	@Override
	protected AuditUsuario createInstance() {
		AuditUsuario auditUsuario = new AuditUsuario();
		auditUsuario.setId(new AuditUsuarioId());
		return auditUsuario;
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

	public AuditUsuario getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
