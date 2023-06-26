package com.Chessbot;

public class Main {
    static Board board;
    public static void main(String[] args) {
        board = new Board("Chess");


        // Demo programme that uses the 'board.allowSingleMouseMoveForWhite()' and 'board.allowSingleMouseMoveForBlack()' to allow the user to play against themselves.
        boolean turn = true; // true = white, false = black
        while (true) {
            // the game will go on forever
            Move nextMove = (turn) ? board.allowSingleMouseMoveForWhite() : board.allowSingleMouseMoveForBlack(); // Allowing a single move for the current turn which is determined by the 'turn' boolean
            boolean moveSuccessful = board.makeMove(nextMove); // make the move and measure if it was successful

            if (moveSuccessful) {
                turn = !turn; // if the move was successful then change the turn. If it wasn't then it will loop back and the unsuccessful side has another turn.
            }
        }
    }
}
