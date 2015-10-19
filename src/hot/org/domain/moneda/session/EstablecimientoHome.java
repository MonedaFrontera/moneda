package org.domain.moneda.session;

import org.domain.moneda.entity.*;

import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("establecimientoHome")
public class EstablecimientoHome extends EntityHome<Establecimiento> {

	@In(create = true)
	PaisHome paisHome;
	@In(create = true)
	EmpresaHome empresaHome;

	public void setEstablecimientoCodigounico(String id) {
		setId(id);
	}

	public String getEstablecimientoCodigounico() {
		return (String) getId();
	}

	@Override
	protected Establecimiento createInstance() {
		Establecimiento establecimiento = new Establecimiento();
		return establecimiento;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Pais pais = paisHome.getDefinedInstance();
		if (pais != null) {
			getInstance().setPais(pais);
		}
		Empresa empresa = empresaHome.getDefinedInstance();
		if (empresa != null) {
			getInstance().setEmpresa(empresa);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Establecimiento getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Puntoestablecimiento> getPuntoestablecimientos() {
		return getInstance() == null ? null
				: new ArrayList<Puntoestablecimiento>(getInstance()
						.getPuntoestablecimientos());
	}

	public List<Transaccion> getTransaccions() {
		return getInstance() == null ? null : new ArrayList<Transaccion>(
				getInstance().getTransaccions());
	}
	
	public List<Autovoz> getAutovozs() {
		return getInstance() == null ? null : new ArrayList<Autovoz>(
				getInstance().getAutovozs());
	}
	
	public List<Gravamenestablecimiento> getGravamenestablecimientos() {
		return getInstance() == null ? null
				: new ArrayList<Gravamenestablecimiento>(getInstance()
						.getGravamenestablecimientos());
	}
	
	public List<Franquiciaestablecimiento> getFranquiciaestablecimientos() {
		return getInstance() == null ? null
				: new ArrayList<Franquiciaestablecimiento>(getInstance()
						.getFranquiciaestablecimientos());
	}

	public List<Factura> getFacturas() {
		return getInstance() == null ? null : new ArrayList<Factura>(
				getInstance().getFacturas());
	}
	
	

}
