package tv.superawesome.lib.sasession;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.lang.reflect.InvocationTargetException;

import tv.superawesome.lib.sautils.SAUtils;

/**
 * Created by gabriel.coman on 21/09/16.
 */
public class SACapper {

    // constant that specifies the local-pref dict key to use
    private static final String SUPER_AWESOME_FIRST_PART_DAU = "SUPER_AWESOME_FIRST_PART_DAU";

    private Context context = null;

    public SACapper (Context context) {
        this.context = context;
    }

    /**
     * static method to enable Capping
     * Through it's listener interface it returns a dauID
     * The dauID can be non-0 -> in which case it's valid
     * or it can be 0 -> in which case it's not valid (user does not have tracking enabled or
     * gms enabled)
     **/
    public void getDauID(final SACapperInterface listener) {

        // guard against horrible errors!
        if (!SAUtils.isClassAvailable("com.google.android.gms.ads.identifier.AdvertisingIdClient") || context == null) {
            listener.didFindDAUId(0);
            return;
        }

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                try {
                    Class<?> advertisingIdClass = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient");
                    java.lang.reflect.Method getAdvertisingIdInfo = advertisingIdClass.getMethod("getAdvertisingIdInfo", Context.class);
                    Object adInfo = getAdvertisingIdInfo.invoke(advertisingIdClass, context);

                    Class<?> advertisingIdInfoClass = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient$Info");

                    java.lang.reflect.Method isLimitAdTrackingEnabled = advertisingIdInfoClass.getMethod("isLimitAdTrackingEnabled");
                    java.lang.reflect.Method getId = advertisingIdInfoClass.getMethod("getId");

                    Boolean isEnabled = (Boolean) isLimitAdTrackingEnabled.invoke(adInfo);
                    if (!isEnabled) {
                        return (String) getId.invoke(adInfo);
                    }
                } catch (ClassNotFoundException e) {
                    return "";
                } catch (NoSuchMethodException e) {
                    return "";
                } catch (InvocationTargetException e) {
                    return "";
                } catch (IllegalAccessException e) {
                    return "";
                }

                return "";
            }

            @Override
            protected void onPostExecute(String firstPartOfDAU) {
                super.onPostExecute(firstPartOfDAU);

                if (firstPartOfDAU != null && !firstPartOfDAU.isEmpty()){

                    // continue as if  user has Ad Tracking enabled and all
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

                    // create the dauID
                    String secondPartOfDAU = preferences.getString(SUPER_AWESOME_FIRST_PART_DAU, null);

                    // third part of dauid
                    String thirdPartOfDau = context != null ? context.getPackageName() : "unknown";

                    if (secondPartOfDAU == null || secondPartOfDAU.isEmpty()) {
                        SharedPreferences.Editor editor = preferences.edit();
                        secondPartOfDAU = SAUtils.generateUniqueKey();
                        editor.putString(SUPER_AWESOME_FIRST_PART_DAU, secondPartOfDAU);
                        editor.apply();
                    }

                    int hash1 = Math.abs(firstPartOfDAU.hashCode());
                    int hash2 = Math.abs(secondPartOfDAU.hashCode());
                    int hash3 = Math.abs(thirdPartOfDau.hashCode());
                    int dauHash = Math.abs(hash1 ^ hash2 ^ hash3);

                    if (listener != null){
                        listener.didFindDAUId(dauHash);
                    }
                }
                // either the service is not available or the user does not have Google Play Services
                else {
                    if (listener != null) {
                        listener.didFindDAUId(0);
                    }
                }
            }
        };
        task.execute();
    }

}
