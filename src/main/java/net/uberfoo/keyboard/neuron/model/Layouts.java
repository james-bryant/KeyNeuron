package net.uberfoo.keyboard.neuron.model;

import lombok.Data;
import java.util.List;

@Data
public class Layouts {
    private List<List<Object>> keymap;
}
