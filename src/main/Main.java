package main;

import main.manager.*;

import main.option.OptionPath;

public class Main {
    public static void main(String[] args) throws Exception {
        UIManager.clearScreen();
        UIManager.sendAndReceive(OptionPath.mainPage);
    }
}
