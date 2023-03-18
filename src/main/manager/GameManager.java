package main.manager;

import java.util.ArrayList;
import java.util.Scanner;

import main.Player;
import main.option.OptionPath;

public class GameManager {
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

    private boolean isGameDone;

    private int roundNumber;

    private ArrayList<String> actionsTook;

    private GameManager(){
        scanner = new Scanner(System.in);
        player1 = new Player("Player 1");
        player2 = new Player("Player 2");

        currentPlayer = player1;
        otherPlayer = player2;

        roundNumber = 1;

        turnDone = false;

        actionsTook = new ArrayList<>();

        setUpCommand();
    }


    public void gameInit(){
        UIManager.getInstance().clearScreen();

        getPlayerName();
        startGameLoop();
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
            changeTurnForInit();

            
            UIManager.getInstance().clearScreen();
        }
    }

    private void changeTurnForInit(){
        if(currentPlayer.equals(player1)){
            currentPlayer.calculateIncome();
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
            roundNumber++;
        }
        UIManager.getInstance().turnChange();
    }

    private void startGameLoop(){
        isGameDone = false;

        UIManager.getInstance().setMessage2(UIManager.getInstance().getColoredText("yellow", "Type 'h' to go back to the main page and 'b' to go to the previous page.\n"));
        UIManager.getInstance().sendAndReceive(OptionPath.mainPage);

        while(!isGameDone){
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

    public void addActionTook(String action){
        actionsTook.add(action);
    }

    public ArrayList<String> getActionsTook(){
        return actionsTook;
    }

    public int getRoundNumber(){
        return roundNumber;
    }

    private void setUpCommand(){
        CommandManager cmd = CommandManager.getInstance();

        cmd.addCommand("end", ()->{
            currentPlayer.resetNumOfMoves();
            UIManager.getInstance().clearScreen();
            UIManager.getInstance().printInColor("purple", "Ending turn..");
            try{
                Thread.sleep(1500);
            }catch(InterruptedException e){}
            UIManager.getInstance().clearScreen();
            changeTurn();
            turnDone = false;
        });

        cmd.addCommand("test", ()->{
            System.out.println("Current Player: " + currentPlayer.damageTook);
            System.out.println("Other Player: " + otherPlayer.damageTook);
        });
    }

    public static GameManager getInstance(){
        return instance;
    }
}

