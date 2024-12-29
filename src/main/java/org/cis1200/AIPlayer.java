package org.cis1200;

import java.util.*;

public class AIPlayer implements Player {
    private int playerNumber;
    private GameLogic currentGame;

    public AIPlayer(int initPlayerNumber, GameLogic initGame) {
        playerNumber = initPlayerNumber;
        currentGame = initGame;

    }

    public double getBoardValue(int[][] boardState) {
        int p1Val = 0;
        int p2Val = 0;
        for (int i = 0; i < boardState.length; i++) {
            for (int j = 0; j < boardState[i].length; j++) {
                if (boardState[i][j] == 1) {
                    p1Val++;
                } else if (boardState[i][j] == 2) {
                    p2Val++;
                } else if (boardState[i][j] == 10) {
                    p1Val += 3;
                } else if (boardState[i][j] == 20) {
                    p2Val += 3;
                }
            }
        }
        if (playerNumber == 1) {
            return p1Val - p2Val;
        } else {
            return p2Val - p1Val;
        }
    }

    public double minimax(
            int[][] boardState, int depth, boolean isMaximizingPlayer, double alpha, double beta,
            int playerNum
    ) {
        double gameStateVal = getBoardValue(boardState);

        if (depth == 0) {
            return gameStateVal;
        }
        if (isMaximizingPlayer) {
            double maxEval = Integer.MIN_VALUE;
            ArrayList<Integer[]> listPossibleMoves = currentGame.getPossibleMoves(playerNum);

            for (Integer[] possibleMove : listPossibleMoves) {
                GameLogic tempGame = new GameLogic(boardState);
                tempGame.movePiece(
                        possibleMove[0], possibleMove[1],
                        new int[] { possibleMove[2], possibleMove[3] }, playerNum
                );
                double eval = minimax(
                        tempGame.getBoardState(), depth - 1, false, alpha, beta, 3 - playerNum
                );
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxEval;
        } else {
            double minEval = Integer.MAX_VALUE;
            ArrayList<Integer[]> listPossibleMoves = currentGame.getPossibleMoves(playerNum);

            for (Integer[] possibleMove : listPossibleMoves) {
                GameLogic tempGame = new GameLogic(boardState);
                tempGame.movePiece(
                        possibleMove[0], possibleMove[1],
                        new int[] { possibleMove[2], possibleMove[3] }, playerNum
                );
                double eval = minimax(
                        tempGame.getBoardState(), depth - 1, true, alpha, beta, 3 - playerNum
                );
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return minEval;
        }

    }

    public Integer[] chooseAction(int[][] boardState) {

        Map<Double, Integer[]> expectedBoardValues = new HashMap<>();

        ArrayList<Integer[]> listPossibleMoves = currentGame.getPossibleMoves(playerNumber);
        for (Integer[] possibleMove : listPossibleMoves) {
            GameLogic tempGame = new GameLogic(boardState);
            System.out.print("Possible Move: ");
            System.out.println(Arrays.toString(possibleMove));
            if (tempGame.possibleJump(
                    possibleMove[0], possibleMove[1],
                    new int[] { possibleMove[2], possibleMove[3] }, playerNumber
            )) {
                tempGame.movePiece(
                        possibleMove[0], possibleMove[1],
                        new int[] { possibleMove[2], possibleMove[3] }, playerNumber
                );
            } else {
                tempGame.movePiece(
                        possibleMove[0], possibleMove[1],
                        new int[] { possibleMove[2], possibleMove[3] }, playerNumber
                );
            }

            expectedBoardValues.put(
                    minimax(
                            tempGame.getBoardState(), 6, false, Integer.MIN_VALUE,
                            Integer.MAX_VALUE, 3 - playerNumber
                    ), possibleMove
            );
        }
        Integer[] bestMove = expectedBoardValues.get(
                Collections.max(expectedBoardValues.keySet())
        );

        return bestMove;
    }
}
