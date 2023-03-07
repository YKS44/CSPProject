package main;

public class Player {
    private String name;

    private int numOfMoves;
    private int currentMovesLeft;

    public Player(String name){
        this.name = name;
        numOfMoves = 3;
        currentMovesLeft = numOfMoves;
    }

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public int getTurnsLeft(){return currentMovesLeft;}
    public void setNumOfMoves(int numOfMoves){this.numOfMoves = numOfMoves;}

    public void decrementMovesLeft(){currentMovesLeft--;}
    public void resetNumOfMoves(){currentMovesLeft = numOfMoves;}
}
