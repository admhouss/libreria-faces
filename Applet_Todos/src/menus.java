/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.applet.Applet;

/**
 *
 * @author Diana
 */
import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;

public class menus extends Applet implements ActionListener
{
    Button boton;
    ventana menuWindow;

    public void init()
    {
        boton = new Button("Visualizar menú en ventana");
        add(boton);
        boton.addActionListener(this);

        menuWindow = new ventana("Menús AWT");
        menuWindow.setSize(200, 200);
        menuWindow.setLocation(500,250);
    }

    public void actionPerformed(ActionEvent event)
    {
        if(event.getSource() == boton){
            menuWindow.setVisible(true);
        }
    }
}

class ventana extends Frame implements ActionListener
{
    Menu menus;
    MenuBar menubar;
    MenuItem menuitem1, menuitem2, menuitem3,menuitem4,menuitem5;

    Label etiqueta;

    ventana(String title)
    {
        super(title);
        etiqueta = new Label("Esta es la vantana de meús");
        setLayout(new GridLayout(1, 1));
        add(etiqueta);
        menubar = new MenuBar();

        menus = new Menu("Archivo");

        menuitem1 = new MenuItem("Abrir Archivo de Texto");
        menuitem2 = new MenuItem("Dibujar Figuras");
        menuitem3 = new MenuItem("Imagenes");
        menuitem4 = new MenuItem("Reproducir Sonido");
        menuitem5 = new MenuItem("Elemento 5");

        menuitem1.addActionListener(this);
        menuitem2.addActionListener(this);
        menuitem3.addActionListener(this);
        menuitem4.addActionListener(this);
        menuitem5.addActionListener(this);

        menus.add(menuitem1);
        menus.add(menuitem2);
        menus.add(menuitem3);
        menus.add(menuitem4);
        menus.add(menuitem5);

        menubar.add(menus);
        setMenuBar(menubar);

         addWindowListener(new WindowAdapter() {public void
              windowClosing(WindowEvent e) {setVisible(false);}});
    }

    public void actionPerformed(ActionEvent event)
    {
        if(event.getSource() == menuitem1){
            etiqueta.setText("Elemento del menú nº1");
        } else if(event.getSource() == menuitem2){
            etiqueta.setText("Elemento del menú nº2");
        } else if(event.getSource() == menuitem3){
            etiqueta.setText("Elemento del menú nº3");
        } else if(event.getSource() == menuitem4){
            etiqueta.setText("Elemento del menú nº4");
        } else if(event.getSource() == menuitem5){
            etiqueta.setText("Elemento del menú nº5");
        }
    }
}
