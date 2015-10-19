package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.math.BigDecimal;
import java.util.Date;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("auditTransferenciaHome")
public class AuditTransferenciaHome extends EntityHome<AuditTransferencia> {

	public void setAuditTransferenciaId(AuditTransferenciaId id) {
		setId(id);
	}

	public AuditTransferenciaId getAuditTransferenciaId() {
		return (AuditTransferenciaId) getId();
	}

	public AuditTransferenciaHome() {
		setAuditTransferenciaId(new AuditTransferenciaId());
	}

	@Override
	public boolean isIdDefined() {
		if (getAuditTransferenciaId().getConsecutivo() == 0)
			return false;
		if (getAuditTransferenciaId().getPreciobolivar() == null)
			return false;
		if (getAuditTransferenciaId().getTipobolivar() == null
				|| "".equals(getAuditTransferenciaId().getTipobolivar()))
			return false;
		if (getAuditTransferenciaId().getValorbolivar() == null)
			return false;
		if (getAuditTransferenciaId().getNumcuenta() == null
				|| "".equals(getAuditTransferenciaId().getNumcuenta()))
			return false;
		if (getAuditTransferenciaId().getNumcuentapromotor() == null
				|| "".equals(getAuditTransferenciaId().getNumcuentapromotor()))
			return false;
		if (getAuditTransferenciaId().getDocumentopromotor() == null
				|| "".equals(getAuditTransferenciaId().getDocumentopromotor()))
			return false;
		if (getAuditTransferenciaId().getPreciocompra() == null)
			return false;
		if (getAuditTransferenciaId().getPrecioasesor() == null)
			return false;
		if (getAuditTransferenciaId().getUsuariomod() == null
				|| "".equals(getAuditTransferenciaId().getUsuariomod()))
			return false;
		if (getAuditTransferenciaId().getFechamod() == null)
			return false;
		return true;
	}

	@Override
	protected AuditTransferencia createInstance() {
		AuditTransferencia auditTransferencia = new AuditTransferencia();
		auditTransferencia.setId(new AuditTransferenciaId());
		return auditTransferencia;
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

	public AuditTransferencia getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
