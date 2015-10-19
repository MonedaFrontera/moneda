package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("variableList")
public class VariableList extends EntityQuery<Variable> {

	private static final String EJBQL = "select variable from Variable variable";

	private static final String[] RESTRICTIONS = {};

	private Variable variable = new Variable();

	public VariableList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Variable getVariable() {
		return variable;
	}
}
