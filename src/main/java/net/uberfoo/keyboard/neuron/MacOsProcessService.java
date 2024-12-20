package net.uberfoo.keyboard.neuron;

import net.uberfoo.keyboard.neuron.model.ProcessInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MacOsProcessService extends AbstractProcessService {

    private String lastProcess = "";
    private String lastPid = "";

    @Override
    protected Runnable getRunner() {
        return () -> {
            try {
                // Prepare the AppleScript command
                String[] command = {
                        "osascript",
                        "-e",
                        "tell application \"System Events\"",
                        "-e",
                        "set frontProcess to first process whose frontmost is true",
                        "-e",
                        "get {name, unix id} of frontProcess",
                        "-e",
                        "end tell"
                };

                // Execute the command using ProcessBuilder
                var processBuilder = new ProcessBuilder(command);
                var process = processBuilder.start();

                // Read the output
                var reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String output = reader.readLine();
                process.waitFor();

                // Parse the output
                if (output != null) {
                    String[] details = output.split(", ");
                    String appName = details[0].trim();
                    String pid = details[1].trim();

                    if (!pid.equals(lastPid)) {
                        var info = new ProcessInfo(appName, appName, Integer.parseInt(pid));
                        notifyConsumer(info);
                        lastProcess = appName;
                        lastPid = pid;
                    }

                } else {
                    System.out.println("Unable to fetch details of the foreground application.");
                }
            } catch (Exception e) {
                System.out.println("Error determining foreground application details");
                e.printStackTrace();
            }

        };
    }
}
