/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Dibujar_Figuras;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;

/**
 * This class reads PARAM tags from its HTML host page and sets
 * the color and label properties of the applet. Program execution
 * begins with the init() method.
 */
public class Applet1 extends Applet implements ActionListener
{
            /**
             * The entry point for the applet.
             */
            int x1,y1,x2,y2;
            private final int alto=640;
            private final int ancho=480;
            private Image imagen;
            private Graphics contextoGrafico;
            Color colorlinea=Color.blue;
            boolean pintar=false;
            Panel figuras;
            Panel dibujo;
            Panel colores;
            private int figura=1;
            Button linea,elipse,rectangulo;
            Button verde,azul;
            //Button transparente;
           // String sttransparente = "Transparente";
           // boolean btransparente=false;

            int color=0;
            public void init()
            {
                        resize(ancho,alto);
                        setLayout(new BorderLayout());
                        figuras = new Panel();
                        colores = new Panel();
                        linea = new Button("Linea");
                        elipse = new Button("Elipse");
                        rectangulo = new Button("Rectangulo");
                        linea.addActionListener(this);
                        elipse.addActionListener(this);
                        rectangulo.addActionListener(this);
                        figuras.add(linea);
                        figuras.add(rectangulo);
                        figuras.add(elipse);
                        add("North",figuras);
                  //      rojo = new Button("rojo");
                        verde = new Button("verde");
                        azul = new Button("azul");
                    //    amarillo = new Button("amarillo");
                      //  rojo.addActionListener(this);
                        verde.addActionListener(this);
                        azul.addActionListener(this);
                      //  amarillo.addActionListener(this);
                      //  colores.add(rojo);
                        colores.add(verde);
                        colores.add(azul);
                      //  colores.add(amarillo);
                     //   transparente = new Button(sttransparente);
                      // transparente.addActionListener(this);
                     //   colores.add(transparente);
                        add("South",colores);

                        try
                        {
                                   imagen=createImage(ancho,alto);
                                   contextoGrafico=imagen.getGraphics();
                        }
                        catch(Exception e)
                        {
                                   contextoGrafico= null;
                        }
            }
            public void paint(Graphics g)
            {
                        if(contextoGrafico!=null)
                        {
                                   dibuja(contextoGrafico);
                                   g.drawImage(imagen,0,0,this);
                        }
                        else
                                   dibuja(g);
            }
            public void dibuja(Graphics g)
            {
                        dibujafigura(g, x2, y2);
            }
            public void dibujafigura(Graphics g, int h, int i)
            {
                        switch (color)
                        {
                        //case 1:
                          //                     if (pintar) colorlinea=Color.red;
                            //                   break;
                        case 1: if (pintar) colorlinea=Color.green;
                                               break;
                        case 2: if (pintar) colorlinea=Color.blue;
                                               break;
                     //   case 4: if (pintar) colorlinea=Color.yellow;
                        //                       break;
                        }
                        g.setColor(colorlinea);

                        switch (figura)
                        {
                        case 1:
                                               if (pintar) g.drawLine(x1,y1,x2,y2);
                                               break;
                       case 2: if(pintar) g.fillOval(x1,y1,x2-x1,y2-y1);
                                               break;
                        case 3: if (pintar) g.fillRect(x1,y1,x2-x1,y2-y1);
                                               break;
                        }
            }
            public boolean mouseDown(Event evtObj, int x, int y)
            {
                        x1=x;
                        y1=y;
                        colorlinea=Color.gray;
                        pintar=false;

                        return true;
            }
            public boolean mouseMove(Event evtObj, int x, int y)
            {
                        x1=x;
                        y1=y;
                        pintar=false;

                        return true;
            }

            public boolean mouseUp(Event evtObj, int x, int y)
            {
                        x2=x;
                        y2=y;
                        colorlinea=Color.blue;
                        pintar=true;
                        repaint();
                        return true;
            }
            public void actionPerformed(ActionEvent e)
            {
                        if(e.getSource()==linea)figura=1;
                        if(e.getSource()==elipse)figura=2;
                        if(e.getSource()==rectangulo)figura=3;
                     //   if(e.getSource()==rojo)color=1;
                        if(e.getSource()==verde)color=2;
                        if(e.getSource()==azul)color=3;
                       // if(e.getSource()==amarillo)color=4;
                        //if(e.getSource()==transparente)
                        {
                               //    btransparente=!btransparente;
                                //   if (btransparente)
                               //    {
                               //                sttransparente ="Opaco";
                              //                 transparente.setLabel(sttransparente);
                              //     }
                             //      else
                             //      {
                             //                  sttransparente = "Transparente";
                             //                  transparente.setLabel(sttransparente);
                             //      }
                                    repaint();
                        }

            }
}