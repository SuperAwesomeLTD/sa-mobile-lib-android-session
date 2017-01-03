package tv.superawesome.lib.sasession;

/**
 * Created by gabriel.coman on 15/09/16.
 */
public enum SAConfiguration {
    PRODUCTION(0),
    STAGING(1);

    private final int value;
    SAConfiguration(int value) {
        this.value = value;
    }

    public static SAConfiguration fromValue (int value) {
        if (value == 0) return PRODUCTION;
        return STAGING;
    }

    public int getValue() {
        return value;
    }
}
