package org.domain.moneda.session;

import org.domain.moneda.entity.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.security.Identity;

@Name("tarjetaHome")
public class TarjetaHome extends EntityHome<Tarjeta> {

	@In(create = true)
	PromotorHome promotorHome;
	@In(create = true)
	FranquiciaHome franquiciaHome;
	@In(create = true)
	BancoHome bancoHome;
	
	@In Identity identity;

	public void setTarjetaNumerotarjeta(String id) {
		setId(id);
	}

	public String getTarjetaNumerotarjeta() {
		return (String) getId();
	}

	@Override
	protected Tarjeta createInstance() {
		Tarjeta tarjeta = new Tarjeta();
		return tarjeta;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		// Aca se establecen los datos para la auditoria como 
		// usuario que la modifico y la fecha de la modificacion o creacion
		getInstance().setFechamod(new Date());
		getInstance().setUsuariomod(identity.getUsername());
		
		Promotor promotor = promotorHome.getDefinedInstance();
		if (promotor != null) {
			getInstance().setPromotor(promotor);
		}
		Franquicia franquicia = franquiciaHome.getDefinedInstance();
		if (franquicia != null) {
			getInstance().setFranquicia(franquicia);
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
		if (getInstance().getBanco() == null)
			return false;
		return true;
	}

	public Tarjeta getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Transaccion> getTransaccions() {
		return getInstance() == null ? null : new ArrayList<Transaccion>(
				getInstance().getTransaccions());
	}

	public List<Depositostarjeta> getDepositostarjetas() {
		return getInstance() == null ? null : new ArrayList<Depositostarjeta>(
				getInstance().getDepositostarjetas());
	}
	
	public List<Tarjetaviaje> getTarjetaviajes() {
		return getInstance() == null ? null : new ArrayList<Tarjetaviaje>(
				getInstance().getTarjetaviajes());
	}
	
	public List<Autovoz> getAutovozs() {
		return getInstance() == null ? null : new ArrayList<Autovoz>(
				getInstance().getAutovozs());
	}
	
}
