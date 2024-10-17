package net.uberfoo.keyboard.neuron;

import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesSpecification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeyboardHidService {

    public static final int PACKET_SIZE = 32;
    private static final int VENDOR_ID = 0x3434;  // Replace with your VID
    private static final int PRODUCT_ID = 0x0850; // Replace with your PID

    public static long getCombined(int vid, int pid) {
        return (long)vid << 16 + (long)pid;
    }

    public void sendPerKeyColors(Map<Integer, int[]> keyColorMap) {

        HidServices hidServices = HidManager.getHidServices();

        HidDevice hidDevice = hidServices.getHidDevice(VENDOR_ID, PRODUCT_ID, null);
        System.out.println("HidDevice: " + hidDevice);

        if (hidDevice != null) {
            if (hidDevice.isClosed()) {
                hidDevice.open();
            }

            if (!hidDevice.isClosed()) {
                int MAX_KEYS_PER_PACKET = (PACKET_SIZE - 2) / 4;
                List<Integer> keys = new ArrayList<>(keyColorMap.keySet());

                for (int i = 0; i < keys.size(); i += MAX_KEYS_PER_PACKET) {
                    int numKeys = Math.min(MAX_KEYS_PER_PACKET, keys.size() - i);
                    byte[] data = new byte[PACKET_SIZE];
                    data[0] = (byte) 0xB0; // Command Indicator
                    data[1] = (byte) numKeys;

                    int idx = 2;
                    for (int j = 0; j < numKeys; j++) {
                        int keyIndex = keys.get(i + j);
                        int[] rgb = keyColorMap.get(keyIndex);

                        data[idx++] = (byte) keyIndex;
                        data[idx++] = (byte) rgb[0]; // Red
                        data[idx++] = (byte) rgb[1]; // Green
                        data[idx++] = (byte) rgb[2]; // Blue
                    }

                    // Send data packet
                    int bytesWritten = hidDevice.write(data, data.length, (byte) 0x00);
                    if (bytesWritten >= 0) {
                        System.out.println("Data packet sent successfully.");
                    } else {
                        System.err.println("Failed to send data packet.");
                    }

                    // Small delay between packets
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                // Close the device when done
                hidDevice.close();
            } else {
                System.err.println("Failed to open HID device.");
            }
        } else {
            System.err.println("HID device not found.");
        }

    }

    public List<HidDevice> enumerateDevices() {
        // Create a HID services object
        HidServices hidServices = HidManager.getHidServices();
        // Start the HID services
        hidServices.start();

        var devices = hidServices.getAttachedHidDevices();

        // Shutdown HID services
        hidServices.shutdown();

        return devices;
    }
}
