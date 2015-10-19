package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.Date;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("tasadolarparametrospromotorHome")
public class TasadolarparametrospromotorHome extends
		EntityHome<Tasadolarparametrospromotor> {

	@In(create = true)
	PromotorHome promotorHome;
	@In(create = true)
	FranquiciaHome franquiciaHome;
	@In(create = true)
	PaisHome paisHome;
	@In(create = true)
	BancoHome bancoHome;

	public void setTasadolarparametrospromotorId(
			TasadolarparametrospromotorId id) {
		setId(id);
	}

	public TasadolarparametrospromotorId getTasadolarparametrospromotorId() {
		return (TasadolarparametrospromotorId) getId();
	}

	public TasadolarparametrospromotorHome() {
		setTasadolarparametrospromotorId(new TasadolarparametrospromotorId());
	}

	@Override
	public boolean isIdDefined() {
		if (getTasadolarparametrospromotorId().getCodbanco() == null
				|| "".equals(getTasadolarparametrospromotorId().getCodbanco()))
			return false;
		if (getTasadolarparametrospromotorId().getCodfranquicia() == null
				|| "".equals(getTasadolarparametrospromotorId()
						.getCodfranquicia()))
			return false;
		if (getTasadolarparametrospromotorId().getCodpais() == null
				|| "".equals(getTasadolarparametrospromotorId().getCodpais()))
			return false;
		if (getTasadolarparametrospromotorId().getDocumento() == null
				|| "".equals(getTasadolarparametrospromotorId().getDocumento()))
			return false;
		if (getTasadolarparametrospromotorId().getFechainicio() == null)
			return false;
		return true;
	}

	@Override
	protected Tasadolarparametrospromotor createInstance() {
		Tasadolarparametrospromotor tasadolarparametrospromotor = new Tasadolarparametrospromotor();
		tasadolarparametrospromotor.setId(new TasadolarparametrospromotorId());
		return tasadolarparametrospromotor;
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

	public Tasadolarparametrospromotor getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
