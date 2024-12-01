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
import java.util.function.Consumer;
import java.util.prefs.Preferences;

public class KeyNeuronApplication extends Application {

    private final Preferences preferences = Preferences.userNodeForPackage(KeyNeuronApplication.class);

    private Scene scene;
    private MainController mainController;

    public void init() throws GitAPIException, IOException {
        StorageService storageService = new StorageService(Paths.get(System.getProperty("user.home")));
        KeyboardDefinitionsRepoService keyboardDefinitionsRepoService = new KeyboardDefinitionsRepoService(storageService);

        keyboardDefinitionsRepoService.syncRepos(new ProgressMonitor() {
            private int totalTasks;
            private int totalWork;
            private int completed = 0;
            private int tasksCompleted = 0;

            @Override
            public void start(int totalTasks) {
                this.totalTasks = totalTasks;
                System.out.println("Starting " + totalTasks + " tasks");
            }

            @Override
            public void beginTask(String title, int totalWork) {
                if (tasksCompleted > totalTasks) {
                    totalTasks = tasksCompleted;
                }
                this.totalWork = totalWork;
                this.completed = 0;
                System.out.println("Beginning task " + title + " with " + totalWork + " work");
            }

            @Override
            public void update(int completed) {
                this.completed += completed;
                double p = tasksCompleted == 0 ? 0 : (double)tasksCompleted / (double)totalTasks;
                p += (((double)this.completed / (double)totalWork) / (double)totalTasks);
                var progress = new Preloader.ProgressNotification(p / 2.0);
                if ((this.completed % 100) == 0) {
                    System.out.printf("Progress: (%d/%d) %1.3f %1.3f%n", this.completed, totalWork, p, progress.getProgress());
                    notifyPreloader(progress);
                }
            }

            @Override
            public void endTask() {
                tasksCompleted++;
                completed = 0;
                System.out.println("Completed task " + tasksCompleted + " of " + totalTasks);
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public void showDuration(boolean enabled) {

            }
        });

        var keymapService = new KeymapService(keyboardDefinitionsRepoService, new Consumer<Preloader.ProgressNotification>() {
            @Override
            public void accept(Preloader.ProgressNotification progressNotification) {
                double p = progressNotification.getProgress();
                notifyPreloader(new Preloader.ProgressNotification((p / 2.0) + 0.5));
            }
        });

        FXMLLoader fxmlLoader = new FXMLLoader(KeyNeuronApplication.class.getResource("main-view.fxml"));
        mainController = new MainController(new KeyboardHidService(keymapService));
        fxmlLoader.setControllerFactory(c -> mainController);

        scene = new Scene(fxmlLoader.load());
    }

    @Override
    public void start(Stage stage) throws IOException, GitAPIException {

        System.out.println("Starting KeyNeuronApplication");

        stage.setX(preferences.getDouble("WINDOW_X", 200));
        stage.setY(preferences.getDouble("WINDOW_Y", 200));

        stage.setTitle("Key Neuron");
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
        System.setProperty("javafx.preloader", KeyNeuronPreloader.class.getName());
        launch();
    }

}