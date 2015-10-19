package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("inventarioHome")
public class InventarioHome extends EntityHome<Inventario> {

	@In(create = true)
	ProductoHome productoHome;
	@In(create = true)
	EstablecimientoHome establecimientoHome;

	public void setInventarioId(InventarioId id) {
		setId(id);
	}

	public InventarioId getInventarioId() {
		return (InventarioId) getId();
	}

	public InventarioHome() {
		setInventarioId(new InventarioId());
	}

	@Override
	public boolean isIdDefined() {
		if (getInventarioId().getRefproducto() == null
				|| "".equals(getInventarioId().getRefproducto()))
			return false;
		if (getInventarioId().getCodigounico() == null
				|| "".equals(getInventarioId().getCodigounico()))
			return false;
		return true;
	}

	@Override
	protected Inventario createInstance() {
		Inventario inventario = new Inventario();
		inventario.setId(new InventarioId());
		return inventario;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Producto producto = productoHome.getDefinedInstance();
		if (producto != null) {
			getInstance().setProducto(producto);
		}
		Establecimiento establecimiento = establecimientoHome
				.getDefinedInstance();
		if (establecimiento != null) {
			getInstance().setEstablecimiento(establecimiento);
		}
	}

	public boolean isWired() {
		if (getInstance().getProducto() == null)
			return false;
		if (getInstance().getEstablecimiento() == null)
			return false;
		return true;
	}

	public Inventario getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Detallefactura> getDetallefacturas() {
		return getInstance() == null ? null : new ArrayList<Detallefactura>(
				getInstance().getDetallefacturas());
	}

}
