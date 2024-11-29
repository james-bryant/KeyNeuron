package net.uberfoo.keyboard.neuron;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
public class StorageService {

    @Getter
    private final Path baseDir;

    public Path getStoragePath() {
        return Paths.get(baseDir.toString(), ".keyboard-neuron");
    }

    public Path getStoragePath(String filename) {
        return Paths.get(getStoragePath().toString(), filename);
    }

    public Path getStoragePath(String... filename) {
        return Paths.get(getStoragePath().toString(), filename);
    }

    public Path getStoragePath(Path path) {
        return Paths.get(getStoragePath().toString(), path.toString());
    }

}
