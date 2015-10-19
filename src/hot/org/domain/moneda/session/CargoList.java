package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("cargoList")
public class CargoList extends EntityQuery<Cargo> {

	private static final String EJBQL = "select cargo from Cargo cargo";

	private static final String[] RESTRICTIONS = {
			"lower(cargo.codcargo) like lower(concat(#{cargoList.cargo.codcargo},'%'))",
			"lower(cargo.nombrecargo) like lower(concat(#{cargoList.cargo.nombrecargo},'%'))", };

	private Cargo cargo = new Cargo();

	public CargoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Cargo getCargo() {
		return cargo;
	}
}
