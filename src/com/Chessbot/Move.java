package com.Chessbot;

import java.awt.*;
import java.util.Arrays;

public class Move {
    private final int pieceStartX, pieceStartY, pieceEndX, pieceEndY;
    boolean isSpecialMove;
    Move(int pieceStartX, int pieceStartY, int pieceEndX, int pieceEndY, byte specialMoveID) {
        this.pieceStartX = pieceStartX;
        this.pieceStartY = pieceStartY;
        this.pieceEndX = pieceEndX;
        this.pieceEndY = pieceEndY;

        
    }
    /**
     @return the move in a byte array where the order is: pieceStartX, pieceStartY, pieceEndX, pieceEndY.
     **/
    public byte[] getMove() {
        return new byte[]{(byte) pieceStartX, (byte) pieceStartY, (byte) pieceEndX, (byte) pieceEndY};
    }

    public boolean isLegal(byte[][] board) {
        return false;
    }
    public Point[] getAllLegalMovesThatStartPieceCanMake(byte[][] board, boolean whiteOnBottomOfScreen) {
        byte startPiece = board[pieceStartX][pieceStartY]; //The 'board' array works as follows:
        // 0 = empty, 1 = White pawn, 2 = White bishop, 3 = White knight, 4 = White rook, 5 = White queen, 6 = White king,
        // 7 = Black pawn, 8 = Black bishop, 9 = Black knight, 10 = Black rook, 11 = Black queen, 12 = Black king.

        // control variables
        boolean isEmpty = startPiece == 0; // checks if the piece is empty

        boolean isPawn = ((startPiece == 1)) || (startPiece == 7); // checks if the piece is a pawn of any colour
        boolean isBishop = (startPiece == 2) || (startPiece == 8); // checks if the piece is a bishop of any colour
        boolean isKnight = ((startPiece == 3) || (startPiece == 9)); // checks if the piece is a knight of any colour
        boolean isRook = ((startPiece == 4) || (startPiece == 10)); //checks if the piece is a rook of any colour
        boolean isQueen = (startPiece == 5) || (startPiece == 11); // checks if the piece is a queen of any colour
        boolean isKing = ((startPiece == 6) || (startPiece == 12)); // checks if the piece is a king of any colour

        boolean isSlidingPiece = (isBishop||isQueen||isRook);
        boolean isNotSlidingPiece = (isPawn||isKnight||isKing);

        if (!isEmpty) {
            // ^ if the player/user is not trying to move an empty piece ^
            int boardWidth = board.length, boardHeight = board[0].length;

            if (isSlidingPiece) {
                Point[] offsets = null;
                if (isRook) {
                    // adding the rooks offsets
                    offsets = new Point[4];
                    offsets[0] = new Point(1, 0); // right
                    offsets[1] = new Point(-1, 0); // left
                    offsets[2] = new Point(0, 1); // down
                    offsets[3] = new Point(0, -1); // up
                }
                if (isBishop) {
                    // adding the bishops offsets
                    offsets = new Point[4];
                    offsets[0] = new Point(1, 1); // diagonally to the bottom right
                    offsets[1] = new Point(-1, -1); // diagonally to the top left
                    offsets[2] = new Point(1, -1); // diagonally to the top right
                    offsets[3] = new Point(-1, 1); // diagonally to the bottom left
                }
                if (isQueen) {
                    // adding the queens offsets
                    offsets = new Point[8];
                    // rooks movement
                    offsets[0] = new Point(1, 0); // right
                    offsets[1] = new Point(-1, 0); // left
                    offsets[2] = new Point(0, 1); // down
                    offsets[3] = new Point(0, -1); // up

                    // bishops movement
                    offsets[4] = new Point(1, 1); // diagonally to the bottom right
                    offsets[5] = new Point(-1, -1); // diagonally to the top left
                    offsets[6] = new Point(1, -1); // diagonally to the top right
                    offsets[7] = new Point(-1, 1); // diagonally to the bottom left
                }

                Point[] output = new Point[0];
                for (Point currentOffset : offsets) {

                    Point newPos = new Point(pieceStartX + currentOffset.x, pieceStartY + currentOffset.y); // the next pos that we will be in
                    int currentX, currentY; // the current position that we are in... on the current offset

                    while ((newPos.x < boardWidth) && (newPos.y < boardHeight)) {
                        // ^ looping until we hit the end of the board ^
                        if ((newPos.x < 0) || (newPos.y < 0)) {
                            // if we are going before the board starts [i.e. currentPos < 0]... Then exit the loop. Or if we are going through a piece
                            break; // exiting the loop
                        }
                        boolean startingColour = board[pieceStartX][pieceStartY] < 7;// true = white, false = black
                        boolean newPosColour = board[newPos.x][newPos.y] < 7; // true = white,false = black
                        // making it exit if it reached a piece of the same colour
                        {
                            if ((board[newPos.x][newPos.y]!=0)&&(newPosColour == startingColour)) {
                                // if we are inside a piece of the same colour... exit
                                break;
                            }
                        }
                        // add the newPos to the end of the output
                        Point[] tmpPointArray = new Point[output.length + 1];
                        System.arraycopy(output, 0, tmpPointArray, 0, output.length);
                        tmpPointArray[tmpPointArray.length - 1] = newPos;
                        output = tmpPointArray;

                        // making it exit if it reached a piece of a different colour, after it added that piece... basically allowing you to eat it
                        {
                            if (board[newPos.x][newPos.y] != 0) {
                                // if it is not on an empty piece, break... but after weve added the piece to the output and also after we've checked if we are in the same colour. In other words if it is on the opposite colour piece then we have already added it, so we can break
                                break;
                            }
                        }

                        // updating newPos to be the next position
                        currentX = newPos.x;
                        currentY = newPos.y;
                        newPos = new Point(currentX + currentOffset.x, currentY + currentOffset.y);
                    }
                }
                return output; // returning the output
            }
            if (isNotSlidingPiece&&!isPawn) {
                Point[] output = new Point[0];

                Point[] offsets = null;
                if (isKnight) {
                    offsets = new Point[8];
                    offsets[0] = new Point(2, 1);
                    offsets[1] = new Point(1, 2);

                    offsets[2] = new Point(-1, 2);
                    offsets[3] = new Point(-2, 1);

                    offsets[4] = new Point(-2, -1);
                    offsets[5] = new Point(-1, -2);

                    offsets[6] = new Point(1, -2);
                    offsets[7] = new Point(2, -1);
                }
                if (isKing) {
                    offsets = new Point[8]; // 8  directions

                    offsets[0] = new Point(1,0);
                    offsets[1] = new Point(1,-1);
                    offsets[2] = new Point(-1,-1);
                    offsets[3] = new Point(0,-1);
                    offsets[4] = new Point(-1,0);
                    offsets[5] = new Point(-1,1);
                    offsets[6] = new Point(0,1);
                    offsets[7] = new Point(1,1);

                }
                for (Point currentOffset : offsets) {
                    Point newPos = new Point(pieceStartX + currentOffset.x, pieceStartY + currentOffset.y);

                    if ((newPos.x >= 0) && (newPos.y >= 0) && (newPos.y < boardHeight) && (newPos.x < boardWidth)) {
                        // if the current knightPos/kingPos is not out of bounds

                        if ((board[newPos.x][newPos.y]==0)||(((startPiece<7)?(board[newPos.x][newPos.y]>6):board[newPos.x][newPos.y]<7))) {
                            // ^ if the newPos is either empty or if it is of the opposite colour that the startPiece is.

                            // adding the newPos to the output
                            Point[] tmpPointArray = new Point[output.length + 1];
                            System.arraycopy(output, 0, tmpPointArray, 0, output.length);
                            tmpPointArray[tmpPointArray.length - 1] = newPos;
                            output = tmpPointArray;
                        }
                    }
                }
                return output;
            }
            if (isPawn) {
                Point[] allLegalMoveEndLocation = new Point[0];
                boolean pawnColour = (startPiece < 7); // true = white, false = black
                byte forwardForPawn = (byte) ((whiteOnBottomOfScreen) ? ((pawnColour) ? -1 : (1)) : -((pawnColour) ? -1 : (1))); // just add this and it will move the pawn forward
                boolean pawnHasNotMoved = ((pawnColour)&&((pieceStartY == boardHeight - 2)) || (!pawnColour)&&(pieceStartY == 1)); // if pawn is on start square, in the notation that white is on the bottom
                        pawnHasNotMoved = whiteOnBottomOfScreen == pawnHasNotMoved; // inverting it if whiteOnBottomOfScreen == false
                boolean somethingIsIn_frontOfPawn = board[pieceStartX][pieceStartY + forwardForPawn] != 0;
                boolean somethingIsDoubleIn_frontOfPawn = pawnHasNotMoved && (board[pieceStartX][pieceStartY + (forwardForPawn * 2)] != 0); // setting it to false if the pawn hasn't moved bc we only need this at the beginning

                //check if the pawn hasn't moved [i.e. it is on the top or bottom row]. And if there is nothing in-front of the pawn, [or double in-front]
                if (pawnHasNotMoved && !somethingIsIn_frontOfPawn && !somethingIsDoubleIn_frontOfPawn) {
                    // ^ the pawn is on the backRow ^

                    // adding double forward
                    Point[] tmpArray = new Point[allLegalMoveEndLocation.length + 1];
                    System.arraycopy(allLegalMoveEndLocation, 0, tmpArray, 0, allLegalMoveEndLocation.length);
                    tmpArray[tmpArray.length - 1] = new Point(pieceStartX, pieceStartY + (forwardForPawn * 2)); // add same x but double forward... literally
                    allLegalMoveEndLocation = tmpArray;
                }

                // adding the single forward
                if (!somethingIsIn_frontOfPawn) {
                    Point[] tmpArray = new Point[allLegalMoveEndLocation.length + 1];
                    System.arraycopy(allLegalMoveEndLocation, 0, tmpArray, 0, allLegalMoveEndLocation.length);
                    tmpArray[tmpArray.length - 1] = new Point(pieceStartX, pieceStartY + (forwardForPawn)); // again... the same x but single forward this time
                    allLegalMoveEndLocation = tmpArray;
                }

                // allowing the pawn to eat diagonally
                {
                byte pieceDiagonallyToRight = (pieceStartX!=boardWidth-1)?board[pieceStartX + 1][pieceStartY + forwardForPawn]:0; // zero because it can't eat to the right if there is nothing there
                byte pieceDiagonallyToLeft = (pieceStartX!=0)?board[pieceStartX - 1][pieceStartY + forwardForPawn]:0; // zero because it can't eat to the left if there is nothing there. Also, I am doing this because if it is on the edge then I get ArrayIndexOutOfBoundsException.

                boolean pieceDiagonallyToRightColour = pieceDiagonallyToRight<7; // true = white, false = black
                boolean pieceDiagonallyToLeftColour = pieceDiagonallyToLeft<7; // true = white, false = black
                    if ((pieceDiagonallyToRight != 0)&&(pawnColour!=pieceDiagonallyToRightColour)) {
                    // adding the square diagonally and to the right if it isn't empty and it isn't of the currentPawns colour
                    Point[] tmpArray = new Point[allLegalMoveEndLocation.length + 1];
                    System.arraycopy(allLegalMoveEndLocation, 0, tmpArray, 0, allLegalMoveEndLocation.length);
                    tmpArray[tmpArray.length - 1] = new Point(pieceStartX + 1, pieceStartY + (forwardForPawn)); // in-front and to the right of the pawn
                    allLegalMoveEndLocation = tmpArray;
                }
                if ((pieceDiagonallyToLeft != 0)&&(pawnColour!=pieceDiagonallyToLeftColour)) {
                    // adding the square diagonally and to the left if it isn't empty and it isn't of the currentPawns colour
                    Point[] tmpArray = new Point[allLegalMoveEndLocation.length + 1];
                    System.arraycopy(allLegalMoveEndLocation, 0, tmpArray, 0, allLegalMoveEndLocation.length);
                    tmpArray[tmpArray.length - 1] = new Point(pieceStartX - 1, pieceStartY + (forwardForPawn)); // in-front and to the left of the pawn
                    allLegalMoveEndLocation = tmpArray;
                }
            }

                return allLegalMoveEndLocation;
            }
        }
        // if it already returned a value then this function would have exited with that return value... If it hasn't returned anything, It will return null
        return null;
    }
}
