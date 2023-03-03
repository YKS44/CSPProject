package main.manager;

import main.Player;

public class GameManager {
    private static final GameManager instance;
    static{
        instance = new GameManager();
    }

    private Player player1;
    private Player player2;

    public void init(){

    }

    public static GameManager getInstance(){
        return instance;
    }
}
