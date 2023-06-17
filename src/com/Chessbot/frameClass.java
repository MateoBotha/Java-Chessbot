package com.Chessbot;

import javax.swing.*;

public class frameClass extends JFrame {
    frameClass(String name) {
        this.add(new panelClass());
        this.setTitle(name);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
