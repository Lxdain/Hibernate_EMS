package com.hibernateems;

import javax.swing.*;

public class App {
    public static void main( String[] args ) {
        GUI gui = new GUI();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(800, 600);
        gui.setVisible(true);
    }
}
