/**
 * @Copyright:   SuperAwesome Trading Limited 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */
package tv.superawesome.lib.sasession.session;

import android.content.Context;
import android.os.Looper;

import java.util.Locale;

import tv.superawesome.lib.sasession.capper.SACapper;
import tv.superawesome.lib.sasession.capper.SACapperInterface;
import tv.superawesome.lib.sasession.defines.SAConfiguration;
import tv.superawesome.lib.sasession.defines.SARTBInstl;
import tv.superawesome.lib.sasession.defines.SARTBPlaybackMethod;
import tv.superawesome.lib.sasession.defines.SARTBPosition;
import tv.superawesome.lib.sasession.defines.SARTBSkip;
import tv.superawesome.lib.sasession.defines.SARTBStartDelay;
import tv.superawesome.lib.sautils.SAUtils;

/**
 * Class that manages an AwesomeAds session, containing all variables needed to setup loading for
 * a certain ad
 */
public class SASession {

    // constants
    private final static String      PRODUCTION_URL = "https://ads.superawesome.tv/v2";
    private final static String      STAGING_URL = "https://ads.staging.superawesome.tv/v2";
    private final static String      DEVICE_PHONE = "phone";
    private final static String      DEVICE_TABLET = "tablet";

    // the current frequency capper
    private SACapper                 capper = null;

    // private state members
    private String                   baseUrl;
    private boolean                  testEnabled;
    private int                      dauId;
    private String                   version;
    private String                   packageName;
    private String                   appName;
    private SAUtils.SAConnectionType connectionType;
    private String                   lang;
    private String                   device;
    private String                   userAgent;
    private SAConfiguration          configuration;
    private SARTBInstl               instl;
    private SARTBPosition            pos;
    private SARTBSkip                skip;
    private SARTBStartDelay          startDelay;
    private SARTBPlaybackMethod      playbackMethod;
    private int                      width;
    private int                      height;

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
        instl = SARTBInstl.FULLSCREEN;
        pos = SARTBPosition.FULLSCREEN;
        skip = SARTBSkip.NO_SKIP;
        startDelay = SARTBStartDelay.PRE_ROLL;
        playbackMethod = SARTBPlaybackMethod.WITH_SOUND_ON_SCREEN;
        width = 0;
        height = 0;

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
     * Setters
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

    public void setInstl(SARTBInstl instl) {
        this.instl = instl;
    }

    public void setPos(SARTBPosition pos) {
        this.pos = pos;
    }

    public void setSkip(SARTBSkip skip) {
        this.skip = skip;
    }

    public void setStartDelay(SARTBStartDelay startDelay) {
        this.startDelay = startDelay;
    }

    public void setPlaybackMethod(SARTBPlaybackMethod playbackMethod) {
        this.playbackMethod = playbackMethod;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setLanguage (String lang) {
        this.lang = lang;
    }

    public void setLanguage (Locale locale) {
        if (locale != null) {
            this.lang = locale.toString();
        }
    }

    /**
     * Getters
     */

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

    public SAUtils.SAConnectionType getConnectionType () {
        return connectionType;
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

    public SARTBInstl getInstl() {
        return instl;
    }

    public SARTBPosition getPos() {
        return pos;
    }

    public SARTBSkip getSkip() {
        return skip;
    }

    public SARTBStartDelay getStartDelay() {
        return startDelay;
    }

    public SARTBPlaybackMethod getPlaybackMethod() {
        return playbackMethod;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
