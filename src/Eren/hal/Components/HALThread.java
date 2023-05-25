package Eren.hal.Components;

import Eren.hal.Interpreter;
import Eren.hal.IO.FileManager;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a HALThread. It is a thread that runs a HAL program.
 * It has a reference to the interpreter, the sending and receiving buffers.
 */
public class HALThread extends Thread {

    private final Interpreter halInterpreter;
    private final String id;
    private Map<Integer, Buffer> sendingBuffers;
    private Map<Integer, Buffer> receivingBuffers;

    public HALThread(String id, String path) {
        this.id = id;
        halInterpreter = new Interpreter(path, false);
        // Initialize buffers
        // Tuning: 2 buffers per port
        sendingBuffers = new HashMap<>();
        receivingBuffers = new HashMap<>();
    }
    /**
     * This method adds a buffer to the receiving buffers map.
     * @param port The port to add the buffer to.
     * @param b The buffer to add.
     */
    public void addRBuffer(int port, Buffer b) {
        receivingBuffers.put(port, b);
    }
/**
     * This method adds a buffer to the sending buffers map.
     * @param port The port to add the buffer to.
     * @param b The buffer to add.
     */
    public void addSBuffer(int port, Buffer b) {
        sendingBuffers.put(port, b);
    }

    public Buffer getRBuffer(int port) { return receivingBuffers.get(port); }

    public Buffer getSBuffer(int port) { return sendingBuffers.get(port); }

    public Interpreter getHalInterpreter() { return halInterpreter; }

    //run the thread
    @Override
    public void run() {
        // super calls the run method of the Thread class
        super.run();

        while (true) {
            /*
            * Get the instruction at the PC address
            * Increment the PC address
            * Ram is a singleton, so we can get the instance from the interpreter
            * Run the instruction
             */
            FileManager manager = halInterpreter.getManager();
            RAM ram = halInterpreter.getRam();
            Instruction i = manager.getInstruction(ram.read(Interpreter.PC_ADDRESS).intValue()); // Get instruction
            ram.write(Interpreter.PC_ADDRESS, ram.read(Interpreter.PC_ADDRESS) + 1); // PC = PC + 1
            if (i == null) {
                if (Interpreter.DEBUG) System.out.println("[DEBUG] Line " + (ram.read(Interpreter.PC_ADDRESS)-1) + " not found, skipping!");
                continue;
            }
            try {
                /*
                * Get the instruction type from the instruction name
                * Run the instruction
                * If debug is enabled, print the instruction
                * If the instruction is invalid, print an error and exit
                 */
                InstructionType type = InstructionType.valueOf(i.getName());
                if (Interpreter.DEBUG) Interpreter.debugOutput(i, ram);
                InstructionType.runInstruction(type, i.getOperand(), ram, this);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid instruction: " + i.getName() + " at line " + i.getLineNumber());
                System.exit(0);
            }
        }
    }
}