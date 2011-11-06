/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.recepcion.jsf;

import escom.libreria.correo.conf.jsf.util.JsfUtil;
import escom.libreria.info.cliente.jpa.Cliente;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author xxx
 */
@ManagedBean (name="recepcionController")
@RequestScoped
public class RecepcionController {


    @EJB private escom.libreria.info.cliente.ejb.ClienteFacade clienteFacade;
    private String correo;
    private String password;
    private Cliente cliente;

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    @PostConstruct
    public void init() {
        
        System.out.println("hola correo:"+getCorreo());
        if(getCorreo()!=null){
        cliente=clienteFacade.find(getCorreo().trim());
        if(cliente.getEstatus()==false){
            cliente.setEstatus(true);
            clienteFacade.edit(cliente);
            JsfUtil.addSuccessMessage("Cliente registrado satisfactoriamente!!");
        }else{JsfUtil.addErrorMessage("El usuario ya se encuentra registrado!!");}
        }
        //user = userService.find(id);
    }

    public String confirmarAlta(){

        if(cliente.getId().equals(getCorreo().trim()) && cliente.getPassword().equals(getPassword())){
          JsfUtil.addSuccessMessage("Inicio session Satisfactoriamente!!");
          return "/index";
        }
        return "/login/Create";
    }




    
}
