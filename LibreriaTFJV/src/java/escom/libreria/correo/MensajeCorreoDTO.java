/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.correo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Abraham de la Cruz
 */
public class MensajeCorreoDTO {
    private List<String> destinatarioList;
    private String asunto;
    private String emisorCorreo;
    private String cuerpo;
    private String tipoMensaje;
    private List<String> adjuntoList;

    public List<String> getAdjuntoList() {
        if(adjuntoList==null)
           adjuntoList=new ArrayList<String>();
        return adjuntoList;
    }

    public void setAdjuntoList(List<String> adjuntoList) {
        this.adjuntoList = adjuntoList;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public List<String> getDestinatarioList() {
        if(destinatarioList==null)
            destinatarioList=new ArrayList<String>();
        return destinatarioList;
    }

    public void setDestinatarioList(List<String> destinatarioList) {
        this.destinatarioList = destinatarioList;
    }

    public String getEmisorCorreo() {
        return emisorCorreo;
    }

    public void setEmisorCorreo(String emisorCorreo) {
        this.emisorCorreo = emisorCorreo;
    }

    public String getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(String tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }

}
