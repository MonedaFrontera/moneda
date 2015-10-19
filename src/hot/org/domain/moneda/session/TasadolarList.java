package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("tasadolarList")
public class TasadolarList extends EntityQuery<Tasadolar> {

	private static final String EJBQL = "select tasadolar from Tasadolar tasadolar";

	private static final String[] RESTRICTIONS = { "lower(tasadolar.id.codigopais) like lower(concat(#{tasadolarList.codpais},'%'))",
		"tasadolar.id.fecha = #{tasadolarList.tasadolar.id.fecha}",};

	private Tasadolar tasadolar;
	
	private Pais pais = new Pais();
	
	private String codpais;

	public TasadolarList() {
		tasadolar = new Tasadolar();
		tasadolar.setId(new TasadolarId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(30);
	}

	public Tasadolar getTasadolar() {
		return tasadolar;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public String getCodpais() {
		if(pais!=null){
			codpais = pais.getCodigopais();
		}
		return codpais;
	}

	public void setCodpais(String codpais) {
		this.codpais = codpais;
	}
	
	
	
	
}
