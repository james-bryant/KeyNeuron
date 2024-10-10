package net.uberfoo.keyboard.neuron.model;

import lombok.Data;
import java.util.List;

@Data
public class ContentItem {
    private String label;
    private String type;
    private String showIf;
    private Object content; // Can be a List<ContentItem> or List<Object>
    private List<Object> options; // Can be List<Integer> or List<List<Object>>
}
