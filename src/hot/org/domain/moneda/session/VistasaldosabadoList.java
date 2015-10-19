package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("vistasaldosabadoList")
public class VistasaldosabadoList extends EntityQuery<Vistasaldosabado> {

	private static final String EJBQL = "select vistasaldosabado from Vistasaldosabado vistasaldosabado";

	private static final String[] RESTRICTIONS = {
			"lower(vistasaldosabado.id.docupromo) like lower(concat(#{vistasaldosabadoList.vistasaldosabado.id.docupromo},'%'))",
			"lower(vistasaldosabado.id.nombrepromo) like lower(concat(#{vistasaldosabadoList.vistasaldosabado.id.nombrepromo},'%'))",
			"lower(vistasaldosabado.id.asesora) like lower(concat(#{vistasaldosabadoList.vistasaldosabado.id.asesora},'%'))", };

	private Vistasaldosabado vistasaldosabado;

	public VistasaldosabadoList() {
		vistasaldosabado = new Vistasaldosabado();
		vistasaldosabado.setId(new VistasaldosabadoId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Vistasaldosabado getVistasaldosabado() {
		return vistasaldosabado;
	}
}
