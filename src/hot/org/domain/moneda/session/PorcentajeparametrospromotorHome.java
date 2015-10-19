package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("porcentajeparametrospromotorHome")
public class PorcentajeparametrospromotorHome extends
		EntityHome<Porcentajeparametrospromotor> {

	@In(create = true)
	PromotorHome promotorHome;
	@In(create = true)
	FranquiciaHome franquiciaHome;
	@In(create = true)
	PaisHome paisHome;
	@In(create = true)
	BancoHome bancoHome;

	public void setPorcentajeparametrospromotorId(
			PorcentajeparametrospromotorId id) {
		setId(id);
	}

	public PorcentajeparametrospromotorId getPorcentajeparametrospromotorId() {
		return (PorcentajeparametrospromotorId) getId();
	}

	public PorcentajeparametrospromotorHome() {
		setPorcentajeparametrospromotorId(new PorcentajeparametrospromotorId());
	}

	@Override
	public boolean isIdDefined() {
		if (getPorcentajeparametrospromotorId().getCodbanco() == null
				|| "".equals(getPorcentajeparametrospromotorId().getCodbanco()))
			return false;
		if (getPorcentajeparametrospromotorId().getCodfranquicia() == null
				|| "".equals(getPorcentajeparametrospromotorId()
						.getCodfranquicia()))
			return false;
		if (getPorcentajeparametrospromotorId().getCodpais() == null
				|| "".equals(getPorcentajeparametrospromotorId().getCodpais()))
			return false;
		if (getPorcentajeparametrospromotorId().getDocumento() == null
				|| ""
						.equals(getPorcentajeparametrospromotorId()
								.getDocumento()))
			return false;
		if (getPorcentajeparametrospromotorId().getFechainicio() == null)
			return false;
		return true;
	}

	@Override
	protected Porcentajeparametrospromotor createInstance() {
		Porcentajeparametrospromotor porcentajeparametrospromotor = new Porcentajeparametrospromotor();
		porcentajeparametrospromotor
				.setId(new PorcentajeparametrospromotorId());
		return porcentajeparametrospromotor;
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
		Franquicia franquicia = franquiciaHome.getDefinedInstance();
		if (franquicia != null) {
			getInstance().setFranquicia(franquicia);
		}
		Pais pais = paisHome.getDefinedInstance();
		if (pais != null) {
			getInstance().setPais(pais);
		}
		Banco banco = bancoHome.getDefinedInstance();
		if (banco != null) {
			getInstance().setBanco(banco);
		}
	}

	public boolean isWired() {
		if (getInstance().getPromotor() == null)
			return false;
		if (getInstance().getFranquicia() == null)
			return false;
		if (getInstance().getPais() == null)
			return false;
		if (getInstance().getBanco() == null)
			return false;
		return true;
	}

	public Porcentajeparametrospromotor getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
