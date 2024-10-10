package net.uberfoo.keyboard.neuron;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ButtonGrid extends VBox {

    private static final double DEFAULT_BUTTON_WIDTH = 50;
    private static final double DEFAULT_BUTTON_HEIGHT = 50;
    private static final double DEFAULT_HSPACING = 5;
    private static final double DEFAULT_VSPACING = 10;

    private double buttonWidth = DEFAULT_BUTTON_WIDTH;
    private double buttonHeight = DEFAULT_BUTTON_HEIGHT;
    private double hSpacing = DEFAULT_HSPACING;
    private double vSpacing = DEFAULT_VSPACING;
    private String defaultColor = "#FFFFFF";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ButtonGrid() {
        setSpacing(vSpacing);
        setPadding(new Insets(10));
    }

    public void setButtonWidth(double buttonWidth) {
        this.buttonWidth = buttonWidth;
    }

    public void setButtonHeight(double buttonHeight) {
        this.buttonHeight = buttonHeight;
    }

    public void setHorizontalSpacing(double hSpacing) {
        this.hSpacing = hSpacing;
    }

    public void setVerticalSpacing(double vSpacing) {
        this.vSpacing = vSpacing;
        setSpacing(vSpacing);
    }

    public void setDefaultColor(String defaultColor) {
        this.defaultColor = defaultColor;
    }

    public void setJsonData(String jsonData) throws IOException {
        // Clear any existing content
        getChildren().clear();

        // Parse JSON data
        List<List<Object>> data = parseJsonData(jsonData);

        // Generate UI
        for (List<Object> row : data) {
            HBox rowBox = new HBox(hSpacing);

            String currentColor = defaultColor; // Reset color for each row
            double currentButtonWidth = buttonWidth;
            for (Object item : row) {
                if (item instanceof String) {
                    // Create a button
                    Button button = new Button((String) item);
                    button.setPrefSize(currentButtonWidth, buttonHeight);
                    button.setStyle("-fx-background-color: " + currentColor + ";");
                    rowBox.getChildren().add(button);
                    currentButtonWidth = buttonWidth;
                } else if (item instanceof Map map) {
                    if (map.containsKey("c")) {
                        // Change the color for subsequent buttons
                        currentColor = (String) map.get("c");
                    }
                    if (map.containsKey("x")) {
                        // Create a blank space
                        double multiplier = ((Number) map.get("x")).doubleValue();
                        Region spacer = new Region();
                        spacer.setPrefSize(buttonWidth * multiplier + hSpacing, buttonHeight);
                        rowBox.getChildren().add(spacer);
                    }
                    if (map.containsKey("w")) {
                        double width = ((Number) map.get("w")).doubleValue();
                        currentButtonWidth = width * buttonWidth;
                    }
                }
            }

            getChildren().add(rowBox);
        }
    }

    private List<List<Object>> parseJsonData(String jsonData) throws IOException {
        // Use TypeReference to specify the generic type
        return objectMapper.readValue(jsonData, new TypeReference<List<List<Object>>>() {});
    }
}