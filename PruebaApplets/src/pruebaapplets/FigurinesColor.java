/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pruebaapplets;



/**
 *
 * @author Diana
 */
import java.applet.Applet;
import java.awt.*;

public class FigurinesColor extends Applet {
  public void paint(Graphics g) {
    g.drawString("Adios, mundo cruel",20,20);
    g.setColor(Color.green);
    g.fillRoundRect(10,30,50,20,10,10);
    g.setColor(new Color(30,40,50,100));
    g.fill3DRect(50,40,50,20,true);
  }
}