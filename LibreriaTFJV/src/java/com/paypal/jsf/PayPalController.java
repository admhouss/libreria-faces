/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.paypal.jsf;

import java.io.Serializable;

/**
 *
 * @author xxx
 */
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

@ManagedBean(name = "PayPalController")
@RequestScoped
public class PayPalController implements Serializable{

    public PayPalController() {
    }

    
    public String procesarPago(){
        doPagoPayPal();
        return "";
        
    }
    private void doPagoPayPal(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
               //response.s
        
    }

}

