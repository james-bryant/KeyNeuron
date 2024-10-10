package net.uberfoo.keyboard.neuron.model;

import lombok.Data;
import java.util.List;

@Data
public class Menu {
    private String label;
    private List<MenuContent> content;
}
