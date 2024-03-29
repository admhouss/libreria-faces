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
import java.util.GregorianCalendar;
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
    private Date date=null;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String clienteConfirmado=(String)request.getParameter("keycode");
      
         if(clienteConfirmado==null){
                 showMessage("No se encuentra ningun registro asociado a esta cuenta", out);
         }else{
            Cliente cliente=clienteFacade.find(clienteConfirmado.trim());
            if(cliente!=null && cliente.getEstatus()==false){
                    cliente.setEstatus(true);
                    cliente.setModificacion(new Date());
                    clienteFacade.edit(cliente);
                    response.sendRedirect(request.getContextPath()+"/faces/login/Create.xhtml");
             }
             else{  showMessage("La cuenta ya se encuentra activada",out);}
        }//end else
    }

    private void showMessage(String cadena,PrintWriter out){
        try {
           // TODO output your page here
            
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Recepci&oacute;n de correo electronico </title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<center><h1>" +cadena + "</h1></center>");
            out.println("</body>");
            out.println("</html>");

        } finally {
           out.close();
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
