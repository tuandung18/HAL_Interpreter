package Eren.hal.Components;

/**
 * This class represents a register.
 * A register is a place where data is stored temporarily.
 * A register has a value.
 */
public class Register {
    float value = 0;

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
