/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.procesar.olvidarcontrasenia;

import escom.libreria.info.articulo.jsf.util.JsfUtil;
import escom.libreria.info.cliente.jpa.Cliente;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jboss.weld.context.ContextLifecycle;
import org.jboss.weld.context.api.BeanStore;
import org.jboss.weld.conversation.ServletConversationManager;

/**
 *
 * @author xxx
 */
public class ProcesarOlvidarContrasenia extends HttpServlet {

    private final ContextLifecycle lifecycle=null;
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

    @EJB private escom.libreria.info.cliente.ejb.ClienteFacade clienteFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();



       // ServletConversationManager conversationManager = (ServletConversationManager)request.getSession().getServletContext();
        //BeanStore beanStore = conversationManager.getBeanStore("correo");
        //String clienteConfirmado= (String)beanStore.get("correo").getInstance();
        String clienteConfirmado=(String)request.getSession().getAttribute("correo");

        
        
       

         if(clienteConfirmado==null){
            //JsfUtil.addErrorMessage("Intente acceder con otro Explorador!");
         }else{
            Cliente cliente=clienteFacade.buscarUsuario(clienteConfirmado);
            if(cliente!=null){

              if(cliente.getEstatus()==false){
                    cliente.setEstatus(true);
                    cliente.setModificacion(new Date());
                    clienteFacade.edit(cliente);
                   // JsfUtil.addSuccessMessage("Confirmacion realizada satisfactoiramente!!");
                    response.sendRedirect(request.getContextPath()+"/faces/login/Create.xhtml");
             }else{
                    //JsfUtil.addErrorMessage("session caducada!");
             }
       

         


        try {
           // TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProcesarOlvidarContrasenia</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Procese un cliente  " +cliente.getNombre() + "</h1>");
            out.println("</body>");
            out.println("</html>");
            
        } finally { 
            out.close();
        }
             }
        }

    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
