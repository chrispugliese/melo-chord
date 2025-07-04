package com.melochord.generators.melody;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MelodyGenerator {
    private Random random = new Random();

    private int minOctave = 3;
    private int maxOctave = 5;
    private int bpm = 120;
    private String noteLength = "q"; // Default to quarter notes
    private boolean randomizeNoteLengths = false;

    // Store the raw melody (notes and octaves only, no durations)
    private String[] rawMelody = null;

    // Note length mappings
    private static final String[] NOTE_LENGTHS = {"w", "h", "q", "i", "s"}; // whole, half, quarter, eighth, sixteenth
    private static final String[] NOTE_LENGTH_NAMES = {"Whole", "Half", "Quarter", "Eighth", "Sixteenth"};

    public String generateMelody(String rootNote, String scaleType, int length){
        String[] scale;
        try{
            scale = ScaleGenerator.generateScale(rootNote, scaleType);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return "";
        }

        // Generate raw melody (notes and octaves only)
        rawMelody = new String[length];
        for (int i = 0; i < length; i++){
            String note = scale[random.nextInt(scale.length)];
            int octave = minOctave + random.nextInt(maxOctave - minOctave + 1);
            rawMelody[i] = note + octave;
        }

        // Return the formatted melody with current timing settings
        return formatMelodyWithTiming();
    }

    public String formatMelodyWithTiming() {
        if (rawMelody == null || rawMelody.length == 0) {
            return "";
        }

        StringBuilder melody = new StringBuilder();
        for (String note : rawMelody) {
            melody.append(note);
            
            // Add note length
            if (randomizeNoteLengths) {
                String randomLength = NOTE_LENGTHS[random.nextInt(NOTE_LENGTHS.length)];
                melody.append(randomLength);
            } else {
                melody.append(noteLength);
            }
            
            melody.append(" ");
        }

        return melody.toString().trim();
    }

    public boolean hasMelody() {
        return rawMelody != null && rawMelody.length > 0;
    }

    public void setOctaveRange(int min, int max) {
        this.minOctave = min;
        this.maxOctave = max;
    }

    public void setBPM(int bpm) {
        this.bpm = bpm;
    }

    public int getBPM() {
        return bpm;
    }

    public void setNoteLength(String noteLength) {
        this.noteLength = noteLength;
    }

    public String getNoteLength() {
        return noteLength;
    }

    public void setRandomizeNoteLengths(boolean randomize) {
        this.randomizeNoteLengths = randomize;
    }

    public boolean isRandomizeNoteLengths() {
        return randomizeNoteLengths;
    }

    public static String getNoteLengthByName(String name) {
        for (int i = 0; i < NOTE_LENGTH_NAMES.length; i++) {
            if (NOTE_LENGTH_NAMES[i].equalsIgnoreCase(name)) {
                return NOTE_LENGTHS[i];
            }
        }
        return "q"; // default fallback
    }

    public static String[] getNoteLengths() {
        return NOTE_LENGTHS;
    }
}
