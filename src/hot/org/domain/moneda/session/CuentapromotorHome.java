package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("cuentapromotorHome")
public class CuentapromotorHome extends EntityHome<Cuentapromotor> {

	@In(create = true)
	PromotorHome promotorHome;
	@In(create = true)
	BancoHome bancoHome;

	public void setCuentapromotorId(CuentapromotorId id) {
		setId(id);
	}

	public CuentapromotorId getCuentapromotorId() {
		return (CuentapromotorId) getId();
	}

	public CuentapromotorHome() {
		setCuentapromotorId(new CuentapromotorId());
	}

	@Override
	public boolean isIdDefined() {
		if (getCuentapromotorId().getNumcuenta() == null
				|| "".equals(getCuentapromotorId().getNumcuenta()))
			return false;
		if (getCuentapromotorId().getDocumento() == null
				|| "".equals(getCuentapromotorId().getDocumento()))
			return false;
		return true;
	}

	@Override
	protected Cuentapromotor createInstance() {
		Cuentapromotor cuentapromotor = new Cuentapromotor();
		cuentapromotor.setId(new CuentapromotorId());
		return cuentapromotor;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Promotor promotor = promotorHome.getDefinedInstance();
		if (promotor != null) {
			getInstance().setPromotor(promotor);
		}
		Banco banco = bancoHome.getDefinedInstance();
		if (banco != null) {
			getInstance().setBanco(banco);
		}
	}

	public boolean isWired() {
		if (getInstance().getPromotor() == null)
			return false;
		if (getInstance().getBanco() == null)
			return false;
		return true;
	}

	public Cuentapromotor getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Transferencia> getTransferencias() {
		return getInstance() == null ? null : new ArrayList<Transferencia>(
				getInstance().getTransferencias());
	}

}
