package net.uberfoo.keyboard.neuron;

import net.uberfoo.keyboard.neuron.model.ProcessInfo;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public abstract class AbstractProcessService implements ProcessService {
    private Consumer<ProcessInfo> consumer;
    private ScheduledExecutorService scheduler;

    @Override
    public void stop() {
        scheduler.shutdown();
        scheduler = null; // Allow it to be garbage collected
    }

    @Override
    public void start() {
        if (scheduler != null) {
            throw new IllegalStateException("Service already started");
        }
        scheduler = new ScheduledThreadPoolExecutor(1);
        scheduler.scheduleWithFixedDelay(getRunner(), 0, 1, TimeUnit.SECONDS); // Check every 5 seconds
    }

    @Override
    public void setConsumer(Consumer<ProcessInfo> consumer) {
        this.consumer = consumer;
    }

    protected void notifyConsumer(ProcessInfo processInfo) {
        if (consumer != null) {
            consumer.accept(processInfo);
        }
    }

    protected abstract Runnable getRunner();
}
