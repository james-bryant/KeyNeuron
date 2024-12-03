package net.uberfoo.keyboard.neuron;

import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;

public class KeyNeuronPreloader extends Preloader {

    private Stage preloaderStage;
    private ProgressBar progressBar;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.preloaderStage = primaryStage;

        progressBar = new ProgressBar();
        StackPane root = new StackPane(progressBar);
        Scene scene = new Scene(root, 300, 150);

        preloaderStage.setScene(scene);
        preloaderStage.setTitle("Starting Application...");
        preloaderStage.show();
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
        if (info instanceof ProgressNotification progressNotification) {
            progressBar.setProgress(progressNotification.getProgress());
        }
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification evt) {
        if (evt.getType() == StateChangeNotification.Type.BEFORE_START) {
            Platform.runLater(() -> preloaderStage.hide());
        }
    }
}
