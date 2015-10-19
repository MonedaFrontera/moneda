package org.domain.moneda.bussiness;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.domain.moneda.entity.Producto;
import org.domain.moneda.session.EstablecimientoHome;
import org.domain.moneda.session.TransaccionHome;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

@Name("AdministrarFactura") 
@Scope(ScopeType.CONVERSATION)
public class AdministrarFactura {
	
	 @Logger private Log log;

	    @In StatusMessages statusMessages;
	    
	    @In
		private EntityManager entityManager;
	    
	    @In
	    private FacesMessages facesMessages;
	    
	    @In(create=true) @Out 
		private TransaccionHome	transaccionHome;
	    
	    @In(create=true) @Out 
		private EstablecimientoHome	establecimientoHome;
	    
	    
	    public void generarDetallefactura(Integer ntransaccion){
	    	Integer total = transaccionHome.getInstance().getValortxpesos().intValue();
	    	Integer acumulado = 0;
	    	String codestablecimiento = establecimientoHome.getInstance().getCodigounico();
	    	Integer i = 0;
	    	String numfactura = transaccionHome.getInstance().getNumfactura();
	    	Integer numtransaccion = ntransaccion;
	    	
	    	entityManager.clear();
	    	entityManager.createNativeQuery("insert into public.factura (numfactura, codigounico, consecutivo) " +
	    			"values ('"+numfactura+"','"+codestablecimiento+"',"+numtransaccion+")").executeUpdate();
	    	entityManager.flush();
	    	entityManager.clear();
	    	
	    	while(total > acumulado && i <= 5){
	    	
	    	List<Producto> listproducto = null;
	    	
	    	if(i>0){
	    	
		    	listproducto = entityManager
		    	.createQuery("select p from Producto p, Inventario i " +
		    			"where i.id.codigounico = '"+codestablecimiento+"' " +
		    			"and p.refproducto = i.id.refproducto and " +
		    			"p.valormaximo < "+(total-acumulado)+"/2 and p.valorminimo > "+(total-acumulado)+"/10").getResultList();
		    	}else{
	    		listproducto = entityManager
		    	.createQuery("select p from Producto p, Inventario i " +
		    			"where i.id.codigounico = '"+codestablecimiento+"' " +
		    			"and i.prioridad = true " +
		    			"and p.refproducto = i.id.refproducto and " +
		    			"p.valormaximo < "+(total-acumulado)+"/2 and p.valorminimo > "+(total-acumulado)+"/10").getResultList();
	    		if(listproducto.size()<=0){
	    			listproducto = entityManager
			    	.createQuery("select p from Producto p, Inventario i " +
			    			"where i.id.codigounico = '"+codestablecimiento+"' " +
			    			"and p.refproducto = i.id.refproducto and " +
			    			"p.valormaximo < "+(total-acumulado)+"/2 and p.valorminimo > "+(total-acumulado)+"/10").getResultList();
	    		}
	    	}
	    	
	    	int x = (int) (Math.random()*(listproducto.size()));
	    	Producto p = null;
	    	
	    	Integer d = 0;
	    	Integer n = 0;
	    	
	    	if(listproducto.size()>0){
	    		System.out.println("Valor de x " + x);
	    		System.out.println("Valor de tamaño " + listproducto.size());
	    	
	    		p = listproducto.get(x);
	    	
	    		d = (p.getValormaximo() - p.getValorminimo())/2 + p.getValorminimo();
	    		
	    		
	    	
	    		n = (Integer) ((total-acumulado) / d) - 1;
	    	
	    		if(n>p.getCantmaximo())
	    			n=p.getCantmaximo().intValue();
	    	
	    		Integer c = d*n;
	    		
	    		if (c>total*p.getPorcentaje()/100){
	    			n = (Integer)((total*p.getPorcentaje()/100)/d);
	    			c=d*n;
	    		}
	    		
	    	
	    		acumulado = acumulado + c;
	    		
	    		
	    	
	    		System.out.println(p.getNombreproducto() + " Precio del Producto Ini: " + d + " cantidad: " + n);
	    		
	    		if(total-acumulado<total*0.1){
	    			if(total-acumulado > d){
	    				n++;
	    				acumulado = acumulado + d;
	    				c = c + d;
	    			}	
	    			c = c + total-acumulado;
	    			acumulado = acumulado + total - acumulado;
	    			d = c / n;
	    			System.out.println(p.getNombreproducto() + " Precio Nuevo del Producto Ini: " + d + " cantidad Nueva: " + n);
	    		}
	    		
	    		
	    		
	    	}else{
	    		
	    		if(i>0){
	    		listproducto = entityManager
		    	.createQuery("select p from Producto p, Inventario i " +
		    			"where i.id.codigounico = '"+codestablecimiento+"' " +
		    			"and p.refproducto = i.id.refproducto and " +
		    			"p.valormaximo < "+(total-acumulado)+"").getResultList();
	    		}else{
	    			listproducto = entityManager
			    	.createQuery("select p from Producto p, Inventario i " +
			    			"where i.id.codigounico = '"+codestablecimiento+"' " +
			    			"and p.refproducto = i.id.refproducto and i.prioridad=true and " +
			    			"p.valormaximo < "+(total-acumulado)+"").getResultList();
	    			if(listproducto.size()<=0){
	    				listproducto = entityManager
	    		    	.createQuery("select p from Producto p, Inventario i " +
	    		    			"where i.id.codigounico = '"+codestablecimiento+"' " +
	    		    			"and p.refproducto = i.id.refproducto and " +
	    		    			"p.valormaximo < "+(total-acumulado)+"").getResultList();
	    			}
	    		}
	    		
	    		p = listproducto.get(x);
		    	
	    		
	    		
	    		d = (p.getValormaximo() - p.getValorminimo())/2 + p.getValorminimo();
	    		
	    		
	    	
	    		n = (Integer) ((total-acumulado) / d);
	    	
	    		if(n>p.getCantmaximo())
	    			n=p.getCantmaximo().intValue();
	    		
	    		
	    		
	    		Integer c = d*n;
	    		
	    		if (c>total*p.getPorcentaje()/100){
	    			n = (Integer)((total*p.getPorcentaje()/100)/d);
	    			c=d*n;
	    		}
	    	
	    		acumulado = acumulado + c;
	    		
	    		System.out.println(p.getNombreproducto() + " Precio del Producto: " + d + " cantidad: " + n);
	    		
	    		/*
	    		if(total-acumulado<p.getValorminimo()){
	    			c = c + total-acumulado;
	    			acumulado = acumulado + total - acumulado;
	    			d = c / n;
	    			System.out.println("Precio Nuevo del Producto: " + d + " cantidad Nueva: " + n);
	    		}
	    		*/
	    		if(total-acumulado<(p.getValormaximo()-d)*n || total-acumulado<total*0.1){
	    			if(n>1){
	    				if(n%10>5){
	    					n=(((Integer)n/10)+1)*10;
	    				}else{
	    					if(n>5){
	    						n=(((Integer)n/10))*10;
	    					}
	    				}
	    			}
	    			c = c + total-acumulado;
	    			acumulado = acumulado + total - acumulado;
	    			d = c / n;
	    			System.out.println(p.getNombreproducto() + " Precio Nuevo del Producto: " + d + " cantidad Nueva: " + n);
	    		}	    		
	    	}
	    	
	    	i++;
	    	
	    	if(p!=null){
	    	entityManager.clear();
	    	entityManager.createNativeQuery("insert into public.detallefactura " +
	    			"(numfactura, refproducto, codigounico, cantidad, valor) " +
	    			"values ('"+numfactura+"','"+p.getRefproducto()+"','"+codestablecimiento+"',"+n+","+d+")").executeUpdate();
	    	entityManager.flush();
	    	}
	    	p=null;
	    	
	    	System.out.println("Acumulado: " + acumulado);
	    	
	    	}
	    	
	    	if(total - acumulado > 0){
	    		entityManager.createNativeQuery("update public.detallefactura set valor = valor + " + acumulado + " " +
	    				"where numfactura = '"+numfactura+"' and refproducto = (select refproducto " +
	    				"from public.detallefactura where cantidad = 1) ").executeUpdate();
	    	}
	    	System.out.println("Acumulado Final: " + acumulado);
	    	
	    }
}//
