/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.escom.phaselistener;

import java.io.Serializable;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpServletResponse;

import javax.faces.event.PhaseEvent;

/**
 *
 * @author xxx
 */
public class PhaseListener {

    public PhaseListener() {
    }

  
   public void beforePhase(PhaseEvent event) {
        ExternalContext ectx = event.getFacesContext().getExternalContext();
        HttpServletResponse response = (HttpServletResponse) ectx.getResponse();
        response.addHeader("X-UA-Compatible", "IE=edge");
    }

}
