package net.uberfoo.keyboard.neuron.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Data
@ToString
public class ProcessInfo {
    private final String name;
    private final String title;
    private final int pid;
}
