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
        String go="";
        
            if (usuario != null && password != null) {
                cliente = clienteFacade.buscarUsuario(usuario, password);
                if(cliente==null){
                    JsfUtil.addErrorMessage("Usuario no identificado ");
                    go="/login/Create";
                }
                else{
                    try {
                        limpiarLogin();
                        JsfUtil.addSuccessMessage("Usuario identificado");
                        ExternalContext external = FacesContext.getCurrentInstance().getExternalContext();
                        external.redirect(external.getRequestContextPath() + "/faces/index.xhtml");
                    } catch (IOException ex) {
                         Logger.getLogger(SistemaController.class.getName()).log(Level.SEVERE, null, ex);
                    }
               }
                
            } //usuario no valido

        return go;

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

    public String olvideContrasenia(){
           List<String> cliList=null;
           Cliente cliente=clienteFacade.buscarUsuario(getUsuario());
           if(cliente!=null){
               cliList=new ArrayList<String>();
               cliList.add(cliente.getId());
           }

           if(cliente==null){
               JsfUtil.addErrorMessage("No se encontro ningun registro para este correo");
               return "/login/OlvideContrasenia" ;
           }
           else if(cliente.getEstatus()==false){


        String query=null;
        StringBuffer buffer=null;
        try{

            buffer=new StringBuffer();
            ExternalContext external = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) external.getRequest();
            buffer.append("http://");
            
            buffer.append("localhost:8080");
            //buffer.append("www.libreria-tfjfa.com");
            buffer.append(request.getContextPath());
            buffer.append("/ProcesarOlvidarContrasenia");
            request.getSession().setAttribute("correo",cliente.getId());
            request.getSession().setMaxInactiveInterval(3);
            query="<form><a href=\""+buffer+"\">Confirmaci&oacute;n de tu cuenta:"+buffer+"?keycode="+request.getSession().getId()+"</a></form>";
            System.out.println(query);
            jMail.enviarCorreo("Confirmacion Registro", query, cliList);
        }catch(Exception e){e.printStackTrace();}

           }
           else{
                
                String query="Estimado "+cliente.getNombre()+" "+cliente.getPaterno()+" "+cliente.getMaterno()+" <br/> Usuario:"+cliente.getId() +"<br/> Passwrod:"+cliente.getPassword()+"<br/>";
                jMail.enviarCorreo("Libreria", query, cliList);
           }
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

