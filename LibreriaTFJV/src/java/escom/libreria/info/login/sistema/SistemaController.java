/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.login.sistema;



import escom.libreria.info.cliente.jpa.Cliente;
import escom.libreria.info.cliente.jsf.util.JsfUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;


@ManagedBean (name="sistemaController")
@SessionScoped
public class SistemaController implements Serializable {


    private Cliente cliente;
    private String usuario,password;
    private @EJB escom.libreria.info.cliente.ejb.ClienteFacade clienteFacade;
    public SistemaController() {
    }

    public void loginAcces(){
        try {
            if (usuario != null && password != null) {
                cliente = clienteFacade.buscarUsuario(usuario, password);
                if (cliente != null) {
                    // JsfUtil.addSuccessMessage("Bienvenido "+cliente.getNombre()+""+cliente.getPaterno()+" "+cliente.getMaterno());
                }
            } //usuario no valido
            JsfUtil.addErrorMessage("Usuario no identificado ");
            ExternalContext external = FacesContext.getCurrentInstance().getExternalContext();
            external.redirect(external.getRequestContextPath());
        } catch (IOException ex) {
            Logger.getLogger(SistemaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }




    }

