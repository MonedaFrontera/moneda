package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("detallefacturaHome")
public class DetallefacturaHome extends EntityHome<Detallefactura> {

	@In(create = true)
	InventarioHome inventarioHome;
	@In(create = true)
	FacturaHome facturaHome;

	public void setDetallefacturaId(DetallefacturaId id) {
		setId(id);
	}

	public DetallefacturaId getDetallefacturaId() {
		return (DetallefacturaId) getId();
	}

	public DetallefacturaHome() {
		setDetallefacturaId(new DetallefacturaId());
	}

	@Override
	public boolean isIdDefined() {
		if (getDetallefacturaId().getNumfactura() == null
				|| "".equals(getDetallefacturaId().getNumfactura()))
			return false;
		if (getDetallefacturaId().getRefproducto() == null
				|| "".equals(getDetallefacturaId().getRefproducto()))
			return false;
		if (getDetallefacturaId().getCodigounico() == null
				|| "".equals(getDetallefacturaId().getCodigounico()))
			return false;
		return true;
	}

	@Override
	protected Detallefactura createInstance() {
		Detallefactura detallefactura = new Detallefactura();
		detallefactura.setId(new DetallefacturaId());
		return detallefactura;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Inventario inventario = inventarioHome.getDefinedInstance();
		if (inventario != null) {
			getInstance().setInventario(inventario);
		}
		Factura factura = facturaHome.getDefinedInstance();
		if (factura != null) {
			getInstance().setFactura(factura);
		}
	}

	public boolean isWired() {
		if (getInstance().getInventario() == null)
			return false;
		if (getInstance().getFactura() == null)
			return false;
		return true;
	}

	public Detallefactura getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
