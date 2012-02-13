/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Imagenes;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

public class Principal extends JApplet {

 panelVisorMiniaturas pm;
 panelVisualizador pv;

 public void init(){
  pm=new panelVisorMiniaturas(this);
  pv=new panelVisualizador(this);
  add(pv);
  ImageIcon imgbotons=new ImageIcon(this.getClass().getResource("C://Documents and Settings/Diana/Mis documentos/NetBeansProjects/Applet_Todos/build/classes/Imagenes/sig.jpg"));
  JButton btnsiguiente=new JButton(imgbotons);C://../Imagenes/siguiente.png
  btnsiguiente.addActionListener(new ActionListener() {

   @Override
   public void actionPerformed(ActionEvent e) {
    pm.siguienteImagen();
    pm.quitarBorder();
   }
  });
  ImageIcon imgbotona=new ImageIcon(this.getClass().getResource("C://Documents and Settings/Diana/Mis documentos/NetBeansProjects/Applet_Todos/build/classes/Imagenes/ant.jpg"));
  JButton btnanterior=new JButton(imgbotona);
  btnanterior.addActionListener(new ActionListener() {

   @Override
   public void actionPerformed(ActionEvent e) {
    pm.anteriorImagen();
    pm.quitarBorder();
   }
  });
  JPanel pbotones=new JPanel();
  pbotones.add(btnanterior);
  pbotones.add(pm);
  pbotones.add(btnsiguiente);
  add(pbotones,BorderLayout.SOUTH);

 }

 public panelVisorMiniaturas getPm() {
  return pm;
 }

 public void setPm(panelVisorMiniaturas pm) {
  this.pm = pm;
 }

 public panelVisualizador getPv() {
  return pv;
 }

 public void setPv(panelVisualizador pv) {
  this.pv = pv;
 }
}
