package tv.superawesome.lib.sasession.defines;

/**
 * Created by gabriel.coman on 03/05/2018.
 */
public enum SARTBPosition {
    ABOVE_THE_FOLD(1),
    FULLSCREEN(7);

    private final int value;

    SARTBPosition(int value) {
        this.value = value;
    }

    public static SARTBPosition fromValue (int pos) {
        switch (pos) {
            case 1: return ABOVE_THE_FOLD;
            default: return FULLSCREEN;
        }
    }

    public int getValue() {
        return value;
    }
}
