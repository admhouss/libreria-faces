/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Imagenes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class panelVisorMiniaturas extends JPanel {

 panelMiniatura paneles[]=new panelMiniatura[4];
 ImageIcon imagenes[]=new ImageIcon[13];
 int indices[]=new int[4];
 Principal p;

 public panelVisorMiniaturas(Principal prin){
  cargarImagenes();
  p=prin;
  setLayout(new GridLayout(1, 4,10,10));
  for(int i=0;i<4;i++){
   paneles[i]=new panelMiniatura(imagenes[i],p);
   add(paneles[i]);
  }
 }

 public void siguienteImagen(){
  if(indices[3]<12){
  for(int i=0;i<4;i++){
   indices[i]=indices[i]+1;
   paneles[i].setImagen(imagenes[indices[i]]);
  }
  repaint();
  p.repaint();
  }
 }

 public void anteriorImagen(){
  if(indices[0]>0){
  for(int i=0;i<4;i++){
   indices[i]=indices[i]-1;
   paneles[i].setImagen(imagenes[indices[i]]);
  }
  repaint();
  p.repaint();
  }
 }

 public void cargarImagenes(){
  for(int i=0;i<4;i++){
   indices[i]=i;
  }
  for(int i=0;i<13;i++){
   imagenes[i]=new ImageIcon(this.getClass().getResource("C://Documents and Settings/Diana/Mis documentos/NetBeansProjects/Applet_Todos/build/classes/Imagenes/imagenes"+(i+1)+".jpg"));
  }
 }

 public void quitarBorder(){
  for(int i=0;i<4;i++){
   paneles[i].setBorder(null);
  }
 }

 public panelMiniatura[] getPaneles() {
  return paneles;
 }

 public void setPaneles(panelMiniatura[] paneles) {
  this.paneles = paneles;
 }

 public ImageIcon[] getImagenes() {
  return imagenes;
 }

 public void setImagenes(ImageIcon[] imagenes) {
  this.imagenes = imagenes;
 }

 public int[] getIndices() {
  return indices;
 }

 public void setIndices(int[] indices) {
  this.indices = indices;
 }
}