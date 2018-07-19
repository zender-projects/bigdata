package common.constant;

/**
 * Flag of enable or disable.
 * enable - 1
 * disable - 0
 *
 * @author mac
 * */
public enum Flag {

    ENABLE(0),DISABLE(1);

    private int value;
    Flag(int value) {
        this.value = value;
    }
}
