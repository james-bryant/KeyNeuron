package net.uberfoo.keyboard.neuron;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.uberfoo.keyboard.neuron.model.Keyboard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class KeymapService {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public List<Keyboard> getKeyboards(Path path) throws IOException {
        var keyboards = new LinkedList<Keyboard>();
        collectKeyboards(path, keyboards);
        return keyboards;
    }

    public void collectKeyboards(Path path, List<Keyboard> keyboards) throws IOException {
        if (Files.isDirectory(path)) {
            try (Stream<Path> stream = Files.list(path)) {
                stream.forEach(p -> {
                    try {
                        collectKeyboards(p, keyboards);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } else {
            keyboards.add(objectMapper.readValue(path.toFile(), Keyboard.class));
        }
    }

}
