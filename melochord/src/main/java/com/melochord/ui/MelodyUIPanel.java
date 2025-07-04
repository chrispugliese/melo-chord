package com.melochord.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import com.melochord.app.MidiExporter;
import com.melochord.generators.melody.MelodyGenerator;
import com.melochord.generators.melody.ScaleGenerator;

import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;

import java.io.File;
import java.time.LocalDateTime;

public class MelodyUIPanel extends VBox {
    private final Label outputLabel = new Label("Waiting for input...");
    private final String[] melodyText = {""};
    private final MelodyGenerator[] generator = {new MelodyGenerator()};

    public MelodyUIPanel(Window parentWindow) {
        setSpacing(10);
        setPadding(new Insets(20));

        ComboBox<String> rootNoteBox = new ComboBox<>();
        rootNoteBox.getItems().addAll("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B");
        rootNoteBox.setValue("C");

        ComboBox<String> scaleTypeBox = new ComboBox<>();
        scaleTypeBox.getItems().addAll(ScaleGenerator.getAvailableScaleTypes());
        scaleTypeBox.setValue("Dorian");

        TextField noteCountField = new TextField("8");
        noteCountField.setPromptText("Number of notes");

        TextField bpmField = new TextField("120");
        bpmField.setPromptText("BPM (60–200)");

        ComboBox<String> noteLengthBox = new ComboBox<>();
        noteLengthBox.getItems().addAll(MelodyGenerator.getNoteLengths());
        noteLengthBox.setValue("Quarter");

        CheckBox randomizeLengthsBox = new CheckBox("Randomize note lengths");

        ComboBox<Integer> minOctaveBox = new ComboBox<>();
        ComboBox<Integer> maxOctaveBox = new ComboBox<>();
        for (int i = 1; i <= 8; i++) {
            minOctaveBox.getItems().add(i);
            maxOctaveBox.getItems().add(i);
        }
        minOctaveBox.setValue(3);
        maxOctaveBox.setValue(5);

        Button generateButton = new Button("Generate Melody");
        Button updateButton = new Button("🔄 Update Timing");
        Button playButton = new Button("▶️ Play");
        Button downloadButton = new Button("💾 Download");

        Runnable updateMelodyTiming = () -> {
            if (generator[0].hasMelody()) {
                try {
                    int bpm = Integer.parseInt(bpmField.getText());
                    if (bpm < 60 || bpm > 200) {
                        outputLabel.setText("Error: BPM must be between 60 and 200");
                        return;
                    }
                    generator[0].setBPM(bpm);

                    String noteLength = MelodyGenerator.getNoteLengthByName(noteLengthBox.getValue());
                    generator[0].setNoteLength(noteLength);
                    generator[0].setRandomizeNoteLengths(randomizeLengthsBox.isSelected());

                    melodyText[0] = generator[0].formatMelodyWithTiming();
                    outputLabel.setText("Melody (updated timing): " + melodyText[0]);
                } catch (NumberFormatException ex) {
                    outputLabel.setText("Error: Please enter valid numbers for BPM");
                }
            } else {
                outputLabel.setText("No melody to update. Generate a melody first.");
            }
        };

        generateButton.setOnAction(e -> {
            try {
                String root = rootNoteBox.getValue();
                String scale = scaleTypeBox.getValue();
                int length = Integer.parseInt(noteCountField.getText());
                int minOctave = minOctaveBox.getValue();
                int maxOctave = maxOctaveBox.getValue();
                int bpm = Integer.parseInt(bpmField.getText());

                if (bpm < 60 || bpm > 200) {
                    outputLabel.setText("Error: BPM must be between 60 and 200");
                    return;
                }

                String noteLength = MelodyGenerator.getNoteLengthByName(noteLengthBox.getValue());

                generator[0] = new MelodyGenerator();
                generator[0].setOctaveRange(minOctave, maxOctave);
                generator[0].setBPM(bpm);
                generator[0].setNoteLength(noteLength);
                generator[0].setRandomizeNoteLengths(randomizeLengthsBox.isSelected());

                melodyText[0] = generator[0].generateMelody(root, scale, length);
                outputLabel.setText("Melody: " + melodyText[0]);
            } catch (NumberFormatException ex) {
                outputLabel.setText("Error: Please enter valid numbers for BPM and melody length");
            } catch (Exception ex) {
                outputLabel.setText("Error: " + ex.getMessage());
            }
        });

        updateButton.setOnAction(e -> updateMelodyTiming.run());

        playButton.setOnAction(e -> {
            if (melodyText[0].isEmpty()) {
                outputLabel.setText("No melody to play");
                return;
            }

            try {
                int bpm = Integer.parseInt(bpmField.getText());
                Pattern pattern = new Pattern("T" + bpm + " " + melodyText[0]);
                new Player().play(pattern);
            } catch (NumberFormatException ex) {
                outputLabel.setText("Error: Invalid BPM value");
            }
        });

        downloadButton.setOnAction(e -> {
            if (melodyText[0].isEmpty()) {
                outputLabel.setText("⚠️ No melody to download!");
                return;
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Melody As...");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MIDI files (*.mid)", "*.mid"));

            String root = rootNoteBox.getValue();
            String scale = scaleTypeBox.getValue();
            String timestamp = LocalDateTime.now().toString().replaceAll("[:.]", "-");
            fileChooser.setInitialFileName("melody_" + root + "_" + scale + "_" + timestamp + ".mid");

            File file = fileChooser.showSaveDialog(parentWindow);
            if (file != null) {
                try {
                    int bpm = Integer.parseInt(bpmField.getText());
                    Pattern pattern = new Pattern("V0 I[Piano] " + melodyText[0]);
                    boolean success = MidiExporter.exportAsFormat1(pattern, file.getAbsolutePath(), bpm);
                    outputLabel.setText(success ? "Saved to: " + file.getAbsolutePath() : "Could not export MIDI file");
                } catch (NumberFormatException ex) {
                    outputLabel.setText("Error: Invalid BPM value");
                }
            } else {
                outputLabel.setText("Save canceled.");
            }
        });

        getChildren().addAll(
            new Label("Root Note:"), rootNoteBox,
            new Label("Scale Type:"), scaleTypeBox,
            new Label("Melody Length:"), noteCountField,
            new Label("BPM:"), bpmField,
            new Label("Note Length:"), noteLengthBox,
            randomizeLengthsBox,
            new Label("Octave Range:"), new HBox(5, new Label("Min:"), minOctaveBox, new Label("Max:"), maxOctaveBox),
            new HBox(10, generateButton, updateButton),
            new HBox(10, playButton, downloadButton),
            outputLabel
        );
    }
}
