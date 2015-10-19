package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("empresaList")
public class EmpresaList extends EntityQuery<Empresa> {

	private static final String EJBQL = "select empresa from Empresa empresa";

	private static final String[] RESTRICTIONS = {
			"lower(empresa.nit) like lower(concat(#{empresaList.empresa.nit},'%'))",
			"lower(empresa.nombrerazonsocial) like lower(concat(#{empresaList.empresa.nombrerazonsocial},'%'))", };

	private Empresa empresa = new Empresa();

	public EmpresaList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Empresa getEmpresa() {
		return empresa;
	}
}
