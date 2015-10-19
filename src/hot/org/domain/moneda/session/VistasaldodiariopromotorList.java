package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("vistasaldodiariopromotorList")
public class VistasaldodiariopromotorList extends
		EntityQuery<Vistasaldodiariopromotor> {

	private static final String EJBQL = "select vistasaldodiariopromotor from Vistasaldodiariopromotor vistasaldodiariopromotor";

	private static final String[] RESTRICTIONS = {
			"lower(vistasaldodiariopromotor.id.documento) like lower(concat(#{vistasaldodiariopromotorList.vistasaldodiariopromotor.id.documento},'%'))",
			"lower(vistasaldodiariopromotor.id.promotor) like lower(concat(#{vistasaldodiariopromotorList.vistasaldodiariopromotor.id.promotor},'%'))", };

	private Vistasaldodiariopromotor vistasaldodiariopromotor;

	public VistasaldodiariopromotorList() {
		vistasaldodiariopromotor = new Vistasaldodiariopromotor();
		vistasaldodiariopromotor.setId(new VistasaldodiariopromotorId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Vistasaldodiariopromotor getVistasaldodiariopromotor() {
		return vistasaldodiariopromotor;
	}
}
