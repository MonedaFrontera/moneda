package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("productoHome")
public class ProductoHome extends EntityHome<Producto> {

	public void setProductoRefproducto(String id) {
		setId(id);
	}

	public String getProductoRefproducto() {
		return (String) getId();
	}

	@Override
	protected Producto createInstance() {
		Producto producto = new Producto();
		return producto;
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

	public Producto getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Inventario> getInventarios() {
		return getInstance() == null ? null : new ArrayList<Inventario>(
				getInstance().getInventarios());
	}

}
