package Eren.hal.Components;

import Eren.hal.Interpreter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This enum represents all possible instructions for the HAL.
 */
public enum InstructionType {
    START(false),
    STOP(false),
    IN, OUT, SYSOUT, LOAD, LOADNUM, STORE, JUMPNEG, JUMPPOS, JUMPNULL, JUMP,
    ADD, ADDNUM, SUB, SUBNUM, MUL, MULNUM, DIV, DIVNUM,
    LOADIND, STOREIND;

    final boolean hasOperand;

    InstructionType(boolean hasOperand) {
        this.hasOperand = hasOperand;
    }

    InstructionType() {
        this.hasOperand = true;
    }

    public static void runInstruction(InstructionType type, Float operand, RAM ram, HALThread runningFrom) {

        boolean debug = Interpreter.DEBUG;
        int acc = Interpreter.ACC_ADDRESS;

        switch (type) {

            case START -> {
                if (debug) System.out.println("Starting Program");
            }

            case STOP -> {
                if (debug) System.out.println("Stopping programm");
                LocalTime endDate = LocalTime.now();

                Duration diff = Duration.between(runningFrom.getHalInterpreter().getStartDate(), endDate);
                long tmp = diff.toSeconds();

                long h = tmp / 3600;
                tmp -= h*3600;

                long m = tmp / 60;
                tmp -= m*60;

                long s = tmp;

                System.out.println("Runtime: " + h + "h " + m + "m " + s + "s");

                System.exit(0);
            }

            case IN -> {
                if (operand == null) { // Backwards compatability
                    Scanner s = new Scanner(System.in);
                    System.out.print("Awaiting user input: ");
                    try {
                        float input = s.nextFloat();
                        ram.write(acc, input);
                    } catch (InputMismatchException e) {
                        System.err.println("The given input is illegal! Please only input numbers!");
                        System.exit(0);
                    }
                    return;
                }
                ram.write(acc, runningFrom.getRBuffer(operand.intValue()).get());
            }

            case OUT -> {
                if (operand == null) { System.out.println("Output: " + ram.read(acc)); return; } // Backwards compatability
                runningFrom.getSBuffer(operand.intValue()).put(ram.read(acc));
            }

            case SYSOUT -> System.out.println("Output: " + ram.read(acc));

            case LOAD -> ram.write(acc, ram.read(operand.intValue()));

            case LOADNUM -> ram.write(acc, operand);

            case STORE -> ram.write(operand.intValue(), ram.read(acc));

            case JUMPNEG -> {
                if (ram.read(acc) < 0)
                    ram.write(Interpreter.PC_ADDRESS, operand.intValue());
            }

            case JUMPPOS -> {
                if (ram.read(acc) > 0) {
                    ram.write(Interpreter.PC_ADDRESS, operand.intValue());
                }
            }

            case JUMPNULL -> {
                if (ram.read(acc) == 0)
                    ram.write(Interpreter.PC_ADDRESS, operand.intValue());
            }

            case JUMP -> ram.write(Interpreter.PC_ADDRESS, operand.intValue());

            case ADD -> ram.write(acc, ram.read(acc) + ram.read(operand.intValue()));

            case ADDNUM -> ram.write(acc, ram.read(acc)+operand);

            case SUB -> ram.write(acc, ram.read(acc)-ram.read(operand.intValue()));

            case SUBNUM -> ram.write(acc, ram.read(acc)-operand);

            case MUL -> ram.write(acc, ram.read(acc)*ram.read(operand.intValue()));

            case MULNUM -> ram.write(acc, ram.read(acc)*operand);

            case DIV -> ram.write(acc, ram.read(acc)/ram.read(operand.intValue()));

            case DIVNUM -> ram.write(acc, ram.read(acc)/operand);

            case LOADIND -> ram.write(acc, ram.read(ram.read(operand.intValue()).intValue()));

            case STOREIND -> ram.write(ram.read(operand.intValue()).intValue(), ram.read(acc));
        }

    }
}
