package org.domain.moneda.session;

import org.domain.moneda.entity.*;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("bindbList")
public class BindbList extends EntityQuery<Bindb> {

	private static final String EJBQL = "select bindb from Bindb bindb";
	//private static final String EJBQL = "";

	private static final String[] RESTRICTIONS = {
			"lower(bindb.bin) like lower(concat(#{bindbList.bindb.bin},'%'))",
			"lower(bindb.nombrebanco) like lower(concat(#{bindbList.bindb.nombrebanco},'%'))",
			"lower(bindb.pais) like lower(concat(#{bindbList.bindb.pais},'%'))",
			"lower(bindb.ciudad) like lower(concat(#{bindbList.bindb.ciudad},'%'))",
			"lower(bindb.producto) like lower(concat(#{bindbList.bindb.producto},'%'))", };

	private Bindb bindb = new Bindb();

	public BindbList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Bindb getBindb() {
		return bindb;
	}
}
