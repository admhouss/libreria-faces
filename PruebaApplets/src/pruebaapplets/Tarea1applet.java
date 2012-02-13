/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pruebaapplets;

import java.awt.event.*;
import java.applet.*;
import java.awt.*;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Tarea1applet extends Applet implements ActionListener{
    Frame f;
    Label lb;
    TextArea ta;
    Label l;
    TextField tf;
    Button b;
    Button bt;
    FileDialog fd;
    String arch, ruta, texto;
    BufferedReader br;
    Panel p;
    Panel p1;
    
    

     int x1,y1,x2,y2,x3,x4,x5,y3,y4,y5,x6;
      Color colorlinea=Color.blue;
    

    public void init(){
       
        f= new Frame ("Archivo");
        lb=new Label ("Archivo:");
        ta= new TextArea();
        l = new Label("Nombre: ", 2);
        tf = new TextField();
        bt= new Button("Abrir");
        
        bt.addActionListener(this);
        p1= new Panel(new GridLayout(1,2));
        p1.add(lb);
        p1.add(bt);
        this.add("North",p1);
        this.add("Center",ta);

        b = new Button("Enviar");
        b.addActionListener(this);
        p = new Panel(new GridLayout(1,3));
        p.add(l);
        p.add(tf);
        p.add(b);

        this.add("Center", p);
        f.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
        this.setSize(600,900);
        f.setLocationRelativeTo(null);

       

      //   f= new Frame ("Archivo");
      //  lb=new Label ("Archivo:");
       // ta= new TextArea();
    }


   public void paint (Graphics g){
       g.drawString("IPN",10,40);
    
 //  g.setColor(Color.green);

   g.drawLine(x1,y1,x2,y2);
   g.setColor(Color.green);
    g.fillOval(x1,y1,x2-x1,y2-y1);
    g.setColor(Color.green);
    g.fillRect(x5,y4,x6-x5,y5-y4);
    }
public boolean mouseDown(Event evtObj, int x, int y)
            {
                        x1=x;
                        y1=y;
                        colorlinea=Color.gray;
                       // pintar=false;

                        return true;
            }
            public boolean mouseMove(Event evtObj, int x, int y)
            {
                        x1=x;
                        y1=y;
                       // pintar=false;

                        return true;
            }

            public boolean mouseUp(Event evtObj, int x, int y)
            {
                        x2=x;
                        y2=y;
                        colorlinea=Color.blue;
                     //   pintar=true;
                        repaint();
                        return true;
            }


    public void actionPerformed(ActionEvent e) {
        tf.setText("Jocelin");
        fd=new FileDialog(f,"Archivo");
            fd.setVisible(true);
            arch=fd.getFile();
            ruta=fd.getDirectory();
            try{
                br=new BufferedReader(new FileReader(ruta+arch));
                texto=br.readLine();
                while(null!=texto){
                    ta.append(texto+"\n");
                    texto=br.readLine();
                }
                br.close();
            }catch(IOException ioe){}
    }


}
