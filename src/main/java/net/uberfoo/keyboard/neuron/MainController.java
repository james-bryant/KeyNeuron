package net.uberfoo.keyboard.neuron;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import net.uberfoo.keyboard.neuron.model.Keyboard;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

public class MainController {

    private static final KeyboardHidService KEYBOARD_HID_SERVICE = new KeyboardHidService();
    private static final KeymapService keymapService = new KeymapService();

    private final ObservableList<Keyboard> keyboards = FXCollections.observableArrayList();

    @FXML
    private VBox vBox;

    @FXML
    private AutoCompleteTextField<Keyboard> keyboardChooser;

    @FXML
    private ButtonGrid buttonGrid;

    @FXML
    void initialize() {

        try {
            var path = Path.of("C:\\Users\\james\\workspace\\keyboards\\src");
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