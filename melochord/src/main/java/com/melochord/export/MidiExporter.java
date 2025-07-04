package com.melochord.export;

import org.jfugue.pattern.Pattern;
import org.jfugue.midi.MidiFileManager;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;


public class MidiExporter {

        public static boolean exportAsFormat1(Pattern pattern, String filename){
        try{

            // convert jfugue pattern to midi sequence

            File tempFile = File.createTempFile("temp_melody", ".mid");
            MidiFileManager.savePatternToMidi(pattern, tempFile);

            Sequence original = MidiSystem.getSequence(tempFile);

            // create a new sequence in format 1 (2 tracks)
            Sequence format1 = new Sequence(Sequence.PPQ,  original.getResolution(), 2);

            format1.createTrack();

            // create a track and copy events from original
            Track source = original.getTracks()[0];
            Track chordTrack = format1.createTrack();
            for (int i = 0; i < source.size(); i++){
                chordTrack.add(source.get(i));
            }

            // write to file
            File midiFile = new File(filename);
            MidiSystem.write(format1, 1, midiFile);

            System.out.println("Exported MIDI in Format 1 to: " + midiFile.getAbsolutePath());
            return true;
        } catch (IOException | InvalidMidiDataException e){
            System.err.println("Failed to export MIDI: " + e.getMessage());
            return false;
        }
    }
}

