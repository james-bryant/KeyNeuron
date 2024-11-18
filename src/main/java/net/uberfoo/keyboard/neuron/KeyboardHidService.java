package net.uberfoo.keyboard.neuron;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesSpecification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class KeyboardHidService {

    public static final int PACKET_SIZE = 32;

    private static final byte DEV_USAGE = 0x61;
    private static final int DEV_USAGE_PAGE = 0xFFFFFF60;
    private static final int DEV_INF_NUM = 0x0001;

    private static final int VENDOR_ID = 0x3434;  // Replace with your VID
    private static final int PRODUCT_ID = 0x0850; // Replace with your PID

    private final KeymapService keymapService;

    @Getter
    private final List<KeyboardHid> keyboardHids;

    public KeyboardHidService(KeymapService keymapService) {
        this.keymapService = keymapService;

        var devices = enumerateDevices();

        keyboardHids = devices.stream()
                .filter(x -> keymapService.getKeyboard(x.getVendorId(), x.getProductId()) != null)
                .filter(x -> x.getUsagePage() == DEV_USAGE_PAGE)
                .filter(x -> x.getUsage() == DEV_USAGE)
                .filter(x -> x.getInterfaceNumber() == DEV_INF_NUM)
                .map(x -> new KeyboardHid(x, this.keymapService.getKeyboard(x.getVendorId(), x.getProductId())))
                .toList();

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
