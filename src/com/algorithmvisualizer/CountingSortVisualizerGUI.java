package com.algorithmvisualizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.concurrent.Task;


/**
 * Classe principale per l'interfaccia grafica del visualizzatore dell'algoritmo
 * di ordinamento Counting Sort.
 * Contiene tutti gli effetti animati e la gestione dell'UI
 */
public class CountingSortVisualizerGUI extends Application {

    private CountingSortEngine engine;
    private VBox visualizationArea;
    private Label statusLabel;
    private Button startButton;
    private ComboBox<String> algorithmSelector;
    private Slider arraySizeSlider;
    private Slider speedSlider;
    private ProgressBar progressBar;
    private Timeline glowAnimation;
    private List<Rectangle> dataElements;
    private int[] currentArray; // Array di dati corrente per la visualizzazione

    @Override
    public void start(Stage primaryStage) {
        engine = new CountingSortEngine();
        dataElements = new ArrayList<>();

        primaryStage.setTitle("Visualizzatore Algoritmi di Ordinamento");
        BorderPane root = createAnimatedRoot();
        VBox controlPanel = createControlPanel();
        visualizationArea = createVisualizationArea();

        root.setTop(controlPanel);
        root.setCenter(visualizationArea);

        Scene scene = new Scene(root, 1400, 900);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setOpacity(0);
        primaryStage.show();

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), scene.getRoot());
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        Timeline stageOpacity = new Timeline(
            new KeyFrame(Duration.seconds(1.5),
                new KeyValue(primaryStage.opacityProperty(), 1))
        );

        ParallelTransition entrance = new ParallelTransition(fadeIn, stageOpacity);
        entrance.play();

        generateRandomArray((int)arraySizeSlider.getValue()); // Genera array iniziale
    }

    private BorderPane createAnimatedRoot() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #1a1a2e, #16213e, #0f3460);");
        Timeline bgPulse = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(root.opacityProperty(), 0.95)),
            new KeyFrame(Duration.seconds(3), new KeyValue(root.opacityProperty(), 1.0)),
            new KeyFrame(Duration.seconds(6), new KeyValue(root.opacityProperty(), 0.95))
        );
        bgPulse.setCycleCount(Timeline.INDEFINITE);
        bgPulse.play();
        return root;
    }

    private VBox createControlPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.setAlignment(Pos.CENTER);

        Label title = new Label("COUNTING SORT VISUALIZER");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);
        DropShadow glowEffect = new DropShadow();
        glowEffect.setColor(Color.CYAN);
        glowEffect.setRadius(20);
        title.setEffect(glowEffect);
        glowAnimation = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(glowEffect.radiusProperty(), 15)),
            new KeyFrame(Duration.seconds(1), new KeyValue(glowEffect.radiusProperty(), 30)),
            new KeyFrame(Duration.seconds(2), new KeyValue(glowEffect.radiusProperty(), 15))
        );
        glowAnimation.setCycleCount(Timeline.INDEFINITE);
        glowAnimation.play();

        HBox algorithmBox = new HBox(10);
        algorithmBox.setAlignment(Pos.CENTER);
        Label algLabel = new Label("Algoritmo:");
        algLabel.setTextFill(Color.WHITE);
        algLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        algorithmSelector = new ComboBox<>();
        algorithmSelector.getItems().addAll(
            "Counting Sort"
        );
        algorithmSelector.setValue("Counting Sort");
        // Stile applicato da CSS, ma un fallback può essere utile
        // algorithmSelector.setStyle("-fx-background-color: #2d3748; -fx-text-fill: white;");
        addHoverEffect(algorithmSelector);
        algorithmBox.getChildren().addAll(algLabel, algorithmSelector);

        HBox controlsBox = new HBox(20);
        controlsBox.setAlignment(Pos.CENTER);
        VBox sizeBox = new VBox(5);
        sizeBox.setAlignment(Pos.CENTER);
        Label sizeLabel = new Label("Dimensione Array: 50");
        sizeLabel.setTextFill(Color.WHITE);
        arraySizeSlider = new Slider(10, 200, 50);
        arraySizeSlider.setShowTickLabels(true);
        arraySizeSlider.setShowTickMarks(true);
        arraySizeSlider.setMajorTickUnit(50);
        // styleSlider(arraySizeSlider); // Stile da CSS
        addHoverEffect(arraySizeSlider);
        arraySizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            sizeLabel.setText("Dimensione Array: " + newVal.intValue());
            generateRandomArray(newVal.intValue());
        });
        sizeBox.getChildren().addAll(sizeLabel, arraySizeSlider);

        VBox speedBox = new VBox(5);
        speedBox.setAlignment(Pos.CENTER);
        Label speedLabel = new Label("Velocità: Normale");
        speedLabel.setTextFill(Color.WHITE);
        speedSlider = new Slider(1, 10, 5);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        // styleSlider(speedSlider); // Stile da CSS
        addHoverEffect(speedSlider);
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            String[] speeds = {"Molto Lenta", "Lenta", "Normale", "Veloce", "Molto Veloce"};
            int index = (int) Math.round(newVal.doubleValue() / 2.5) ; // Mappatura più granulare 1-4 -> 0-3
            index = Math.min(speeds.length - 1, Math.max(0, index));
            speedLabel.setText("Velocità: " + speeds[index]);
        });

        speedBox.getChildren().addAll(speedLabel, speedSlider);
        controlsBox.getChildren().addAll(sizeBox, speedBox);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        startButton = createAnimatedButton("AVVIA ALGORITMO", Color.LIMEGREEN); // Leggermente diverso
        Button generateButton = createAnimatedButton("GENERA ARRAY", Color.ORANGE);
        Button resetButton = createAnimatedButton("RESET", Color.TOMATO);
        startButton.setOnAction(e -> startAlgorithm());
        generateButton.setOnAction(e -> generateRandomArray((int)arraySizeSlider.getValue()));
        resetButton.setOnAction(e -> resetVisualization());
        buttonBox.getChildren().addAll(startButton, generateButton, resetButton);

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(400);
        // Stile da CSS
        // progressBar.setStyle("-fx-accent: linear-gradient(to right, #ff6b6b, #4ecdc4, #45b7d1);");

        statusLabel = new Label("Pronto per l'analisi algoritmica!");
        statusLabel.setTextFill(Color.WHITE);
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        // statusLabel.getStyleClass().add("status-label"); // Usa classe CSS

        panel.getChildren().addAll(title, algorithmBox, controlsBox, buttonBox, progressBar, statusLabel);
        return panel;
    }

    private Button createAnimatedButton(String text, Color glowColor) {
        Button button = new Button(text);
        // Stile da CSS, ma fallback:
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        // button.setStyle("-fx-background-color: linear-gradient(to bottom, #667eea 0%, #764ba2 100%); " +
        //               "-fx-text-fill: white; -fx-background-radius: 25;");
        button.setPrefSize(180, 40);

        DropShadow buttonGlow = new DropShadow();
        buttonGlow.setColor(glowColor);
        buttonGlow.setRadius(10); // Default
        button.setEffect(buttonGlow);

        button.setOnMouseEntered(e -> {
            // Animazioni hover da CSS, ma si possono aggiungere qui se necessario
            // Esempio: Aumentare il glow specificamente
            Timeline glowIncrease = new Timeline(
                new KeyFrame(Duration.millis(100), new KeyValue(buttonGlow.radiusProperty(), 20)),
                new KeyFrame(Duration.millis(100), new KeyValue(buttonGlow.colorProperty(), glowColor.brighter()))
            );
            glowIncrease.play();
        });
        button.setOnMouseExited(e -> {
            // Ripristino da CSS, o:
            Timeline glowDecrease = new Timeline(
                 new KeyFrame(Duration.millis(100), new KeyValue(buttonGlow.radiusProperty(), 10)), // Torna al default
                 new KeyFrame(Duration.millis(100), new KeyValue(buttonGlow.colorProperty(), glowColor))
            );
            glowDecrease.play();
        });
        return button;
    }

    private void addHoverEffect(Control control) {
        // L'hover è gestito principalmente da CSS per .button, .combo-box, .slider
        // Questa funzione potrebbe essere usata per effetti aggiuntivi non coperti da CSS
        // o se si vuole un controllo programmatico più fine.
        // Per ora, lascio che CSS gestisca gli hover principali.
    }

    /* // Non più necessario se CSS gestisce lo stile
    private void styleSlider(Slider slider) {
        slider.setStyle("-fx-control-inner-background: #2d3748; " +
                       "-fx-accent: linear-gradient(to right, #667eea, #764ba2);");
        addHoverEffect(slider);
    }
    */

    private VBox createVisualizationArea() {
        VBox area = new VBox(10);
        area.setPadding(new Insets(20));
        area.setAlignment(Pos.CENTER);
        // area.setStyle("-fx-background-color: rgba(255,255,255,0.1); " +
        //              "-fx-background-radius: 15; -fx-border-radius: 15;");
        area.getStyleClass().add("visualization-area"); // Usa classe CSS

        Label vizTitle = new Label("COUNTING SORT IN AZIONE");
        vizTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        vizTitle.setTextFill(Color.WHITE);

        area.getChildren().add(vizTitle);
        return area;
    }



    private void generateRandomArray(int size) {
        currentArray = new int[size];
        Random random = new Random();

        visualizationArea.getChildren().removeIf(node -> node instanceof HBox && "arrayBox".equals(node.getId()));
        dataElements.clear();

        HBox arrayBox = new HBox(Math.max(1, 800.0 / size > 5 ? 2 : 1)); // Spaziatura dinamica
        arrayBox.setAlignment(Pos.CENTER);
        arrayBox.setId("arrayBox");

        double barWidth = Math.max(2, (visualizationArea.getWidth() > 0 ? visualizationArea.getWidth() * 0.8 : 800.0) / size - arrayBox.getSpacing());
        if (size > 100) barWidth = Math.max(1, barWidth * 0.7); // Riduci larghezza per molti elementi


        for (int i = 0; i < size; i++) {
            currentArray[i] = random.nextInt(280) + 20; // Minimo 20 per visibilità altezza
            
            Rectangle element = new Rectangle(barWidth, currentArray[i]);
            element.setFill(createGradientFill());
            element.setArcWidth(5); // Arrotonda angoli barre
            element.setArcHeight(5);

            DropShadow elementGlow = new DropShadow();
            elementGlow.setColor(Color.rgb(102,126,234,0.7)); // Colore glow base
            elementGlow.setRadius(5);
            element.setEffect(elementGlow);

            dataElements.add(element);
            arrayBox.getChildren().add(element);

            element.setOpacity(0);
            TranslateTransition tt = new TranslateTransition(Duration.millis(100 + i * (1000.0/size)), element);
            tt.setFromY(30);
            tt.setToY(0);
            FadeTransition ft = new FadeTransition(Duration.millis(200 + i * (1000.0/size)), element);
            ft.setToValue(1);
            
            ParallelTransition pt = new ParallelTransition(tt, ft);
            pt.play();
        }
        visualizationArea.getChildren().add(arrayBox);
        updateStatus("[OK] Array generato con " + size + " elementi!");
        resetVisualizationState();
    }

    private LinearGradient createGradientFill() {
        return new LinearGradient(0, 0, 0, 1, true, null,
            new Stop(0, Color.web("#667eea")),
            new Stop(0.5, Color.web("#764ba2")),
            new Stop(1, Color.web("#8364a6")) // Leggermente modificato per omogeneità
        );
    }

    private void updateVisualizationFromArrayState(int[] arrayState) {
        if (arrayState == null || dataElements.isEmpty()) return;
        for (int i = 0; i < dataElements.size() && i < arrayState.length; i++) {
            if (dataElements.get(i) != null) {
                dataElements.get(i).setHeight(Math.max(1, arrayState[i]));
            }
        }
    }

    private void startAlgorithm() {
        String selectedAlgorithm = algorithmSelector.getValue();
        if (currentArray == null || currentArray.length == 0) {
            updateStatus("[ATTENZIONE] Genera prima un array!");
            return;
        }
        updateStatus("[ESECUZIONE] " + selectedAlgorithm + " in corso...");
        startButton.setDisable(true);
        progressBar.setProgress(0);

        // Usare un Task per eseguire l'algoritmo in background
        Task<Object> algorithmTask = new Task<Object>() {
            @Override
            protected Object call() throws Exception {
                // Simula un po' di lavoro per la progress bar iniziale
                for(int i=0; i<3; i++){
                    updateProgress(i, 10); // Simula progresso
                    Thread.sleep(100); // Non blocca la GUI perché è in un Task
                }
return engine.executeAlgorithm(currentArray.clone()); // Work on a copy
            }
        };

        algorithmTask.setOnSucceeded(event -> {
            Object algorithmResult = algorithmTask.getValue();
            progressBar.setProgress(0.1); // Progresso dopo calcolo engine, prima dell'animazione
            CountingSortResult result = (CountingSortResult) algorithmResult;
            animateAlgorithmSteps(result.getSteps(), result.getFinalArrayState(), result.getExecutionTime());
        });

        algorithmTask.setOnFailed(event -> {
            updateStatus("[ERRORE] Errore durante l'esecuzione dell'algoritmo: " + algorithmTask.getException().getMessage());
            startButton.setDisable(false);
            progressBar.setProgress(0);
        });

        // Collega il progresso del task alla progress bar (opzionale se l'animazione è principale)
        // progressBar.progressProperty().bind(algorithmTask.progressProperty());
        new Thread(algorithmTask).start();
    }

    private void animateAlgorithmSteps(List<CountingSortStep> steps, int[] finalArrayState, long engineExecutionTimeMs) {
        if (steps == null || steps.isEmpty()) {
            updateStatus("[INFO] Nessuno step da visualizzare o algoritmo non produce step.");
            if (finalArrayState != null) {
                updateVisualizationFromArrayState(finalArrayState);
                currentArray = finalArrayState.clone();
            }
            animateCompletion();
            startButton.setDisable(false);

            return;
        }

        Timeline timeline = new Timeline();
        double baseDelayPerStep = Math.max(50, 2500.0 / speedSlider.getValue() / (steps.size() > 100 ? Math.log(steps.size()) : 1)); // Più veloce per molti step
        double minorOpFactor = 0.5; // Gli step non "StateChange" sono più veloci

        double accumulatedDelay = 0;

        for (int k = 0; k < steps.size(); k++) {
            CountingSortStep step = steps.get(k);
            final int currentStepIndexInAnimation = k;

            KeyFrame kf = new KeyFrame(Duration.millis(accumulatedDelay), e -> {
                updateStatus(step.getDescription());
                updateVisualizationFromArrayState(step.getArrayState());
                resetAllElementEffects();

                if (step.getPrimaryIndex() != -1) {
                    highlightElement(step.getPrimaryIndex(), Color.YELLOWGREEN, true);
                }
                if (step.getSecondaryIndex() != -1) {
                    // Se è lo stesso del primario (es. pivot), usa un altro colore o non evidenziare due volte
                    if (step.getSecondaryIndex() != step.getPrimaryIndex()){
                        highlightElement(step.getSecondaryIndex(), Color.LIGHTCORAL, true);
                    } else {
                         highlightElement(step.getSecondaryIndex(), Color.GOLD, true); // Colore diverso se è lo stesso indice
                    }
                }
                progressBar.setProgress((double)(currentStepIndexInAnimation + 1) / steps.size());
            });
            timeline.getKeyFrames().add(kf);
            accumulatedDelay += step.isStateChange() ? baseDelayPerStep : baseDelayPerStep * minorOpFactor;
        }
        
        // KeyFrame per lo stato finale e pulizia
        KeyFrame finalKeyFrame = new KeyFrame(Duration.millis(accumulatedDelay + baseDelayPerStep), e -> {
            updateVisualizationFromArrayState(finalArrayState);
            currentArray = finalArrayState.clone(); // Aggiorna l'array GUI con lo stato finale
            resetAllElementEffects();
            animateCompletion();
            String algoName = steps.get(0).getAlgorithm();
            updateStatus("[COMPLETATO] " + algoName + " completato! Tempo Engine: " + engineExecutionTimeMs + "ms.");
            startButton.setDisable(false);
            progressBar.setProgress(1.0);
        });
        timeline.getKeyFrames().add(finalKeyFrame);

        timeline.play();
    }

    private void highlightElement(int index, Color color, boolean temporary) {
        if (index >= 0 && index < dataElements.size()) {
            Rectangle element = dataElements.get(index);
            if (element == null) return;

            // Salva effetto e stroke originali se non già fatto (per ripristino più preciso)
            // Object originalEffect = element.getUserData() instanceof Effect ? element.getUserData() : element.getEffect();
            // if (!(element.getUserData() instanceof Effect)) element.setUserData(originalEffect);
            
            element.setStroke(color.interpolate(Color.BLACK, 0.3)); 
            element.setStrokeWidth(2); 

            DropShadow highlightGlow = new DropShadow(12, color); // Raggio e colore per evidenziazione
            // Composizione effetti: glow + (forse) il base glow
            // element.setEffect(new Blend(BlendMode.ADD, baseGlow, highlightGlow));
            element.setEffect(highlightGlow);


            ScaleTransition st = new ScaleTransition(Duration.millis(100), element);
            st.setToX(1.05);
            st.setToY(1.05);
            st.setAutoReverse(true);
            st.setCycleCount(2);
            st.play();
        }
    }

    private void resetAllElementEffects() {
        for (Rectangle element : dataElements) {
            if (element == null) continue;
            element.setStroke(null);
            // Ripristina effetto base
            // if (element.getUserData() instanceof Effect) {
            //    element.setEffect((Effect)element.getUserData());
            // } else {
                DropShadow baseGlow = new DropShadow();
                baseGlow.setColor(Color.rgb(102,126,234,0.7)); // Colore glow base
                baseGlow.setRadius(5);
                element.setEffect(baseGlow);
            // }
        }
    }
    
 // Example animateCompletion method (adjusted with the fix)
    private void animateCompletion() {
        Timeline rainbowWave = new Timeline();
        for (int i = 0; i < dataElements.size(); i++) {
            final Rectangle element = dataElements.get(i);
            if (element == null) continue;

            FillTransition ft = new FillTransition(Duration.millis(500), element);
            
            // Fix for ClassCastException: LinearGradient cannot be cast to Color
            // Check the current fill type before setting the 'from' value
            Paint currentFill = element.getFill();
            if (currentFill instanceof Color) {
                ft.setFromValue((Color) currentFill);
            } else if (currentFill instanceof LinearGradient) {
                LinearGradient lg = (LinearGradient) currentFill;
                if (!lg.getStops().isEmpty()) {
                    ft.setFromValue(lg.getStops().get(0).getColor()); // Use the first color of the gradient
                } else {
                    ft.setFromValue(Color.LIGHTGRAY); // Fallback if gradient is empty
                }
            } else {
                ft.setFromValue(Color.LIGHTGRAY); // Fallback for other Paint types or null
            }

            ft.setToValue(Color.hsb( (i * 360.0 / dataElements.size()) % 360, 0.8, 0.9));
            ft.setDelay(Duration.millis(i * (500.0 / dataElements.size()) ));
            
            ScaleTransition st = new ScaleTransition(Duration.millis(250), element);
            st.setToY(1.1);
            st.setToX(1.02);
            st.setAutoReverse(true);
            st.setCycleCount(2);
            st.setDelay(Duration.millis(i * (500.0 / dataElements.size()) ));

            ParallelTransition pt = new ParallelTransition(ft, st);
            pt.play();
        }
        
        rainbowWave.setOnFinished(e -> {
            // Ritorno ai colori originali con un un fade
            for (int i = 0; i < dataElements.size(); i++) {
                 final Rectangle el = dataElements.get(i);
                 if (el == null) continue;

                 // Corrected part from previous turn: Use Timeline with KeyValue to set fillProperty to LinearGradient
                 Timeline resetFillTimeline = new Timeline(
                     new KeyFrame(Duration.millis(300 + (i * (200.0/dataElements.size()))), // Add a small base duration for the transition itself
                         new KeyValue(el.fillProperty(), createGradientFill(), Interpolator.EASE_OUT)
                     )
                 );
                 resetFillTimeline.play();
            }
        });
        
        // Add a KeyFrame to the rainbowWave timeline to ensure its setOnFinished event fires.
        // The duration should be long enough for all individual ParallelTransitions to complete.
        double maxAnimationDuration = (dataElements.size() - 1) * (500.0 / dataElements.size()) + 500; // Max delay + max FillTransition duration
        rainbowWave.getKeyFrames().add(new KeyFrame(Duration.millis(maxAnimationDuration)));
        rainbowWave.play();
    }



    private void resetVisualization() {
        // Non pulire performanceChart qui, per mantenere la cronologia delle performance
        if (currentArray != null && currentArray.length > 0) {
             generateRandomArray((int)arraySizeSlider.getValue()); 
        } else { // Se non c'è array, pulisci l'area
            visualizationArea.getChildren().removeIf(node -> node instanceof HBox && "arrayBox".equals(node.getId()));
            dataElements.clear();
        }
        progressBar.setProgress(0);
        updateStatus("[RESET] Visualizzazione resettata. Genera o esegui.");
        resetAllElementEffects();
        startButton.setDisable(false);
    }
    
    private void resetVisualizationState() { // Chiamato dopo la generazione dell'array
        progressBar.setProgress(0);
        resetAllElementEffects();
        startButton.setDisable(false);
        // Non cancella i dati del grafico delle performance qui
    }

    private void updateStatus(String message) {
        if (statusLabel.getText().equals(message)) return; // Evita animazioni inutili per lo stesso messaggio

        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), statusLabel);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            statusLabel.setText(message);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(250), statusLabel);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}