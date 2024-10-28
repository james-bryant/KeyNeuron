package net.uberfoo.keyboard.neuron;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class MainController {

    private final KeymapService keymapService;
    private final KeyboardHidService keyboardHidService;

    private final ObservableList<KeyboardHid> keyboards = FXCollections.observableArrayList();

    @FXML
    public ChoiceBox<KeyboardHid> usbDeviceChooser;

    @FXML
    private VBox vBox;

    @FXML
    private ButtonGrid buttonGrid;

    public MainController() throws IOException {
        var envPath = System.getenv("KEYBOARDS_HOME");
        var path = Path.of(envPath);
        keymapService = new KeymapService(path);
        keyboardHidService = new KeyboardHidService(keymapService);
    }

    @FXML
    void initialize() {

        var keys = Map.of(1, new int[]{ 0, 255, 0});
        keyboardHidService.sendPerKeyColors(keys);

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
                // Perform actions with selectedDevice
            }
        });

        var list = keyboardHidService.getKeyboardHids();
        System.out.println(list.size());
        keyboards.addAll(list);

        usbDeviceChooser.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                buttonGrid.setData(newValue.getKeyboard().getLayouts().getKeymap(), newValue);
                vBox.setMinWidth(buttonGrid.getPrefWidth() + 10);
                vBox.setMinHeight(buttonGrid.getPrefHeight() + 5 + usbDeviceChooser.getHeight());
                vBox.getScene().getWindow().sizeToScene();
                newValue.sendRGBMatrixMode(12);
                newValue.sendPerKeyColors(
                        Map.of(
                                18, new int[]{255,0,0},
                                55, new int[]{0,255,0},
                                99, new int[]{0,0,255}));
            }
        });

    }
}