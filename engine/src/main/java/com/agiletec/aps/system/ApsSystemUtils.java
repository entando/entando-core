/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.aps.system;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.AsyncAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe di utilità.
 * E' la classe detentrice del log di sistema.
 * @author E.Santoboni
 */
public class ApsSystemUtils {

	/**
	 * Inizializzazione della classe di utilità.
	 * @throws Exception
	 */
	public void init() throws Exception {

		String appenderName = "ENTANDO";
		
		//TODO set this in constant
		String conversionPattern = (String) this._systemParams.get("log4jConversionPattern");
		if (StringUtils.isBlank(conversionPattern)) {
			conversionPattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} - %-5p -  %c - %m%n"; //default conversionPattern
		}
		PatternLayout layout = new PatternLayout(conversionPattern);
		
		String maxFileSize = (String) this._systemParams.get(INIT_PROP_LOG_FILE_SIZE);
		if (StringUtils.isBlank(maxFileSize)) {
			maxFileSize = "1MB"; //default size
		} else {
			long mega  = new Long(maxFileSize) / KILOBYTE;
			maxFileSize = mega + "KB";
		}
		String filename = (String) this._systemParams.get(INIT_PROP_LOG_FILE_PREFIX);
		int maxBackupIndex = Integer.parseInt((String) this._systemParams.get(INIT_PROP_LOG_FILES_COUNT));
		String log4jLevelString = (String) this._systemParams.get(INIT_PROP_LOG_LEVEL);
		if (StringUtils.isBlank(log4jLevelString)) {
			log4jLevelString = "INFO"; //default level
		}
		
		RollingFileAppender fileAppender = (RollingFileAppender) LogManager.getRootLogger().getAppender(appenderName);
		if (null == fileAppender) {
			fileAppender = new RollingFileAppender();
			fileAppender.setName(appenderName);
		}
		fileAppender.setMaxBackupIndex(maxBackupIndex);
		fileAppender.setThreshold(org.apache.log4j.Level.toLevel(log4jLevelString));
		fileAppender.setLayout(layout);
		fileAppender.setMaxFileSize(maxFileSize);
		
		fileAppender.setFile(filename);
		fileAppender.activateOptions();
		
		AsyncAppender async = (AsyncAppender) LogManager.getRootLogger().getAppender("async");
		async.addAppender(fileAppender);
	}

	/**
	 * Restituisce il logger di sistema.
	 * @return Il logger
	 */
	public static Logger getLogger() {
		return _logger;
	}

	/**
	 * Traccia una eccezione sul logger del contesto. Se il livello di soglia
	 * del logger è superiore a FINER, viene emesso solo un breve messaggio di
	 * livello SEVERE, altrimenti viene tracciato anche lo stack trace della
	 * eccezione (con il livello FINER).
	 * @param t L'eccezione da tracciare
	 * @param caller La classe chiamante, in cui si è verificato l'errore.
	 * @param methodName Il metodo in cui si è verificato l'errore.
	 * @param message Testo da includere nel tracciamento.
	 */
	public static void logThrowable(Throwable t, Object caller,
			String methodName, String message){
		String className = null;
		if(caller != null) {
			className = caller.getClass().getName();
		}
		_logger.error("{} in {}.{}", message, className, methodName,  t);
	}

	/**
	 * Traccia una eccezione sul logger del contesto. Se il livello di soglia
	 * del logger è superiore a FINER, viene emesso solo un breve messaggio di
	 * livello SEVERE, altrimenti viene tracciato anche lo stack trace della
	 * eccezione (con il livello FINER).
	 * @param t L'eccezione da tracciare
	 * @param caller La classe chiamante, in cui si è verificato l'errore.
	 * @param methodName Il metodo in cui si è verificato l'errore.
	 */
	public static void logThrowable(Throwable t, Object caller, String methodName) {
		logThrowable(t, caller, methodName, "Exception");
	}

	/**
	 * Setta la mappa dei parametri di inizializzazione.
	 * @param systemParams I parametri di inizializzazione.
	 */
	public void setSystemParams(Map<String, Object> systemParams) {
		this._systemParams = systemParams;
	}

	/**
	 * Nome della property che definisce il nome
	 * da assegnare al logger (tipo: String)
	 */
	public static final String INIT_PROP_LOG_NAME = "logName";

	/**
	 * Nome della property che definisce il path del file di log; il nome
	 * completo del file sarà ottenuto aggiungendo eventuale estensione
	 * secondo le regole di java.util.logging.Logger. (tipo: String)
	 */
	public static final String INIT_PROP_LOG_FILE_PREFIX = "logFilePrefix";

	/**
	 * Nome della property che definisce il livello di log; utilizzare
	 * uno dei nomi delle costanti di java.util.logging.Level. (tipo: String)
	 */
	public static final String INIT_PROP_LOG_LEVEL = "logLevel";

	/**
	 * Nome della property che definisce il size (in byte) del singolo file di log
	 */
	public static final String INIT_PROP_LOG_FILE_SIZE = "logFileSize";

	/**
	 * Nome della property che definisce numero di file per ciclo di log.
	 */
	public static final String INIT_PROP_LOG_FILES_COUNT = "logFilesCount";

	/**
	 * Logger di sistema.
	 */
	private static final Logger _logger =  LoggerFactory.getLogger(ApsSystemUtils.class);
	
	private Map<String, Object> _systemParams;

	private static final long  KILOBYTE = 1024L;
	
}