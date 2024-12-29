package org.cis1200;

import java.util.Arrays;
import java.util.ArrayList;

public class GameLogic {
    private int[][] boardState;
    // private Player player1;
    // private Player player2;
    private int currentPlayerNum;
    private int numMoves = 0;
    private int numMovesAfterJump = 0;

    // initializes the board state and the players
    public GameLogic(int[][] bs) {
        this.boardState = new int[bs.length][bs[0].length];
        for (int i = 0; i < bs.length; i++) {
            this.boardState[i] = Arrays.copyOf(bs[i], bs[i].length);
        }
        // player1 = p1;
        // player2 = p2;

        currentPlayerNum = 1;
    }

    public int getNumMoves() {
        return numMoves;
    }

    public void setNumMoves(int moves) {
        numMoves = moves;
    }

    public int getNumMovesAfterJump() {
        return numMovesAfterJump;
    }

    public void setNumMovesAfterJump(int moves) {
        numMovesAfterJump = moves;
    }

    public void resetGame() {
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

        // TODO: NEED TO FIGURE OUT WHERE TO DO THIS FILE RESET STUFF
        boardState = startingBoardState;
        currentPlayerNum = 1;
        numMoves = 0;
        numMovesAfterJump = 0;

        final String PATH_TO_HISTORY = "game_history.csv";
        FileUtilities.resetFile(PATH_TO_HISTORY);
    }

    public void undoGame() {
        final String PATH_TO_HISTORY = "game_history.csv";
        if (FileUtilities.getLastLine(PATH_TO_HISTORY).charAt(0) != '[') {
            return;
        }
        FileUtilities.deleteLastLine(PATH_TO_HISTORY);
        String finalLine = FileUtilities.getLastLine(PATH_TO_HISTORY);

        String arrayPart = finalLine.substring(0, finalLine.lastIndexOf(']') + 1);
        String numbersPart = finalLine.substring(finalLine.lastIndexOf(']') + 2);

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

        String[] numbers = numbersPart.split(",\\s*");
        int importedCurrentPlayer = Integer.parseInt(numbers[0]);
        int importedNumMoves = Integer.parseInt(numbers[1]);
        int importedNumMovesAfterJump = Integer.parseInt(numbers[2]);

        boardState = importedBoardState;
        currentPlayerNum = importedCurrentPlayer;
        numMoves = importedNumMoves;
        numMovesAfterJump = importedNumMovesAfterJump;

    }

    // gets the board state
    public int[][] getBoardState() {
        return this.boardState;
    }

    // gets the current player
    public int getCurrentPlayerNum() {
        return currentPlayerNum;
    }

    // sets the current player
    public void setCurrentPlayerNum(int num) {
        currentPlayerNum = num;
    }

    // gets the value of a position
    // 1 is p1 piece, 2 is p2 piece, 10 is p1 king, 20 is p2 king
    public int getPosVal(int row, int col) {
        return this.boardState[row][col];
    }

    // sets the value of a position
    public void setPosVal(int row, int col, int val) {
        if (val != 1 && val != 2 && val != 10 && val != 20 && val != 0) {
            System.out.println("Invalid value");
        } else if (row < 0 || row >= this.boardState.length || col < 0
                || col >= this.boardState[0].length) {
            System.out.println("Invalid position");
        } else {
            this.boardState[row][col] = val;
        }
    }

    // goes to next player
    public void nextPlayer() {
        currentPlayerNum = 3 - currentPlayerNum;
    }

    // checks if piece belongs to player
    public boolean pieceBelongsToPlayer(int row, int col, int playerNum) {
        if (playerNum == 1) {
            return (getPosVal(row, col) == 1 || getPosVal(row, col) == 10);
        } else {
            return (getPosVal(row, col) == 2 || getPosVal(row, col) == 20);
        }
    }

    // checks if piece is king
    public boolean pieceIsKing(int row, int col) {
        return (getPosVal(row, col) % 10 == 0);
    }

    public boolean possibleMove(int row, int col, int[] direction, int playerNum) {
        int nrow = row + direction[0];
        int ncol = col + direction[1];
        if (nrow < 0 || nrow >= this.boardState.length || ncol < 0
                || ncol >= this.boardState[0].length) {
            return false;
        } else { // on board
            return (getPosVal(nrow, ncol) == 0);
        }
    }

    public boolean possibleJump(int row, int col, int[] direction, int playerNum) {
        int nrow = row + direction[0] * 2;
        int ncol = col + direction[1] * 2;
        int middleRow = row + direction[0];
        int middleCol = col + direction[1];
        if (nrow < 0 || nrow >= this.boardState.length || ncol < 0
                || ncol >= this.boardState[0].length) {
            return false;
        } else { // on board
            return (getPosVal(nrow, ncol) == 0 &&
                    pieceBelongsToPlayer(middleRow, middleCol, 3 - playerNum));
        }
    }

    // containsHelper
    private boolean containsDirection(int[] direction, int[][] directions) {
        for (int[] d : directions) {
            if (Arrays.equals(d, direction)) {
                return true;
            }
        }
        return false;
    }

    // direction is 2-d list, where -1/1 is L/R & -1/1 is D/U
    // get all the possible directions the piece is allowed to move in
    // (regardless of whether it actually can)
    public int[][] getAllowedDirections(int row, int col, int playerNum) {
        // checks if player piece is present
        if (pieceBelongsToPlayer(row, col, playerNum)) {
            if (playerNum == 1) { // for player 1
                if (!pieceIsKing(row, col)) {
                    int[][] validDirections = { { -1, -1 }, { -1, 1 } };
                    return validDirections;
                } else {
                    int[][] validDirections = { { -1, 1 }, { 1, 1 }, { -1, -1 }, { 1, -1 } };
                    return validDirections;
                }
            } else { // for player 2
                if (!pieceIsKing(row, col)) {
                    int[][] validDirections = { { 1, 1 }, { 1, -1 } };
                    return validDirections;
                } else {
                    int[][] validDirections = { { -1, 1 }, { 1, 1 }, { -1, -1 }, { 1, -1 } };
                    return validDirections;
                }
            }

        } else {
            // System.out.println("Didn't select player for getAllowedDirections");
            int[][] noVal = {};
            return noVal;
        }
    }

    // reached end

    // promote piece
    public boolean promotePiece(int row, int col) {
        if (getPosVal(row, col) == 1) {
            setPosVal(row, col, 10);
            return true;
        } else if (getPosVal(row, col) == 2) {
            setPosVal(row, col, 20);
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<int[]> getAllowedMoves(int row, int col, int playerNum) {
        if (0 > row || 8 < row || 0 > col || 8 < col) {
            return new ArrayList<>();
        }
        ArrayList<int[]> allowedMoves = new ArrayList<>();
        int[][] directions = getAllowedDirections(row, col, playerNum);
        for (int[] direction : directions) {
            if (possibleJump(row, col, direction, playerNum)) {
                allowedMoves.add(new int[] { row + 2 * direction[0], col + 2 * direction[1] });
            } else if (possibleMove(row, col, direction, playerNum)) {
                allowedMoves.add(new int[] { row + direction[0], col + direction[1] });
            }
        }
        return allowedMoves;
    }

    // moves piece and returns if actually moved
    // 1 is successful move, -1 is unsuccessful move, 0 is double jump
    public int movePiece(int row, int col, int[] direction, int playerNum) {
        // System.out.println("Will move player" + playerNum + "'s " + row + ", " + col
        // + "in direction " + direction[0] + ", " + direction[1]);
        int[][] directions = getAllowedDirections(row, col, playerNum);
        if (directions.length == 0) {
            // System.out.println("Didn't select player for movePiece");
            return -1;
        } else if (containsDirection(direction, directions)) {
            if (possibleJump(row, col, direction, playerNum)) {
                int nrow = row + direction[0] * 2;
                int ncol = col + direction[1] * 2;
                int middleRow = row + direction[0];
                int middleCol = col + direction[1];

                int curVal = getPosVal(row, col);
                setPosVal(nrow, ncol, curVal);
                setPosVal(middleRow, middleCol, 0);
                setPosVal(row, col, 0);

                if ((getPosVal(nrow, ncol) == 1 && nrow == 0)
                        || (getPosVal(nrow, ncol) == 2 && nrow == 7)) {
                    System.out.println("Promoted");
                    System.out.println(promotePiece(nrow, ncol));
                } else if (getPossibleChipJumps(nrow, ncol, playerNum).size() > 0) {
                    System.out.println("Double Jump");
                    return 0;
                }

                numMovesAfterJump = 0;
                numMoves++;

                return 1;
            } else if (possibleMove(row, col, direction, playerNum)) {
                int nrow = row + direction[0];
                int ncol = col + direction[1];

                int curVal = getPosVal(row, col);
                setPosVal(nrow, ncol, curVal);
                setPosVal(row, col, 0);

                if ((getPosVal(nrow, ncol) == 1 && nrow == 0)
                        || (getPosVal(nrow, ncol) == 2 && nrow == 7)) {
                    System.out.println("Promoted");
                    System.out.println(promotePiece(nrow, ncol));
                }

                numMoves++;
                numMovesAfterJump++;

                return 1;
            } else {
                // System.out.println("Invalid move");
                return -1;
            }
        } else {
            // System.out.println("Invalid direction");
            return -1;
        }
    }

    public void drawGame() {
        System.out.println("  0 1 2 3 4 5 6 7");
        for (int row = 0; row < this.boardState.length; row++) {
            System.out.print(row);
            System.out.print(" ");
            for (int col = 0; col < this.boardState[0].length; col++) {
                if (this.boardState[row][col] == 1) {
                    System.out.print("x ");
                } else if (this.boardState[row][col] == 2) {
                    System.out.print("o ");
                } else if (this.boardState[row][col] == 10) {
                    System.out.print("X ");
                } else if (this.boardState[row][col] == 20) {
                    System.out.print("O ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }

    public ArrayList<Integer[]> getPossibleMoves(int playerNum) {
        ArrayList<Integer[]> p1Locs = new ArrayList<>();
        ArrayList<Integer[]> p2Locs = new ArrayList<>();
        // check if a player does not have any moves left
        for (int row = 0; row < this.boardState.length; row++) {
            for (int col = 0; col < this.boardState[0].length; col++) {
                if (this.boardState[row][col] == 1 || this.boardState[row][col] == 10) {
                    Integer[] pos = { row, col };
                    p1Locs.add(pos);
                } else if (this.boardState[row][col] == 2 || this.boardState[row][col] == 20) {
                    Integer[] pos = { row, col };
                    p2Locs.add(pos);
                }
            }
        }

        // add possible moves
        ArrayList<Integer[]> listPossibleMoves = new ArrayList<>();

        ArrayList<Integer[]> pLocs = new ArrayList<>();
        if (playerNum == 1) {
            pLocs = p1Locs;
        } else {
            pLocs = p2Locs;
        }

        for (Integer[] move : pLocs) {

            int[][] directions = getAllowedDirections(move[0], move[1], playerNum);
            for (int[] direction : directions) {
                if (possibleMove(move[0], move[1], direction, playerNum)
                        || possibleJump(move[0], move[1], direction, playerNum)) {
                    listPossibleMoves
                            .add(new Integer[] { move[0], move[1], direction[0], direction[1] });
                }
            }
        }

        return listPossibleMoves;
    }

    public ArrayList<Integer[]> getPossibleChipJumps(int playerNum, int row, int col) {

        // add possible moves
        ArrayList<Integer[]> listPossibleMoves = new ArrayList<>();

        int[][] directions = getAllowedDirections(row, col, playerNum);
        for (int[] direction : directions) {
            if (possibleJump(row, col, direction, playerNum)) {
                listPossibleMoves.add(new Integer[] { row, col, direction[0], direction[1] });
            }
        }

        return listPossibleMoves;
    }

    public int getWinner() {
        ArrayList<Integer[]> p1Locs = new ArrayList<>();
        ArrayList<Integer[]> p2Locs = new ArrayList<>();
        // check if a player does not have any moves left
        for (int row = 0; row < this.boardState.length; row++) {
            for (int col = 0; col < this.boardState[0].length; col++) {
                if (this.boardState[row][col] == 1 || this.boardState[row][col] == 10) {
                    Integer[] pos = { row, col };
                    p1Locs.add(pos);
                } else if (this.boardState[row][col] == 2 || this.boardState[row][col] == 20) {
                    Integer[] pos = { row, col };
                    p2Locs.add(pos);
                }
            }
        }
        if (p1Locs.size() == 0) {
            return 2;
        } else if (p2Locs.size() == 0) {
            return 1;
        } else {
            if (getPossibleMoves(currentPlayerNum).size() == 0
                    & getPossibleMoves(3 - currentPlayerNum).size() == 0) {
                return 3;
            } else if (getPossibleMoves(currentPlayerNum).size() == 0) {
                return 3 - currentPlayerNum;
            } else if (numMovesAfterJump >= 40) {
                return 3;
            }
        }

        return -1;
    }

}
