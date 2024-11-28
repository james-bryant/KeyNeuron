package net.uberfoo.keyboard.neuron;

import net.uberfoo.keyboard.neuron.model.ProcessInfo;

import java.util.function.Consumer;

public interface ProcessService {
    void stop();
    void start();

    void setConsumer(Consumer<ProcessInfo> consumer);
}
