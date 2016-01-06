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
	@In(create = true)
	BancocolHome bancocolHome;

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
		Bancocol bancocol = bancocolHome.getDefinedInstance();
		if (bancocol != null) {
			getInstance().setBancocol(bancocol);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Establecimiento getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Tasadolarpromotorparametro> getTasadolarpromotorparametros() {
		return getInstance() == null ? null
				: new ArrayList<Tasadolarpromotorparametro>(getInstance()
						.getTasadolarpromotorparametros());
	}

	public List<Porcentcomisiontxparampromo> getPorcentcomisiontxparampromos() {
		return getInstance() == null ? null
				: new ArrayList<Porcentcomisiontxparampromo>(getInstance()
						.getPorcentcomisiontxparampromos());
	}

	public List<Porcentajecomisiontxparam> getPorcentajecomisiontxparams() {
		return getInstance() == null ? null
				: new ArrayList<Porcentajecomisiontxparam>(getInstance()
						.getPorcentajecomisiontxparams());
	}

	public List<Tasaeuropromotorparametro> getTasaeuropromotorparametros() {
		return getInstance() == null ? null
				: new ArrayList<Tasaeuropromotorparametro>(getInstance()
						.getTasaeuropromotorparametros());
	}

	public List<Tasadolarparametro> getTasadolarparametros() {
		return getInstance() == null ? null
				: new ArrayList<Tasadolarparametro>(getInstance()
						.getTasadolarparametros());
	}

	public List<Inventario> getInventarios() {
		return getInstance() == null ? null : new ArrayList<Inventario>(
				getInstance().getInventarios());
	}

	public List<Tasaeuroparametro> getTasaeuroparametros() {
		return getInstance() == null ? null : new ArrayList<Tasaeuroparametro>(
				getInstance().getTasaeuroparametros());
	}

	public List<Establecimientoprecio> getEstablecimientoprecios() {
		return getInstance() == null ? null
				: new ArrayList<Establecimientoprecio>(getInstance()
						.getEstablecimientoprecios());
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
