package tv.superawesome.lib.sasession;

import java.util.HashMap;

/**
 * Created by gabriel.coman on 15/07/16.
 */
public class SASession {

    // static constants
    public static final int CONFIGURATION_PRODUCTION = 0;
    public static final int CONFIGURATION_STAGING = 1;

    // constants
    private final static String PRODUCTION_URL = "https://ads.superawesome.tv/v2";
    private final static String STAGING_URL = "https://ads.staging.superawesome.tv/v2";

    // singleton instance
    private static SASession instance = new SASession();

    // state variables
    private String baseUrl;
    private boolean testEnabled;
    private int dauId;
    private String version;
    private int configuration;

    // constructor
    private SASession() {
        setConfigurationProduction();
        setTestDisabled();
        setDauId(0);
        setVersion("0.0.0");
    }

    // public singleton functions
    public static SASession getInstance(){
        return instance;
    }

    // setters

    public void setConfiguration(int configuration) {
        if (configuration == CONFIGURATION_PRODUCTION) {
            setConfigurationProduction();
        } else {
            setConfigurationStaging();
        }
    }

    public void setConfigurationProduction () {
        configuration = CONFIGURATION_PRODUCTION;
        baseUrl = PRODUCTION_URL;
    }

    public void setConfigurationStaging () {
        configuration = CONFIGURATION_STAGING;
        baseUrl = STAGING_URL;
    }

    public void setTestEnabled () {
        testEnabled = true;
    }

    public void setTestDisabled () {
        testEnabled = false;
    }

    public void setTest(boolean testEnabled) {
        this.testEnabled = testEnabled;
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

    public boolean isTestEnabled () {
        return testEnabled;
    }

    public int getDauId () {
        return dauId;
    }

    public String getVersion () {
        return version;
    }

    public int getConfiguration () { return configuration; }
}
