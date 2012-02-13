/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Abrir_archivo;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.io.*;

public class Texto extends Applet implements ActionListener {
    Frame f;
    Label l;
    TextArea ta;
    Button b;
    Panel p;
    FileDialog fd;
    String arch, ruta, texto;
    BufferedReader br;

    public void init() {
        f = new Frame("Archivo");
        l = new Label("Archivo: ");
        ta = new TextArea();
        b = new Button("Abrir");
        b.addActionListener(this);
        p = new Panel(new GridLayout(1,2));
        p.add(l);
        p.add(b);
        this.add("North",p);
        this.add("Center",ta);
        f.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
        this.setSize(300, 600);
        f.setLocationRelativeTo(null);
        //f.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
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

    /*public static void main(String [] args){
        new Texto();
    }*/
}