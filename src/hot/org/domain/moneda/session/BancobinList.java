package org.domain.moneda.session;

import org.domain.moneda.entity.*;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("bancobinList")
public class BancobinList extends EntityQuery<Bancobin> {

	private static final String EJBQL = "select bancobin from Bancobin bancobin";

	private static final String[] RESTRICTIONS = {
			"lower(bancobin.bin) like lower(concat(#{bancobinList.bancobin.bin},'%'))",
			"lower(bancobin.codbanco) like lower(concat(#{bancobinList.bancobin.codbanco},'%'))", };

	private Bancobin bancobin = new Bancobin();

	public BancobinList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Bancobin getBancobin() {
		return bancobin;
	}
}
