package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("productoList")
public class ProductoList extends EntityQuery<Producto> {

	private static final String EJBQL = "select producto from Producto producto";

	private static final String[] RESTRICTIONS = {
			"lower(producto.refproducto) like lower(concat(#{productoList.producto.refproducto},'%'))",
			"lower(producto.nombreproducto) like lower(concat(#{productoList.producto.nombreproducto},'%'))",
			"lower(producto.marca) like lower(concat(#{productoList.producto.marca},'%'))", };

	private Producto producto = new Producto();

	public ProductoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Producto getProducto() {
		return producto;
	}
}
