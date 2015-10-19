package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("tasadebolivaroficinaList")
public class TasadebolivaroficinaList extends EntityQuery<Tasadebolivaroficina> {

	private static final String EJBQL = "select tasadebolivaroficina from Tasadebolivaroficina tasadebolivaroficina";

	private static final String[] RESTRICTIONS = {
			"lower(tasadebolivaroficina.id.tipo) like lower(concat(#{tasadebolivaroficinaList.tasadebolivaroficina.id.tipo},'%'))",
			"lower(tasadebolivaroficina.usuariomod) like lower(concat(#{tasadebolivaroficinaList.tasadebolivaroficina.usuariomod},'%'))", };

	private Tasadebolivaroficina tasadebolivaroficina;

	public TasadebolivaroficinaList() {
		tasadebolivaroficina = new Tasadebolivaroficina();
		tasadebolivaroficina.setId(new TasadebolivaroficinaId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Tasadebolivaroficina getTasadebolivaroficina() {
		return tasadebolivaroficina;
	}
}
