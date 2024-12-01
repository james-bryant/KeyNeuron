package net.uberfoo.keyboard.neuron;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Preloader;
import net.uberfoo.keyboard.neuron.model.Keyboard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class KeymapService {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final KeyboardDefinitionsRepoService keyboardDefinitionsRepoService;
    private final Map<Integer, Map<Integer, Keyboard>> vendorProducts = new HashMap<>();
    private final int fileCount;
    private int currentCount = 0;

    public KeymapService(KeyboardDefinitionsRepoService keyboardDefinitionsRepoService, Consumer<Preloader.ProgressNotification> progressNotificationConsumer) throws IOException {
        this.keyboardDefinitionsRepoService = keyboardDefinitionsRepoService;
        List<Path> keyboardDefinitionPaths = keyboardDefinitionsRepoService.getKeyboardDefinitionPaths();
        int count = 0;
        for (var path : keyboardDefinitionPaths) {
            try {
                count += (int) Files.walk(path)
                        .parallel()               // Enable parallel processing for speed
                        .filter(Files::isRegularFile) // Only count files
                        .count();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        fileCount = count;

        getKeyboards(progressNotificationConsumer);
    }

    public Keyboard getKeyboard(int vendorId, int productId) {
        if (!vendorProducts.containsKey(vendorId)) {
            return null;
        }
        return vendorProducts.get(vendorId).get(productId);
    }

    public void getKeyboards(Consumer<Preloader.ProgressNotification> progressNotificationConsumer) throws IOException {
        final int[] p = {0};
        for (var path : keyboardDefinitionsRepoService.getKeyboardDefinitionPaths()) {
            collectKeyboards(path, progressNotificationConsumer);
        }
    }

    private void collectKeyboards(Path path, Consumer<Preloader.ProgressNotification> progressNotificationConsumer) throws IOException {
        if (Files.isDirectory(path)) {
            try (Stream<Path> stream = Files.list(path)) {
                stream.filter(x -> Files.isDirectory(x) || x.toString().endsWith(".json")).forEach(p -> {
                    try {
                        collectKeyboards(p, progressNotificationConsumer);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } else {
            var keyboard = objectMapper.readValue(path.toFile(), Keyboard.class);
            var vendorId = Integer.parseInt(keyboard.getVendorId().trim().replaceFirst("0[xX]", ""), 16);
            if (!vendorProducts.containsKey(vendorId)) {
                vendorProducts.put(vendorId, new HashMap<>());
            }
            vendorProducts.get(vendorId).put(Integer.parseInt(keyboard.getProductId().trim().replaceFirst("0[xX]", ""), 16), keyboard);
            currentCount++;
            progressNotificationConsumer.accept(new Preloader.ProgressNotification((double)currentCount/fileCount));
        }
    }

}
