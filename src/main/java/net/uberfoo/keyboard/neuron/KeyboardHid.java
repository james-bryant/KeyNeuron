package net.uberfoo.keyboard.neuron;

import lombok.Getter;
import lombok.ToString;
import net.uberfoo.keyboard.neuron.model.Keyboard;
import org.hid4java.HidDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.uberfoo.keyboard.neuron.KeyboardHidService.PACKET_SIZE;

@Getter
@ToString
public class KeyboardHid {

    private final HidDevice device;
    private final Keyboard keyboard;

    public KeyboardHid(final HidDevice device, Keyboard keyboard) {
        this.device = device;
        this.keyboard = keyboard;
    }

    public void sendRGBMatrixMode(int mode) {
        byte[] data = newPacket((byte)0xB1, 4);
        data[3] = (byte) mode;

        if (device.isClosed()) {
            var open = device.open();
            System.out.printf("device open: %b%n", open);
        }
        device.write(data, data.length, (byte)0x00);
        device.close();
    }

    public void sendPerKeyColors(Map<Integer, Map<Integer, int[]>> keyColorMap) {

        int MAX_KEYS_PER_PACKET = (PACKET_SIZE - 4) / 5;

        int numKeys = keyColorMap.values().stream().mapToInt(Map::size).sum();

        if (device.isClosed()) {
            var open = device.open();
            System.out.printf("device open: %b%n", open);
        }

        new Thread(() -> {
            List<Integer> keys = new ArrayList<>(keyColorMap.keySet());

            for (int i = 0; i < numKeys; i += MAX_KEYS_PER_PACKET) {
                int numKeysInPacket = Math.min(MAX_KEYS_PER_PACKET, numKeys - i);
                byte[] data = newPacket((byte)0xB0, (5 * numKeys) + 4);
                data[3] = (byte) numKeysInPacket;

                int idx = 4;
                for (int y : keys) {
                    var row = keyColorMap.get(y);
                    for(int x : row.keySet()) {
                        int[] rgb = row.get(x);
                        data[idx++] = (byte) y; // Row
                        data[idx++] = (byte) x; // Column
                        data[idx++] = (byte) rgb[0]; // Red
                        data[idx++] = (byte) rgb[1]; // Green
                        data[idx++] = (byte) rgb[2]; // Blue
                    }
                }

                // Send data packet
                int bytesWritten = device.write(data, data.length, (byte) 0x00);
                if (bytesWritten >= data.length) {
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
            device.close();
        }).start();

    }

    private byte[] newPacket(byte command, int length) {
        byte[] data = new byte[32];
        data[0] = (byte) 0x07;
        data[1] = (byte) 0xFF;
        data[2] = command;
        return data;
    }
}
