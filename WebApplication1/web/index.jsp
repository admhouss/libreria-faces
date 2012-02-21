<%-- 
    Document   : index
    Created on : 19/02/2012, 03:39:14 PM
    Author     : Joshelyne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form action="response.jsp">
        <table border="1" rules=none>
            <thead>
                <tr>
                    <th></th>
                    <th></th>
                    <th></th>
                    <th></th>
                    <th></th>
                    <th></th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td></td>
                    <td></td>
                  
                    <td rowspan="1" colspan="3"> <h3><center>----- ENCUESTA ----- </center</h3></td>
                   
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                    <td rowspan="1" colspan="3">Introduzca su nombre: <input type="text" name="nombre" value="" /></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                    <td>Indique sexo:</td>
                    <td></td>
                    <td>Seleccione el vehículo</td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td>de su preferencia:</td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                    <td><input type="radio" name="sexo" value="mujer" /> Mujer </td>
                    <td></td>
                    <td>Moto <select name="moto">
                            <option>Yamaha</option>
                            <option>Harley Davedson</option>
                            <option>Honda</option>
                            <option>Italica</option>
                        </select> </td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                    <td><input type="radio" name="sexo" value="hombre" /> Hombre</td>
                    <td></td>
                    <td>Automovil <select name="automovil">
                            <option>Jetta</option>
                            <option>Bora </option>
                            <option>Chevy</option>
                            <option>Platina</option>
                        </select> </td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                    <td><center><input type="submit" value="Aceptar" name="enviar" /></center> </td>
                    <td></td>
                    <td><center><input type="reset" value="Borrar" name="limpiar" /></center></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
            </tbody>
        </table>

         
           
        </form>
    </bod
</html>
