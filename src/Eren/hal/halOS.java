
package Eren.hal;

import Eren.hal.Components.Buffer;
import Eren.hal.Components.ConsoleBuffer;
import Eren.hal.Components.HALThread;
import org.json.JSONArray;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class halOS {
    private static Map<String, HALThread> threads;

    public static void main(String[] args) throws IOException {
        // create maps of threads
        threads = new HashMap<>();
        // parse from json file
        if (args[0].endsWith(".json"))
            parseJSONFile(args[0]);

        threads.forEach((s, halThread) -> {
            // start threads
            halThread.start();
        });
    }

    /**
     * Parses a JSON file and creates the HALThreads and their connections
     * @param filePath the path to the JSON file
     * @throws IOException if the file could not be read
     */

    private static void parseJSONFile(String filePath) throws IOException {
        File jsonFile = new File(filePath);
        String content = Files.readString(jsonFile.toPath(), StandardCharsets.US_ASCII);
        JSONObject root = new JSONObject(content);
        // parse hal threads
        JSONArray hals = root.getJSONArray("hal");
        hals.forEach(o -> {
            JSONObject hal = (JSONObject) o;
            HALThread t = new HALThread(hal.getString("id"), hal.getString("program-file"));
            threads.put(hal.getString("id"), t);
        });
        // parse connections
        JSONArray connections = root.getJSONArray("connections");
        connections.forEach(o -> {
            JSONObject conn = (JSONObject) o;
            // check if it is a console connection
            if (conn.getString("startID").equalsIgnoreCase("STDIN")) {
                threads.get(conn.getString("destID")).addRBuffer(conn.getInt("destPort"), new ConsoleBuffer());
                return;
            }
            // check if it is a console connection
            if (conn.getString("destID").equalsIgnoreCase("STDOUT")) {
                threads.get(conn.getString("startID")).addSBuffer(conn.getInt("startPort"), new ConsoleBuffer());
                return;
            }

            // Create Buffer
            Buffer b = new Buffer();

            // Add to corresponding thread
            threads.get(conn.getString("startID")).addSBuffer(conn.getInt("startPort"), b);
            threads.get(conn.getString("destID")).addRBuffer(conn.getInt("destPort"), b);
        });
    }
}
