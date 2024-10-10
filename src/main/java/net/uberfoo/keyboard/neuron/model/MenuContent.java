package net.uberfoo.keyboard.neuron.model;

import lombok.Data;
import java.util.List;

@Data
public class MenuContent {
    private String label;
    private String type;
    private String showIf;
    private List<Object> options;
    private Object content; // Can be a List<MenuContent> or List<Object>
}
