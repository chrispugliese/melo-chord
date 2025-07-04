package com.melochord.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import com.melochord.ui.ChordUIPanel;
import com.melochord.ui.MelodyUIPanel;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();

        // Chord tab
        Tab chordTab = new Tab("Chords");
        chordTab.setContent(new ChordUIPanel(primaryStage));
        chordTab.setClosable(false);

        // Melody tab
        Tab melodyTab = new Tab("Melody");
        melodyTab.setContent(new MelodyUIPanel(primaryStage));
        melodyTab.setClosable(false);

        // Add tabs to pane
        tabPane.getTabs().addAll(chordTab, melodyTab);

        // Set up the stage
        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setTitle("MeloChord");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}