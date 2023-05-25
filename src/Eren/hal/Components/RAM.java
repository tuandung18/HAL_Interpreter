package Eren.hal.Components;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a RAM. It is a memory unit that stores values.
 * It has a write and a read method.
 * It has a map of registers.
 */
public class RAM {

        private final Map<Integer, Register> ramRegisters;

        public RAM(int size) {
            ramRegisters = new HashMap<>();

            for (int i = 0; i < size; i++) {
                ramRegisters.put(i, new Register());
            }
        }

    /**
     * This method writes a value to a register.
     * @param address The address of the register.
     * @param value The value to write.
     */
        public void write(int address, float value) {
            try {
                ramRegisters.get(address).setValue(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    /**
     * This method reads a value from a register.
     * @param address The address of the register.
     * @return The value read.
     */
        public Float read(int address) {
            try {
                return ramRegisters.get(address).getValue();
            } catch (Exception e) {
                return null;
            }
        }


    }
