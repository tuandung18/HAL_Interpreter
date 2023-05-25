package Eren.hal.Components;
/**
 * This class represents a HAL Instruction.
 * An instruction contains a line number, an instruction name and an optional operand.
 */
public class Instruction {

    final private String name;
    final private Float operand;
    final private boolean hasOperand;
    final int lineNumber;

    /**
     * This constructor is used to create an instruction object.
     * @param instructionLine
     */
    public Instruction(String instructionLine) {
        String[] parts = instructionLine.split( " ");
        int size = parts.length;
        lineNumber = Integer.parseInt(parts[0]);
        name = parts[1];
        if (size == 3) {
            operand = Float.parseFloat(parts[2]);
            hasOperand = true;
        } else {
            operand = null;
            hasOperand = false;
        }
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getName() {
        return name;
    }

    public Float getOperand() {
        return operand;
    }

    public boolean hasOperand() {
        return hasOperand;
    }
}