package net.uberfoo.keyboard.neuron.model;

import lombok.Data;

@Data
public class MenuLabel implements MenuItem {
    private String label;

    // Constructor for deserialization
    public MenuLabel(String label) {
        this.label = label;
    }

    // Default constructor for Jackson
    public MenuLabel() {}
}
