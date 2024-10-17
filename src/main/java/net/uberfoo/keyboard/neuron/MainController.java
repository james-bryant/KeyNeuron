package net.uberfoo.keyboard.neuron;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import net.uberfoo.keyboard.neuron.model.Keyboard;
import org.hid4java.HidDevice;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class MainController {

    private static final KeyboardHidService keyboardHidService = new KeyboardHidService();
    private static final KeymapService keymapService = new KeymapService();

    private final ObservableList<Keyboard> keyboards = FXCollections.observableArrayList();

    @FXML
    public ChoiceBox<HidDevice> usbDeviceChooser;

    @FXML
    private VBox vBox;

    @FXML
    private AutoCompleteTextField<Keyboard> keyboardChooser;

    @FXML
    private ButtonGrid buttonGrid;

    @FXML
    void initialize() {

        var keys = Map.of(1, new int[]{ 0, 255, 0});
        keyboardHidService.sendPerKeyColors(keys);

        usbDeviceChooser.setConverter(new StringConverter<>() {

            @Override
            public String toString(HidDevice device) {
                if (device == null) return "";
                return String.format("%s (V:0x%04X P:0x%04X)",
                        device.getProduct(), device.getVendorId(), device.getProductId());
            }

            @Override
            public HidDevice fromString(String string) {
                return null;
            }
        });

        usbDeviceChooser.getItems().setAll(keyboardHidService.enumerateDevices());

        // Add listener for selection changes
        usbDeviceChooser.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Selected device: " + newValue);
                // Perform actions with selectedDevice
            }
        });

        try {
            var envPath = System.getenv("KEYBOARDS_HOME");
            var path = Path.of(envPath);
            var list = keymapService.getKeyboards(path);
            System.out.println(list.size());
            keyboards.addAll(list);
            keyboardChooser.getItems().setAll(list);
        } catch (IOException e) {
            AlertDialogs.unexpectedAlert(vBox.getScene().getWindow(), e);
            return;
        }

        keyboardChooser.getItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                buttonGrid.setData(newValue.getLayouts().getKeymap());
                vBox.setMinWidth(buttonGrid.getPrefWidth() + 10);
                vBox.setMinHeight(buttonGrid.getPrefHeight() + 5 + keyboardChooser.getHeight());
                vBox.getScene().getWindow().sizeToScene();
            }
        });

    }
}