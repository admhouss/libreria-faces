/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Imagenes;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class panelVisualizador extends JPanel {

 Principal p;
 ImageIcon imagen;

 public panelVisualizador(Principal prin){
  p=prin;
 }

 public void paintComponent(Graphics g){
  super.paintComponents(g);
  if(imagen!=null){
   g.drawImage(imagen.getImage(),0 , 0, p.getPv().getWidth(), p.getPv().getHeight(), this);
  }
 }

 public Principal getP() {
  return p;
 }

 public void setP(Principal p) {
  this.p = p;
 }

 public ImageIcon getImagen() {
  return imagen;
 }

 public void setImagen(ImageIcon imagen) {
  this.imagen = imagen;
 }
}
