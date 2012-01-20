/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.escom.libreria.listener;

import escom.libreria.cliente.log4g.xml.XMLReference;
import escom.libreria.info.task.ApplicationScheduleTask;
import java.io.InputStream;
import java.util.Timer;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Level;
import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Web application lifecycle listener.
 * @author xxx
 */
@WebListener()
public class ApplicationContextListener implements ServletContextListener {

    private Timer timer = new Timer();
	private static boolean log4jInitialized = false;
	private static  Logger logger = Logger.getLogger(ApplicationContextListener.class);

      @Override
	public void contextDestroyed(ServletContextEvent arg0) {
		timer.cancel();
	}

    @Override
	public void contextInitialized(ServletContextEvent event) {
	    initializeLog4j();
            scheduleApplicationTask();
	}

	private void scheduleApplicationTask() {
		ApplicationScheduleTask applicationScheduleTask = new ApplicationScheduleTask();
		long minute = (1000 * 60);
		timer.schedule(applicationScheduleTask, 0, (minute * 60));
	}

	
	public synchronized void initializeLog4j() {
		try {
			if (!log4jInitialized) {
				log4jInitialized = true;
				InputStream log4jConfigStream = XMLReference.class.getResourceAsStream("log4j.configure.xml");

				DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document log4jDocument = docBuilder.parse(log4jConfigStream);
				DOMConfigurator.configure((Element) log4jDocument.getFirstChild());
			}
		} catch (Exception e) {
			logger.debug("No es posible configurar el log: "  +e.getMessage());
			e.printStackTrace();
		}
	}

}
