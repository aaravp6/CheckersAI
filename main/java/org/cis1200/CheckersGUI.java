package org.cis1200;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CheckersGUI extends JPanel {
    private GameLogic myGame;
    private Player[] playerList;
    private int squareSize = 100;
    private JLabel statusLabel;
    private int[] activatedPiece = { -1, -1 };

    // Constructor
    public CheckersGUI(GameLogic gameInstance, Player[] playerList, JLabel statusLabelInit) {
        this.myGame = gameInstance;
        this.playerList = playerList;
        this.statusLabel = statusLabelInit;

        this.setPreferredSize(new Dimension(8 * squareSize, 8 * squareSize)); // 8x8 board
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePress(e.getX(), e.getY());
            }

            // TODO: ADD MORE MOUSE THINGS
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the checkerboard
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                System.out
                        .println("Activated pieces" + activatedPiece[0] + "," + activatedPiece[1]);
                System.out.println("row" + row + "col" + col);

                boolean possibleMove = false;
                ArrayList<int[]> allowedMoves = myGame.getAllowedMoves(
                        activatedPiece[0], activatedPiece[1], myGame.getCurrentPlayerNum()
                );
                for (int[] move : allowedMoves) {
                    if (move[0] == row && move[1] == col) {
                        possibleMove = true;
                    }
                }

                if (activatedPiece[0] == row && activatedPiece[1] == col) {
                    System.out.println("Yellow");
                    g.setColor(Color.YELLOW);
                } else if (possibleMove) {
                    System.out.println("Orange");
                    g.setColor(Color.ORANGE);
                } else if ((row + col) % 2 == 0) {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(col * squareSize, row * squareSize, squareSize, squareSize);

                // Draw the pieces
                int piece = myGame.getBoardState()[row][col];
                if (piece != 0) {
                    g.setColor((piece == 1 || piece == 10) ? Color.RED : Color.BLUE);
                    g.fillOval(
                            col * squareSize + squareSize / 4, row * squareSize + squareSize / 4,
                            squareSize / 2, squareSize / 2
                    );
                    if (piece % 10 == 0) {
                        g.setColor(Color.BLACK);

                        /*
                         * int[] xPoints = {100, 120, 160, 130, 140, 100, 60, 70, 40, 80}; //
                         * X-coordinates
                         * int[] yPoints = {20, 70, 70, 100, 150, 120, 150, 100, 70, 70}; //
                         * Y-coordinates
                         * int nPoints = 10;
                         *
                         * g.fillPolygon(xPoints, yPoints, nPoints);
                         */

                        int centerX = col * squareSize + squareSize * 1 / 2;
                        int centerY = row * squareSize + squareSize * 1 / 2;
                        int radius = 15; // Outer radius of the star
                        int innerRadius = 10; // Inner radius of the star
                        int numPoints = 5; // Number of star tips

                        int[] xPoints = new int[numPoints * 2];
                        int[] yPoints = new int[numPoints * 2];

                        double angleStep = Math.PI / numPoints; // Half of a full tip cycle (outer +
                        // inner)
                        double startAngle = -Math.PI / 2; // Start pointing up (90 degrees)

                        for (int i = 0; i < numPoints * 2; i++) {
                            double angle = startAngle + i * angleStep; // Increment angle for each
                            // point
                            int r = (i % 2 == 0) ? radius : innerRadius; // Alternate between outer
                            // and inner radius
                            xPoints[i] = centerX + (int) (r * Math.cos(angle));
                            yPoints[i] = centerY + (int) (r * Math.sin(angle)); // Use + here to
                            // account for
                            // Java's inverted
                            // Y-axis
                        }

                        g.fillPolygon(xPoints, yPoints, numPoints * 2);

                        // g.fillRect(col*squareSize + squareSize*3/8, row*squareSize +
                        // squareSize*3/8, squareSize/4, squareSize/4);
                    }
                }
            }
        }

        statusLabel.setText(
                "Current Player: " + (myGame.getCurrentPlayerNum() == 1 ? "Red" : "Blue")
                        + ", Move Number: " + myGame.getNumMoves()
        ); // + ", Moves after last jump: " + myGame.getNumMovesAfterJump());// Redraw the
        // board

    }

    // Handle mouse click to select and move pieces
    private void handleMousePress(int x, int y) {
        int col = x / squareSize;
        int row = y / squareSize;

        int currentPlayer = myGame.getCurrentPlayerNum();
        System.out.println("Player " + currentPlayer + " clicked on (" + row + ", " + col + ")");

        class Helper {
            void iter(int inpRow, int inpCol, int[] inpDir, int inpCurrentPlayer) {
                int outcome = myGame.movePiece(inpRow, inpCol, inpDir, inpCurrentPlayer);
                if (outcome == 1) {
                    System.out.println("successful move");
                    myGame.nextPlayer();
                    // myGame.increaseNumMoves();
                } else if (outcome == -1) {
                    System.out.println("unsuccessful move");
                } else {
                    System.out.println("double jump move [NEED TO COMPLETE]");
                    myGame.nextPlayer();
                }
                myGame.drawGame();
            }
        }

        if (playerList[currentPlayer - 1] instanceof AIPlayer) {

            Integer[] calcOutput = playerList[currentPlayer - 1]
                    .chooseAction(myGame.getBoardState());

            int calcRow;
            int calcCol;
            int[] calcDir;

            calcRow = calcOutput[0];
            calcCol = calcOutput[1];
            calcDir = new int[] { calcOutput[2], calcOutput[3] };

            Helper helper = new Helper();
            helper.iter(calcRow, calcCol, calcDir, currentPlayer);

            // updates the IO
            final String PATH_TO_HISTORY = "game_history.csv";
            int[][] endCurrentBoardState = myGame.getBoardState();
            int endCurrentPlayer = myGame.getCurrentPlayerNum();
            int endNumMoves = myGame.getNumMoves();
            int endNumMovesAfterJump = myGame.getNumMovesAfterJump();
            FileUtilities.writeLinesToFile(
                    endCurrentBoardState, endCurrentPlayer, endNumMoves, endNumMovesAfterJump,
                    PATH_TO_HISTORY
            );

            repaint();

        } else {
            // Check if there's a piece to drag
            if (activatedPiece[0] == -1 && activatedPiece[1] == -1) {
                int[][] board = myGame.getBoardState();
                if (myGame.pieceBelongsToPlayer(row, col, currentPlayer)) {
                    activatedPiece[0] = row;
                    activatedPiece[1] = col;
                    System.out.println("ACTIVATED");
                    repaint();

                }
            } else {

                boolean possibleMove = false;
                ArrayList<int[]> allowedMoves = myGame.getAllowedMoves(
                        activatedPiece[0], activatedPiece[1], myGame.getCurrentPlayerNum()
                );
                for (int[] move : allowedMoves) {
                    if (move[0] == row && move[1] == col) {
                        possibleMove = true;
                    }
                }
                System.out.println("possibleMove" + ((possibleMove ? 1 : 0)));

                if (possibleMove) {
                    Helper helper = new Helper();
                    int[] dir = new int[2];
                    if (row > activatedPiece[0] && col > activatedPiece[1]) {
                        dir[0] = 1;
                        dir[1] = 1;
                    } else if (row < activatedPiece[0] && col > activatedPiece[1]) {
                        dir[0] = -1;
                        dir[1] = 1;
                    } else if (row > activatedPiece[0] && col < activatedPiece[1]) {
                        dir[0] = 1;
                        dir[1] = -1;
                    } else if (row < activatedPiece[0] && col < activatedPiece[1]) {
                        dir[0] = -1;
                        dir[1] = -1;
                    } else {
                        System.out.println("invalid move");
                    }

                    helper.iter(activatedPiece[0], activatedPiece[1], dir, currentPlayer);
                    activatedPiece[0] = -1;
                    activatedPiece[1] = -1;

                    // updates the IO
                    final String PATH_TO_HISTORY = "game_history.csv";
                    int[][] endCurrentBoardState = myGame.getBoardState();
                    int endCurrentPlayer = myGame.getCurrentPlayerNum();
                    int endNumMoves = myGame.getNumMoves();
                    int endNumMovesAfterJump = myGame.getNumMovesAfterJump();
                    FileUtilities.writeLinesToFile(
                            endCurrentBoardState, endCurrentPlayer, endNumMoves,
                            endNumMovesAfterJump, PATH_TO_HISTORY
                    );

                } else if (myGame.pieceBelongsToPlayer(row, col, currentPlayer)) {
                    activatedPiece[0] = row;
                    activatedPiece[1] = col;
                }

                System.out.println("FINISHED");

                repaint();
            }

        }

        // updates the label
        final String PATH_TO_HISTORY = "game_history.csv";

        if (myGame.getWinner() == -1) {
            statusLabel.setText(
                    "Current Player: " + (myGame.getCurrentPlayerNum() == 1 ? "Red" : "Blue")
                            + ", Move Number: " + myGame.getNumMoves()
            ); // + ", Moves after last jump: " + myGame.getNumMovesAfterJump());// Redraw the
            // board

        } else if (myGame.getWinner() == 1) {
            statusLabel.setText("Player 1 Wins!");
            repaint(); // Redraw the board
            JOptionPane.showMessageDialog(
                    null, "Player 1 Wins!", "Please click reset to start again.",
                    JOptionPane.INFORMATION_MESSAGE
            );
            myGame.resetGame();
            FileUtilities.resetFile(PATH_TO_HISTORY);
            this.repaint();
        } else if (myGame.getWinner() == 2) {
            statusLabel.setText("Player 2 Wins!");
            JOptionPane.showMessageDialog(
                    null, "Player 2 Wins!", "Please click reset to start again.",
                    JOptionPane.INFORMATION_MESSAGE
            );
            myGame.resetGame();
            FileUtilities.resetFile(PATH_TO_HISTORY);
            this.repaint();
        } else if (myGame.getWinner() == 3) {
            statusLabel.setText("There was a tie!");
            JOptionPane.showMessageDialog(
                    null, "There was a tie!", "Please click reset to start again.",
                    JOptionPane.INFORMATION_MESSAGE
            );
            myGame.resetGame();
            FileUtilities.resetFile(PATH_TO_HISTORY);
            this.repaint();
        }

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(8 * squareSize, 8 * squareSize);
    }
}
