package superawesome.tv.sasessiondemo;

import android.app.Application;
import android.content.res.Resources;
import android.os.Looper;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.DisplayMetrics;
import android.util.Log;

import tv.superawesome.lib.sasession.SAConfiguration;
import tv.superawesome.lib.sasession.SASession;
import tv.superawesome.lib.sasession.SASessionInterface;
import tv.superawesome.lib.sautils.SAUtils;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class SASession_Tests extends ApplicationTestCase<Application> {


    private SASession session;
    private static final String TEST_PRODUCTION_URL = "https://ads.superawesome.tv/v2";
    private static final String TEST_STAGING_URL = "https://ads.staging.superawesome.tv/v2";

    public SASession_Tests () {
        super(Application.class);
        session = new SASession(getContext());
    }

    @SmallTest
    public void testDefaults () {
        // given
        // new session

        // when
        SAConfiguration expectedConfig = SAConfiguration.PRODUCTION;
        boolean expectedTestMode = false;
        String expectedVersion = "0.0.0";
        String expectedBaseURL = TEST_PRODUCTION_URL;

        // then
        SAConfiguration configuration = session.getConfiguration();
        boolean testMode = session.getTestMode();
        String version = session.getVersion();
        String baseUrl = session.getBaseUrl();

        // assert
        assertEquals(configuration, expectedConfig);
        assertEquals(testMode, expectedTestMode);
        assertEquals(version, expectedVersion);
        assertEquals(baseUrl, expectedBaseURL);
    }

    @SmallTest
    public void testConfigurationProduction1 () {
        // given
        session.setConfigurationProduction();

        // when
        SAConfiguration expectedConfig = SAConfiguration.PRODUCTION;
        String expectedBaseUrl = TEST_PRODUCTION_URL;

        // then
        SAConfiguration configuration = session.getConfiguration();
        String baseUrl = session.getBaseUrl();

        // assert
        assertEquals(configuration, expectedConfig);
        assertEquals(baseUrl, expectedBaseUrl);
    }

    @SmallTest
    public void testConfigurationProduction2 () {
        // given
        session.setConfiguration(SAConfiguration.PRODUCTION);

        // when
        SAConfiguration expectedConfig = SAConfiguration.PRODUCTION;
        String expectedBaseUrl = TEST_PRODUCTION_URL;

        // then
        SAConfiguration configuration = session.getConfiguration();
        String baseUrl = session.getBaseUrl();

        // assert
        assertEquals(configuration, expectedConfig);
        assertEquals(baseUrl, expectedBaseUrl);
    }

    @SmallTest
    public void testConfigurationStaging1 () {
        // given
        session.setConfigurationStaging();

        // when
        SAConfiguration expectedConfig = SAConfiguration.STAGING;
        String expectedBaseUrl = TEST_STAGING_URL;

        // then
        SAConfiguration configuration = session.getConfiguration();
        String baseUrl = session.getBaseUrl();

        // assert
        assertEquals(configuration, expectedConfig);
        assertEquals(baseUrl, expectedBaseUrl);
    }

    @SmallTest
    public void testConfigurationStaging2 () {
        // given
        session.setConfiguration(SAConfiguration.STAGING);

        // when
        SAConfiguration expectedConfig = SAConfiguration.STAGING;
        String expectedBaseUrl = TEST_STAGING_URL;

        // then
        SAConfiguration configuration = session.getConfiguration();
        String baseUrl = session.getBaseUrl();

        // assert
        assertEquals(configuration, expectedConfig);
        assertEquals(baseUrl, expectedBaseUrl);
    }

    @SmallTest
    public void testEnableTestMode1 () {
        // given
        session.enableTestMode();

        // when
        boolean expectedTestMode = true;

        // then
        boolean testMode = session.getTestMode();

        // assert
        assertEquals(testMode, expectedTestMode);
    }

    @SmallTest
    public void testEnableTestMode2 () {
        // given
        session.setTestMode(true);

        // when
        boolean expectedTestMode = true;

        // then
        boolean testMode = session.getTestMode();

        // assert
        assertEquals(testMode, expectedTestMode);
    }

    @SmallTest
    public void testDisableTestMode1 () {
        // given
        session.disableTestMode();

        // when
        boolean expectedTestMode = false;

        // then
        boolean testMode = session.getTestMode();

        // assert
        assertEquals(testMode, expectedTestMode);
    }

    @SmallTest
    public void testDisableTestMode2 () {
        // given
        session.setTestMode(false);

        // when
        boolean expectedTestMode = false;

        // then
        boolean testMode = session.getTestMode();

        // assert
        assertEquals(testMode, expectedTestMode);
    }

    @SmallTest
    public void testVersion () {
        // given
        session.setVersion("3.2.1");

        // when
        String expectedVersion = "3.2.1";

        // then
        String  version = session.getVersion();

        // assert
        assertEquals(version, expectedVersion);
    }

    @SmallTest
    public void testValues1 () {
        // give
        session = new SASession(getContext());
        session.setConfigurationProduction();
        session.disableTestMode();
        session.setVersion("3.2.1");

        // when
        SAConfiguration expectedConfig = SAConfiguration.PRODUCTION;
        String expectedBaseUrl = TEST_PRODUCTION_URL;
        boolean expectedTestMode = false;
        String expectedVersion = "3.2.1";
        String expectedPackageName = "superawesome.tv.sasessiondemo";
        String expectedAppName = "SASessionDemo";
        String expectedLang = "en_US";
        int expectedConnectionType = SAUtils.SAConnectionType.wifi.ordinal();

        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi = (double)width/(double)dens;
        double hi = (double)height/(double)dens;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi, 2);
        double screenInches = Math.sqrt(x + y);
        String expectedDevice = screenInches < 6 ? "phone" : "tablet";

        // then
        SAConfiguration configuration = session.getConfiguration();
        String baseUrl = session.getBaseUrl();
        boolean testMode = session.getTestMode();
        String version = session.getVersion();
        String packageName = session.getPackageName();
        String appName = session.getAppName();
        String lang = session.getLang();
        int connectionType = session.getConnectionType().ordinal();
        String device = session.getDevice();
        int cachebuster = session.getCachebuster();
        String userAgent = session.getUserAgent();
        int dauID = session.getDauId();

        // assert
        assertEquals(configuration, expectedConfig);
        assertEquals(baseUrl, expectedBaseUrl);
        assertEquals(testMode, expectedTestMode);
        assertEquals(version, expectedVersion);
        assertEquals(packageName, expectedPackageName);
        assertEquals(appName, expectedAppName);
        assertEquals(lang, expectedLang);
        assertEquals(connectionType, expectedConnectionType);
        assertEquals(device, expectedDevice);
        assertTrue(cachebuster > 0);
        assertNotNull(userAgent);

    }

    @SmallTest
    public void testValues2 () {
        // give
        session = new SASession(null);
        session.setConfigurationProduction();
        session.disableTestMode();
        session.setVersion("3.2.1");

        // when
        SAConfiguration expectedConfig = SAConfiguration.PRODUCTION;
        String expectedBaseUrl = TEST_PRODUCTION_URL;
        boolean expectedTestMode = false;
        String expectedVersion = "3.2.1";
        String expectedPackageName = "unknown";
        String expectedAppName = "unknown";
        String expectedLang = "en_US";
        int expectedConnectionType = SAUtils.SAConnectionType.unknown.ordinal();

        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi = (double)width/(double)dens;
        double hi = (double)height/(double)dens;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi, 2);
        double screenInches = Math.sqrt(x + y);
        String expectedDevice = screenInches < 6 ? "phone" : "tablet";

        // then
        SAConfiguration configuration = session.getConfiguration();
        String baseUrl = session.getBaseUrl();
        boolean testMode = session.getTestMode();
        String version = session.getVersion();
        String packageName = session.getPackageName();
        String appName = session.getAppName();
        String lang = session.getLang();
        int connectionType = session.getConnectionType().ordinal();
        String device = session.getDevice();
        int cachebuster = session.getCachebuster();
        String userAgent = session.getUserAgent();
        int dauID = session.getDauId();

        // assert
        assertEquals(configuration, expectedConfig);
        assertEquals(baseUrl, expectedBaseUrl);
        assertEquals(testMode, expectedTestMode);
        assertEquals(version, expectedVersion);
        assertEquals(packageName, expectedPackageName);
        assertEquals(appName, expectedAppName);
        assertEquals(lang, expectedLang);
        assertEquals(connectionType, expectedConnectionType);
        assertEquals(device, expectedDevice);
        assertTrue(cachebuster > 0);
        assertNotNull(userAgent);

    }
}