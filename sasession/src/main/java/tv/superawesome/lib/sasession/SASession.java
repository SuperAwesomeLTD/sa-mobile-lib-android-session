package tv.superawesome.lib.sasession;

import java.util.HashMap;

/**
 * Created by gabriel.coman on 15/07/16.
 */
public class SASession {

    // constants
    private final static String PRODUCTION_URL = "https://ads.superawesome.tv/v2";
    private final static String STAGING_URL = "https://ads.staging.superawesome.tv/v2";

    // state variables
    private String baseUrl;
    private boolean testEnabled;
    private int dauId;
    private String version;
    private SAConfiguration configuration;

    // constructor
    public SASession() {
        setConfigurationProduction();
        disableTestMode();
        setDauId(0);
        setVersion("0.0.0");
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

    public boolean isTestEnabled () {
        return testEnabled;
    }

    public int getDauId () {
        return dauId;
    }

    public String getVersion () {
        return version;
    }

    public SAConfiguration getConfiguration () { return configuration; }
}
