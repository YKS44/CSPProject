package main;

import main.manager.GameManager;

public class Main {
    public static void main(String[] args) throws Exception {
        GameManager.getInstance().gameInit();
    }
}
