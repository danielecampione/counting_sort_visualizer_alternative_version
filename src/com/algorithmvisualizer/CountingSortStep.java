package com.algorithmvisualizer;

import java.util.Arrays;

/**
 * Classe per rappresentare un singolo passo dell'algoritmo
 */
public class CountingSortStep {
    private final String algorithm;
    private final String description;
    private final int primaryIndex;
    private final int secondaryIndex;
    private final long timestamp;
    private final long comparisons;
    private final long swaps;
    private final long memoryAccesses;
    private final int[] arrayState;
    private final boolean isStateChange;

    public CountingSortStep(String algorithm, String description, int primaryIndex,
                       int secondaryIndex, long timestamp, long comparisons,
                       long swaps, long memoryAccesses, int[] arrayState, boolean isStateChange) {
        this.algorithm = algorithm;
        this.description = description;
        this.primaryIndex = primaryIndex;
        this.secondaryIndex = secondaryIndex;
        this.timestamp = timestamp;
        this.comparisons = comparisons;
        this.swaps = swaps;
        this.memoryAccesses = memoryAccesses;
        this.arrayState = arrayState;
        this.isStateChange = isStateChange;
    }

    // Getters
    public String getAlgorithm() { return algorithm; }
    public String getDescription() { return description; }
    public int getPrimaryIndex() { return primaryIndex; }
    public int getSecondaryIndex() { return secondaryIndex; }
    public long getTimestamp() { return timestamp; }
    public long getComparisons() { return comparisons; }
    public long getSwaps() { return swaps; }
    public long getMemoryAccesses() { return memoryAccesses; }
    public int[] getArrayState() { return arrayState; }
    public boolean isStateChange() { return isStateChange; }
}