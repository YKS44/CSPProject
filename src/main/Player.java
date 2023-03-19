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

    public HashMap<String, Action> damageTook;
    private int costToRepair;

    public Action trojanMail = null;

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
        costToRepair = 100;
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

    public void decreaseHealth(int amount){
        currentHealth -= amount;
    }


    public Options getRepairDamagePage(){
        ArrayList<Option> repairDamagePage = new ArrayList<>();

        Option checkTrojanOption = OptionPath.buyOption("Check for Trojan Mails", ()->{
            UIManager.getInstance().setMessage2(UIManager.getInstance().getColoredText("yellow", "Moves Left: " + currentMovesLeft));
            if(trojanMail != null){
                trojanMail = null;
                UIManager.getInstance().setMessage1("You have removed " + UIManager.getInstance().getColoredText("red", "Trojan Mail"));
                UIManager.getInstance().sendAndReceive(OptionPath.mainPage);
            }else{
                UIManager.getInstance().setMessage1(UIManager.getInstance().getColoredText("red", "You did not have any trojan mails."));
                UIManager.getInstance().sendAndReceive(OptionPath.mainPage);
            }
        }, 0, "Checks for Trojan Mails. Uses one move when used.");

        repairDamagePage.add(checkTrojanOption);

        Option repairInfraHPOption = OptionPath.buyOption("Repair Infrastructure HP", ()->{

        }, 0, "Repairs infrastructure HP");

        repairDamagePage.add(repairInfraHPOption);

        if(!damageTook.isEmpty()){            
            int numOfDamage = damageTook.size();

            Option repairEconOption = OptionPath.buyOption("Repair All Attacks", ()->{
                damageTook.clear();
                decrementMovesLeft();
                UIManager.getInstance().setMessage1("You have removed all damages.");
                UIManager.getInstance().sendAndReceive(OptionPath.mainPage);
            }, numOfDamage * costToRepair, "Repairs all damage took");

            repairDamagePage.add(repairEconOption);
        }

        return Options.buildPage(repairDamagePage, "Repair Damage");

    }

    public void updateStats() {
        if (!damageTook.isEmpty()) {
            for (int i = 0; i < damageTook.size(); i++) {
                String key = damageTook.keySet().toArray(String[]::new)[i];
                Action action = damageTook.get(key);
                action.execute();
            }
        }

        if(trojanMail != null){
            trojanMail.execute();
        }
    }

    public void addDamageTook(String name, Action action){
        damageTook.put(name,action);
    }

    public int calculateIncome() {
        double calculateIncome = (100 * Math.sqrt(10 * GameManager.getInstance().getRoundNumber())) + income;
        incomeAffected = (int) Math.round(calculateIncome);
        updateStats();
        
        currentMoneyLeft += incomeAffected;
        return incomeAffected;
    }
}
