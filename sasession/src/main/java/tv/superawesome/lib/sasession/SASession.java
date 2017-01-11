/**
 * @Copyright:   SuperAwesome Trading Limited 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */
package tv.superawesome.lib.sasession;

import android.content.Context;
import android.os.Looper;

import java.util.Locale;

import tv.superawesome.lib.sautils.SAUtils;

/**
 * Class that manages an AwesomeAds session, containing all variables needed to setup loading for
 * a certain ad
 */
public class SASession {

    // constants
    private final static String PRODUCTION_URL = "https://ads.superawesome.tv/v2";
    private final static String STAGING_URL = "https://ads.staging.superawesome.tv/v2";
    private final static String DEVICE_PHONE = "phone";
    private final static String DEVICE_TABLET = "tablet";

    // the current frequency capper
    private SACapper capper = null;

    // private state members
    private String baseUrl;
    private boolean testEnabled;
    private int dauId;
    private String version;
    private SAConfiguration configuration;
    private String packageName;
    private String appName;
    private SAUtils.SAConnectionType connectionType;
    private String lang;
    private String device;
    private String userAgent;

    /**
     * Main constructor for the Session
     *
     * @param context current context (activity or fragment)
     */
    public SASession(Context context) {
        // create the capper
        capper = new SACapper(context);

        // set default configuration
        setConfigurationProduction();
        disableTestMode();
        setDauId(0);
        setVersion("0.0.0");
        packageName = context != null ? context.getPackageName() : "unknown";
        appName = context != null ? SAUtils.getAppLabel(context) : "unknown";
        connectionType = context != null ? SAUtils.getNetworkConnectivity(context) : SAUtils.SAConnectionType.unknown;
        lang = Locale.getDefault().toString();
        device = SAUtils.getSystemSize() == SAUtils.SASystemSize.phone ? DEVICE_PHONE : DEVICE_TABLET;

        // get user agent if the current thread is the main one (otherwise this will crash
        // in some scenarios)
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            userAgent = SAUtils.getUserAgent(context);
        } else {
            userAgent = SAUtils.getUserAgent(null);
        }

    }

    /**
     * Method that "prepares" the session. Mainly this means getting the DAU ID integer, which has
     * to be done on a secondary thread
     *
     * @param listener an instance of the SASessionInterface
     */
    public void prepareSession (final SASessionInterface listener) {
        capper.getDauID(new SACapperInterface() {
            @Override
            public void didFindDAUID(int dauID) {
                setDauId(dauID);

                if (listener != null) {
                    listener.didFindSessionReady();
                }
            }
        });
    }

    /**
     * Explicit configuration setup, which also sets the base session URL
     *
     * @param configuration a new configuration enum
     */
    public void setConfiguration(SAConfiguration configuration) {
        if (configuration == SAConfiguration.PRODUCTION) {
            this.configuration = SAConfiguration.PRODUCTION;
            baseUrl = PRODUCTION_URL;
        } else {
            this.configuration = SAConfiguration.STAGING;
            baseUrl = STAGING_URL;
        }
    }

    /**
     * Implicit production setter
     */
    public void setConfigurationProduction () {
        setConfiguration(SAConfiguration.PRODUCTION);
    }

    /**
     * Implicit staging setter
     */
    public void setConfigurationStaging () {
        setConfiguration(SAConfiguration.STAGING);
    }

    /**
     * Explicit test setup
     *
     * @param testEnabled the new setup, as a bool
     */
    public void setTestMode(boolean testEnabled) {
        this.testEnabled = testEnabled;
    }

    /**
     * Implicit test enabled setter
     */
    public void enableTestMode () {
        setTestMode(true);
    }

    /**
     * Implicit test disabled setter
     */
    public void disableTestMode () {
        setTestMode(false);
    }

    /**
     * Setter for the DAU ID value
     *
     * @param dauId the new DAU ID integer
     */
    public void setDauId(int dauId) {
        this.dauId = dauId;
    }

    /**
     * Version setter
     *
     * @param version new version string
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Getter for the base url
     *
     * @return the current base URL
     */
    public String getBaseUrl () {
        return baseUrl;
    }

    /**
     * Getter for the test model
     *
     * @return the current testEnabled variable
     */
    public boolean getTestMode () {
        return testEnabled;
    }

    /**
     * Getter for the DAU ID
     *
     * @return the current dauId variable
     */
    public int getDauId () {
        return dauId;
    }

    /**
     * Getter for the version
     *
     * @return the current version variable
     */
    public String getVersion () {
        return version;
    }

    /**
     * Getter for the configuration
     *
     * @return the current configuration variable
     */
    public SAConfiguration getConfiguration () { return configuration; }

    /**
     * Getter for the cache buster
     *
     * @return a random int
     */
    public int getCachebuster () {
        return SAUtils.getCacheBuster();
    }

    /**
     * Getter for the package name
     *
     * @return the current package name
     */
    public String getPackageName () {
        return packageName;
    }

    /**
     * Getter for the app name
     *
     * @return the current app name
     */
    public String getAppName () {
        return appName;
    }

    /**
     * Getter for the connection type
     *
     * @return the current connection type enum
     */
    public SAUtils.SAConnectionType getConnectionType () {
        return connectionType;
    }

    /**
     * Getter for the current language
     *
     * @return the current lang variable (as en_US)
     */
    public String getLang () {
        return lang;
    }

    /**
     * Getter for the current device
     *
     * @return the current device variable ("phone" or "tablet")
     */
    public String getDevice () {
        return device;
    }

    /**
     * Getter for the current user agent
     *
     * @return the current userAgent variable
     */
    public String getUserAgent () {
        return userAgent;
    }
}
