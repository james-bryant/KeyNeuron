package net.uberfoo.keyboard.neuron.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.DEDUCTION, // Automatically deduce the type based on the fields present
    defaultImpl = MenuLabel.class // Default to MenuLabel if no other type matches
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Menu.class, name = "Menu"),
    @JsonSubTypes.Type(value = MenuLabel.class, name = "MenuLabel")
})
public interface MenuItem {
}
