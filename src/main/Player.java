package main;

public class Player {
    private String name;

    private int numOfMoves;
    private int currentMovesLeft;

    private int currentMoneyLeft;

    public Player(String name){
        this.name = name;

        numOfMoves = 3;
        currentMovesLeft = numOfMoves;
        currentMoneyLeft = 100;
    }

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public int getTurnsLeft(){return currentMovesLeft;}
    public void setNumOfMoves(int numOfMoves){this.numOfMoves = numOfMoves;}

    public void decrementMovesLeft(){currentMovesLeft--;}
    public void resetNumOfMoves(){currentMovesLeft = numOfMoves;}

    public int getMoneyLeft(){return currentMoneyLeft;}
    public void decreseMoneyLeft(int amount){currentMoneyLeft -= amount;}
}
