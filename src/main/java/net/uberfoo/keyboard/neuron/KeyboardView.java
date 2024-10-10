package net.uberfoo.keyboard.neuron;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.*;
import javafx.stage.Window;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class KeyboardView extends Dialog<Boolean> {

    private static final double BUTTON_WIDTH = 160;
    private static final double BUTTON_HEIGHT = 60;

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String KEYMAP_JSON =
"""
    [
                   [
                    {
                        "c": "#777777"
                    },
                    "0, 0",
                    {
                        "x": 1,
                        "c": "#cccccc"
                    },
                    "0, 1",
                    "0, 2",
                    "0, 3",
                    "0, 4",
                    {
                        "x": 0.5,
                        "c": "#aaaaaa"
                    },
                    "0, 5",
                    "0, 6",
                    "0, 7",
                    "0, 8",
                    {
                        "x": 0.5,
                        "c": "#cccccc"
                    },
                    "0, 9",
                    "0, 10",
                    "0, 11",
                    "0, 12",
                    {
                        "x": 0.25,
                        "c": "#aaaaaa"
                    },
                    "0, 13",
                    {
                        "x": 0.25,
                        "c": "#cccccc"
                    },
                    "0, 15",
                    "0, 16",
                    "0, 17",
                    {
                        "x": 0.25,
                        "c": "#aaaaaa"
                    },
                    "0, 18\\n\\n\\n\\n\\n\\n\\n\\n\\ne0"
                  ],
                  [
                    {
                        "y": 0.25,
                        "c": "#aaaaaa"
                    },
                    "1, 0",
                    {
                        "c": "#cccccc"
                    },
                    "1, 1",
                    "1, 2",
                    "1, 3",
                    "1, 4",
                    "1, 5",
                    "1, 6",
                    "1, 7",
                    "1, 8",
                    "1, 9",
                    "1, 10",
                    "1, 11",
                    "1, 12",
                    {
                        "w": 2,
                        "c": "#aaaaaa"
                    },
                    "1, 13",
                    {
                        "x": 0.25
                    },
                    "1, 14",
                    {
                        "x": 0.25,
                        "c": "#cccccc"
                    },
                    "1, 15",
                    "1, 16",
                    "1, 17",
                    "1, 18"
                  ],
                  [
                    {
                        "w": 1.5,
                        "c": "#aaaaaa"
                    },
                    "2, 0",
                    {
                        "c": "#cccccc"
                    },
                    "2, 1",
                    "2, 2",
                    "2, 3",
                    "2, 4",
                    "2, 5",
                    "2, 6",
                    "2, 7",
                    "2, 8",
                    "2, 9",
                    "2, 10",
                    "2, 11",
                    "2, 12",
                    {
                        "w": 1.5,
                        "c": "#aaaaaa"
                    },
                    "2, 13",
                    {
                        "x": 0.25
                    },
                    "2, 14",
                    {
                        "x": 0.25,
                        "c": "#cccccc"
                    },
                    "2, 15",
                    "2, 16",
                    "2, 17",
                    {
                        "h": 2
                    },
                    "2, 18"
                  ],
                  [
                    {
                        "w": 1.75,
                        "c": "#aaaaaa"
                    },
                    "3, 0",
                    {
                        "c": "#cccccc"
                    },
                    "3, 1",
                    "3, 2",
                    "3, 3",
                    "3, 4",
                    "3, 5",
                    "3, 6",
                    "3, 7",
                    "3, 8",
                    "3, 9",
                    "3, 10",
                    "3, 11",
                    {
                        "w": 2.25,
                        "c": "#777777"
                    },
                    "3, 12",
                    {
                        "x": 0.25,
                        "c": "#aaaaaa"
                    },
                    "3, 13",
                    {
                        "x": 0.25,
                        "c": "#cccccc"
                    },
                    "3, 15",
                    "3, 16",
                    "3, 17"
                  ],
                  [
                    {
                        "w": 2.25,
                        "c": "#aaaaaa"
                    },
                    "4, 0",
                    {
                        "c": "#cccccc"
                    },
                    "4, 2",
                    "4, 3",
                    "4, 4",
                    "4, 5",
                    "4, 6",
                    "4, 7",
                    "4, 8",
                    "4, 9",
                    "4, 10",
                    "4, 11",
                    {
                        "w": 1.75,
                        "c": "#aaaaaa"
                    },
                    "4, 12",
                    {
                        "x": 0.25,
                        "y": 0.25,
                        "c": "#cccccc"
                    },
                    "4, 13",
                    {
                        "x": 1.25,
                        "y": -0.25
                    },
                    "4, 15",
                    "4, 16",
                    "4, 17",
                    {
                        "h": 2
                    },
                    "4, 18"
                  ],
                  [
                    {
                        "w": 1.25,
                        "c": "#aaaaaa"
                    },
                    "5, 0",
                    {
                        "w": 1.25
                    },
                    "5, 1",
                    {
                        "w": 1.25
                    },
                    "5, 2",
                    {
                        "w": 6.25,
                        "c": "#cccccc"
                    },
                    "5, 6",
                    {
                        "c": "#aaaaaa"
                    },
                    "5, 9",
                    "5, 10",
                    "5, 11",
                    {
                        "x": 0.25,
                        "y": 0.25,
                        "c": "#cccccc"
                    },
                    "5, 12",
                    "5, 13",
                    "5, 14",
                    {
                        "x": 0.25,
                        "y": -0.25,
                        "w": 2
                    },
                    "5, 16",
                    "5, 17"
                  ]
                ]
""";
    private List<List<?>> keymap;


    public KeyboardView(Window owner) {
        var root = new VBox(10);

        setHeight(600);
        setWidth(800);

        try {
            keymap = mapper.readValue(KEYMAP_JSON, new TypeReference<List<List<?>>>() {});
        } catch (JsonProcessingException e) {
            unexpectedAlert(owner, e);
        }

        System.out.println(Arrays.deepToString(keymap.toArray()));

        for (List<?> row : keymap) {
            HBox rowBox = new HBox(5);

            String currentColor = "#FFFFFF"; // Default color
            for (Object item : row) {
                if (item instanceof String) {
                    // Create a button
                    Button button = new Button((String) item);
                    button.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
                    button.setStyle("-fx-background-color: " + currentColor + ";");
                    rowBox.getChildren().add(button);
                } else if (item instanceof Map map) {
                    if (map.containsKey("c")) {
                        // Change the color for subsequent buttons
                        currentColor = (String) map.get("c");
                    }
                    if (map.containsKey("w")) {
                        // Create a blank space
                        double multiplier = ((Number) map.get("w")).doubleValue();
                        Region spacer = new Region();
                        spacer.setPrefSize(BUTTON_WIDTH * multiplier, BUTTON_HEIGHT);
                        rowBox.getChildren().add(spacer);
                    }
                }
            }

            root.getChildren().add(rowBox);
        }
        var pane = new DialogPane();
        pane.getChildren().add(root);
        setDialogPane(pane);
    }

    @FXML
    public void initialize() throws JsonProcessingException {

    }

    static void unexpectedAlert(Window owner, Exception e) {
        var errDialog = new Alert(Alert.AlertType.ERROR, "Unexpected error: " + e.getMessage());
        WindowUtil.positionDialog(owner, errDialog, errDialog.getDialogPane().getWidth(), errDialog.getDialogPane().getHeight());
        errDialog.showAndWait();
    }

}
