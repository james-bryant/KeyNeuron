package net.uberfoo.keyboard.neuron;

public class ProcessServiceFactory {

    public static ProcessService createProcessService() {
        var osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return new WindowsProcessService();
        } else if (osName.contains("mac")) {
            return new MacOsProcessService();
        }
        throw new UnsupportedOperationException("Unsupported OS: " + osName);
    }
}
