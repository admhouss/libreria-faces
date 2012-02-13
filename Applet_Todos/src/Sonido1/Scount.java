/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Sonido1;

import java.awt.Graphics;
import java.applet.*;

public class Scount extends Applet{
    AudioClip sonido;
    @Override
    public void init(){
        sonido = getAudioClip(getCodeBase(), "welcom.wav");
    }
    @Override
    public void paint(Graphics g){
        g.drawString("Prueba de audio: ",25,25);
    }
    public void start(){
        sonido.loop();
    }
    public void stop(){
        sonido.stop();
    }
}
