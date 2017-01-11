/**
 * @Copyright:   SuperAwesome Trading Limited 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */
package tv.superawesome.lib.sasession;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import tv.superawesome.lib.sanetwork.asynctask.SAAsyncTask;
import tv.superawesome.lib.sanetwork.asynctask.SAAsyncTaskInterface;
import tv.superawesome.lib.sautils.SAUtils;

/**
 * Class that abstracts away generating a distinct ID called "DAU ID", which consists of:
 *  - the Advertising ID int
 *  - a random ID
 *  - the package name
 * each hashed and then XOR-ed together
 */
public class SACapper {

    // constants
    private static final String GOOGLE_ADVERTISING_CLASS = "com.google.android.gms.ads.identifier.AdvertisingIdClient";
    private static final String GOOGLE_ADVERTISING_ID_CLASS = "com.google.android.gms.ads.identifier.AdvertisingIdClient$Info";
    private static final String GOOGLE_ADVERTISING_INFO_METHOD = "getAdvertisingIdInfo";
    private static final String GOOGLE_ADVERTISING_TRACKING_METHOD = "isLimitAdTrackingEnabled";
    private static final String GOOGLE_ADVERTISING_ID_METHOD = "getId";
    private static final String SUPER_AWESOME_FIRST_PART_DAU = "SUPER_AWESOME_FIRST_PART_DAU";

    // private current context
    private Context context = null;

    /**
     * Main constructor for the capper, which takes the current context as paramter
     *
     * @param context the current context (activity or fragment)
     */
    public SACapper (Context context) {
        this.context = context;
    }

    /**
     * Main capper method that takes an SACapperInterface interface instance as parameter, to be
     * able to sent back the generated ID when the async operation finishes
     *
     * @param listener1 an instance of the SACapperInterface
     */
    public void getDauID(final SACapperInterface listener1) {

        // get a listener that's never going to be null
        final SACapperInterface listener = listener1 != null ? listener1 : new SACapperInterface() {@Override public void didFindDAUID(int dauID) {}};

        // guard against this class not being available or th context being null
        if (!SAUtils.isClassAvailable(GOOGLE_ADVERTISING_CLASS) || context == null) {
            listener.didFindDAUID(0);
            return;
        }

        new SAAsyncTask<>(context, new SAAsyncTaskInterface<String>() {
            /**
             * Overridden method that describes the task to be executed. In this case, we're
             * using reflection to async pull the Google Advertising ID string
             *
             * @return           either an advertising String or an empty string
             * @throws Exception any type of exception thrown by the google code
             */
            @Override
            public String taskToExecute() throws Exception {

                Class<?> advertisingIdClass = Class.forName(GOOGLE_ADVERTISING_CLASS);
                java.lang.reflect.Method getAdvertisingIdInfo = advertisingIdClass.getMethod(GOOGLE_ADVERTISING_INFO_METHOD, Context.class);
                Object adInfo = getAdvertisingIdInfo.invoke(advertisingIdClass, context);

                Class<?> advertisingIdInfoClass = Class.forName(GOOGLE_ADVERTISING_ID_CLASS);

                java.lang.reflect.Method isLimitAdTrackingEnabled = advertisingIdInfoClass.getMethod(GOOGLE_ADVERTISING_TRACKING_METHOD);
                java.lang.reflect.Method getId = advertisingIdInfoClass.getMethod(GOOGLE_ADVERTISING_ID_METHOD);

                Boolean isEnabled = (Boolean) isLimitAdTrackingEnabled.invoke(adInfo);
                return !isEnabled ? (String) getId.invoke(adInfo) : "";
            }

            /**
             * When the operation finishes, there should be a valid (or empty) first part of the
             * DAU ID string, corresponding to the Google Advertising ID
             *
             * @param firstPartOfDAU the Google Advertising ID
             */
            @Override
            public void onFinish(String firstPartOfDAU) {

                if (firstPartOfDAU != null && !firstPartOfDAU.isEmpty()) {

                    // continue as if user has Ad Tracking enabled and all
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

                    // get the second part of the DAU ID
                    String secondPartOfDAU = preferences.getString(SUPER_AWESOME_FIRST_PART_DAU, null);

                    // if the second part of the DAU ID is empty then generate & save a new one
                    if (secondPartOfDAU == null || secondPartOfDAU.isEmpty()) {
                        secondPartOfDAU = SAUtils.generateUniqueKey();
                        preferences.edit().putString(SUPER_AWESOME_FIRST_PART_DAU, secondPartOfDAU).apply();
                    }

                    // form the third part of the DAU ID as the package name
                    String thirdPartOfDau = context != null ? context.getPackageName() : "unknown";

                    // generate three hashes for the three strings
                    int hash1 = Math.abs(firstPartOfDAU.hashCode());
                    int hash2 = Math.abs(secondPartOfDAU.hashCode());
                    int hash3 = Math.abs(thirdPartOfDau.hashCode());
                    // and do a XOR on them
                    int dauID = Math.abs(hash1 ^ hash2 ^ hash3);

                    // finally call the listener to sent the DAU ID
                    listener.didFindDAUID(dauID);
                }
                // either the service is not available or the user does not have Google Play Services
                else {
                    listener.didFindDAUID(0);
                }
            }

            /**
             *
             */
            @Override
            public void onError() {
                listener.didFindDAUID(0);
            }
        });
    }
}
