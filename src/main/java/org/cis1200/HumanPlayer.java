package org.cis1200;

import java.util.Scanner;

public class HumanPlayer implements Player {
    private int playerNumber;
    private GameLogic currentGame;

    public HumanPlayer(int playerNumber, GameLogic currentGame) {
        playerNumber = playerNumber;
        currentGame = currentGame;
    }

    public Integer[] chooseAction(int[][] boardState) {

        final int[] NE = { -1, 1 };
        final int[] NW = { -1, -1 };
        final int[] SE = { 1, 1 };
        final int[] SW = { 1, -1 };

        Scanner scanner = new Scanner(System.in);

        int row = -1;
        while (row < 0 || row > 7) {
            System.out.print("Enter the row you'd like to select: ");
            row = scanner.nextInt();
        }

        int col = -1;
        while (col < 0 || col > 7) {
            System.out.print("Enter the col you'd like to select: ");
            col = scanner.nextInt();
        }

        scanner.nextLine();
        System.out.print("Enter the direction you'd like to select (NE, NW, SE, SW): ");
        String directionString = scanner.nextLine();
        int[] direction;
        while (true) {
            if (directionString.equals("NW")) {
                direction = NW;
                break;
            } else if (directionString.equals("NE")) {
                direction = NE;
                break;
            } else if (directionString.equals("SW")) {
                direction = SW;
                break;
            } else if (directionString.equals("SE")) {
                direction = SE;
                break;
            } else {
                throw new RuntimeException("Invalid direction");
            }
        }

        return new Integer[] { row, col, direction[0], direction[1] };
    }

}
