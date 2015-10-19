package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("paisisoList")
public class PaisisoList extends EntityQuery<Paisiso> {

	private static final String EJBQL = "select paisiso from Paisiso paisiso";

	private static final String[] RESTRICTIONS = {
			"lower(paisiso.codigopais) like lower(concat(#{paisisoList.paisiso.codigopais},'%'))",
			"lower(paisiso.nombrepaisEs) like lower(concat(#{paisisoList.paisiso.nombrepaisEs},'%'))",
			"lower(paisiso.nombrepaisEn) like lower(concat(#{paisisoList.paisiso.nombrepaisEn},'%'))",
			"lower(paisiso.capital) like lower(concat(#{paisisoList.paisiso.capital},'%'))",
			"lower(paisiso.nombrecompletoEn) like lower(concat(#{paisisoList.paisiso.nombrecompletoEn},'%'))",
			"lower(paisiso.codigopaisNumero) like lower(concat(#{paisisoList.paisiso.codigopaisNumero},'%'))",
			"lower(paisiso.codigo31663Es) like lower(concat(#{paisisoList.paisiso.codigo31663Es},'%'))",
			"lower(paisiso.codigomoneda) like lower(concat(#{paisisoList.paisiso.codigomoneda},'%'))",
			"lower(paisiso.nombreMoneda) like lower(concat(#{paisisoList.paisiso.nombreMoneda},'%'))",
			"lower(paisiso.pathBanderaRelativo) like lower(concat(#{paisisoList.paisiso.pathBanderaRelativo},'%'))",
			"lower(paisiso.pathBanderaAbsoluta) like lower(concat(#{paisisoList.paisiso.pathBanderaAbsoluta},'%'))",
			"lower(paisiso.pathBanderasExtracto) like lower(concat(#{paisisoList.paisiso.pathBanderasExtracto},'%'))", };

	private Paisiso paisiso = new Paisiso();

	public PaisisoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Paisiso getPaisiso() {
		return paisiso;
	}
}
