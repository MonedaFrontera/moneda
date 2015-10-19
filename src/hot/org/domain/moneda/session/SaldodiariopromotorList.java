package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("saldodiariopromotorList")
public class SaldodiariopromotorList extends EntityQuery<Saldodiariopromotor> {

	private static final String EJBQL = "select saldodiariopromotor from Saldodiariopromotor saldodiariopromotor";

	private static final String[] RESTRICTIONS = {
			"lower(saldodiariopromotor.id.documento) like lower(concat(#{saldodiariopromotorList.saldodiariopromotor.id.documento},'%'))",
			"lower(saldodiariopromotor.usuariomod) like lower(concat(#{saldodiariopromotorList.saldodiariopromotor.usuariomod},'%'))", };

	private Saldodiariopromotor saldodiariopromotor;

	public SaldodiariopromotorList() {
		saldodiariopromotor = new Saldodiariopromotor();
		saldodiariopromotor.setId(new SaldodiariopromotorId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Saldodiariopromotor getSaldodiariopromotor() {
		return saldodiariopromotor;
	}
}
