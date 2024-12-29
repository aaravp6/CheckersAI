package org.cis1200;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
    public static void main(String[] args) throws IOException {


        String checkersInstructions = "INSTRUCTIONS:\n" +
                "Welcome to Checkers! This game has 3 modes: " +
                "human vs human, human vs AI, & AI vs AI.\n"
                +
                "\n" +
                "Objective: Capture all of your opponent's pieces or block them so " +
                "they cannot make a move.\n"
                +
                "\n" +
                "Setup:\n" +
                "    - The game is played on an 8x8 board.\n" +
                "    - Each player starts with 12 pieces placed on the dark " +
                "squares of the first three rows on their side.\n"
                +
                "\n" +
                "Basic Rules:\n" +
                "    - Pieces can only move diagonally on dark squares.\n" +
                "    - On a turn, move one piece forward diagonally to an empty square.\n" +
                "    - If an opponent’s piece is diagonally adjacent and " +
                "the square beyond it is empty,\n"
                +
                "      you are NOT required to capture it (this is conventional for home games " +
                "but unconventional in tournaments).\n"
                +
                "    - For more casual gameplay, you are NOT allowed to double jump.\n" +
                "\n" +
                "Becoming a King:\n" +
                "    - When a piece reaches the opponent's back row, it becomes a King.\n" +
                "    - Kings can move and capture diagonally both forward and backward.\n" +
                "\n" +
                "Winning:\n" +
                "    - You win by capturing all of your opponent's pieces or " +
                "making it impossible for them to move.\n"
                +
                "\n" +
                "Draw:\n" +
                "    - The game ends in a tie if no moves can " +
                "lead to a win or 40 moves pass by where no piece is captured.";

        JOptionPane.showMessageDialog(
                null, checkersInstructions, "Welcome to Checkers!", JOptionPane.INFORMATION_MESSAGE
        );

        System.out.println("Starting main");

        // Creates the I/O Stuff
        final String PATH_TO_HISTORY = "game_history.csv";
        if (!FileUtilities.fileExists(PATH_TO_HISTORY)) {
            File file = new File(PATH_TO_HISTORY);
            file.createNewFile();
        }

        BufferedReader csvReader = FileUtilities.fileToReader(PATH_TO_HISTORY);
        List<String> stringList = new ArrayList<>();

        FileUtilities.writeStringsToFile(stringList, PATH_TO_HISTORY, true);
        System.out.println(FileUtilities.getLastLine(PATH_TO_HISTORY));

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

        // FileUtilities.writeStringsToFile(startingBoardState);

        Player p1;
        Player p2;
        GameLogic myGame;
        String[] optionsLoadGame = { "Load Game", "Reset Game" };
        var loadGame = JOptionPane.showOptionDialog(
                null, "Would you load the previous game?", "Select one:",
                0, 3, null, optionsLoadGame, optionsLoadGame[0]
        );
        if (loadGame == 0) {
            int[][] importedBoardState = FileUtilities.getBoardState(PATH_TO_HISTORY);
            myGame = new GameLogic(importedBoardState);
            String firstLine = FileUtilities.getFirstLine(PATH_TO_HISTORY);
            if (firstLine.charAt(0) == 'H') {
                p1 = new HumanPlayer(1, myGame);
            } else {
                p1 = new AIPlayer(1, myGame);
            }

            if (firstLine.charAt(1) == 'H') {
                p2 = new HumanPlayer(2, myGame);
            } else {
                p2 = new AIPlayer(2, myGame);
            }
            myGame.setNumMoves(FileUtilities.getNumMoves(PATH_TO_HISTORY));
            myGame.setNumMovesAfterJump(FileUtilities.getNumMovesAfterJump(PATH_TO_HISTORY));
            myGame.setCurrentPlayerNum((myGame.getNumMoves() % 2 == 0) ? 2 : 1);
        } else {
            myGame = new GameLogic(startingBoardState); // your existing game logic
            String[] options = { "2 Human Players", "1 Human Player, 1 AI Player", "2 AI Players" };
            var mode = JOptionPane.showOptionDialog(
                    null, "Which mode?", "Select one:",
                    0, 3, null, options, options[0]
            );

            if (mode == 0) {
                p1 = new HumanPlayer(1, myGame);
                p2 = new HumanPlayer(2, myGame);
                FileUtilities.resetFile(PATH_TO_HISTORY, false, false);
            } else if (mode == 1) {
                p1 = new HumanPlayer(1, myGame);
                p2 = new AIPlayer(2, myGame);
                FileUtilities.resetFile(PATH_TO_HISTORY, false, true);
            } else if (mode == 2) {
                p1 = new AIPlayer(1, myGame);
                p2 = new AIPlayer(2, myGame);
                FileUtilities.resetFile(PATH_TO_HISTORY, true, true);
            } else {
                JOptionPane.showMessageDialog(
                        null, "Error with game mode selection: will default to 2 humans"
                );
                p1 = new HumanPlayer(1, myGame);
                p2 = new AIPlayer(2, myGame);
                FileUtilities.resetFile(PATH_TO_HISTORY, false, true);
            }

        }

        Player[] playerList = { p1, p2 };

        // Create the frame for the GUI
        JFrame frame = new JFrame("Checkers Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 900);

        // North Panel
        final JPanel north_panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        frame.add(north_panel, BorderLayout.NORTH);

        // Reset button
        /*
         * final JPanel control_panel = new JPanel();
         * frame.add(control_panel, BorderLayout.NORTH);
         */
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> {
            System.out.println("Tried to reset");
            myGame.resetGame();
            System.out.println(Arrays.deepToString(myGame.getBoardState()));
            north_panel.repaint();
            frame.repaint();
        }
        );
        north_panel.add(reset);// TODO implement this shit

        // Undo
        final JButton undo = new JButton("Undo");
        undo.addActionListener(e -> {
            System.out.println("Tried to undo");
            myGame.undoGame();
            north_panel.repaint();
            frame.repaint();
        });
        north_panel.add(undo);

        // Status
        final JLabel status = new JLabel("Setting up...");
        status.setFont(new Font("Arial", Font.BOLD, 20)); // Set font on the JLabel
        north_panel.add(status);

        // Create the panel for the GUI and pass the game instance
        CheckersGUI checkersPanel = new CheckersGUI(myGame, playerList, status);
        // GUITEST checkersPanel = new GUITEST();
        frame.add(checkersPanel);

        // Display the frame
        frame.setVisible(true);

        // checks if game is over
        if (myGame.getWinner() != -1) {
            JOptionPane.showMessageDialog(null, "Game Over");
        }

    }
}
