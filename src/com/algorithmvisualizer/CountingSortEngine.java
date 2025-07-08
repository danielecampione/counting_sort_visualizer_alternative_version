package com.algorithmvisualizer;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Motore degli algoritmi - Gestisce la logica di business per l'algoritmo
 * di ordinamento Counting Sort.
 */
public class CountingSortEngine {

    // Contatori per analisi performance
    private long comparisons;
    private long swaps;
    private long memoryAccesses;
    private List<CountingSortStep> executionSteps;
    private Consumer<CountingSortStep> stepCallback;

    /**
     * Costruttore - Inizializza il motore degli algoritmi
     */
    public CountingSortEngine() {
        executionSteps = new ArrayList<>();
        resetCounters();
    }

    /**
     * Esegue l'algoritmo specificato sull'array fornito
     * @param algorithmName Nome dell'algoritmo da eseguire
     * @param data Array di dati su cui operare
     * @return Risultato dell'esecuzione con statistiche
     */
    public CountingSortResult executeAlgorithm(int[] data) {
        resetCounters();
        executionSteps.clear();

        long startTime = System.nanoTime();
        int[] resultData = data.clone(); // Lavoriamo su una copia

        resultData = countingSort(resultData);

        long endTime = System.nanoTime();
        long executionTime = (endTime - startTime) / 1_000_000; // Conversione in millisecondi

        return new CountingSortResult(
            resultData, 
            executionTime,
            comparisons,
            swaps,
            memoryAccesses,
            "O(n)",
            new ArrayList<>(executionSteps)
        );
    }

    /**
     * Implementazione Counting Sort con tracking delle operazioni
     */
    private int[] countingSort(int[] arr) {
        if (arr.length == 0) return new int[0];
        
        addStep("Inizio Counting Sort", -1, -1, arr, true);
        
        // Trova il valore massimo per determinare la dimensione dell'array di conteggio
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            memoryAccesses++;
            comparisons++;
            addStep("Controllo elemento " + arr[i] + " per trovare il massimo", i, -1, arr, false);
            if (arr[i] > max) {
                max = arr[i];
                addStep("Nuovo massimo trovato: " + max, i, -1, arr, false);
            }
        }
        
        // Crea l'array di conteggio (dimensione = max + 1 poiché partiamo da 0)
        int[] count = new int[max + 1];
        addStep("Creato array di conteggio di dimensione " + (max + 1), -1, -1, arr, true);
        
        // Conta le occorrenze di ogni elemento
        for (int i = 0; i < arr.length; i++) {
            memoryAccesses++;
            count[arr[i]]++;
            addStep("Conteggio elemento " + arr[i], i, -1, arr, false);
        }
        
        addStep("Fase di conteggio completata", -1, -1, arr, true);
        
        // Costruisce l'array risultato
        int[] result = new int[arr.length];
        int currentPosition = 0;
        
        // Ricostruisce l'array ordinato
        for (int i = 0; i <= max; i++) {
            while (count[i] > 0) {
                result[currentPosition] = i;
                addStep("Posizionamento elemento " + i + " nella posizione " + currentPosition, -1, currentPosition, result, true);
                currentPosition++;
                count[i]--;
                memoryAccesses++;
            }
        }
        
        addStep("Counting Sort Completato", -1, -1, result, true);
        return result;
    }

    // Metodi di supporto essenziali

    /**
     * Funzione di utilità per lo scambio di elementi
     */
    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        memoryAccesses += 4; // 2 letture + 2 scritture
    }

    /**
     * Aggiunge un passo all'esecuzione dell'algoritmo per il tracking
     * @param isStateChange Indica se questo step rappresenta un cambio di stato principale (es. inizio passata, fine algoritmo)
     * o un'operazione intermedia (es. confronto). Utile per la visualizzazione.
     */
    private void addStep(String description, int index1, int index2, int[] currentArrayState, boolean isStateChange) {
        CountingSortStep step = new CountingSortStep(
            "Counting Sort",
            description,
            index1, 
            index2, 
            System.nanoTime(),
            comparisons,
            swaps,
            memoryAccesses,
            Arrays.copyOf(currentArrayState, currentArrayState.length), 
            isStateChange
        );
        executionSteps.add(step);
        if (stepCallback != null) {
            stepCallback.accept(step);
        }
    }

    /**
     * Reset dei contatori per nuova esecuzione
     */
    private void resetCounters() {
        comparisons = 0;
        swaps = 0;
        memoryAccesses = 0;
    }

    /**
     * Imposta callback per aggiornamenti in tempo reale
     */
    public void setStepCallback(Consumer<CountingSortStep> callback) {
        this.stepCallback = callback;
    }

}