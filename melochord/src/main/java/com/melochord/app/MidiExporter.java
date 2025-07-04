package com.melochord.app;

import org.jfugue.pattern.Pattern;
import org.jfugue.midi.MidiFileManager;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

public class MidiExporter {

    // Export with optional tempo (use -1 if you don't want to include tempo)
    public static boolean exportAsFormat1(Pattern pattern, String filename, int bpm) {
        try {
            // Save pattern to a temporary MIDI file
            File tempFile = File.createTempFile("temp_melody", ".mid");
            MidiFileManager.savePatternToMidi(pattern, tempFile);

            Sequence original = MidiSystem.getSequence(tempFile);
            Sequence format1 = new Sequence(Sequence.PPQ, original.getResolution(), 2);

            Track source = original.getTracks()[0];
            Track midiTrack = format1.createTrack();

            // If BPM is valid, add tempo meta event at tick 0
            if (bpm > 0) {
                int tempo = 60000000 / bpm;
                MetaMessage tempoMessage = new MetaMessage();
                tempoMessage.setMessage(0x51, new byte[]{
                        (byte)(tempo >> 16), (byte)(tempo >> 8), (byte)tempo
                }, 3);
                midiTrack.add(new MidiEvent(tempoMessage, 0));
            }

            // Copy original events into the new track
            for (int i = 0; i < source.size(); i++) {
                midiTrack.add(source.get(i));
            }

            // Save to the desired location
            File midiFile = new File(filename);
            MidiSystem.write(format1, 1, midiFile);

            System.out.println("Exported MIDI in Format 1 to: " + midiFile.getAbsolutePath()
                    + (bpm > 0 ? " with BPM: " + bpm : ""));
            return true;

        } catch (IOException | InvalidMidiDataException e) {
            System.err.println("Failed to export MIDI: " + e.getMessage());
            return false;
        }
    }

    // Overloaded convenience methods
    public static boolean exportAsFormat1(Pattern pattern, String filename) {
        return exportAsFormat1(pattern, filename, -1); // No tempo event
    }
}
