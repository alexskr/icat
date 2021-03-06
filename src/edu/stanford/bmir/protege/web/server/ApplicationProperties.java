package edu.stanford.bmir.protege.web.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;

import edu.stanford.bmir.protege.web.client.rpc.data.ApplicationPropertyDefaults;
import edu.stanford.bmir.protege.web.client.rpc.data.ApplicationPropertyNames;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protege.util.URIUtilities;

/**
 * Provides static methods for accessing WebProtege configuration setting. For
 * example, accessing the properties stored in protege.properties.
 *
 * @author Tania Tudorache <tudorache@stanford.edu>
 *
 */
public class ApplicationProperties {

    /*
     * Paths
     */
    private final static String PROJECTS_DIR = "projects"; //not important - just for the default location of the metaproject
    private final static String METAPROJECT_FILE = "metaproject" + File.separator + "metaproject.pprj";
    private static final String LOCAL_METAPROJECT_PATH_DEFAULT = PROJECTS_DIR + File.separator + METAPROJECT_FILE;

    /*
     * Application settings
     */

    private static final String APPLICATION_NAME_DEFAULT = "WebProtege";
    private static final String APPLICATION_URL_DEFAULT = "localhost";

    /*
     * Protege server settings
     */
    private static final String PROTEGE_SERVER_HOSTNAME_DEFAULT = "localhost";

    private final static String PROTEGE_SERVER_USER_DEFAULT = "webprotege";
    private final static String PROTEGE_SERVER_PASSWORD_DEFAULT = "webprotege";

    private static final boolean LOAD_ONTOLOGIES_FROM_PROTEGE_SERVER_DEFAULT = false;

    /*
     * Notifications
     */
    private static final Boolean ENABLE_IMMEDIATE_NOTIFICATION_DEFAULT = Boolean.FALSE;
    private static final Boolean ENABLE_ALL_NOTIFICATION_DEFAULT = Boolean.TRUE;
    private static final int IMMEDIATE_NOTIFICATION_THREAD_INTERVAL_DEFAULT = 2 * 1000 * 60;
    private static final int IMMEDIATE_NOTIFICATION_THREAD_STARTUP_DELAY_DEFAULT = 0 * 1000 * 60; //TODO: DO NOT COMMIT
    private static final int HOURLY_NOTIFICATION_THREAD_STARTUP_DELAY_DEFAULT = 15 * 1000 * 60;
    private static final int DAILY_NOTIFICATION_THREAD_STARTUP_DELAY_DEFAULT = 30 * 1000 * 60;
    private static final int EMAIL_RETRY_DELAY_DEFAULT = 2 * 1000 * 60;

    /*
     * Open id Authentication setting
     */
    private static final boolean WEBPROTEGE_AUTHENTICATE_WITH_OPENID_DEFAULT = false;
    private static final boolean LOGIN_WITH_HTTPS_DEFAULT = false;

    /*
     * ICD export path
     */
    private static final String ICD_EXPORT_PATH_DEFAULT = "." + File.separator;

    /*
     * WebProtege upload directory
     */
    
    private static final String UPLOAD_DIRECTORY_DEFAULT = "/tmp/";
    
    /*
     * Automatic save for local projects
     */
    private static final int SAVE_INTERVAL_DEFAULT = 120;
    public static final int NO_SAVE = -1;

    private static final Properties blacklistedProperties = new Properties();

    static  {
        try {
            File propertyFile = new File(FileUtil.getRealPath(), "blacklist.properties");
            InputStream is = new FileInputStream(propertyFile);
            blacklistedProperties.load(is);
        } catch (Exception e){
            Log.getLogger().warning("Could not retrieve blacklist.properties from " + FileUtil.getRealPath() + ". " + e.getMessage());
            if (Log.getLogger().isLoggable(Level.FINE)) {
                Log.getLogger().log(Level.FINE, "Could not retrieve blacklist.properties.", e);
            }
        }
    }

    public static URI getWeprotegeDirectory() {
        String uri = FileUtil.getRealPath();
        return URIUtilities.createURI(uri);
    }

    public static URI getLocalMetaprojectURI() {
        String path = getApplicationOrEnvProperty(ApplicationPropertyNames.LOCAL_METAPROJECT_PATH_PROP);
        if (path == null) {
            path = FileUtil.getRealPath() + LOCAL_METAPROJECT_PATH_DEFAULT;
        }
        Log.getLogger().info("Path to local metaproject: " + path);
        return URIUtilities.createURI(path);
    }

    public static String getProtegeServerHostName() {
        return getApplicationOrEnvProperty(ApplicationPropertyNames.PROTEGE_SERVER_HOSTNAME_PROP,
                PROTEGE_SERVER_HOSTNAME_DEFAULT);
    }

    static String getProtegeServerUser() {
        return getApplicationOrEnvProperty(ApplicationPropertyNames.PROTEGE_SERVER_USER_PROP,
                PROTEGE_SERVER_USER_DEFAULT);
    }

    static String getProtegeServerPassword() {
        return getApplicationOrEnvProperty(ApplicationPropertyNames.PROTEGE_SERVER_PASSWORD_PROP,
                PROTEGE_SERVER_PASSWORD_DEFAULT);
    }

    public static boolean getLoadOntologiesFromServer() {
        return getBooleanProperty(
                ApplicationPropertyNames.LOAD_ONTOLOGIES_FROM_PROTEGE_SERVER_PROP, LOAD_ONTOLOGIES_FROM_PROTEGE_SERVER_DEFAULT);
    }

    public static int getLocalProjectSaveInterval() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getIntegerProperty(ApplicationPropertyNames.SAVE_INTERVAL_PROP,
                SAVE_INTERVAL_DEFAULT);
    }

    public static String getSmtpHostName() {
        return getApplicationOrEnvProperty(ApplicationPropertyNames.EMAIL_SMTP_HOST_NAME_PROP, "");
    }

    public static String getSmtpPort() {
        return getApplicationOrEnvProperty(ApplicationPropertyNames.EMAIL_SMTP_PORT_PROP, "");
    }

    public static String getSslFactory() {
        return getApplicationOrEnvProperty(ApplicationPropertyNames.EMAIL_SSL_FACTORY_PROP, "javax.net.ssl.SSLSocketFactory");
    }

    public static String getEmailAccount() {
        return getApplicationOrEnvProperty(ApplicationPropertyNames.EMAIL_ACCOUNT_PROP, "");
    }

    public static String getEmailPassword() {
        return getApplicationOrEnvProperty(ApplicationPropertyNames.EMAIL_PASSWORD_PROP, "");
    }

    public static String getApplicationName() {
        return getApplicationOrEnvProperty(ApplicationPropertyNames.APPLICATION_NAME_PROP,
                APPLICATION_NAME_DEFAULT);
    }

    public static boolean getWebProtegeAuthenticateWithOpenId() {
        return getBooleanProperty(
                ApplicationPropertyNames.WEBPROTEGE_AUTHENTICATE_WITH_OPENID_PROP, WEBPROTEGE_AUTHENTICATE_WITH_OPENID_DEFAULT);
    }

    public static boolean getLoginWithHttps() {
        return getBooleanProperty(
                ApplicationPropertyNames.LOGIN_WITH_HTTPS_PROP, LOGIN_WITH_HTTPS_DEFAULT);
    }

    public static String getApplicationHttpsPort() {
        return getApplicationOrEnvProperty(
                ApplicationPropertyNames.APPLICATION_PORT_HTTPS_PROP, ApplicationPropertyDefaults.APPLICATION_PORT_HTTPS_DEFAULT);
    }

    public static String getICDExportDirectory() {
        String exportPath =  getApplicationOrEnvProperty(ApplicationPropertyNames.ICD_EXPORT_DIR_PROP, ICD_EXPORT_PATH_DEFAULT);
        exportPath = exportPath.endsWith(File.separator) ? exportPath : exportPath + File.separator;
        return exportPath;
    }

    public static String getDownloadServerPath() {
        String exportPath =  getApplicationOrEnvProperty(ApplicationPropertyNames.DOWNLOAD_SERVER_PATH_PROP, ".");
        exportPath = exportPath.endsWith(File.separator) ? exportPath : exportPath + File.separator;
        return exportPath;
    }

    public static String getDownloadClientRelPath() {
        String exportPath =  getApplicationOrEnvProperty(ApplicationPropertyNames.DOWNLOAD_CLIENT_REL__PATH_PROP, "");
        exportPath = exportPath.endsWith("/") ? exportPath : exportPath + "/";
        return exportPath;
    }

    public static int getServerPollingTimeoutMinutes() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getIntegerProperty(ApplicationPropertyNames.SERVER_POLLING_TIMEOUT_MINUTES_PROP,
                ApplicationPropertyDefaults.SERVER_POLLING_TIMEOUT_MINUTES_DEFAULT);
    }

    public static String getApplicationUrl() {
        return getApplicationOrEnvProperty(ApplicationPropertyNames.APPLICATION_URL_PROP, APPLICATION_URL_DEFAULT);
    }

    public static Boolean getImmediateThreadsEnabled() {
        return getBooleanProperty(ApplicationPropertyNames.ENABLE_IMMEDIATE_NOTIFICATION,
                ENABLE_IMMEDIATE_NOTIFICATION_DEFAULT);
    }

    public static Boolean getAllNotificationEnabled() {
        return getBooleanProperty(ApplicationPropertyNames.ENABLE_ALL_NOTIFICATION,
                ENABLE_ALL_NOTIFICATION_DEFAULT);
    }

    public static Integer getEmailRetryDelay() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getIntegerProperty(ApplicationPropertyNames.EMAIL_RETRY_DELAY_PROP,
                EMAIL_RETRY_DELAY_DEFAULT);
    }

    public static Integer getImmediateThreadStartupDelay() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getIntegerProperty(ApplicationPropertyNames.IMMEDIATE_NOTIFICATION_THREAD_STARTUP_DELAY_PROP
                , IMMEDIATE_NOTIFICATION_THREAD_STARTUP_DELAY_DEFAULT);
    }

    public static Integer getHourlyThreadStartupDelay() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getIntegerProperty(ApplicationPropertyNames.HOURLY_NOTIFICATION_THREAD_STARTUP_DELAY_PROP,
                HOURLY_NOTIFICATION_THREAD_STARTUP_DELAY_DEFAULT);
    }

    public static Integer getDailyThreadStartupDelay() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getIntegerProperty(ApplicationPropertyNames.DAILY_NOTIFICATION_THREAD_STARTUP_DELAY_PROP,
                DAILY_NOTIFICATION_THREAD_STARTUP_DELAY_DEFAULT);
    }

    public static Integer getImmediateThreadInterval() {
        return edu.stanford.smi.protege.util.ApplicationProperties.getIntegerProperty(ApplicationPropertyNames.IMMEDIATE_NOTIFICATION_THREAD_INTERVAL_PROP,
                IMMEDIATE_NOTIFICATION_THREAD_INTERVAL_DEFAULT);
    }

    public static String getUploadDirectory() {
        String uploadDir =  getApplicationOrEnvProperty(ApplicationPropertyNames.UPLOAD_DIR_PROP, UPLOAD_DIRECTORY_DEFAULT);
        uploadDir = uploadDir.endsWith(File.separator) ? uploadDir : uploadDir + File.separator;
        return uploadDir;
    }
    
    
    
    public static String getApplicationOrEnvProperty(String name) {
        return getApplicationOrEnvProperty(name, null);
    }

    
    /***** Util methods ******/
    
    public static String getApplicationOrEnvProperty(String name, String defaultValue) {
        String value = null;
    
    	try {
    		value = System.getenv(name.replaceAll("\\.", "_"));
		} catch (SecurityException e) {
			// do nothing, it happens if there is a SecurityManager
		}
      
        if (value == null) {
        	value = edu.stanford.smi.protege.util.ApplicationProperties.getString(name);
        	
        	if (value == null) {
        		value = defaultValue;
        	}
        }
        
        System.out.println(name + " -> " + value);
        return value;
    }
    
    public static boolean getBooleanProperty(String name, boolean defaultValue) {
        boolean value = defaultValue;
        String strValue = null;
        
        try {
    		strValue = System.getenv(name.replaceAll("\\.", "_"));
    		
    		if (strValue != null) {
    			value = Boolean.valueOf(strValue).booleanValue();
    		}
		} catch (Exception e) {
			// do nothing, it happens if there is a SecurityManager
		}
     
        if (strValue == null) { //property not found in the env vars, try to get it from protege.properties
        	strValue = edu.stanford.smi.protege.util.ApplicationProperties.getApplicationProperties().getProperty(name);
            
        	if (strValue != null) { //found in protege.properties
	            try {
	                value = Boolean.valueOf(strValue).booleanValue();
	            } catch (Exception e) {
	                // do nothing
	            }
        	}
        }
    
        return value;
    }
        
    public static HashMap<String, String> getPropertiesForClient(){
        final Properties applicationProperties1 = edu.stanford.smi.protege.util.ApplicationProperties.getApplicationProperties();
        final Set<String> stringSet = applicationProperties1.stringPropertyNames();
        final HashMap<String, String> applicationProperties = new HashMap<String, String>();
        for (String propertyName : stringSet) {
            applicationProperties.put(propertyName, applicationProperties1.getProperty(propertyName));
        }

        for (String blacklistedProperty : ApplicationProperties.blacklistedProperties.stringPropertyNames()) {
            applicationProperties.remove(blacklistedProperty);
        }

        return applicationProperties;
    }

}
