package org.cis1200;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtilities {

    public static BufferedReader fileToReader(String filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new IllegalArgumentException("File does not exist: " + filePath);
            }
            return new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Error reading file: " + filePath, e);
        }

    }

    public static boolean fileExists(String filePath) {

        // Create a File object
        File file = new File(filePath);

        // Check if the file exists
        return file.exists();
    }

    public static void writeStringsToFile(
            List<String> stringsToWrite, String filePath,
            boolean append
    ) {
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(filePath, append));
            for (String line : stringsToWrite) {
                bw.write(line);
                bw.write(",");
            }
            bw.newLine();
            bw.flush();
            bw.close();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath);
        }
    }

    public static void resetFile(
            String filePath, boolean isFirstPlayerAI, boolean isSecondPlayerAI
    ) {
        BufferedWriter bw;
        int[][] startingBoardState = {
                { 0, 2, 0, 2, 0, 2, 0, 2 },
                { 2, 0, 2, 0, 2, 0, 2, 0 },
                { 0, 2, 0, 2, 0, 2, 0, 2 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 1, 0, 1, 0, 1, 0, 1, 0 },
                { 0, 1, 0, 1, 0, 1, 0, 1 },
                { 1, 0, 1, 0, 1, 0, 1, 0 }
        };
        try {
            bw = new BufferedWriter(new FileWriter(filePath, false));
            // set players
            bw.write(isFirstPlayerAI ? "A" : "H");
            bw.write(isSecondPlayerAI ? "A" : "H");
            // bw.newLine();

            // boardstate
            bw.write(Arrays.deepToString(startingBoardState));
            bw.write(",");

            // current player
            bw.write(String.valueOf(1));
            bw.write(",");

            // num moves
            bw.write(String.valueOf(0));
            bw.write(",");

            // num moves after jump
            bw.write(String.valueOf(0));
            bw.newLine();

            bw.flush();
            bw.close();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath);
        }
    }

    public static void resetFile(String filePath) {
        String firstLine = FileUtilities.getFirstLine(filePath);
        BufferedWriter bw;
        int[][] startingBoardState = {
                { 0, 2, 0, 2, 0, 2, 0, 2 },
                { 2, 0, 2, 0, 2, 0, 2, 0 },
                { 0, 2, 0, 2, 0, 2, 0, 2 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 1, 0, 1, 0, 1, 0, 1, 0 },
                { 0, 1, 0, 1, 0, 1, 0, 1 },
                { 1, 0, 1, 0, 1, 0, 1, 0 }
        };
        try {
            bw = new BufferedWriter(new FileWriter(filePath, false));
            // set players
            bw.write(firstLine);
            // bw.newLine();

            // boardstate
            bw.write(Arrays.deepToString(startingBoardState));
            bw.write(",");

            // current player
            bw.write(String.valueOf(1));
            bw.write(",");

            // num moves
            bw.write(String.valueOf(0));
            bw.write(",");

            // num moves after jump
            bw.write(String.valueOf(0));
            bw.newLine();

            bw.flush();
            bw.close();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath);
        }
    }

    public static void writeLinesToFile(
            int[][] currentBoardState, int currentPlayer, int numMoves, int numMovesAfterJump,
            String filePath
    ) {
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(filePath, true));
            // boardstate
            bw.write(Arrays.deepToString(currentBoardState));
            bw.write(",");

            // current player

            bw.write(String.valueOf(currentPlayer));
            bw.write(",");

            // num moves
            bw.write(String.valueOf(numMoves));
            bw.write(",");

            // num moves after jump
            bw.write(String.valueOf(numMovesAfterJump));
            bw.newLine();

            bw.flush();
            bw.close();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath);
        }
    }

    public static void deleteLastLine(String filePath) {
        try {
            // Create a mutable list by wrapping the result in ArrayList
            List<String> lines = new ArrayList<>(
                    new BufferedReader(new FileReader(filePath)).lines().toList()
            );

            if (lines.isEmpty()) {
                return; // If no lines, do nothing
            }

            System.out.println(lines);
            System.out.println(lines.size() - 1);

            // Remove the last line
            lines.remove(lines.size() - 1);

            // Write the remaining lines back to the file
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false));
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }

            bw.flush();
            bw.close();
        } catch (IOException e) {
            System.err.println("Error deleting last line from file: " + filePath);
            e.printStackTrace();
        }

    }

    public static String getLastLine(String filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }

        String lastLine = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;

            // Read each line and keep updating lastLine
            while ((currentLine = reader.readLine()) != null) {
                lastLine = currentLine;
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            e.printStackTrace();
        }

        return lastLine;
    }

    public static String getFirstLine(String filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }

        String firstLine = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            firstLine = reader.readLine();
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            e.printStackTrace();
        }

        return firstLine;
    }

    public static int getNumMoves(String filePath) {
        String finalLine = FileUtilities.getLastLine(filePath);
        while (finalLine.equals("")) {
            FileUtilities.deleteLastLine(filePath);
            finalLine = FileUtilities.getLastLine(filePath);
        }
        String[] parts = finalLine.split(",");
        return Integer.parseInt(parts[parts.length - 2].trim());
    }

    public static int getNumMovesAfterJump(String filePath) {
        String finalLine = FileUtilities.getLastLine(filePath);

        while (finalLine.equals("")) {
            FileUtilities.deleteLastLine(filePath);
            finalLine = FileUtilities.getLastLine(filePath);
        }
        String[] parts = finalLine.split(",");
        return Integer.parseInt(parts[parts.length - 1].trim());
    }

    public static int[][] getBoardState(String filePath) {
        String finalLine = FileUtilities.getLastLine(filePath);
        while (finalLine.equals("")) {
            FileUtilities.deleteLastLine(filePath);
            finalLine = FileUtilities.getLastLine(filePath);
        }

        String arrayPart = finalLine.substring(0, finalLine.lastIndexOf(']') + 1);

        arrayPart = arrayPart.substring(1, arrayPart.length() - 1);
        String[] rows = arrayPart.split("], \\[");

        // Initialize the 2D array
        int[][] importedBoardState = new int[rows.length][];

        for (int i = 0; i < rows.length; i++) {
            // Remove any remaining brackets and split by commas
            String[] values = rows[i].replace("[", "").replace("]", "").split(", ");
            importedBoardState[i] = new int[values.length];

            for (int j = 0; j < values.length; j++) {
                importedBoardState[i][j] = Integer.parseInt(values[j]);
            }
        }

        return importedBoardState;
    }

}
