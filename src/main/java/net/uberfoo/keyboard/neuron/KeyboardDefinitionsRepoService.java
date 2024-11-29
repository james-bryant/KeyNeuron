package net.uberfoo.keyboard.neuron;

import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@RequiredArgsConstructor
public class KeyboardDefinitionsRepoService {

    public static final String DEFAULT_KEYBOARD_DEFINITIONS_REPO = "https://github.com/Uberfoo-Heavy-Industries/keyboards.git";
    public static final String DEFAULT_KEYBOARD_DEFINITIONS_REPO_BRANCH = "master";
    public static final String DEFAULT_KEYBOARD_DEFINITIONS_REPO_PATH = "src";

    private final StorageService storageService;

    public void syncRepos() throws GitAPIException, IOException {
        var defaultDir = storageService.getStoragePath("keyboards", "default").toFile();
        if (defaultDir.mkdirs()) {
            // Clone the default repo
            try (var git = Git.cloneRepository()
                    .setURI(DEFAULT_KEYBOARD_DEFINITIONS_REPO)
                    .setDirectory(defaultDir)
                    .setBranch(DEFAULT_KEYBOARD_DEFINITIONS_REPO_BRANCH)
                    .call()) {}

        } else {
            // Pull the default repo
            try (var git = Git.open(storageService.getStoragePath("keyboards", "default").toFile())) {
                git.pull().call();
            }
        }
    }

    public List<Path> getKeyboardDefinitionPaths() {
        return List.of(
                storageService.getStoragePath("keyboards", "default", DEFAULT_KEYBOARD_DEFINITIONS_REPO_PATH));
    }
}
