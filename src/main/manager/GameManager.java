package main.manager;

import java.util.Scanner;

import main.Player;
import main.option.OptionPath;

public class GameManager {
    enum GameState{
        INGAME,
        END
    }

    private static GameManager instance;
    private final Scanner scanner;
    private Player currentPlayer;


    private Player player1;
    private Player player2;

    private GameState currentState;

    private GameManager(){
        scanner = new Scanner(System.in);
        player1 = new Player("Player 1");
        player2 = new Player("Player 2");

        currentPlayer = player1;
    }


    public void gameInit(){
        UIManager.getInstance().clearScreen();

        getPlayerName();

        System.out.println("Player1: " + player1.getName());
        System.out.println("Player2: " + player2.getName() + "\n");

        startGame();
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    private void getPlayerName(){
        for(int i = 0; i < 2; i++){
            UIManager.getInstance().clearScreen();

            System.out.println("Please type your name, " + currentPlayer.getName() + ".");

            String input = scanner.nextLine();
            currentPlayer.setName(input);
            changeTurn();

            UIManager.getInstance().clearScreen();
        }
    }

    private void changeTurn(){
        if(currentPlayer.equals(player1)){
            player1 = currentPlayer;
            currentPlayer = player2;
        }else if(currentPlayer.equals(player2)){
            player2 = currentPlayer;
            currentPlayer = player1;
        }
        UIManager.getInstance().printInColor("blue", "It is now your turn, " + currentPlayer.getName());
    }

    private void startGame(){
        currentState = GameState.INGAME;

        UIManager.getInstance().printInColor("yellow", "Type 'home' or 'h' to go back to the main page and 'back' or 'b' to go to the previous page.\n");
        UIManager.getInstance().sendAndReceive(OptionPath.mainPage);

        while(currentState != GameState.END){
            if(currentPlayer.getTurnsLeft() > 0){
                UIManager.getInstance().sendAndReceive(OptionPath.mainPage);
            }else{
                currentPlayer.resetNumOfMoves();
                changeTurn();
            }
        }
    }

    public static GameManager getInstance(){
        if(instance == null){instance = new GameManager(); }

        return instance;
    }
}

