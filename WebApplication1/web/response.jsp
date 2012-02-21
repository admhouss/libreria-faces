<%-- 
    Document   : response
    Created on : 19/02/2012, 04:27:41 PM
    Author     : Joshelyne
--%>
<%@page import="java.io.*" %>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.util.Properties"%>
<%@page import="java.util.*" %>
<%@page import="encuesta.Encuesta" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>

     <% String nombre=request.getParameter("nombre");

     String  sexo=request.getParameter("sexo");
     String moto=request.getParameter("moto");
     String coche=request.getParameter("automovil")  ;
 Encuesta e=new Encuesta (nombre,sexo,moto,coche);


     %>


   
   <%--  < String nombre=request.getParameter("nombre");
     String  sexo=request.getParameter("sexo");
     String moto=request.getParameter("moto");
     String coche=request.getParameter("automovil")  ;


//if(nombre==null||sexo==null||coche==null||moto==null)
  //  {

%>
<%--
<br>No ha seleccionado, debe seleccionar una en cada uno de ellos<br>
Pulse aqui<a href="javascript:history.back()">volver</a> para realizar seleccion.
<%}else {
         Properties p= new Properties();
         p.put("nombre",nombre);
         p.put("genero",sexo);
         p.put("coche",coche);
         p.put("moto", moto);
         FileOutputStream fos=
         new FileOutputStream(application.getRealPath("/datos")+"/"+nombre);
         p.store(fos,"Encuesta, realizada a"+nombre);
         fos.flush();
         fos.close();

         %>
     --%>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>

        <h1>Resultados Encuesta
            <%--  <%=e.toString()%> --%>

            <table border="1">
                <thead>
                    <tr>
                        <th></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Nombre</td>
                        <td><%=e.getName()%></td>
                    </tr>
                    <tr>
                        <td>Genero</td>
                        <td><%=e.getSexo()%></td>
                    </tr>
                    <tr>
                        <td>Moto</td>
                        <td> <%=e.getMoto()%></td>
                    </tr>
                    <tr>
                        <td>Automovil</td>
                        <td><%=e.getAutomovil()
        %></td>
                    </tr>
                </tbody>
            </table>

        




</h1>
        <%--     <table border="1">
            <thead>
                <tr>
                    <th></th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
              
                        <tr>
                    <td>Nombre</td>
                    <td><%=nombre%></td>
                </tr>
                <tr>
                    <td>Genero</td>
                    <td><%=sexo%> </td>
                </tr>
                <tr>
                    <td>Moto</td>
                    <td><%=moto%> </td>
                </tr>
                <tr>
                    <td>Automovil</td>
                    <td><%=coche%></td>
                </tr>

            </tbody>
        </table>

            --%>
    </body>
</html>
