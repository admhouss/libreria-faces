package escom.libreria.info.task;

import java.util.Date;
import java.util.TimerTask;

import org.apache.log4j.Logger;

public class ApplicationScheduleTask extends TimerTask {
	private static Logger logger = Logger.getLogger(ApplicationScheduleTask.class);
	
	public void run() {
		logger.info("Running Task  : "+new Date());
		//Invocaci�n al m�todo que disparar� el proceso deseado
	}
}
