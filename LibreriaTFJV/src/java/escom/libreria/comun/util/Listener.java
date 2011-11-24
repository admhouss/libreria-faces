/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package escom.libreria.comun.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import propertiesConfig.XMLReference;

/**
 * Web application lifecycle listener.
 * @author xxx
 */
public class Listener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
       // XMLReference.class.getResource("");
        //throw new UnsupportedOperationException("Not supported yet.");


        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
