/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.login.sistema;



import escom.libreria.info.cliente.jpa.Cliente;
import escom.libreria.info.cliente.jsf.util.JsfUtil;
import escom.libreria.info.usarioAdministrativo.jpa.Usuarioadministrativo;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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
import javax.servlet.http.HttpServletRequest;


@ManagedBean (name="sistemaController")
@SessionScoped
public class SistemaController implements Serializable {

    private Usuarioadministrativo  usuarioAdministrador;
    private Cliente cliente;
    private String usuario,password;//cliente
    private String usuarioAdmin,passwordAdmin;//administrador;
    private @EJB escom.libreria.info.cliente.ejb.ClienteFacade clienteFacade;
    private @EJB escom.libreria.info.usarioAdministrativo.ejb.UsuarioadministrativoFacade adminFacade;
    private @EJB escom.libreria.correo.ProcesoJMail jMail;

    public SistemaController() {}

    public Usuarioadministrativo getUsuarioAdministrador() {
        return usuarioAdministrador;
    }

    public void setUsuarioAdministrador(Usuarioadministrativo usuarioAdministrador) {
        this.usuarioAdministrador = usuarioAdministrador;
    }
    public String  loginAcces(){      
                cliente = clienteFacade.buscarUsuario(usuario, password);//proceso de logeo
                if(cliente==null){// el usuario no existe aparentemente
                 cliente=clienteFacade.find(usuario);
                       if(cliente==null)//el usuario no existe delplano
                            JsfUtil.addErrorMessage("Usuario no identificado ");
                        else{
                            JsfUtil.addErrorMessage("Contraseña no valida");
                            return "/login/Create";
                         }
                }
                else{
                        try {
                       
                            JsfUtil.addSuccessMessage("Usuario a iniciado session satisfactoriamente");
                            ExternalContext external = FacesContext.getCurrentInstance().getExternalContext();
                            external.redirect(external.getRequestContextPath() + "/faces/index.xhtml");
                        } catch (IOException ex) {
                             Logger.getLogger(SistemaController.class.getName()).log(Level.SEVERE, null, ex);
                        }
               }
                limpiarLogin();
                return "/cliente/Create";

    }

private  void limpiarLogin(){
            setUsuarioAdmin("");
            setPasswordAdmin("");
            setUsuario("");
            setPassword("");
}
    public String accesoAdministrador(){
        String go="";
            usuarioAdministrador=adminFacade.buscarUsuarioAdmin(usuarioAdmin,passwordAdmin);
            if(usuarioAdministrador==null)
                JsfUtil.addErrorMessage("Personal no identificado ");
            else{
                       
                        setCliente(null);
                        limpiarLogin();
                        JsfUtil.addSuccessMessage("Usuario identificado");
            }
                 ExternalContext external = FacesContext.getCurrentInstance().getExternalContext();
            try {
                    external.redirect(external.getRequestContextPath() + "/faces/index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(SistemaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return go;
    }

    public String getPasswordAdmin() {
        return passwordAdmin;
    }

    public void setPasswordAdmin(String passwordAdmin) {
        this.passwordAdmin = passwordAdmin;
    }

    public boolean isOperacionPerfilAdminProducto(){//operacion
            boolean permitido=false;
            String perfil="";

            if(usuarioAdministrador==null)
              permitido=false;
            else{
                perfil=usuarioAdministrador.getCargo().toUpperCase();
                if(perfil.indexOf(LibreriaConst.SUPERUSARIO)!=-1 || perfil.indexOf(LibreriaConst.ADMINISTRADOR_PRODUCTO)!=-1)
                permitido=true;
            }
       
            return permitido;

    }

    public boolean isOperacionPerfilAdminCliente(){//perfil Admin Clientes
            boolean permitido=false;
            String perfil="";
            if(usuarioAdministrador==null)
                permitido=false;
            else{
                perfil=usuarioAdministrador.getCargo().toUpperCase();
                if(perfil.indexOf(LibreriaConst.SUPERUSARIO)!=-1 || 
                   perfil.indexOf(LibreriaConst.ADMINISTRADOR)!=-1 ||
                   perfil.indexOf(LibreriaConst.ADMINISTRADOR_GENERAL)!=-1)
                  permitido=true;
            }

            return permitido;

    }

    public void cerrrarSession(){
         usuarioAdministrador=null;
         cliente=null;
         JsfUtil.addSuccessMessage("Se cerro la session Satisfactoriamente");

    }

    private List<String> cliList=null;
    private String query;
    public String olvideContrasenia(){
           
           Cliente clienteX=clienteFacade.buscarUsuario(getUsuario());
           if(clienteX==null){
               JsfUtil.addErrorMessage("No se encontro ningun registro para este correo");
           }
           else if(clienteX.getEstatus()==false){
               jMail.EnviarConfimarCorreo(clienteX);
               JsfUtil.addSuccessMessage("Su cuenta se enucnetra desactivada,necesita ir a su bandeja!");
           } else {
               cliList=new ArrayList<String>();
               cliList.add(clienteX.getId());
               query="Apreciable "+clienteX.getNombre()+" "+clienteX.getPaterno()+" "+clienteX.getMaterno()+" <br/> Usuario:"+clienteX.getId() +"<br/> Passwrod:"+clienteX.getPassword()+"<br/>";
               jMail.enviarCorreo("Libreria", query, cliList);
           }
           setUsuario("");
           return "/login/OlvideContrasenia" ;
    }

    public String getUsuarioAdmin() {
        return usuarioAdmin;
    }

    public void setUsuarioAdmin(String usuarioAdmin) {
        this.usuarioAdmin = usuarioAdmin;
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


public String cerrarCession(){
    cliente=null;
    usuarioAdministrador =null;
    JsfUtil.addSuccessMessage("Session Cerrada Satisfactoriamente");
         ExternalContext external = FacesContext.getCurrentInstance().getExternalContext();
            try {
                external.redirect(external.getRequestContextPath());
            } catch (IOException ex) {
                Logger.getLogger(SistemaController.class.getName()).log(Level.SEVERE, null, ex);
            }
         
   return "";
   }

    }

