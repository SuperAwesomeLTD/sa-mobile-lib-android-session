package tv.superawesome.lib.sasession;

import android.content.Context;

import java.util.Locale;

import tv.superawesome.lib.sautils.SAUtils;

/**
 * Created by gabriel.coman on 15/07/16.
 */
public class SASession {

    // constants
    private final static String PRODUCTION_URL = "https://ads.superawesome.tv/v2";
    private final static String STAGING_URL = "https://ads.staging.superawesome.tv/v2";

    // context
    private SACapper capper = null;

    // state variables
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

    // constructor
    public SASession(Context context) {
        capper = new SACapper(context);
        setConfigurationProduction();
        disableTestMode();
        setDauId(0);
        setVersion("0.0.0");
        packageName = context != null ? context.getPackageName() : "unknown";
        appName = context != null ? SAUtils.getAppLabel(context) : "unknown";
        connectionType = context != null ? SAUtils.getNetworkConnectivity(context) : SAUtils.SAConnectionType.unknown;
        lang = Locale.getDefault().toString();
        device = SAUtils.getSystemSize() == SAUtils.SASystemSize.mobile ? "mobile" : "tablet";
        userAgent = SAUtils.getUserAgent(context);
    }

    // prepare

    public void prepareSession (final SASessionInterface listener) {
        capper.getDauID(new SACapperInterface() {
            @Override
            public void didFindDAUId(int dauID) {
                setDauId(dauID);

                if (listener != null) {
                    listener.sessionReady();
                }
            }
        });
    }

    // setters

    public void setConfiguration(SAConfiguration configuration) {
        if (configuration == SAConfiguration.PRODUCTION) {
            this.configuration = SAConfiguration.PRODUCTION;
            baseUrl = PRODUCTION_URL;
        } else {
            this.configuration = SAConfiguration.STAGING;
            baseUrl = STAGING_URL;
        }
    }

    public void setConfigurationProduction () {
        setConfiguration(SAConfiguration.PRODUCTION);
    }

    public void setConfigurationStaging () {
        setConfiguration(SAConfiguration.STAGING);
    }

    public void setTestMode(boolean testEnabled) {
        this.testEnabled = testEnabled;
    }

    public void enableTestMode () {
        setTestMode(true);
    }

    public void disableTestMode () {
        setTestMode(false);
    }

    public void setDauId(int dauId) {
        this.dauId = dauId;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    // getters

    public String getBaseUrl () {
        return baseUrl;
    }

    public boolean getTestMode () {
        return testEnabled;
    }

    public int getDauId () {
        return dauId;
    }

    public String getVersion () {
        return version;
    }

    public SAConfiguration getConfiguration () { return configuration; }

    public int getCachebuster () {
        return SAUtils.getCacheBuster();
    }

    public String getPackageName () {
        return packageName;
    }

    public String getAppName () {
        return appName;
    }

    public int getConnectionType () {
        return connectionType.ordinal();
    }

    public String getLang () {
        return lang;
    }

    public String getDevice () {
        return device;
    }

    public String getUserAgent () {
        return userAgent;
    }
}
