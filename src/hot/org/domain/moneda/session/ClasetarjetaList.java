package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("clasetarjetaList")
public class ClasetarjetaList extends EntityQuery<Clasetarjeta> {

	private static final String EJBQL = "select clasetarjeta from Clasetarjeta clasetarjeta";

	private static final String[] RESTRICTIONS = {
			"lower(clasetarjeta.id.codbanco) like lower(concat(#{clasetarjetaList.clasetarjeta.id.codbanco},'%'))",
			"lower(clasetarjeta.id.codfranquicia) like lower(concat(#{clasetarjetaList.clasetarjeta.id.codfranquicia},'%'))",
			"lower(clasetarjeta.id.clase) like lower(concat(#{clasetarjetaList.clasetarjeta.id.clase},'%'))",
			"lower(clasetarjeta.nombretipo) like lower(concat(#{clasetarjetaList.clasetarjeta.nombretipo},'%'))", };

	private Clasetarjeta clasetarjeta;

	public ClasetarjetaList() {
		clasetarjeta = new Clasetarjeta();
		clasetarjeta.setId(new ClasetarjetaId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Clasetarjeta getClasetarjeta() {
		return clasetarjeta;
	}
}
