package com.Chessbot;

public class Move {
    private final int pieceStartX, pieceStartY, pieceEndX, pieceEndY;
    Move(int pieceStartX, int pieceStartY, int pieceEndX, int pieceEndY) {
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
}
