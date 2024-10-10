package net.uberfoo.keyboard.neuron;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HelloController {

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

    private static final KeyboardService keyboardService = new KeyboardService();

    @FXML
    private Label welcomeText;

    @FXML
    private Pane pane;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");

        Map<Integer, int[]> keyColorMap = new HashMap<>();
        keyColorMap.put(5, new int[]{255, 0, 0});    // Key 0: Red
        keyColorMap.put(6, new int[]{0, 255, 0});    // Key 1: Green
        keyColorMap.put(7, new int[]{0, 0, 255});    // Key 2: Blue

        keyboardService.sendPerKeyColors(keyColorMap);

        ButtonGrid buttonGrid = new ButtonGrid();
        try {
            buttonGrid.setJsonData(KEYMAP_JSON);
            pane.getChildren().add(buttonGrid);
        } catch (IOException e) {
            AlertDialogs.unexpectedAlert(pane.getParent().getScene().getWindow(), e);
        }
    }
}