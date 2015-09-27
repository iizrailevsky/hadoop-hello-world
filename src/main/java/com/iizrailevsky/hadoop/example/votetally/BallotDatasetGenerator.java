package com.iizrailevsky.hadoop.example.votetally;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Generates random ballot dataset
 *
 */
public class BallotDatasetGenerator {

    private static final String[] candidates = {
        "Jeb Bush",
        "Ben Carson",
        "Chris Christie",
        "Ted Cruz",
        "Carly Fiorina",
        "Jim Gilmore",
        "Lindsey Graham",
        "Mike Huckabee",
        "Bobby Jindal",
        "John Kasich",
        "George Pataki",
        "Rand Paul",
        "Marco Rubio",
        "Rick Santorum",
        "Donald Trump  "
    };

    /**
     * Generates sample dataset for the Election Winner map-reduce program
     * @param fileNamePrefix File name prefix
     * @param size Number of ballot per file
     * @param numOfFiles Number of files to be generated
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     */
    public void generateDataset(String fileNamePrefix, int size, int numOfFiles)
            throws FileNotFoundException, UnsupportedEncodingException {
        // error checking
        if (fileNamePrefix == null || fileNamePrefix.equals("") || size < 1 || numOfFiles < 1) {
            throw new IllegalArgumentException("Invalid input for data generation!");
        }

        // init variables
        Random random = new Random();

        // create number of files requested
        for (int i = 0; i < numOfFiles; i++) {
            PrintWriter writer = new PrintWriter(fileNamePrefix + i, "UTF-8");
            for (int j = 0; j < size; j++) {
                int index = random.nextInt(candidates.length);
                writer.println(candidates[index]);
            }
            writer.close();
        }
    }

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        BallotDatasetGenerator gen = new BallotDatasetGenerator();
        gen.generateDataset("ballotsCounty", 1000, 5);
    }


}
