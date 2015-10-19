package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("variableHome")
public class VariableHome extends EntityHome<Variable> {

	public void setVariableNumregistro(Integer id) {
		setId(id);
	}

	public Integer getVariableNumregistro() {
		return (Integer) getId();
	}

	@Override
	protected Variable createInstance() {
		Variable variable = new Variable();
		return variable;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public Variable getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
