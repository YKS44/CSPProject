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

    static{
        instance = new GameManager();
    }

    private final Scanner scanner;

    
    private Player player1;
    private Player player2;
    
    private Player currentPlayer;
    private Player otherPlayer;

    public boolean turnDone;

    private GameState currentState;

    private GameManager(){
        scanner = new Scanner(System.in);
        player1 = new Player("Player 1");
        player2 = new Player("Player 2");

        currentPlayer = player1;
        otherPlayer = player2;

        turnDone = false;

        setUpCommand();
    }


    public void gameInit(){
        UIManager.getInstance().clearScreen();

        getPlayerName();
        startGame();
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public Player getOtherPlayer(){
        return otherPlayer;
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
            player2 = otherPlayer;

            currentPlayer = player2;
            otherPlayer = player1;
        }else if(currentPlayer.equals(player2)){
            player2 = currentPlayer;
            player1 = otherPlayer;

            currentPlayer = player1;
            otherPlayer = player2;
        }
        UIManager.getInstance().setMessage1(UIManager.getInstance().getColoredText("blue", "It is now your turn, " + currentPlayer.getName()));
    }

    private void startGame(){
        currentState = GameState.INGAME;

        UIManager.getInstance().setMessage2(UIManager.getInstance().getColoredText("yellow", "Type 'home' or 'h' to go back to the main page and 'back' or 'b' to go to the previous page.\n"));
        UIManager.getInstance().sendAndReceive(OptionPath.mainPage);

        while(currentState == GameState.INGAME){
            if(currentPlayer.getTurnsLeft() > 0){
                UIManager.getInstance().setMessage2(UIManager.getInstance().getColoredText("yellow", "Moves Left: " + currentPlayer.getTurnsLeft()));
                UIManager.getInstance().sendAndReceive(OptionPath.mainPage);
            }else{
                turnDone = true;
                UIManager.getInstance().setMessage2(UIManager.getInstance().getColoredText("blue", "Your turn has now ended. Type 'end' to end your turn."));
                UIManager.getInstance().sendAndReceive(OptionPath.mainPage);
            }
        }
    }

    private void setUpCommand(){
        CommandManager cmd = CommandManager.getInstance();

        cmd.addCommand("end", ()->{
            currentPlayer.resetNumOfMoves();
            UIManager.getInstance().clearScreen();
            UIManager.getInstance().printInColor("purple", "Changing Player..");
            try{
                Thread.sleep(2000);
            }catch(InterruptedException e){}
            UIManager.getInstance().clearScreen();
            changeTurn();
            turnDone = false;
        });
    }

    public static GameManager getInstance(){
        return instance;
    }
}

