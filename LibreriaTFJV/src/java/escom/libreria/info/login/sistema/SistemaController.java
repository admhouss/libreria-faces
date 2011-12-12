/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.info.login.sistema;



import escom.libreria.comun.GeneradorHTML;
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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@ManagedBean (name="sistemaController")
@SessionScoped
public class SistemaController implements Serializable {

    private Usuarioadministrativo  usuarioAdministrador;
    private Cliente cliente;
    private String correo,password;//cliente
    private String usuarioAdmin,passwordAdmin;//administrador;
    private @EJB escom.libreria.info.cliente.ejb.ClienteFacade clienteFacade;
    private @EJB escom.libreria.info.usarioAdministrativo.ejb.UsuarioadministrativoFacade adminFacade; //para administrador
    private @EJB escom.libreria.correo.ProcesoJMail jMail; //enviar correos
    private String menssageBienvenida;
    private String bandera;

    public String getBandera() {
        return bandera;
    }

    public void setBandera(String bandera) {
        this.bandera = bandera;
    }


    
    public String getMenssageBienvenida() {

        if(menssageBienvenida==null)
            return "";
        
        return  menssageBienvenida.toUpperCase();
    }


    @PostConstruct
    public void init() {

        try{
            if((getCorreo()!=null && !getCorreo().trim().equals("") && getBandera()!=null && getBandera().equals("1"))){
                cliente=clienteFacade.find(getCorreo().trim());
                if(cliente!=null && cliente.getEstatus()==false){
                    cliente.setEstatus(true);
                    clienteFacade.edit(cliente);
                    JsfUtil.addSuccessMessage("Cliente registrado satisfactoriamente");
                }else{JsfUtil.addErrorMessage("El Cliente ya se encuentra registrado");}
            }
        }catch(Exception e){
        
        }
    }

    

    public SistemaController() {}

   

    public String  loginAcces(){
                setUsuarioAdministrador(null);
                cliente = clienteFacade.buscarUsuario(correo.trim(), password);//proceso de logeo
                if(cliente==null){

                            JsfUtil.addErrorMessage("Usuario o Contraseña  invalida");
                            return "/login/Create";
                }
                else{
                   if(cliente.getEstatus()==true){
                        try {
                            limpiarLogin();
                            setMenssageBienvenida("BIENVENIDO "+cliente.getNombre()+" "+cliente.getPaterno()+" "+cliente.getMaterno());
                            JsfUtil.addSuccessMessage("Usuario a iniciado sesion satisfactoriamente");
                            ExternalContext external = FacesContext.getCurrentInstance().getExternalContext();
                            external.redirect(external.getRequestContextPath() + "/faces/index.xhtml");
                        } catch (IOException ex) {
                             Logger.getLogger(SistemaController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }else{
                          JsfUtil.addErrorMessage("Su cuenta se encuentra desactivada ,necesita activarla");
                          return "/login/Create";
                    }
               }
                
                return "/cliente/Create";

    }

private  void limpiarLogin(){
            setUsuarioAdmin("");
            setPasswordAdmin("");
            setCorreo("");
            setPassword("");
}
    public String accesoAdministrador(){
            String go="";
            setCliente(null);
            
            if(usuarioAdmin.indexOf("@")!=-1){
                correo=usuarioAdmin;
                password=passwordAdmin;
                return loginAcces();
            }else{

            usuarioAdministrador=adminFacade.buscarUsuarioAdmin(usuarioAdmin,passwordAdmin);
            if(usuarioAdministrador==null){
                JsfUtil.addErrorMessage("Acceso exclusivo para administradores");
                return "/login/Create";
            }
            else{
                       setMenssageBienvenida("BIENVENIDO "+usuarioAdministrador.getNombre()+" "+usuarioAdministrador.getPaterno()+" "+usuarioAdministrador.getMaterno());
                       setCliente(null);
                       limpiarLogin();
                       JsfUtil.addSuccessMessage("Usuario a inisiado sesion satisfactoriamente");   
            }
               
            try {
                    ExternalContext external = FacesContext.getCurrentInstance().getExternalContext();
                    external.redirect(external.getRequestContextPath() + "/faces/index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(SistemaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }//else para administradores
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
            if(isAdministrado()){
                perfil=usuarioAdministrador.getCargo();
                if(perfil.equalsIgnoreCase(LibreriaConst.SUPERUSARIO) ||
                   perfil.equalsIgnoreCase(LibreriaConst.ADMINISTRADOR_PRODUCTO) ||
                   perfil.equalsIgnoreCase(LibreriaConst.ADMINISTRADOR_GENERAL)
                  )
                permitido=true;
            }
            return permitido;
    }
    
     



    public boolean isOperacionPerfilAdminCliente(){//perfil Admin Clientes
            boolean permitido=false;
            String perfil="";
            if(isAdministrado()){
                perfil=usuarioAdministrador.getCargo();
                if(perfil.equalsIgnoreCase(LibreriaConst.SUPERUSARIO) ||
                   perfil.equalsIgnoreCase(LibreriaConst.ADMINISTRADOR_CLIENTES) ||
                   perfil.equalsIgnoreCase(LibreriaConst.ADMINISTRADOR_GENERAL))
                   permitido=true;
                 }

            return permitido;

    }
    
    public boolean isAdministrado(){
        return usuarioAdministrador==null?false:true;
    }

    public boolean isOperacionPerfilSuperAndGeneral(){
        boolean permitido=false;
            String perfil="";
            if(isAdministrado()){
                perfil=usuarioAdministrador.getCargo();
                if(perfil.equalsIgnoreCase(LibreriaConst.SUPERUSARIO) 
                  )
                 permitido=true;
            }

           return permitido;
    }

   

    private List<String> cliList=null;
    private String query;
    public String olvideContrasenia(){
           
           Cliente clienteX=clienteFacade.find(getCorreo().trim());
           if(clienteX==null){
               JsfUtil.addErrorMessage("No se encontro ningun registro para este correo");
           }
           else if(clienteX.getEstatus()==false){
               jMail.EnviarConfimarCorreo(clienteX);
               JsfUtil.addErrorMessage("Su cuenta se enucuentra desactivada,necesita ir a su bandeja de correo!");
           } else {
               cliList=new ArrayList<String>();
               cliList.add(clienteX.getId().trim());
               query=clienteX.getNombre()+" "+clienteX.getPaterno()+" "+clienteX.getMaterno();
               GeneradorHTML generar=new GeneradorHTML();
               query=generar.generdarHTMLOlvideContrasenia(clienteX.getId(),clienteX.getPassword(),query);
               jMail.enviarCorreo("Recuperar Contraseña", query, cliList);
           }
           setCorreo("");
           setPassword("");
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public void setMenssageBienvenida(String menssageBienvenida) {
        this.menssageBienvenida = menssageBienvenida;
    }


     public Usuarioadministrativo getUsuarioAdministrador() {
        return usuarioAdministrador;
    }

    public void setUsuarioAdministrador(Usuarioadministrativo usuarioAdministrador) {
        this.usuarioAdministrador = usuarioAdministrador;
    }


public String cerrarCession(){
        try {
            cliente = null;
            usuarioAdministrador = null;
            ExternalContext external = FacesContext.getCurrentInstance().getExternalContext();
            try {
                HttpSession session = (HttpSession) external.getSession(false);
                session.invalidate();
            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(SistemaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            JsfUtil.addSuccessMessage("Session Cerrada Satisfactoriamente");
            external.redirect(external.getRequestContextPath()+"/");
            return "";
        } catch (IOException ex) {
            Logger.getLogger(SistemaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
   }

    }

