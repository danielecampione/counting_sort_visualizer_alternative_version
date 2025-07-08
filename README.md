# Counting Sort Visualizer

Un'applicazione JavaFX per la visualizzazione dell'algoritmo Counting Sort con interfaccia grafica moderna e animazioni.

## Caratteristiche

- **Visualizzazione in tempo reale** dell'algoritmo Counting Sort
- **Interfaccia grafica moderna** con effetti di animazione
- **Controlli interattivi** per dimensione array e velocità di esecuzione
- **Separazione della logica di business** dalla GUI

## Screenshot
![Png](https://i.ibb.co/YBZcYGgM/Immagine-2025-07-08-222731.png)

## Requisiti

- Java 1.8 (JDK)
- JavaFX (incluso in Java 1.8)

## Come Usare l'Applicazione

1. **Genera Array**: Clicca su "GENERA ARRAY" per creare un array casuale
2. **Imposta Dimensione**: Usa lo slider per modificare la dimensione dell'array (10-200 elementi)
3. **Regola Velocità**: Controlla la velocità dell'animazione con l'apposito slider
4. **Avvia Algoritmo**: Clicca su "AVVIA ALGORITMO" per visualizzare il Counting Sort
5. **Reset**: Usa "RESET" per pulire la visualizzazione

## Algoritmo Counting Sort

Il Counting Sort è un algoritmo di ordinamento non comparativo con complessità temporale O(n + k), dove:
- n = numero di elementi
- k = range dei valori (max - min + 1)

### Fasi dell'algoritmo

1. **Trova min e max** nell'array di input
2. **Crea array di conteggio** di dimensione (max - min + 1)
3. **Conta le occorrenze** di ogni elemento
4. **Calcola posizioni cumulative** nell'array di conteggio
5. **Costruisce l'array ordinato** usando le posizioni calcolate

## Architettura

## Funzionalità Tecniche

- **Animazioni JavaFX**: Timeline, FillTransition, ScaleTransition
- **Effetti visivi**: DropShadow, gradients, hover effects
- **Threading**: Task per esecuzione asincrona dell'algoritmo
- **Responsive UI**: Adattamento dinamico alle dimensioni dell'array

## Personalizzazioni

L'applicazione supporta:
- Array di dimensioni variabili
- 5 livelli di velocità di animazione
- Visualizzazione con barre colorate e effetti di glow
- Evidenziazione degli elementi durante l'ordinamento

---

*Progetto sviluppato per dimostrare l'algoritmo Counting Sort con una moderna interfaccia JavaFX.*