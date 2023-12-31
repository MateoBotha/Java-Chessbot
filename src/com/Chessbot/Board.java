package com.Chessbot;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.Chessbot.Board.*;
public class Board extends JFrame {
    private final panel boardPanel;
    static volatile Point pieceThatMustFollowMouse = null; // the x and y of the piece that must follow the mouse
    static int lastMouseX = 0,lastMouseY = 0;
    static int currentMouseX,currentMouseY;
    boolean ColourThatFollowsMouse; // white = true, black = false;
    boolean piecesAreFollowingMouse = false; // this boolean is true if any pieces are following the mouse;
    static volatile Move mouseMove = null;
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

        boardPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                for (int ySquare = 0; ySquare < boardPanel.numSquaresHeight; ySquare++) {
                    for (int xSquare = 0; xSquare < boardPanel.numSquaresWidth; xSquare++) {
                        int x = boardPanel.widthBorder + (xSquare * boardPanel.squareWidth);
                        int y = boardPanel.heightBorder + (ySquare * boardPanel.squareHeight);
                        int width = boardPanel.squareWidth;
                        int height = boardPanel.squareHeight;
                        if (e.getX() > x && e.getY() > y && e.getX() < x+width && e.getY() < y+height) {
                            // if the current square that were looping over is the one that the mouse is in
                            if ((boardPanel.board[xSquare][ySquare]!=0)&&((ColourThatFollowsMouse)?boardPanel.board[xSquare][ySquare]<7:boardPanel.board[xSquare][ySquare]>6)) {
                                // if the selected square is not empty then it will follow the mouse. And if the selected piece is of the colour that the user is allowed to move
                                if (piecesAreFollowingMouse) {
                                    // if there are any pieces following the mouse;
                                    pieceThatMustFollowMouse = new Point(xSquare, ySquare);
                                }
                            }
                            if (pieceThatMustFollowMouse!=null&&(!(new Point(xSquare,ySquare).equals(pieceThatMustFollowMouse)))) {
                                // a piece is being followed and the user just clicked again

                                byte specialMoveID = 0; // setting it to 0 because 0 in binary is 00000000, which is not a special move
                                Move mouseMove = new Move(pieceThatMustFollowMouse.x, pieceThatMustFollowMouse.y,xSquare,ySquare,specialMoveID); // getting the mouseMove from the start and end points
                                //System.out.println(Arrays.toString(new int[]{pieceThatMustFollowMouse.x, pieceThatMustFollowMouse.y, xSquare, ySquare}));
                                pieceThatMustFollowMouse=null; // setting the piece that is following the mouse to null, essentially making it so that no piece is following the mouse
                                piecesAreFollowingMouse = false; // making it so that if the user clicks again, nothing will happen
                                Board.mouseMove = mouseMove;
                            }
                        }
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }
    public int openPromotionWindow(String windowName) {
        promotionWindow promotionWindow = new promotionWindow(windowName);
        while (!promotionWindow.promotionDecided) {
            {
                try {
                    Thread.sleep(1); // do something
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return promotionWindow.whatToPromoteTo;
    }
    public Point getPieceThatMustFollowMouseForWhite() {
        piecesAreFollowingMouse = true;
        ColourThatFollowsMouse = true;

        while (pieceThatMustFollowMouse==null) {
            continue;
        }
        return pieceThatMustFollowMouse;
    }
    public Point getPieceThatMustFollowMouseForBlack() {
        piecesAreFollowingMouse = true;
        ColourThatFollowsMouse = false;

        while (pieceThatMustFollowMouse==null) {
            continue;
        }
        return pieceThatMustFollowMouse;
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
            boardPanel.updateBoardAndAllNonMovingPieces = true;
            boardPanel.updatePieces = true;
            return true;
        } else {
            return false;
        }
    }
    /**
     * Allows the user to make a move as white with the mouse!
     * @return The move that was attempted by the user
    **/
    public Move allowSingleMouseMoveForWhite() {
        ColourThatFollowsMouse = true;
        piecesAreFollowingMouse = true;

        while (mouseMove == null) {
            continue;
        }
        return mouseMove;
    }
    /**
     * Allows the user to make a move as Black with the mouse!
     * @return The move that was attempted by the user
     **/
    public Move allowSingleMouseMoveForBlack() {
        ColourThatFollowsMouse = false;
        piecesAreFollowingMouse = true;

        while (mouseMove == null) {
            continue;
        }
        return mouseMove;
    }
    /**
     * adds a piece to be highlighted.
     * @param x The x position for the highlighted piece on the board
     * @param y The y position for the highlighted piece on the board
     **/
    public void addHighlightedPiece(int x, int y) {
        boolean isAlreadyHighlighted = false;
        boolean isOutOfBounds = false;
        // below is code to check if the current piece is already highlighted
        for (int i = 0; i < boardPanel.numHighlightedPieces; i++) {
            if (boardPanel.allHighlightedPieces[i].equals(new Point(x, y))) {
                // this element is already highlighted
                isAlreadyHighlighted = true;
            }
        }

        //below is code to check if the current piece is out of bounds
        if ((x>boardPanel.numSquaresWidth)||(y>boardPanel.numSquaresHeight)||(x<0)||(y<0)) {
            isOutOfBounds = true; // if it is bigger than the board or is negative then it is considered out of bounds
        }

        if ((!isAlreadyHighlighted)&&(!isOutOfBounds)) {
            // ^ if the new highlighted piece isn't already highlighted ^

            //adding the highlighted piece
            boardPanel.numHighlightedPieces++;
            Point[] copy = boardPanel.allHighlightedPieces; // making a copy
            boardPanel.allHighlightedPieces = new Point[boardPanel.numHighlightedPieces]; // updating the size
            System.arraycopy(copy, 0, boardPanel.allHighlightedPieces, 0, copy.length); // adding the previous amount of data
            boardPanel.allHighlightedPieces[boardPanel.numHighlightedPieces - 1] = new Point(x, y); // adding the new data

            // updating the board
            boardPanel.updateBoardAndAllNonMovingPieces = true;
            boardPanel.updatePieces = true;
        }
    }
    /**
     * Clears all highlights made.
     **/
    public void clearAllHighlights() {
        // resting the highlighted pieces
        boardPanel.numHighlightedPieces = 0;
        boardPanel.allHighlightedPieces = new Point[boardPanel.numHighlightedPieces];

        //updating the board
        boardPanel.updateBoardAndAllNonMovingPieces = true;
        boardPanel.updatePieces = true;
    }
    /**
     * gets the board as a 2D byte array.
     * @return The 2D byte array
     **/
    public byte[][] getBoard() {
        return boardPanel.board;
    }
}
class panel extends JPanel {
    int Screen_Width = 1000;
    int Screen_Height = 500;
    int previous_Screen_Width;
    int previous_Screen_Height;
    final int numSquaresWidth = 8;
    final int numSquaresHeight = 8;
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
    Color highLightedSquares = new Color(248, 2, 2);
    byte[][] board = new byte[numSquaresWidth][numSquaresHeight]; //The 'board' array works as follows:
    // 0 = empty, 1 = White pawn, 2 = White bishop, 3 = White knight, 4 = White rook, 5 = White queen, 6 = White king,
    // 7 = Black pawn, 8 = Black bishop, 9 = Black knight, 10 = Black rook, 11 = Black queen, 12 = Black king.
    BufferedImage boardAndAllNonMovingPieces; // this will allow for faster performance because we won't have to repaint everything
    boolean updateBoardAndAllNonMovingPieces = true; // this will trigger when you should update the bufferedImage 'boardAndAllNonMovingPieces'
    boolean updatePieces = true; // will override the 'updateBoardAndAllNonMovingPieces' to update the pieces, This is used to make the piece disappear when you click on it.
    byte numUpdates = 3; //this is awful code, but I found out that [on my pc] if I update the bufferedImage exactly 3 times, it loads properly [this is only for when the programme boots up... because for some reason after the first move... everything suddenly works] oh and btw if you set the number of times to 0,1,2 you get different results each time, which makes no sense! Can someone help me TODO: make it work without this weird hard coded running of five times
    int delay = 5000; // updates the bufferedImage every 5 seconds
    Timer timer; // every 'delay' milliseconds we update the bufferedImage, even if nothing happens
    int numHighlightedPieces = 0;// the num of highlighted pieces
    Point[] allHighlightedPieces = new Point[numHighlightedPieces]; // an array to store all highlighted pieces
    panel() {
        this.setPreferredSize(new Dimension(Screen_Width,Screen_Height));
        this.setLayout(null);
        timer = new Timer(delay, e -> {
            // updating the bufferedImage
            updateBoardAndAllNonMovingPieces = true;
            updatePieces = true;
        });
        timer.start();
        setupBoard();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        previous_Screen_Width = Screen_Width;
        previous_Screen_Height = Screen_Height;
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
        if ((numUpdates > 0)||(Screen_Width!=previous_Screen_Width)||(Screen_Height!=previous_Screen_Height)||(boardAndAllNonMovingPieces==null)||(updateBoardAndAllNonMovingPieces)) {
            // creating the imageBuffer to store the board and non-moving things if the window was resized (or if it is null so that we can initialize it, or [for like the 18 time] if an update to it is due) so that we don't have to repaint it
            boardAndAllNonMovingPieces = new BufferedImage(squareWidth * numSquaresWidth, squareHeight * numSquaresHeight, BufferedImage.TYPE_INT_RGB); // it could be set to boardWidth and boardHeight, but then it will be slightly inaccurate as sometimes we lose a coupe of pixels as it is impossible to evenly divide some numbers by 8 (or numSquaresWidth if you prefer)
            updateBoardAndAllNonMovingPieces = true; // update the board because the Screen was resized
            updatePieces = true;
            numUpdates--; // counting it down
        }
        draw(g);
        //TODO: make this code above better and make it so that a constant number of pixels that aren't part of the board are visible on the x axis. Like the widthBorder but it should work if the window is resized.
    }
    public void draw(Graphics g) {
        Graphics2D stationaryGraphics = boardAndAllNonMovingPieces.createGraphics(); // a place to draw all things that may stand still for a while
        if (updateBoardAndAllNonMovingPieces) {
            // drawing the squares on the BufferedImage
            for (int ySquare = 0; ySquare < numSquaresHeight; ySquare++) {
                for (int xSquare = 0; xSquare < numSquaresWidth; xSquare++) {
                    stationaryGraphics.setColor(((xSquare + ySquare) % 2 != 0) ? darkSquare : lightSquare); // changes the colour of the currentSquare
                    // the code below just checks if the current square is a highlighted piece and if so, it changes the colour of the square
                    for (Point allHighlightedPiece : allHighlightedPieces) {
                        if (allHighlightedPiece.equals(new Point(xSquare, ySquare))) {
                            // if the current piece is highlighted
                            stationaryGraphics.setColor(highLightedSquares);
                        }
                    }

                    stationaryGraphics.fillRect(xSquare * squareWidth, ySquare * squareHeight, squareWidth, squareHeight); //draws the current square
                }
            }
            updateBoardAndAllNonMovingPieces = false;
        }

        if (updatePieces) {
            // drawing the pieces on the bufferedImage
            for (int ySquare = 0; ySquare < numSquaresHeight; ySquare++) {
                for (int xSquare = 0; xSquare < numSquaresWidth; xSquare++) {
                    try {
                        if (!(new Point(xSquare, ySquare).equals(pieceThatMustFollowMouse))) {
                            drawPiece(stationaryGraphics, board[xSquare][ySquare], xSquare, ySquare);
                        } else {
                            stationaryGraphics.setColor(((xSquare + ySquare) % 2 != 0) ? darkSquare : lightSquare); // filling that square with the colour of the current Square because it will only replace when the window is resized [because of the optimization]

                            // the code below just checks if the current square is a highlighted piece and if so, it changes the colour of the square
                            for (Point allHighlightedPiece : allHighlightedPieces) {
                                if (allHighlightedPiece.equals(new Point(xSquare, ySquare))) {
                                    // if the current piece is highlighted
                                    stationaryGraphics.setColor(highLightedSquares);
                                }
                            }

                            stationaryGraphics.fillRect(xSquare * squareWidth, ySquare * squareHeight, squareWidth, squareHeight); //filling the square with the colour of the piece it was on to cover it up
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            updatePieces = false;
        }
        // drawing the bufferedImage
        g.drawImage(boardAndAllNonMovingPieces,widthBorder,heightBorder,(squareWidth*numSquaresWidth),squareHeight*numSquaresHeight, null);
        // drawing the piece that is currently been moved by the mouse
        if (pieceThatMustFollowMouse!=null) {
            // if there is any piece that is being moved by the mouse
            updatePieces = true; // update the pieces so that the one that is following the mouse will disappear
            try {
                lastMouseX = currentMouseX;
                lastMouseY = currentMouseY;
                currentMouseX = (getMousePosition() == null) ? lastMouseX : getMousePosition().x;
                currentMouseY = (getMousePosition() == null) ? lastMouseY : getMousePosition().y;
                //System.out.println((new File(getPathnameFromPieceNum(board[pieceThatMustFollowMouse.x][pieceThatMustFollowMouse.y]))));
                g.drawImage(ImageIO.read(new File(getPathnameFromPieceNum(board[pieceThatMustFollowMouse.x][pieceThatMustFollowMouse.y]))), currentMouseX-(squareWidth/2), currentMouseY-(squareHeight/2), squareWidth, squareHeight, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        repaint(); // repainting the moving piece and if a move was made then updating all the pieces
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
    public String getPathnameFromPieceNum(int pieceNum) {
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
        return (squareEmpty)?null:pathname;
    }
    public void drawPiece(Graphics2D g2d, int pieceNum, int xSquare, int ySquare) throws IOException {
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
            g2d.drawImage(ImageIO.read(new File(pathname)), xSquare * squareWidth, ySquare * squareHeight, squareWidth, squareHeight, null);
        }
    }
}
class promotionWindow {
    promotionPanel promotionPanel;
    JFrame frame = new JFrame();
    int whatToPromoteTo;
    boolean promotionDecided = false;
    promotionWindow(String name) {
        promotionPanel = new promotionPanel();

        frame.add(promotionPanel);
        frame.setTitle(name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setMinimumSize(new Dimension(promotionPanel.Screen_Width,promotionPanel.Screen_Height)); // making the window size never get smaller to prolong the space on the side. [This feature may not be permanent]
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
    class promotionPanel extends JPanel {
        int Screen_Width = 500;
        int Screen_Height = 500;

        int mouseX;
        int mouseY;
        boolean mouseClicked = false; // when it pulses, we check the x and y and set it to false
        promotionPanel() {
            this.setPreferredSize(new Dimension(Screen_Width,Screen_Height));
            this.setLayout(null);

            this.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    mouseX = e.getX();
                    mouseY = e.getY();
                    mouseClicked = true;
                    repaint();
                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
        }
        public void paintComponent(Graphics g) {
            Screen_Width = this.getWidth();
            Screen_Height = this.getHeight();

            super.paintComponent(g);
            draw(g);
        }
        public void draw(Graphics g) {
            byte[] promotablePieces = new byte[]{2,3,4,5}; // the three are part of: 0 = empty, 1 = pawn, 2 = bishop, 3 = knight, 4 = rook, 5 = queen, 6 = king. [no colour specified here]
            int numOfPromotablePieces = promotablePieces.length;

            for (int i = 0; i < numOfPromotablePieces; i++) {
                int x = 0;
                int y = i*(Screen_Height/numOfPromotablePieces);
                int width = Screen_Width;
                int height = Screen_Height/numOfPromotablePieces;
                g.drawRect(x,y,width,height);

                String piece = new String[]{"Empty","Pawn","Bishop","Knight","Rook","Queen","King"}[promotablePieces[i]];
                g.setFont(new Font("Ink free",Font.BOLD,25));
                FontMetrics metrics = g.getFontMetrics();
                g.drawString(piece,((x+width)/2)-(metrics.stringWidth(piece)/2),(y+(height)/2)); // drawing the pieces

                if (mouseClicked) {
                    if (((mouseX<(x+width))&&(mouseX>x))&&((mouseY<(y+height))&&(mouseY>y))) {
                        // if we are in a block
                        whatToPromoteTo = promotablePieces[i];
                        promotionDecided = true;
                        frame.dispose();
                    }
                }
            }
            mouseClicked = false; // turning it back off after we've drawn and checked every block

        }
    }
}
