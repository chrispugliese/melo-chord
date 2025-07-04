package com.melochord.generators.chord;

import java.util.*;

public class ChordProgressionGenerator {


    public static final Map<String, Integer> ROMAN_TO_DEGREE = Map.ofEntries(
        Map.entry("I", 0),
        Map.entry("ii", 1),
        Map.entry("iii", 2), 
        Map.entry("IV", 3),
        Map.entry("V", 4), 
        Map.entry("vi", 5),
        Map.entry("vii", 6),
        Map.entry("vii°", 6),
        Map.entry("i", 0),
        Map.entry("II", 1),
        Map.entry("III", 2),
        Map.entry("iv", 3),
        Map.entry("v", 4),
        Map.entry("VI", 5),
        Map.entry("VII", 6)
    );

    public static final Map<String, List<String[]>> PROGRESSION_TEMPLATES = new HashMap<>(Map.of(
        "pop", List.of(
            new String[]{"I", "V", "vi", "IV"},
            new String[]{"vi", "IV", "I", "V"},
            new String[]{"I", "vi", "IV", "V"},
            new String[]{"I", "IV", "V", "IV"}
        ),

        "rock", List.of(
            new String[]{"I", "IV", "V", "I"},
            new String[]{"i", "VI", "iii", "vii"}

        ),
        "r&b", List.of(
            new String[]{"ii", "V", "I"},
            new String[]{"IV", "V", "iii", "vi"},
            new String[]{"I", "iii", "IV", "iv"}

        ),
        "jazz", List.of(
            new String[]{"ii", "V", "I"},
            new String[]{"I", "vi", "ii", "V"}
        ),
        "classical", List.of(
            new String[]{"I", "IV", "V", "I"},
            new String[]{"I", "vi", "ii", "V"}, 
            new String[]{"vi", "ii", "V", "I"}, 
            new String[]{"I", "V", "vi", "iii", "IV", "I", "IV", "V"}
        )        
    ));

    public static List<String[]> generateProgression(String rootNote, String scaleType, String genre, int templateIndex) {

        List<String[]> templates = PROGRESSION_TEMPLATES.get(genre.toLowerCase());
        if (templates == null) {
            throw new IllegalArgumentException("Unsupported genre: " + genre);
        }
        if (templateIndex < 0 || templateIndex >= templates.size()) {
            throw new IllegalArgumentException("Template index out of bounds: " + templateIndex);
        }

        // now pick *that* one, not a random one
        String[] chosenProgression = templates.get(templateIndex);

        // (the rest is identical to your original)
        String[] scale = ChordGenerator.generateScale(rootNote, scaleType);
        List<String> scaleList = Arrays.asList(scale);
        List<String[]> progressionChords = new ArrayList<>();

        for (String roman : chosenProgression) {
            Integer degree = ROMAN_TO_DEGREE.get(roman);
            if (degree == null) {
                throw new IllegalArgumentException("Unsupported chord: " + roman);
            }
            String[] chord = ChordGenerator.buildChord(scaleList.get(degree), "Major");
            progressionChords.add(chord);
        }

        return progressionChords;
    }

        public static Set<String> getAvailableProgressions(){
            return PROGRESSION_TEMPLATES.keySet();
        }

        public static List<String> getProgressionTemplates(String genre) {
            List<String[]> raw = PROGRESSION_TEMPLATES.get(genre.toLowerCase());
            if (raw == null) return List.of();

            List<String> readable = new ArrayList<>();
            for (String[] progression : raw) {
                readable.add(String.join(" - ", progression));
        }
        return readable;
    }


    
}


