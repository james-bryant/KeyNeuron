package net.uberfoo.keyboard.neuron;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.uberfoo.keyboard.neuron.model.Autocompletable;

public class AutoCompleteTextField<T extends Autocompletable> extends TextField {

    @Getter
    private final ObservableList<T> items = FXCollections.observableArrayList();

    @Getter
    private final ObjectProperty<T> itemProperty = new SimpleObjectProperty<>();

    private ListView<String> suggestionListView;
    private Popup suggestionPopup;

    @Setter
    private int maxSuggestions = 10;

    public AutoCompleteTextField() {
        initialize();
    }

    private void initialize() {
        suggestionListView = new ListView<>();
        suggestionListView.setPrefHeight(150);

        suggestionPopup = new Popup();
        suggestionPopup.getContent().add(suggestionListView);
        suggestionPopup.setAutoHide(true);

        textProperty().addListener((obs, oldText, newText) -> {
            if (newText == null || newText.isEmpty()) {
                //suggestionListView.getItems().clear();
                suggestionPopup.hide();
            } else {
                ObservableList<String> filteredItems =
                        FXCollections.observableList(items.stream().map(Autocompletable::getName).filter(x -> x.toLowerCase().contains(newText.toLowerCase())).toList());
                
                if (filteredItems.size() > maxSuggestions) {
                    filteredItems = FXCollections.observableArrayList(
                        filteredItems.subList(0, maxSuggestions));
                }
                
                suggestionListView.setItems(filteredItems);
                
                if (!filteredItems.isEmpty()) {
                    if (!suggestionPopup.isShowing()) {
                        showPopup();
                    }
                } else {
                    suggestionPopup.hide();
                }
            }
        });

        suggestionListView.setOnMouseClicked(event -> {
            String selectedItem = suggestionListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                setText(selectedItem);
                itemProperty.set(items.stream().filter(x -> x.getName().equals(selectedItem)).findFirst().get());
                positionCaret(getText().length());
                suggestionPopup.hide();
            }
        });

        setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DOWN) {
                suggestionListView.requestFocus();
                suggestionListView.getSelectionModel().selectFirst();
            }
        });

        suggestionListView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String selectedItem = suggestionListView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    setText(selectedItem);
                    itemProperty.set(items.stream().filter(x -> x.getName().equals(selectedItem)).findFirst().get());
                    positionCaret(getText().length());
                    suggestionPopup.hide();
                }
            } else if (event.getCode() == KeyCode.UP && 
                       suggestionListView.getSelectionModel().getSelectedIndex() == 0) {
                this.requestFocus();
            }
        });
    }

    private void showPopup() {
        Platform.runLater(() -> {
            if (!isFocused()) {
                return;
            }
            Pane parent = (Pane) getParent();
            double x = localToScene(0, 0).getX() + parent.getScene().getWindow().getX() + parent.getScene().getX();
            double y = localToScene(0, 0).getY() + parent.getScene().getWindow().getY() + parent.getScene().getY() + getHeight();
            suggestionPopup.show(this, x, y);
        });
    }

}
