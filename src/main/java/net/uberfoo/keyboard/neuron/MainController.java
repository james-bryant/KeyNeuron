package net.uberfoo.keyboard.neuron;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;

public class MainController {

    private final KeyboardHidService keyboardHidService;

    private final ObservableList<KeyboardHid> keyboards = FXCollections.observableArrayList();

    @FXML
    public ChoiceBox<KeyboardHid> usbDeviceChooser;

    @FXML
    private VBox vBox;

    @FXML
    private ButtonGrid buttonGrid;

    private KeyboardHid selectedDevice;

    public MainController() throws IOException {
        var envPath = System.getenv("KEYBOARDS_HOME");
        var path = Path.of(envPath);
        KeymapService keymapService = new KeymapService(path);
        keyboardHidService = new KeyboardHidService(keymapService);
    }

    @FXML
    void initialize() {

        usbDeviceChooser.setConverter(new StringConverter<>() {

            @Override
            public String toString(KeyboardHid device) {
                if (device == null) return "";
                return String.format("%s (V:0x%04X P:0x%04X)",
                        device.getDevice().getProduct(),
                        device.getDevice().getVendorId(),
                        device.getDevice().getProductId());
            }

            @Override
            public KeyboardHid fromString(String string) {
                return null;
            }
        });

        usbDeviceChooser.getItems().setAll(keyboardHidService.getKeyboardHids());

        // Add listener for selection changes
        usbDeviceChooser.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Selected device: " + newValue);
                selectedDevice = newValue;
            }
        });

        var list = keyboardHidService.getKeyboardHids();
        keyboards.addAll(list);

        usbDeviceChooser.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                buttonGrid.setData(newValue.getKeyboard().getLayouts().getKeymap(), newValue);
                vBox.setMinWidth(buttonGrid.getPrefWidth() + 10);
                vBox.setMinHeight(buttonGrid.getPrefHeight() + 5 + usbDeviceChooser.getHeight());
                vBox.getScene().getWindow().sizeToScene();
                newValue.sendRGBMatrixMode(12);
            }
        });

        buttonGrid.setOnButtonPressed(keyData -> {
            System.out.println("Key pressed: " + keyData.label);
            var coords = keyData.label.lines().findFirst();
            if (coords.isPresent()) {
                var xy = Arrays.stream(coords.get().split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .toArray(Integer[]::new);
                selectedDevice.sendPerKeyColors(Map.of(xy[0], Map.of(xy[1], new int[]{255, 0, 0})));
            }
        });
    }
}