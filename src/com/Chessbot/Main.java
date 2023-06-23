package com.Chessbot;

public class Main {
    static Board board;
    public static void main(String[] args) {
        board = new Board("Chess");

        System.out.println(board.makeMove(new Move(0,6,0,6)));
    }
}
