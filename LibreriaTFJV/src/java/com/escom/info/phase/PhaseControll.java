/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.escom.info.phase;


import escom.libreria.info.cliente.jsf.util.JsfUtil;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 *
 * @author xxx
 */
public class PhaseControll implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent event) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        ExternalContext externalContext;
        if (event.getPhaseId() == PhaseId.RESTORE_VIEW) {
            if (event.getFacesContext().getExternalContext().getSession(false) == null) {
                try {
                    //event.getFacesContext().getApplication().getNavigationHandler().
                    //handleNavigation(event.getFacesContext(),"", "sessionExpired");
                    JsfUtil.addErrorMessage("Su session ha caducado");
                    externalContext = event.getFacesContext().getExternalContext();
                    String contexto = externalContext.getRequestContextPath();
                    externalContext.redirect(contexto + "/faces/login/Creante.xhtml");
                } //if
                catch (IOException ex) {
                    Logger.getLogger(PhaseControll.class.getName()).log(Level.SEVERE, null, ex);
                }

           }//if
    }//end

    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
       //throw new UnsupportedOperationException("Not supported yet.");
        
    }

}
