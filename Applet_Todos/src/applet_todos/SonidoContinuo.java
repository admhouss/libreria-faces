/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.applet.*;
import java.awt.*;


public class SonidoContinuo extends Applet{

    /**
     * @param args the command line arguments
     */
    AudioClip sonido;



    public void init() {
        // TODO code application logic here
    sonido=getAudioClip(getCodeBase(),"MiSonido.wav");
    }
    public void paint(Graphics g){
     g.drawString("Prueba Audio", 25, 25);
    }
    public void start(){
        sonido.loop();
    }
    public void stop(){
        sonido.stop();
    }

}