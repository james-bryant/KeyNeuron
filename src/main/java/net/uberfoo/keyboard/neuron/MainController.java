package net.uberfoo.keyboard.neuron;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import net.uberfoo.keyboard.neuron.model.Keyboard;

import java.io.IOException;
import java.nio.file.Path;

public class MainController {

    private static final KeyboardHidService KEYBOARD_HID_SERVICE = new KeyboardHidService();
    private static final KeymapService keymapService = new KeymapService();

    private ObservableList<Keyboard> keyboards;

    @FXML
    private VBox vBox;

    @FXML
    private ChoiceBox<Keyboard> keyboardChooser;

    @FXML
    private ButtonGrid buttonGrid = new ButtonGrid();

    @FXML
    void initialize() {

        keyboardChooser.setConverter(new StringConverter<Keyboard>() {
             @Override
             public String toString(Keyboard keyboard) {
                 if (keyboard == null) {
                     return "";
                 }
                 return keyboard.getName();
             }

             @Override
             public Keyboard fromString(String string) {
                 return keyboards.stream().filter(x -> x.getName().equals(string)).findFirst().orElse(null);
             }
         });

        try {
            var path = Path.of("C:\\Users\\james\\workspace\\VIA_keymaps");
            var list = keymapService.getKeyboards(path);
            keyboards = FXCollections.observableList(list);
            keyboardChooser.setItems(keyboards);

            keyboardChooser.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                buttonGrid.setData(newValue.getLayouts().getKeymap());
                vBox.setMinWidth(buttonGrid.getPrefWidth() + 10);
                vBox.setMinHeight(buttonGrid.getPrefHeight() + 5 + keyboardChooser.getHeight());
                vBox.getScene().getWindow().sizeToScene();
            });
        } catch (IOException e) {
            AlertDialogs.unexpectedAlert(vBox.getScene().getWindow(), e);
        }
    }
}