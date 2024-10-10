package net.uberfoo.keyboard.neuron;

import javafx.scene.control.Alert;
import javafx.stage.Window;

public class AlertDialogs {

    public static void unexpectedAlert(Window owner, Exception e) {
        var errDialog = new Alert(Alert.AlertType.ERROR, "Unexpected error: " + e.getMessage());
        WindowUtil.positionDialog(owner, errDialog, errDialog.getDialogPane().getWidth(), errDialog.getDialogPane().getHeight());
        errDialog.showAndWait();
    }

}
