module net.uberfoo.keyboard.neuron.keyneuron {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.prefs;
    requires hid4java;
    requires com.fasterxml.jackson.databind;
    requires lombok;

    opens net.uberfoo.keyboard.neuron to javafx.fxml;
    exports net.uberfoo.keyboard.neuron;
    exports net.uberfoo.keyboard.neuron.model;
}