package net.uberfoo.keyboard.neuron.model;

import lombok.Data;
import java.util.List;

@Data
public class Device {
    private String name;
    private String vendorId;
    private String productId;
    private List<String> keycodes;
    private List<Menu> menus;
    private List<CustomKeycode> customKeycodes;
    private Matrix matrix;
    private Layouts layouts;
}
