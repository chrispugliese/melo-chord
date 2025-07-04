package com.melochord.generators.chord;

import java.util.*;


public class ChordGenerator {
        private static final String[] CHROMATIC = {
        "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"
    };

    private static final Map<String, int[]> SCALE_PATTERNS = new HashMap<>();
    static{
        SCALE_PATTERNS.put("Major (Ionian)",            new int[]{2, 2, 1, 2, 2, 2, 1});
        SCALE_PATTERNS.put("Natural Minor (Aeolian)",   new int[]{2, 1, 2, 2, 1, 2, 2});
        SCALE_PATTERNS.put("Harmonic Minor",            new int[]{2, 1, 2, 2, 1, 3, 1});
        SCALE_PATTERNS.put("Melodic Minor",             new int[]{2, 1, 2, 2, 2, 2, 1});

 
        SCALE_PATTERNS.put("Dorian",                    new int[]{2, 1, 2, 2, 2, 1, 2});
        SCALE_PATTERNS.put("Phrygian",                  new int[]{1, 2, 2, 2, 1, 2, 2});
        SCALE_PATTERNS.put("Lydian",                    new int[]{2, 2, 2, 1, 2, 2, 1});
        SCALE_PATTERNS.put("Mixolydian",                new int[]{2, 2, 1, 2, 2, 1, 2});
        SCALE_PATTERNS.put("Aeolian (Natural Minor)",   new int[]{2, 1, 2, 2, 1, 2, 2});
        SCALE_PATTERNS.put("Locrian",                   new int[]{1, 2, 2, 1, 2, 2, 2});

        // Pentatonic
        SCALE_PATTERNS.put("Major Pentatonic",          new int[]{2, 2, 3, 2, 3});
        SCALE_PATTERNS.put("Minor Pentatonic",          new int[]{3, 2, 2, 3, 2});

        // Blues
        SCALE_PATTERNS.put("Blues Scale",               new int[]{3, 2, 1, 1, 3, 2});
        SCALE_PATTERNS.put("Minor Blues",               new int[]{3, 2, 1, 1, 3, 2});
        SCALE_PATTERNS.put("Major Blues",               new int[]{2, 1, 1, 3, 2, 3});

        // Whole Tone & Octatonic
        SCALE_PATTERNS.put("Whole Tone",                new int[]{2, 2, 2, 2, 2, 2});
        SCALE_PATTERNS.put("Octatonic (Half-Whole)",    new int[]{1, 2, 1, 2, 1, 2, 1, 2});
        SCALE_PATTERNS.put("Octatonic (Whole-Half)",    new int[]{2, 1, 2, 1, 2, 1, 2, 1});

        // Chromatic Scale
        SCALE_PATTERNS.put("Chromatic",                 new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
    }

    private static final Map<String, int[]> CHORD_SHAPES = new HashMap<>();
    static{
        CHORD_SHAPES.put("Major",           new int[]{0, 4, 7});
        CHORD_SHAPES.put("Minor",           new int[]{0, 3, 7});
        CHORD_SHAPES.put("Diminished",      new int[]{0, 3, 6});
        CHORD_SHAPES.put("Augmented",       new int[]{0, 4, 8});
        CHORD_SHAPES.put("Sus2",            new int[]{0, 2, 7});
        CHORD_SHAPES.put("Sus4",            new int[]{0, 5, 7});

        // 7th chords
        CHORD_SHAPES.put("Major7",          new int[]{0, 4, 7, 11});
        CHORD_SHAPES.put("Dominant7",       new int[]{0, 4, 7, 10});
        CHORD_SHAPES.put("Minor7",          new int[]{0, 3, 7, 10});
        CHORD_SHAPES.put("HalfDim7",        new int[]{0, 3, 6, 10});
        CHORD_SHAPES.put("Dim7",            new int[]{0, 3, 6, 9});
        CHORD_SHAPES.put("MinorMaj7",       new int[]{0, 3, 7, 11});
    }

    public static String[] generateScale(String rootNote, String scaleType){
        List<String> scale = new ArrayList<>();
        int[] intervals = SCALE_PATTERNS.get(scaleType);
        if (intervals == null){
            throw new IllegalArgumentException("Unsupported scale type: " + scaleType);
        }

        int index = Arrays.asList(CHROMATIC).indexOf(rootNote);
        if (index == -1){
            throw new IllegalArgumentException("Invalid root note: " + rootNote);
        }

            scale.add(CHROMATIC[index]);
            for (int i = 0; i < intervals.length - 1; i++) {
                index = (index + intervals[i]) % 12;
                scale.add(CHROMATIC[index]);
            }

            return scale.toArray(new String[0]);

        }

        public static Set<String> getAvailableScaleTypes(){
            return SCALE_PATTERNS.keySet();

        }

        public static String[] buildChord(String root, String chordType) {
            int[] intervals = CHORD_SHAPES.get(chordType);
            if (intervals == null) {
                throw new IllegalArgumentException("Unsupported chord type: " + chordType);
            }

            List<String> chord = new ArrayList<>();
            int rootIndex = Arrays.asList(CHROMATIC).indexOf(root);
            if(rootIndex == -1 ){
                throw new IllegalArgumentException("Invalid rootnote: " + root);
            }
            for (int interval : intervals){
                int noteIndex = (rootIndex + interval) % 12;
                chord.add(CHROMATIC[noteIndex]);
            }
            return chord.toArray(new String[0]);
        }

        // public static String[] buildTriad(List<String> scale, int degree){
        //     String root = scale.get(degree % scale.size());
        //     String third = scale.get((degree + 2) % scale.size());
        //     String fifth = scale.get((degree + 4) % scale.size());
        //     return new String[] {root, third, fifth};
        // }

        // public static List<String[]> generateChords(String rootNote, String scaleType) {
        //     String[] scale = generateScale(rootNote, scaleType);
        //     List<String> scaleList = Arrays.asList(scale);
        //     List<String[]> chords = new ArrayList<>();

        //     System.out.println("Generated scale: " + scaleList);  // Add this line
            
            
        //     for (int i = 0; i < scaleList.size(); i++){
        //         String[] triad = buildTriad(scaleList, i);
        //         System.out.println("Chord " + i + ": " + Arrays.toString(triad));  // Add this line
        //         chords.add(triad);

        //     }
            
        //     return chords;
        // }
}
