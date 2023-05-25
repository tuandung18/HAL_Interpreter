package Eren.hal;

import Eren.hal.IO.FileManager;
import Eren.hal.Components.HALThread;
import Eren.hal.Components.Instruction;
import Eren.hal.Components.RAM;

import java.time.LocalTime;

public class Interpreter {

    public static boolean DEBUG = false;
    public static final int PC_ADDRESS = 0;
    public static final int ACC_ADDRESS = 1;

    public LocalTime startDate;

    private final RAM ram;

    FileManager manager;

    public FileManager getManager() {
        return manager;
    }

    public RAM getRam() {
        return ram;
    }

    public LocalTime getStartDate() {
        return startDate;
    }

    public static void main(String[] args) {

//        System.out.println("Running only one hal!");

        String programFileArgument = "";
        boolean isDebug = false;

        if (args.length == 0) {
            System.err.println("Please specify a File to run!");
            System.exit(0);
        } else if (args.length == 1) {
            programFileArgument = args[0];
        } else if (args.length == 2) {
            // Check if debug flag is set
            if (args[0].equals("-d") || args[0].equals("--debug")) {
                isDebug = true;
            }
            programFileArgument = args[1];
        } else {
            System.err.println("Unknown argument configuration. Usage: HALInterpreter [-d --debug] file/path.txt");
            System.exit(0);
        }

        System.out.println("Starting thread");

        HALThread t = new HALThread("0", programFileArgument);
        t.start();
    }

    /**
     * Creates a new Interpreter instance
     * @param path The path to the program file
     * @param debug Whether to enable debug mode or not
     */
    public Interpreter(String path, boolean debug) {
        startDate = LocalTime.now();
        DEBUG = debug;
        ram = new RAM(0xFF);
        ram.write(0, 0); // PC Setup
        manager = new FileManager(path);
    }

    /**
        * Prints a debug message to the console
        * @param i The instruction to print
        * @param ram The RAM to read from
        */
    public static void debugOutput(Instruction i, RAM ram) {
        System.out.print("[DEBUG]: Executing " + i.getName() + " ");
        if (i.hasOperand()) {
            System.out.print("with operand " + i.getOperand());
            if (!i.getName().equals("JUMP")) {
                Float f = ram.read(i.getOperand().intValue());
                if (f != null) System.out.println(" with value at [" + i.getOperand() + "] = " + f);
            }
        }
        else System.out.println();

    }

}
