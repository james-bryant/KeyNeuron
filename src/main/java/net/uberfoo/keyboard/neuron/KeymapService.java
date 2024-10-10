package net.uberfoo.keyboard.neuron;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.uberfoo.keyboard.neuron.model.Keyboard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class KeymapService {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public List<Keyboard> getKeyboards(Path path) throws IOException {
        var keyboards = new LinkedList<Keyboard>();
        collectKeyboards(path, keyboards);
        return keyboards;
    }

    public void collectKeyboards(Path path, List<Keyboard> keyboards) throws IOException {
        if (Files.isDirectory(path)) {
            try (Stream<Path> stream = Files.list(path)) {
                stream.filter(x -> Files.isDirectory(x) || x.toString().endsWith(".json")).forEach(p -> {
                    try {
                        collectKeyboards(p, keyboards);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } else {
            System.out.println(path);
            keyboards.add(objectMapper.readValue(path.toFile(), Keyboard.class));
        }
    }

}
