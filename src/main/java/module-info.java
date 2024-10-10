module net.uberfoo.keyboard.neuron.keyneuron {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.prefs;
    requires hid4java;
    requires com.fasterxml.jackson.databind;

    opens net.uberfoo.keyboard.neuron to javafx.fxml;
    exports net.uberfoo.keyboard.neuron;
}