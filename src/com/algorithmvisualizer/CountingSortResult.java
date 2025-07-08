package com.algorithmvisualizer;

import java.util.List;

/**
 * Classe per i risultati dell'esecuzione di un algoritmo
 */
public class CountingSortResult {
    private final int[] finalArrayState;
    private final long executionTime;
    private final long comparisons;
    private final long swaps;
    private final long memoryAccesses;
    private final String complexity;
    private final List<CountingSortStep> steps;

    public CountingSortResult(int[] finalArrayState, long executionTime, long comparisons,
                         long swaps, long memoryAccesses, String complexity,
                         List<CountingSortStep> steps) {
        this.finalArrayState = finalArrayState;
        this.executionTime = executionTime;
        this.comparisons = comparisons;
        this.swaps = swaps;
        this.memoryAccesses = memoryAccesses;
        this.complexity = complexity;
        this.steps = steps;
    }

    // Getters
    public int[] getFinalArrayState() { return finalArrayState; }
    public long getExecutionTime() { return executionTime; }
    public long getComparisons() { return comparisons; }
    public long getSwaps() { return swaps; }
    public long getMemoryAccesses() { return memoryAccesses; }
    public String getComplexity() { return complexity; }
    public List<CountingSortStep> getSteps() { return steps; }

    @Override
    public String toString() {
        return String.format("Risultato: Tempo=%dms, Confronti=%d, Scambi=%d, Accessi=%d, Complessità=%s",
                executionTime, comparisons, swaps, memoryAccesses, complexity);
    }
}