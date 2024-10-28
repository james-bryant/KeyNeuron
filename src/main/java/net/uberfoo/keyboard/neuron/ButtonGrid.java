package net.uberfoo.keyboard.neuron;

import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.util.*;

public class ButtonGrid extends Pane {

    private static final double DEFAULT_KEY_WIDTH = 50.0;
    private static final double DEFAULT_KEY_HEIGHT = 50.0;
    private static final double HORIZONTAL_GAP = 5.0;
    private static final double VERTICAL_GAP = 5.0;

    private final List<KeyData> keys = new ArrayList<>();

    public void setData(List<Object> data, KeyboardHid keyboardHid) {
        keys.clear();
        getChildren().clear();

        // Keep track of occupied cells
        Map<Point2D, Boolean> occupiedCells = new HashMap<>();

        int rowIndex = 0;
        int keyIndex = 0;

        for (Object obj : data) {
            if (obj instanceof List row) {
                int columnIndex = 0;
                double widthMultiplier = 1.0;
                double heightMultiplier = 1.0;
                String currentColor = "#FFFFFF";

                for (int i = 0; i < row.size(); i++) {
                    Object item = row.get(i);

                    if (item instanceof Map map) {
                        if (map.containsKey("c")) {
                            currentColor = (String) map.get("c");
                        }
                        if (map.containsKey("x")) {
                            double xAdjust = ((Number) map.get("x")).doubleValue();
                            columnIndex += xAdjust * 4; // Assuming units of 0.25
                        }
                        if (map.containsKey("y")) {
                            double yAdjust = ((Number) map.get("y")).doubleValue();
                            rowIndex += yAdjust * 4; // Assuming units of 0.25
                        }
                        if (map.containsKey("w")) {
                            widthMultiplier = ((Number) map.get("w")).doubleValue();
                        }
                        if (map.containsKey("h")) {
                            heightMultiplier = ((Number) map.get("h")).doubleValue();
                        }
                    } else if (item instanceof String keyLabel) {
                        // Calculate colSpan and rowSpan
                        int colSpan = (int) (widthMultiplier * 4); // Multiply by 4 to handle 0.25 increments
                        int rowSpan = (int) (heightMultiplier * 4);
                        int idx = keyLabel.endsWith("e0") ? -1 : keyIndex++;
                        // Create Button
                        Button keyButton = new Button(Integer.toString(idx));
                        keyButton.setStyle("-fx-background-color: " + currentColor + ";");

                        // Add KeyData
                        KeyData keyData = new KeyData(keyButton, idx, rowIndex, columnIndex, rowSpan, colSpan);
                        keys.add(keyData);
                        getChildren().add(keyButton);

                        // Mark occupied cells
                        for (int r = rowIndex; r < rowIndex + rowSpan; r++) {
                            for (int c = columnIndex; c < columnIndex + colSpan; c++) {
                                Point2D p = new Point2D(c, r);
                                if (occupiedCells.containsKey(p)) {
                                    System.out.println("Overlap detected at " + p);
                                }
                                occupiedCells.put(p, true);
                            }
                        }

                        // Advance columnIndex
                        columnIndex += colSpan;

                        // Reset adjustments
                        widthMultiplier = 1.0;
                        heightMultiplier = 1.0;
                    }
                }
                // Advance to next row
                rowIndex += 4; // Assuming each row is 1 unit (4 * 0.25)
            }
        }
        requestLayout();
    }

    @Override
    protected void layoutChildren() {
        double unitWidth = (DEFAULT_KEY_WIDTH + HORIZONTAL_GAP) / 4.0; // Units of 0.25
        double unitHeight = (DEFAULT_KEY_HEIGHT + VERTICAL_GAP) / 4.0;

        for (KeyData key : keys) {
            double x = key.column * unitWidth;
            double y = key.row * unitHeight;
            double width = key.colSpan * unitWidth - HORIZONTAL_GAP;
            double height = key.rowSpan * unitHeight - VERTICAL_GAP;

            key.button.resizeRelocate(x, y, width, height);
        }
    }

    @Override
    protected double computePrefWidth(double height) {
        double unitWidth = (DEFAULT_KEY_WIDTH + HORIZONTAL_GAP) / 4.0;
        double maxX = 0;
        for (KeyData key : keys) {
            double keyMaxX = key.column * unitWidth + key.colSpan * unitWidth - HORIZONTAL_GAP;
            if (keyMaxX > maxX) {
                maxX = keyMaxX;
            }
        }
        return maxX + getInsets().getLeft() + getInsets().getRight();
    }

    @Override
    protected double computePrefHeight(double width) {
        double unitHeight = (DEFAULT_KEY_HEIGHT + VERTICAL_GAP) / 4.0;
        double maxY = 0;
        for (KeyData key : keys) {
            double keyMaxY = key.row * unitHeight + key.rowSpan * unitHeight - VERTICAL_GAP;
            if (keyMaxY > maxY) {
                maxY = keyMaxY;
            }
        }
        return maxY + getInsets().getTop() + getInsets().getBottom();
    }

    public static class KeyData {
        Button button;
        int index;
        int row;
        int column;
        int rowSpan;
        int colSpan;

        public KeyData(Button button, int index, int row, int column, int rowSpan, int colSpan) {
            this.button = button;
            this.index = index;
            this.row = row;
            this.column = column;
            this.rowSpan = rowSpan;
            this.colSpan = colSpan;
        }
    }
}
