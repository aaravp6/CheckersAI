package org.cis1200;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Testing {
    private GameLogic myGame;

    @BeforeEach
    public void setUp() {
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

        myGame = new GameLogic(startingBoardState);
    }

    @Test
    public void testBasicMove() {
        int[] direction = new int[]{-1, 1};
        myGame.movePiece(5, 2, direction, 1);

        int[][] intendedEndBoardState = {
                { 0, 2, 0, 2, 0, 2, 0, 2 },
                { 2, 0, 2, 0, 2, 0, 2, 0 },
                { 0, 2, 0, 2, 0, 2, 0, 2 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 0, 0, 0 },
                { 1, 0, 0, 0, 1, 0, 1, 0 },
                { 0, 1, 0, 1, 0, 1, 0, 1 },
                { 1, 0, 1, 0, 1, 0, 1, 0 }
        };
        assertArrayEquals(intendedEndBoardState, myGame.getBoardState());
    }

    @Test
    public void testInvalidMove() {
        int[] direction = new int[]{-1, -1};
        myGame.movePiece(5, 0, direction, 1);

        int[][] intendedEndBoardState = {
                { 0, 2, 0, 2, 0, 2, 0, 2 },
                { 2, 0, 2, 0, 2, 0, 2, 0 },
                { 0, 2, 0, 2, 0, 2, 0, 2 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 1, 0, 1, 0, 1, 0, 1, 0 },
                { 0, 1, 0, 1, 0, 1, 0, 1 },
                { 1, 0, 1, 0, 1, 0, 1, 0 }
        };
        assertArrayEquals(intendedEndBoardState, myGame.getBoardState());
    }

    @Test
    public void testJump() {
        int[] direction;
        direction = new int[]{-1, 1};
        myGame.movePiece(5, 2, direction, 1);

        direction = new int[]{1, 1};
        myGame.movePiece(2, 1, direction, 2);

        direction = new int[]{-1, -1};
        myGame.movePiece(4, 3, direction, 1);

        int[][] intendedEndBoardState = {
                { 0, 2, 0, 2, 0, 2, 0, 2 },
                { 2, 0, 2, 0, 2, 0, 2, 0 },
                { 0, 1, 0, 2, 0, 2, 0, 2 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 1, 0, 0, 0, 1, 0, 1, 0 },
                { 0, 1, 0, 1, 0, 1, 0, 1 },
                { 1, 0, 1, 0, 1, 0, 1, 0 }
        };

        assertArrayEquals(intendedEndBoardState, myGame.getBoardState());

    }

    @Test
    public void moveWithoutPiece() {
        int[] direction = new int[]{-1, -1};
        myGame.movePiece(5, 3, direction, 1);

        int[][] intendedEndBoardState = {
                { 0, 2, 0, 2, 0, 2, 0, 2 },
                { 2, 0, 2, 0, 2, 0, 2, 0 },
                { 0, 2, 0, 2, 0, 2, 0, 2 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 1, 0, 1, 0, 1, 0, 1, 0 },
                { 0, 1, 0, 1, 0, 1, 0, 1 },
                { 1, 0, 1, 0, 1, 0, 1, 0 }
        };
        assertArrayEquals(intendedEndBoardState, myGame.getBoardState());
    }

    @Test
    public void moveOpponentPiece() {
        int[] direction;
        direction = new int[]{1, 1};
        myGame.movePiece(2, 1, direction, 1);

        int[][] intendedEndBoardState = {
                { 0, 2, 0, 2, 0, 2, 0, 2 },
                { 2, 0, 2, 0, 2, 0, 2, 0 },
                { 0, 2, 0, 2, 0, 2, 0, 2 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 1, 0, 1, 0, 1, 0, 1, 0 },
                { 0, 1, 0, 1, 0, 1, 0, 1 },
                { 1, 0, 1, 0, 1, 0, 1, 0 }
        };
        assertArrayEquals(intendedEndBoardState, myGame.getBoardState());
    }

    @Test
    public void testPromotePiece() {
        int[][] startingBoardState;
        int[] direction;
        int[][] intendedEndBoardState;
        //promote player 1
        startingBoardState = new int[][]{
                { 0, 0, 0, 2, 0, 2, 0, 2 },
                { 0, 0, 1, 0, 2, 0, 2, 0 },
                { 0, 0, 0, 0, 0, 2, 0, 2 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1, 0, 1, 0 },
                { 0, 2, 0, 1, 0, 1, 0, 1 },
                { 0, 0, 0, 0, 1, 0, 1, 0 }
        };
        myGame = new GameLogic(startingBoardState);
        direction = new int[]{-1, -1};
        myGame.movePiece(1, 2, direction, 1);

        intendedEndBoardState = new int[][]{
                { 0, 10, 0, 2, 0, 2, 0, 2 },
                { 0, 0, 0, 0, 2, 0, 2, 0 },
                { 0, 0, 0, 0, 0, 2, 0, 2 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1, 0, 1, 0 },
                { 0, 2, 0, 1, 0, 1, 0, 1 },
                { 0, 0, 0, 0, 1, 0, 1, 0 }
        };
        assertArrayEquals(intendedEndBoardState, myGame.getBoardState());

        //promote player 2
        startingBoardState = new int[][]{
                { 0, 0, 0, 2, 0, 2, 0, 2 },
                { 0, 0, 1, 0, 2, 0, 2, 0 },
                { 0, 0, 0, 0, 0, 2, 0, 2 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1, 0, 1, 0 },
                { 0, 2, 0, 1, 0, 1, 0, 1 },
                { 0, 0, 0, 0, 1, 0, 1, 0 }
        };
        myGame = new GameLogic(startingBoardState);
        direction = new int[]{1, 1};
        myGame.movePiece(6, 1, direction, 2);

        intendedEndBoardState = new int[][]{
                { 0, 0, 0, 2, 0, 2, 0, 2 },
                { 0, 0, 1, 0, 2, 0, 2, 0 },
                { 0, 0, 0, 0, 0, 2, 0, 2 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1, 0, 1, 0 },
                { 0, 0, 0, 1, 0, 1, 0, 1 },
                { 0, 0, 20, 0, 1, 0, 1, 0 }
        };
        assertArrayEquals(intendedEndBoardState, myGame.getBoardState());
    }

    @Test
    public void testKingMove() {
        int[][] startingBoardState;
        int[] direction;
        int[][] intendedEndBoardState;

        //more forward as normal
        startingBoardState = new int[][]{
                { 0, 0, 0, 2, 0, 2, 0, 2 },
                { 0, 0, 0, 0, 2, 0, 2, 0 },
                { 0, 0, 0, 0, 0, 2, 0, 2 },
                { 0, 0, 10, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1, 0, 1, 0 },
                { 0, 0, 0, 1, 0, 1, 0, 1 },
                { 0, 0, 0, 0, 1, 0, 1, 0 }
        };
        myGame = new GameLogic(startingBoardState);
        direction = new int[]{-1, -1};
        myGame.movePiece(3, 2, direction, 1);

        intendedEndBoardState = new int[][]{
                { 0, 0, 0, 2, 0, 2, 0, 2 },
                { 0, 0, 0, 0, 2, 0, 2, 0 },
                { 0, 10, 0, 0, 0, 2, 0, 2 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1, 0, 1, 0 },
                { 0, 0, 0, 1, 0, 1, 0, 1 },
                { 0, 0, 0, 0, 1, 0, 1, 0 }
        };
        assertArrayEquals(intendedEndBoardState, myGame.getBoardState());

        //move backward
        startingBoardState = new int[][]{
                { 0, 0, 0, 2, 0, 2, 0, 2 },
                { 0, 0, 0, 0, 2, 0, 2, 0 },
                { 0, 0, 0, 0, 0, 2, 0, 2 },
                { 0, 0, 10, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1, 0, 1, 0 },
                { 0, 0, 0, 1, 0, 1, 0, 1 },
                { 0, 0, 0, 0, 1, 0, 1, 0 }
        };
        myGame = new GameLogic(startingBoardState);
        direction = new int[]{1, 1};
        myGame.movePiece(3, 2, direction, 1);

        intendedEndBoardState = new int[][]{
                { 0, 0, 0, 2, 0, 2, 0, 2 },
                { 0, 0, 0, 0, 2, 0, 2, 0 },
                { 0, 0, 0, 0, 0, 2, 0, 2 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 10, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1, 0, 1, 0 },
                { 0, 0, 0, 1, 0, 1, 0, 1 },
                { 0, 0, 0, 0, 1, 0, 1, 0 }
        };
        assertArrayEquals(intendedEndBoardState, myGame.getBoardState());
    }

    @Test
    public void testKingJump() {
        int[][] startingBoardState;
        int[] direction;
        int[][] intendedEndBoardState;

        //more jump forward as normal
        startingBoardState = new int[][]{
                { 0, 0, 0, 2, 0, 2, 0, 2 },
                { 0, 0, 0, 0, 2, 0, 2, 0 },
                { 0, 2, 0, 0, 0, 2, 0, 2 },
                { 0, 0, 10, 0, 0, 0, 0, 0 },
                { 0, 2, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1, 0, 1, 0 },
                { 0, 0, 0, 1, 0, 1, 0, 1 },
                { 0, 0, 0, 0, 1, 0, 1, 0 }
        };
        myGame = new GameLogic(startingBoardState);
        direction = new int[]{-1, -1};
        myGame.movePiece(3, 2, direction, 1);

        intendedEndBoardState = new int[][]{
                { 0, 0, 0, 2, 0, 2, 0, 2 },
                { 10, 0, 0, 0, 2, 0, 2, 0 },
                { 0, 0, 0, 0, 0, 2, 0, 2 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 2, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1, 0, 1, 0 },
                { 0, 0, 0, 1, 0, 1, 0, 1 },
                { 0, 0, 0, 0, 1, 0, 1, 0 }
        };
        assertArrayEquals(intendedEndBoardState, myGame.getBoardState());

        //jump backward
        startingBoardState = new int[][]{
                { 0, 0, 0, 2, 0, 2, 0, 2 },
                { 0, 0, 0, 0, 2, 0, 2, 0 },
                { 0, 2, 0, 0, 0, 2, 0, 2 },
                { 0, 0, 10, 0, 0, 0, 0, 0 },
                { 0, 2, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1, 0, 1, 0 },
                { 0, 0, 0, 1, 0, 1, 0, 1 },
                { 0, 0, 0, 0, 1, 0, 1, 0 }
        };
        myGame = new GameLogic(startingBoardState);
        direction = new int[]{1, -1};
        myGame.movePiece(3, 2, direction, 1);

        intendedEndBoardState = new int[][]{
                { 0, 0, 0, 2, 0, 2, 0, 2 },
                { 0, 0, 0, 0, 2, 0, 2, 0 },
                { 0, 2, 0, 0, 0, 2, 0, 2 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 10, 0, 0, 0, 1, 0, 1, 0 },
                { 0, 0, 0, 1, 0, 1, 0, 1 },
                { 0, 0, 0, 0, 1, 0, 1, 0 }
        };
        assertArrayEquals(intendedEndBoardState, myGame.getBoardState());
    }

    @Test
    public void noEndState() {
        int[][] startingBoardState = new int[][]{
                { 0, 0, 0, 2, 0, 2, 0, 2 },
                { 0, 0, 0, 0, 2, 0, 2, 0 },
                { 0, 2, 0, 0, 0, 2, 0, 2 },
                { 0, 0, 10, 0, 0, 0, 0, 0 },
                { 0, 2, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1, 0, 1, 0 },
                { 0, 0, 0, 1, 0, 1, 0, 1 },
                { 0, 0, 0, 0, 1, 0, 1, 0 }
        };
        myGame = new GameLogic(startingBoardState);
        assertEquals(-1, myGame.getWinner());
    }

    @Test
    public void testWinOpponentNoPieces() {
        int[][] startingBoardState = new int[][]{
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 1, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 10, 0, 0, 0, 0, 0 },
                { 0, 1, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1, 0, 1, 0 },
                { 0, 0, 0, 1, 0, 1, 0, 1 },
                { 0, 0, 0, 0, 1, 0, 1, 0 }
        };
        myGame = new GameLogic(startingBoardState);
        assertEquals(1, myGame.getWinner());
    }

    @Test
    public void testWinOpponentNoMoves() {
        int[][] startingBoardState = new int[][]{
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 1, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 10, 0, 0, 0, 2, 0 },
                { 0, 1, 0, 0, 0, 2, 0, 2 },
                { 0, 0, 0, 0, 1, 0, 1, 0 },
                { 0, 0, 0, 1, 0, 1, 0, 1 },
                { 0, 0, 0, 0, 1, 0, 1, 0 }
        };
        myGame = new GameLogic(startingBoardState);
        myGame.setCurrentPlayerNum(2);
        assertEquals(1, myGame.getWinner());
    }

    @Test
    public void testTieTooManyMoves() {
        int[][] startingBoardState = new int[][]{
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 1, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 10, 0, 0, 0, 0, 0 },
                { 0, 1, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1, 0, 1, 0 },
                { 0, 20, 0, 1, 0, 1, 0, 1 },
                { 0, 0, 0, 0, 1, 0, 1, 0 }
        };
        myGame = new GameLogic(startingBoardState);
        int[] direction;
        for (int i = 0; i < 11; i++) {
            direction = new int[]{-1, 1};
            myGame.movePiece(3, 2, direction, 1);

            direction = new int[]{1, 1};
            myGame.movePiece(6, 1, direction, 2);

            direction = new int[]{1, -1};
            myGame.movePiece(2, 3, direction, 1);

            direction = new int[]{-1, -1};
            myGame.movePiece(7, 2, direction, 2);
        }
        assertEquals(3, myGame.getWinner());
    }

    @Test
    public void testTieBothPlayersNoMoves() {
        int[][] startingBoardState = new int[][]{
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 2, 0, 0 },
                { 2, 0, 2, 0, 2, 0, 2, 0 },
                { 0, 2, 0, 2, 0, 2, 0, 1 },
                { 1, 0, 1, 0, 1, 0, 1, 0 }
        };
        myGame = new GameLogic(startingBoardState);
        assertEquals(3, myGame.getWinner());
    }
}
