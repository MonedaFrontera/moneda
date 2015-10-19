package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("vistasaldoviernesList")
public class VistasaldoviernesList extends EntityQuery<Vistasaldoviernes> {

	private static final String EJBQL = "select vistasaldoviernes from Vistasaldoviernes vistasaldoviernes";

	private static final String[] RESTRICTIONS = {
			"lower(vistasaldoviernes.id.docupromo) like lower(concat(#{vistasaldoviernesList.vistasaldoviernes.id.docupromo},'%'))",
			"lower(vistasaldoviernes.id.nombrepromo) like lower(concat(#{vistasaldoviernesList.vistasaldoviernes.id.nombrepromo},'%'))",
			"lower(vistasaldoviernes.id.asesora) like lower(concat(#{vistasaldoviernesList.vistasaldoviernes.id.asesora},'%'))", };

	private Vistasaldoviernes vistasaldoviernes;

	public VistasaldoviernesList() {
		vistasaldoviernes = new Vistasaldoviernes();
		vistasaldoviernes.setId(new VistasaldoviernesId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Vistasaldoviernes getVistasaldoviernes() {
		return vistasaldoviernes;
	}
}
