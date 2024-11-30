package net.uberfoo.keyboard.neuron;

import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ProgressMonitor;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.prefs.Preferences;

public class KeyNeuronApplication extends Application {

    private final Preferences preferences = Preferences.userNodeForPackage(KeyNeuronApplication.class);

    private Scene scene;
    private MainController mainController;

    private KeyboardDefinitionsRepoService keyboardDefinitionsRepoService;
    private StorageService storageService;

    public void init() throws GitAPIException, IOException {
        storageService = new StorageService(Paths.get(System.getProperty("user.home")));
        keyboardDefinitionsRepoService = new KeyboardDefinitionsRepoService(storageService);

        keyboardDefinitionsRepoService.syncRepos(new ProgressMonitor() {
            private int totalTasks;
            private int totalWork;
            private int completed = 0;
            @Override
            public void start(int totalTasks) {
                this.totalTasks = totalTasks;
                System.out.println("Starting " + totalTasks + " tasks");
            }

            @Override
            public void beginTask(String title, int totalWork) {
                this.totalWork = totalWork;
                completed = 0;
                System.out.println("Beginning task " + title + " with " + totalWork + " work");
            }

            @Override
            public void update(int completed) {
                this.completed += completed;
                System.out.println("Completed " + this.completed + " of " + totalWork + " work");
                notifyPreloader(new Preloader.ProgressNotification(1.0 * this.completed / totalWork));
            }

            @Override
            public void endTask() {

            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public void showDuration(boolean enabled) {

            }
        });
    }

    @Override
    public void start(Stage stage) throws IOException, GitAPIException {

        System.out.println("Starting KeyNeuronApplication");

        FXMLLoader fxmlLoader = new FXMLLoader(KeyNeuronApplication.class.getResource("main-view.fxml"));

        var keymapService = new KeymapService(keyboardDefinitionsRepoService);

        mainController = new MainController(new KeyboardHidService(keymapService));
        fxmlLoader.setControllerFactory(c -> mainController);

        stage.setX(preferences.getDouble("WINDOW_X", 200));
        stage.setY(preferences.getDouble("WINDOW_Y", 200));

        stage.setTitle("Key Neuron");
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
        stage.sizeToScene();
    }

    @Override
    public void stop() {
        preferences.putDouble("WINDOW_X", scene.getWindow().getX());
        preferences.putDouble("WINDOW_Y", scene.getWindow().getY());
        mainController.close();
    }

    public static void main(String[] args) {
        System.out.println("Launching KeyNeuronApplication");
        launch();
    }

}