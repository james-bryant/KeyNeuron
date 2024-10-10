package net.uberfoo.keyboard.neuron.model;

import lombok.Data;
import java.util.List;

@Data
public class Menu implements MenuItem {
    private String label;
    private List<MenuContent> content;
}
