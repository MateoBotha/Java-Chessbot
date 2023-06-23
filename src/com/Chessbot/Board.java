package com.Chessbot;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Board extends JFrame {
    private final panel boardPanel;
    Board(String name) {
        boardPanel = new panel();
        this.add(boardPanel);
        this.setTitle(name);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setMinimumSize(new Dimension(boardPanel.Screen_Width,boardPanel.Screen_Height)); // making the window size never get smaller to prolong the space on the side. [This feature may not be permanent]
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
    /**
     * Makes a move on the board! It also has a basic legal move detection system, which allows it to stop a user trying to move an empty space, or trying to capture their own piece!
     * @return True if the move was successful [you weren't trying to move an empty piece], and false if it wasn't successful [you tried to move an empty piece]
     **/
    public boolean makeMove(Move move) {
        byte[][] boardCopy = boardPanel.board;

        byte[] moveArray = move.getMove();
        byte pieceStartX = moveArray[0], pieceStartY = moveArray[1], pieceEndX = moveArray[2], pieceEndY = moveArray[3];
        if ((boardCopy[pieceStartX][pieceStartY] != 0) && ((boardCopy[pieceEndX][pieceEndY] == 0) || (((boardCopy[pieceStartX][pieceStartY]<7/*is white start piece*/))?boardCopy[pieceEndX][pieceEndY]>6:boardCopy[pieceEndX][pieceEndY]<7))) {
            //Okay, So, the above if statement says that if you are not trying to move an empty space, and either the end piece is empty or you are moving into the other sides piece [capturing their piece].
            boardCopy[pieceEndX][pieceEndY] = boardCopy[pieceStartX][pieceStartY]; // duplicating the piece to its end location
            boardCopy[pieceStartX][pieceStartY] = 0; // deleting the original

            boardPanel.board = boardCopy;
            return true;
        } else {
            return false;
        }
    }
}
class panel extends JPanel {
    int Screen_Width = 1000;
    int Screen_Height = 500;
    static final int numSquaresWidth = 8;
    static final int numSquaresHeight = 8;
    int squareWidth;
    int squareHeight;
    int widthBorder = 100; // the chessBoards default width border [it can be set to anything because it updates when the programme starts]
    int heightBorder = 100; // the chessBoards default height border [it can also be set to anything]
    int boardX; // the chessBoards xPos [which is calculated in the paintComponent function]
    int boardY; // the chessBoards yPos [which is also calculated in the paintComponent function]
    int boardWidth = 480; // the height of the chess board
    int boardHeight = 480; // the width of the chess board
    Color darkSquare = new Color(171,122,101);
    Color lightSquare = new Color(238,216,192);
    byte[][] board = new byte[numSquaresWidth][numSquaresHeight]; //The 'board' array works as follows:
    // 0 = empty, 1 = White pawn, 2 = White bishop, 3 = White knight, 4 = White rook, 5 = White queen, 6 = White king,
    // 7 = Black pawn, 8 = Black bishop, 9 = Black knight, 10 = Black rook, 11 = Black queen, 12 = Black king.
    panel() {
        this.setPreferredSize(new Dimension(Screen_Width,Screen_Height));
        this.setLayout(null);
        setupBoard();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Screen_Height = getHeight();
        Screen_Width = getWidth();
        squareWidth = (Screen_Width-(widthBorder*2))/numSquaresWidth; // calculating how wide each square should be, and has the widthBorder to control the width border
        squareHeight = (Screen_Height-(heightBorder*2))/numSquaresHeight; // calculating how high each square should be, and has the heightBorder to control the height border

        //calculating the size of the height and width border to make the board appear in the center
        boardX = (Screen_Width/2)-((boardWidth)/2); // calculating the xPos of the chessboard [centered with the help of the boardWidth]
        boardX = Math.max(boardX, 0); // making it always be positive
        boardY = (Screen_Height/2)-((boardHeight)/2); // calculating the yPos of the chessBoard [centered with the help of boardHeight]
        boardY = Math.max(boardY,0); // making it also always be positive
        widthBorder = boardX; // setting the widthBorder so that the boardX is where the boards topRight xPos really is
        heightBorder = boardY; // doing the same for heightBorder


        //calculating the largest size that the board can be
        int sizeX = Math.min(Screen_Width, Screen_Height); // setting the width to be the smallest of the Screen_width and Screen_height
        int sizeY = Math.min(Screen_Height,Screen_Width); // doing the same thing
        boardWidth = sizeX;
        boardHeight = sizeY;
        draw(g);
        //TODO: make this code above better and make it so that a constant number of pixels that aren't part of the board are visible on the x axis. Like the widthBorder but it should work if the window is resized.
    }
    public void draw(Graphics g) {
        for (int ySquare = 0; ySquare < numSquaresHeight; ySquare++) {
            for (int xSquare = 0; xSquare < numSquaresWidth; xSquare++) {
                g.setColor(((xSquare+ySquare)%2!=0)?darkSquare:lightSquare); // changes the colour of the currentSquare
                g.fillRect(widthBorder+xSquare*squareWidth,heightBorder+ySquare*squareHeight,squareWidth,squareHeight); //draws the current square

                try {
                    drawPiece(g, board[xSquare][ySquare], xSquare, ySquare);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        repaint();
    }
    public void setupBoard() {
        for (int i = 0; i < numSquaresHeight; i++) {
            for (int j = 0; j < numSquaresWidth; j++) {
                // white pieces
                if (i == numSquaresHeight-1) {
                    // bottom row
                    switch (j) {
                        case 0, 7 -> board[j][i] = 4; //rooks
                        case 1, 6 -> board[j][i] = 3;//knights
                        case 2, 5 -> board[j][i] = 2;//bishops
                        case 3 -> board[j][i] = 5;//queen
                        case 4 -> board[j][i] = 6;//king
                    }
                }
                if (i == numSquaresHeight-2) {
                    board[j][i] = 1; // pawns
                }

                //black pieces
                if (i == 0) {
                    // top row
                    switch (j) {
                        case 0, 7 -> board[j][i] = 10; //rooks
                        case 1, 6 -> board[j][i] = 9;//knights
                        case 2, 5 -> board[j][i] = 8;//bishops
                        case 3 -> board[j][i] = 11;//queen
                        case 4 -> board[j][i] = 12;//king
                    }
                }
                if (i == 1) {
                    board[j][i] = 7; // pawns
                }
            }
        }
    }
    public void drawPiece(Graphics g, int pieceNum, int xSquare, int ySquare) throws IOException {
        String pathname = "C:\\Users\\mateo\\IdeaProjects\\Chessbot\\assets\\";
        boolean squareEmpty = false;
        switch (pieceNum) {
            case 0 -> squareEmpty = true;
            case 1 -> pathname += "White_Pawn.png";
            case 2 -> pathname += "White_Bishop.png";
            case 3 -> pathname += "White_Knight.png";
            case 4 -> pathname += "White_Rook.png";
            case 5 -> pathname += "White_Queen.png";
            case 6 -> pathname += "White_King.png";
            case 7 -> pathname += "Black_Pawn.png";
            case 8 -> pathname += "Black_Bishop.png";
            case 9 -> pathname += "Black_Knight.png";
            case 10 -> pathname += "Black_Rook.png";
            case 11 -> pathname += "Black_Queen.png";
            case 12 -> pathname += "Black_King.png";
        }
        if (!squareEmpty) {
            g.drawImage(ImageIO.read(new File(pathname)), widthBorder + xSquare * squareWidth, heightBorder + ySquare * squareHeight, squareWidth, squareHeight, null);
        }
    }
}