package com.Chessbot;

import java.awt.*;
import java.util.Arrays;

public class Main {
    static Board board;
    public static void main(String[] args) {
        board = new Board("Chess");
        board.openPromotionWindow("Promote to:");

        // Demo programme that uses the 'board.allowSingleMouseMoveForWhite()' and 'board.allowSingleMouseMoveForBlack()' to allow the user to play against themselves.
        boolean turn = true; // true = white, false = black
        boolean moveMade = false;
        while (true) {
            // the game will go on forever

            if (!moveMade) {
                // ^ if the move wasn't made… i.e. it won't loop forever because this should only run before a move to show all the legal moves
                // looking at what piece is clicked and highlighting all its legal moves
                Point pieceToMove = (turn) ? board.getPieceThatMustFollowMouseForWhite() : board.getPieceThatMustFollowMouseForBlack();
                byte specialMoveID = 0; // 0 is 00000000 in binary which will be set as a non-special move
                highlightAllLegalPieces(new Move(pieceToMove.x, pieceToMove.y,0,0,specialMoveID),board.getBoard());
            }

            Move nextMove = (turn) ? board.allowSingleMouseMoveForWhite() : board.allowSingleMouseMoveForBlack(); // Allowing a single move for the current turn which is determined by the 'turn' boolean
            boolean moveSuccessful = board.makeMove(nextMove); // make the move and measure if it was successful
            moveMade = true;

            if (moveSuccessful) {
                turn = !turn; // if the move was successful then change the turn. If it wasn't then it will loop back and the unsuccessful side has another turn.
                board.clearAllHighlights();
                moveMade = false;
            }
        }
    }
    public static void highlightAllLegalPieces(Move move,byte[][] chessBoard) {
        // clearing any already made highlights
        //board.clearAllHighlights();

        Point[] allLegalMoves = move.getAllLegalMovesThatStartPieceCanMake(chessBoard, true); //TODO: make it get the data as to if white is on the top of bottom from the 'Board' class
        //System.out.println(Arrays.toString(allLegalMoves));
        if (!(allLegalMoves==null)) {
            for (Point currentMove : allLegalMoves) {
                //System.out.printf("XPos: %d\tYPos: %d.", Math.max(currentMove.x, 0), Math.max(currentMove.y, 0));
                //System.out.println("\t\t\t" + new Point(Math.max(currentMove.x, 0), Math.max(currentMove.y, 0)) + "\n");
                board.addHighlightedPiece(Math.max(currentMove.x, 0), Math.max(currentMove.y, 0));
            }
        } else {
            System.out.println("That piece is empty or currently not supported");
        }
    }
}
