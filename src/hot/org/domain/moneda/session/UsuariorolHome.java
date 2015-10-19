package org.domain.moneda.session;

import org.domain.moneda.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("usuariorolHome")
public class UsuariorolHome extends EntityHome<Usuariorol> {

	public void setUsuariorolId(UsuariorolId id) {
		setId(id);
	}

	public UsuariorolId getUsuariorolId() {
		return (UsuariorolId) getId();
	}

	public UsuariorolHome() {
		setUsuariorolId(new UsuariorolId());
	}

	@Override
	public boolean isIdDefined() {
		if (getUsuariorolId().getUsuario() == null
				|| "".equals(getUsuariorolId().getUsuario()))
			return false;
		if (getUsuariorolId().getRol() == 0)
			return false;
		return true;
	}

	@Override
	protected Usuariorol createInstance() {
		Usuariorol usuariorol = new Usuariorol();
		usuariorol.setId(new UsuariorolId());
		return usuariorol;
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

	public Usuariorol getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
