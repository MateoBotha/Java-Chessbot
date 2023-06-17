package com.Chessbot;

import javax.swing.*;
import java.awt.*;

public class panelClass extends JPanel {
    int Screen_Width = 500;
    int Screen_Height = 500;
    static final int numSquaresWidth = 8;
    static final int numSquaresHeight = 8;
    int squareWidth;
    int squareHeight;
    byte[][] board = new byte[numSquaresWidth][numSquaresHeight]; //The 'board' array works as follows:
    // 0 = empty, 1 = White pawn, 2 = White bishop, 3 = White knight, 4 = White rook, 5 = White queen, 6 = White king,
    // 7 = Black pawn, 8 = Black bishop, 9 = Black knight, 10 = Black rook, 11 = Black queen, 12 = Black king.
    panelClass() {
        this.setPreferredSize(new Dimension(Screen_Width,Screen_Height));
        this.setLayout(null);
        setupBoard();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
        Screen_Height = getHeight();
        Screen_Width = getWidth();
        squareWidth = Screen_Width/numSquaresWidth;
        squareHeight = Screen_Height/numSquaresHeight;
    }
    public void draw(Graphics g) {
        for (int ySquare = 0; ySquare < numSquaresHeight; ySquare++) {
            for (int xSquare = 0; xSquare < numSquaresWidth; xSquare++) {
                g.setColor(((xSquare+ySquare)%2!=0)?Color.black:Color.white);
                g.fillRect(xSquare*squareWidth,ySquare*squareHeight,squareWidth,squareHeight);
            }
        }
    }
    public void setupBoard() {
        for (int i = 0; i < numSquaresHeight; i++) {
            for (int j = 0; j < numSquaresWidth; j++) {
                // white pieces
                if (i == 0) {
                    // bottom row
                    switch (j) {
                        case 0, 7 -> board[j][i] = 4; //rooks
                        case 1, 6 -> board[j][i] = 3;//knights
                        case 2, 5 -> board[j][i] = 2;//bishops
                        case 3 -> board[j][i] = 5;//queen
                        case 4 -> board[j][i] = 6;//king
                    }
                }
                if (i == 1) {
                    board[j][i] = 1; // pawns
                }

                //black pieces
                if (i == numSquaresHeight-1) {
                    // top row
                    switch (j) {
                        case 0, 7 -> board[j][i] = 10; //rooks
                        case 1, 6 -> board[j][i] = 8;//bishops
                        case 3 -> board[j][i] = 11;//queen
                        case 4 -> board[j][i] = 12;//king
                    }
                }
                if (i == numSquaresHeight-2) {
                    board[j][i] = 7; // pawns
                }
            }
        }
    }
}
