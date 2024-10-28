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
        if (device.isClosed()) {
            device.open();
        }
    }

    public void sendRGBMatrixMode(int mode) {
        byte[] data = newPacket((byte)0xB1, 4);
        data[0] = (byte) 0x07;
        data[1] = (byte) 0xFF;
        data[2] = (byte) 0xB1; // Command Indicator
        data[3] = (byte) mode;

        device.write(data, data.length, (byte)0x00);
    }

    public void sendPerKeyColors(Map<Integer, int[]> keyColorMap) {

        int MAX_KEYS_PER_PACKET = (PACKET_SIZE - 4) / 4;
        List<Integer> keys = new ArrayList<>(keyColorMap.keySet());

        for (int i = 0; i < keys.size(); i += MAX_KEYS_PER_PACKET) {
            int numKeys = Math.min(MAX_KEYS_PER_PACKET, keys.size() - i);
            byte[] data = newPacket((byte)0xB0, (4 * numKeys) + 4);
            data[3] = (byte) numKeys;

            int idx = 4;
            for (int j = 0; j < numKeys; j++) {
                int keyIndex = keys.get(i + j);
                int[] rgb = keyColorMap.get(keyIndex);

                data[idx++] = (byte) keyIndex;
                data[idx++] = (byte) rgb[0]; // Red
                data[idx++] = (byte) rgb[1]; // Green
                data[idx++] = (byte) rgb[2]; // Blue
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

    }

    private byte[] newPacket(byte command, int length) {
        byte[] data = new byte[length];
        data[0] = (byte) 0x07;
        data[1] = (byte) 0xFF;
        data[2] = command;
        return data;
    }
}
