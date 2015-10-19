package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("inventarioList")
public class InventarioList extends EntityQuery<Inventario> {

	private static final String EJBQL = "select inventario from Inventario inventario";

	private static final String[] RESTRICTIONS = {
			"lower(inventario.id.refproducto) like lower(concat(#{inventarioList.inventario.id.refproducto},'%'))",
			"lower(inventario.id.codigounico) like lower(concat(#{inventarioList.inventario.id.codigounico},'%'))", };

	private Inventario inventario;

	public InventarioList() {
		inventario = new Inventario();
		inventario.setId(new InventarioId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Inventario getInventario() {
		return inventario;
	}
}
