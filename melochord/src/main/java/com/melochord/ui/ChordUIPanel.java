package com.melochord.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;

import com.melochord.app.MidiExporter;
import com.melochord.generators.chord.ChordGenerator;
import com.melochord.generators.chord.ChordProgressionGenerator;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public class ChordUIPanel extends VBox {
    private List<String[]> currentChords;
    private final Label outputLabel = new Label("Waiting for input...");

    public ChordUIPanel(Window parentWindow) {
        setSpacing(10);
        setPadding(new Insets(20));

        ComboBox<String> rootNoteBox = new ComboBox<>();
        rootNoteBox.getItems().addAll("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B");
        rootNoteBox.setValue("Choose Root Note");

        ComboBox<String> scaleTypeBox = new ComboBox<>();
        scaleTypeBox.getItems().addAll(ChordGenerator.getAvailableScaleTypes());
        scaleTypeBox.setValue("Choose Scale");

        ComboBox<String> progressionTypeBox = new ComboBox<>();
        progressionTypeBox.getItems().addAll(ChordProgressionGenerator.getAvailableProgressions());
        progressionTypeBox.setValue("Choose Genre");

        ComboBox<String> progressionTemplateBox = new ComboBox<>();
        progressionTemplateBox.setDisable(true);

        progressionTypeBox.setOnAction(e -> {
            String selectedGenre = progressionTypeBox.getValue();
            progressionTemplateBox.getItems().clear();
            progressionTemplateBox.getItems().addAll(ChordProgressionGenerator.getProgressionTemplates(selectedGenre));
            progressionTemplateBox.setDisable(false);
            if (!progressionTemplateBox.getItems().isEmpty()) {
                progressionTemplateBox.setValue(progressionTemplateBox.getItems().get(0));
            }
        });

        Button generateButton = new Button("Generate");
        Button playButton = new Button("Play");
        Button downloadButton = new Button("Download MIDI");

        generateButton.setOnAction(e -> {
            String genre = progressionTypeBox.getValue();
            if (genre == null || genre.isEmpty()) {
                outputLabel.setText("Please select a progression genre.");
                return;
            }

            int selectedTemplateIndex = progressionTemplateBox.getSelectionModel().getSelectedIndex();
            if (selectedTemplateIndex == -1) {
                outputLabel.setText("Please select a progression template.");
                return;
            }

            List<String[]> templates = ChordProgressionGenerator.PROGRESSION_TEMPLATES.get(genre.toLowerCase());
            if (templates == null || selectedTemplateIndex >= templates.size()) {
                outputLabel.setText("Invalid template selection.");
                return;
            }

            try {
                currentChords = ChordProgressionGenerator.generateProgression(
                        rootNoteBox.getValue(),
                        scaleTypeBox.getValue(),
                        genre,
                        selectedTemplateIndex
                );

                StringBuilder sb = new StringBuilder("Chords:\n");
                for (String[] chord : currentChords) {
                    sb.append(String.join("-", chord)).append("\n");
                }
                outputLabel.setText(sb.toString());
            } catch (Exception ex) {
                outputLabel.setText("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        playButton.setOnAction(e -> {
            if (currentChords == null || currentChords.isEmpty()) {
                outputLabel.setText("Generate a chord progression first.");
                return;
            }

            StringBuilder patternBuilder = new StringBuilder();
            for (String[] chord : currentChords) {
                patternBuilder.append(String.join("+", chord)).append("w ");
            }

            Pattern pattern = new Pattern(patternBuilder.toString().trim());
            try {
                Player player = new Player();
                player.play(pattern);
            } catch (Exception ex) {
                outputLabel.setText("Failed to play: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        downloadButton.setOnAction(e -> {
            if (currentChords == null || currentChords.isEmpty()) {
                outputLabel.setText("Generate a chord progression first.");
                return;
            }

            Pattern pattern = new Pattern("V0 I[Piano]");
            for (String[] chord : currentChords) {
                pattern.add(String.join("+", chord) + "w ");
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Chord Progression As...");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MIDI files (*.mid)", "*.mid"));

            String root = rootNoteBox.getValue();
            String scale = scaleTypeBox.getValue();
            String timestamp = LocalDateTime.now().toString().replaceAll("[:.]", "-");
            fileChooser.setInitialFileName("chord_progression_" + root + "_" + scale + "_" + timestamp + ".mid");

            File file = fileChooser.showSaveDialog(parentWindow);

            if (file != null) {
                boolean success = MidiExporter.exportAsFormat1(pattern, file.getAbsolutePath());
                outputLabel.setText(success ? "Saved to: " + file.getAbsolutePath() : "Could not export MIDI file");
            } else {
                outputLabel.setText("Save canceled.");
            }
        });

        getChildren().addAll(
                new Label("Root Note:"), rootNoteBox,
                new Label("Scale Type:"), scaleTypeBox,
                new Label("Progression Type:"), progressionTypeBox,
                new Label("Progression Template:"), progressionTemplateBox,
                new HBox(10, generateButton, playButton, downloadButton),
                outputLabel
        );
    }
}
