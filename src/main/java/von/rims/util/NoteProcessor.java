package von.rims.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class NoteProcessor {
    public static final int MIN_OCTAVE = -3;
    public static final int MAX_OCTAVE = 5;
    public static final int MIN_NOTE = 1;
    public static final int MAX_NOTE = 12;

    public static List<List<Integer>> processNotes(String inputFile, int transposeSemitones, String outputFile) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<List<Integer>> notes = mapper.readValue(new File(inputFile), new TypeReference<>(){});
            List<List<Integer>> transposedNotes = transposeNotes(notes, transposeSemitones);
            mapper.writeValue(new File(outputFile), transposedNotes);
            return transposedNotes;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static List<List<Integer>> transposeNotes(List<List<Integer>> notes, int transposeSemitones) {
        return notes.stream()
                .map(notePair -> transposeSingleNotePair(notePair, transposeSemitones))
                .collect(Collectors.toList());
    }

    private static List<Integer> transposeSingleNotePair(List<Integer> notePair, int semitones) {
        if (notePair == null || notePair.size() != 2) {
            throw new IllegalArgumentException("Every pair should contain [$octaveNumber, $noteNumber]");
        }

        int octave = notePair.get(0);
        int noteNumber = notePair.get(1);

        int newNoteNumber = noteNumber + semitones;

        while (newNoteNumber < MIN_NOTE) {
            newNoteNumber += MAX_NOTE;
            octave--;
        }
        while (newNoteNumber > MAX_NOTE){
            newNoteNumber -= MAX_NOTE;
            octave++;
        }

        validateNoteAndOctave(octave, newNoteNumber);

        return List.of(octave, newNoteNumber);
    }

    private static void validateNoteAndOctave(int octave, int noteNumber) {
        if (octave < MIN_OCTAVE || octave > MAX_OCTAVE) {
            throw new IllegalArgumentException("Invalid octave: " + octave);
        }
        if ((octave == -3 && noteNumber < 10) || (octave == 5 && noteNumber > 1)) {
            throw new IllegalArgumentException("Invalid note " + noteNumber + " for octave " + octave);
        }
    }
}