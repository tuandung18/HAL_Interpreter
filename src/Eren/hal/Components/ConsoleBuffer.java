package Eren.hal.Components;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleBuffer extends Buffer {
    @Override
    public synchronized void put(float x) {
        System.out.println("Output: " + x);
    }

    @Override
    public synchronized float get() {
        Scanner s = new Scanner(System.in);
        System.out.print("User input: ");
        try {
            return s.nextFloat();
        } catch (InputMismatchException e) {
            System.err.println("The given input is illegal! Please only input numbers!");
            System.exit(0);
            return 0F;
        }
    }
}
