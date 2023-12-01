package von.rims.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NoteProcessorTest {
    private static List<Integer> notePair;

    private static Method transposeMethod;
    private static Method validateMethod;

    @BeforeAll
    static void setUp() throws NoSuchMethodException {
        notePair = List.of(2, 5);

        transposeMethod = NoteProcessor.class.getDeclaredMethod("transposeSingleNotePair", List.class, int.class);
        transposeMethod.setAccessible(true);

        validateMethod = NoteProcessor.class.getDeclaredMethod("validateNoteAndOctave", int.class, int.class);
        validateMethod.setAccessible(true);
    }

    @Test
    void transposeSingleNotePair_withPositiveShift() throws InvocationTargetException, IllegalAccessException {
        int semitones = 4;
        List<Integer> result = (List<Integer>) transposeMethod.invoke(null, notePair, semitones);

        assertEquals(List.of(2, 9), result, "Transposing 2nd octave 5th note by 4 semitones should result in 2nd octave 9th note");
    }

    @Test
    void transposeSingleNotePair_withNegativeShift() throws InvocationTargetException, IllegalAccessException {
        int semitones = -4;
        List<Integer> result = (List<Integer>) transposeMethod.invoke(null, notePair, semitones);

        assertEquals(List.of(2, 1), result, "Transposing 2nd octave 5th note by 4 semitones should result in 2nd octave 1st note");
    }

    @Test
    void transposeSingleNotePair_withPositiveShiftChangingOctave() throws InvocationTargetException, IllegalAccessException {
        int semitones = 8;
        List<Integer> result = (List<Integer>) transposeMethod.invoke(null, notePair, semitones);

        assertEquals(List.of(3, 1), result, "Transposing 2nd octave 5th note by 8 semitones should result in 3rd octave 1st note");
    }

    @Test
    void transposeSingleNotePair_withNegativeShiftChangingOctave() throws InvocationTargetException, IllegalAccessException {
        int semitones = -6;
        List<Integer> result = (List<Integer>) transposeMethod.invoke(null, notePair, semitones);

        assertEquals(List.of(1, 11), result, "Transposing 2nd octave 5st note by -6 semitones should result in 1st octave 11th note");
    }

    @Test
    void validateNoteAndOctave_InvalidOctave() {
        int octave = 6;
        int noteNumber = 5;

        Exception exception = assertThrows(InvocationTargetException.class, () -> {
            validateMethod.invoke(null, octave, noteNumber);
        }, "InvocationTargetException expected");

        assertTrue(exception.getCause() instanceof IllegalArgumentException, "Exception should be thrown for invalid octave");
    }

    @Test
    void validateNoteAndOctave_InvalidNoteInOctave() {
        int octave = -3;
        int noteNumber = 9;

        Exception exception = assertThrows(InvocationTargetException.class, () -> {
            validateMethod.invoke(null, octave, noteNumber);
        }, "InvocationTargetException expected");

        assertTrue(exception.getCause() instanceof IllegalArgumentException, "Exception should be thrown for invalid note in octave");
    }
}