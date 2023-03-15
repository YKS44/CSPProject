package main;

import java.util.ArrayList;
import java.util.HashMap;

import main.manager.GameManager;
import main.manager.UIManager;
import main.option.Action;
import main.option.Option;
import main.option.OptionPath;
import main.option.Options;

public class Player {
    private String name;

    private int numOfMoves;
    private int currentMovesLeft;

    private int currentMoneyLeft;
    private int income;
    private int incomeAffected;

    private int maxHealth;
    private int currentHealth;

    private HashMap<String, HashMap<Action, Integer>> damageTook;

    public Player(String name) {
        this.name = name;

        numOfMoves = 3;
        currentMovesLeft = numOfMoves;

        currentMoneyLeft = 1000;
        income = 0;
        incomeAffected = income;

        maxHealth = 100;
        currentHealth = maxHealth;

        damageTook = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTurnsLeft() {
        return currentMovesLeft;
    }

    public void setNumOfMoves(int numOfMoves) {
        this.numOfMoves = numOfMoves;
    }

    public void decrementMovesLeft() {
        currentMovesLeft--;
    }

    public void resetNumOfMoves() {
        currentMovesLeft = numOfMoves;
    }

    public int getMoneyLeft() {
        return currentMoneyLeft;
    }

    public void decreseMoneyLeft(int amount) {
        currentMoneyLeft -= amount;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getIncome(){
        return income;
    }

    public void decreaseIncomeAffected(int income){
        incomeAffected -= income;
    }

    public void printDamageTook() {
        ArrayList<Option> damageTookOption = new ArrayList<>();

        if (!damageTook.isEmpty()) {
            for (int i = 0; i < damageTook.size(); i++) {
                String key = damageTook.keySet().toArray(String[]::new)[i];
                Action key2 = damageTook.get(key).keySet().toArray(Action[]::new)[i];
                int price = damageTook.get(key).get(key2);

                Option option = OptionPath.buyOption(key, () -> {
                    UIManager.getInstance().setMessage1("You have removed " + UIManager.getInstance().getColoredText("red", key));
                    damageTook.remove(key);
                    OptionPath.decrementMovesLeft();
                }, price);
                damageTookOption.add(option);
            }
        } else {
            UIManager.getInstance()
                    .setMessage1(UIManager.getInstance().getColoredText("red", "You have not receieved any attacks."));
            return;
        }
        Options page = new Options(damageTookOption, "What would you like to remove?");

        UIManager.getInstance().sendAndReceive(page);
    }

    public void updateStats() {
        if (!damageTook.isEmpty()) {
            for (int i = 0; i < damageTook.size(); i++) {
                String key = damageTook.keySet().toArray(String[]::new)[i];
                Action action = damageTook.get(key).keySet().toArray(Action[]::new)[i];
                action.execute();
            }
        }
    }

    public void addDamage(String name, Action action, int price){
        HashMap<Action, Integer> map2 = new HashMap<>();
        map2.put(action,price);

        damageTook.put(name,map2);
    }

    public int calculateIncome() {
        double calculateIncome = (100 * Math.sqrt(10 * GameManager.getInstance().getRoundNumber())) + income;
        incomeAffected = (int) Math.round(calculateIncome);
        updateStats();
        
        currentMoneyLeft += incomeAffected;
        return incomeAffected;
    }
}
