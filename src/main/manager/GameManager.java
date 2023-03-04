package main.manager;

import main.Player;

public class GameManager {
    private static GameManager instance;

    private GameManager(){}

    public static GameManager getInstance(){
        if(instance == null){instance = new GameManager(); }

        return instance;
    }
}
