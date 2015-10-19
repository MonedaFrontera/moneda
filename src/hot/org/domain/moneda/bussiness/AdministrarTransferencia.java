package org.domain.moneda.bussiness;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.domain.moneda.entity.Banco;
import org.domain.moneda.entity.Cuentapromotor;
import org.domain.moneda.entity.Envios;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

@Name("AdministrarTransferencia")
@Scope(CONVERSATION)
public class AdministrarTransferencia {
	@Logger private Log log; 

    @In StatusMessages statusMessages;
    
    @In private FacesMessages facesMessages;
    
    @In
	private EntityManager entityManager;
    
    @DataModel
	private List<Object> tarjetas = new ArrayList<Object>();
    
    private List<Cuentapromotor> cuentas = new ArrayList<Cuentapromotor>();
    
    BigDecimal totalgastos =  null;
    BigDecimal totaldepositos =  null;
    
    BigDecimal totaltransferencia =  null;
    BigDecimal totalsaldoinicial =  null;
	public BigDecimal getTotalgastos() {
		return totalgastos;
	}
	public void setTotalgastos(BigDecimal totalgastos) {
		this.totalgastos = totalgastos;
	}
	public BigDecimal getTotaldepositos() {
		return totaldepositos;
	}
	public void setTotaldepositos(BigDecimal totaldepositos) {
		this.totaldepositos = totaldepositos;
	}
	public BigDecimal getTotaltransferencia() {
		return totaltransferencia;
	}
	public void setTotaltransferencia(BigDecimal totaltransferencia) {
		this.totaltransferencia = totaltransferencia;
	}
	public BigDecimal getTotalsaldoinicial() {
		return totalsaldoinicial;
	}
	public void setTotalsaldoinicial(BigDecimal totalsaldoinicial) {
		this.totalsaldoinicial = totalsaldoinicial;
	}
	public List<Object> getTarjetas() {
		return tarjetas;
	}
	public void setTarjetas(List<Object> tarjetas) {
		this.tarjetas = tarjetas;
	}
    
    public void buscar(){
    	String sql = "select v.consecutivo, t.numerotarjeta, t.tarjetahabiente, " +
    			"v.cupointernet, v.cupoviajero, tr.fechatx from " +
    			"Viaje v, Tarjetaviaje tv, Tarjeta t left outer join " +
    			"(select tr.numerotarjeta, max(tr.fechatx) as fechatx " +
    			"from Transaccion tr group by tr.numerotarjeta) tr on" +
    			"tr.numerotarjeta = t.numerotarjeta " +
    			"where tv.id.numerotarjeta = t.numerotarjeta and " +
    			"tv.id.consecutivoviaje = v.consecutivo and " +
    			"tv.estado = 1 and now() between v.fechainicio and v.fechafin ";
    	
    	this.tarjetas = entityManager.createQuery(sql).getResultList();
    }
	public List<Cuentapromotor> getCuentas() {
		return cuentas;
	}
	public void setCuentas(List<Cuentapromotor> cuentas) {
		this.cuentas = cuentas;
	}
	
	Banco banco = new Banco();
	
	
	
	
	public Banco getBanco() {
		return banco;
	}
	public void setBanco(Banco banco) {
		this.banco = banco;
	}
	public List<Cuentapromotor> cuentasPromotores(String documento, String codbanco){
		String sql = "select c from Cuentapromotor c " +
				"where " +
				//
				//"c.banco.codbanco = '"+codbanco+"' and " +
						"c.id.documento = '"+documento+"'";
		return entityManager.createQuery(sql).getResultList();
	}
    
    
    
    


}
