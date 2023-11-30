package von.rims;


import von.rims.util.NoteProcessor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class App {

    public static void main(String[] args) {
        if (args.length != 2){
            System.out.println("Required 2 parameters: an input JSON file with a collection of notes and a number of semitones to transpose to (can be negative)");
            return;
        }

        String inputFile = args[0];
        int transposeSemitones = Integer.parseInt(args[1]);

        String outputFile = generateOutputFileName(inputFile);

        NoteProcessor.processNotes(inputFile, transposeSemitones, outputFile);
    }

    private static String generateOutputFileName(String inputPath){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmm");
        String dateTime = formatter.format(new Date());

        int lastDotIndex = inputPath.lastIndexOf(".");
        String basePath = (lastDotIndex != -1) ? inputPath.substring(0, lastDotIndex) : inputPath;

        return basePath + "_output_" + dateTime + ".json";
    }
}
