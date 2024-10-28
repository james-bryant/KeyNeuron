package net.uberfoo.keyboard.neuron;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.uberfoo.keyboard.neuron.model.Keyboard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class KeymapService {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final Path path;
    private final List<Keyboard> keyboards;
    private final Map<Integer, Map<Integer, Keyboard>> vendorProducts = new HashMap<>();

    public KeymapService(Path path) throws IOException {
        this.path = path;
        keyboards = getKeyboards();
        for (Keyboard keyboard : keyboards) {
            var vendorId = Integer.parseInt(keyboard.getVendorId().trim().replaceFirst("0[xX]", ""), 16);
            if (!vendorProducts.containsKey(vendorId)) {
                vendorProducts.put(vendorId, new HashMap<>());
            }
            vendorProducts.get(vendorId).put(Integer.parseInt(keyboard.getProductId().trim().replaceFirst("0[xX]", ""), 16), keyboard);
        }
    }

    public Keyboard getKeyboard(int vendorId, int productId) {
        if (!vendorProducts.containsKey(vendorId)) {
            return null;
        }
        return vendorProducts.get(vendorId).get(productId);
    }

    public List<Keyboard> getKeyboards() throws IOException {
        var keyboards = new LinkedList<Keyboard>();
        collectKeyboards(path, keyboards);
        return keyboards;
    }

    private void collectKeyboards(Path path, List<Keyboard> keyboards) throws IOException {
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
            keyboards.add(objectMapper.readValue(path.toFile(), Keyboard.class));
        }
    }

}
