package main;

import java.util.ArrayList;
import java.util.HashMap;

import main.Utils.MathUtil;
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

    private double reputation;

    private boolean canTakePhysicalAttack;
    private boolean canTakeReputationalAttack;

    public HashMap<String, Action> damageTook;
    private int costToRepair;

    public Action trojanMail = null;

    private UIManager uim  = UIManager.getInstance();

    private int numOfEmployees;
    private double emailTypeAttackEffectiveness;
    private double repairTechnician;

    private double numOfEmployeesMultiplier = 1.04;

    // OPTION UNLOCKING BOOLEANS
    public boolean legitimateInc = false;
    public boolean employeesForHire = false;
    public boolean megacorp = false;
    public boolean managers = false;

    public Player(String name) {
        this.name = name;

        numOfMoves = 3;
        currentMovesLeft = numOfMoves;

        currentMoneyLeft = 500000000;
        income = 0;
        incomeAffected = income;

        maxHealth = 100;
        currentHealth = maxHealth;

        reputation = 10000;

        canTakePhysicalAttack = false;
        canTakeReputationalAttack = false;

        numOfEmployees = 0;
        emailTypeAttackEffectiveness = 1.0;
        repairTechnician = 1.0;

        damageTook = new HashMap<>();
        costToRepair = 100;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean canTakeReputationalAttack()
    {
        return canTakeReputationalAttack;
    }

    public void multiplyNumOfEmployeesMultiplierBy(double amount)
    {
        numOfEmployeesMultiplier *= amount;
    }

    public void setCanTakeReputationalAttack(boolean state)
    {
        canTakeReputationalAttack = state;
    }

    public int getTurnsLeft() {
        return currentMovesLeft;
    }

    public void multiplyIncomeBy(double amount)
    {
        income = (int) Math.round(income * amount);
    }

    public void increaseNumOfEmployees(int amount)
    {
        numOfEmployees += amount;
    }

    public void increaseNumOfMoves(int amount)
    {
        numOfMoves += amount;
    }

    public void decrementMovesLeft() {
        currentMovesLeft--;
    }

    public void decreaseIncome(int amount)
    {
        income -= amount;
    }

    public void resetNumOfMoves() {
        currentMovesLeft = numOfMoves;
    }

    public double getReputation()
    {
        return reputation;
    }

    public boolean canTakePhysicalAttack(){
        return canTakePhysicalAttack;
    }

    public void setCanTakePhysicalAttack(boolean state)
    {
        canTakePhysicalAttack = state;
    }

    public void increaseReputation(double amount)
    {
        reputation += amount;
    }
    public void doEmailTypeAttack(int amount)
    {
        calculateEmailTypeAttackEffectiveness();

        amount = (int) Math.round(amount * emailTypeAttackEffectiveness);
        currentHealth -= amount;
    }

    public void calculateEmailTypeAttackEffectiveness()
    {
        emailTypeAttackEffectiveness = (Math.pow(numOfEmployeesMultiplier, numOfEmployees)) * repairTechnician;
    }

    public void setRepairTechnicianMultiplier(double amount)
    {
        repairTechnician *= amount;
    }

    public void setCostOfRepairMultiplier(double amount)
    {
        costToRepair *= amount;
        costToRepair = (int) Math.round(costToRepair);
    }

    public int getNumOfEmployees()
    {
        return numOfEmployees;
    }

    public void setIncomePerRound(int amount)
    {
        income = amount;
    }

    public int getMoneyLeft() {
        return currentMoneyLeft;
    }

    public void decreseMoneyLeft(int amount) {
        currentMoneyLeft -= amount;
    }

    public void increaseHealthLeft(int amount)
    {
        currentHealth += amount;
        MathUtil.clamp(currentHealth, 0, maxHealth);
    }

    public void increaseMaxHealth(int amount)
    {
        maxHealth += amount;
    }

    public void increaseIncomePerRound(int amount)
    {
        income += amount;
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

    public void affectIncomeAffected(int amount){
        incomeAffected += amount;
    }

    public void decreaseHealth(int amount){
        currentHealth -= amount;
    }


    public Options getRepairDamagePage(){
        ArrayList<Option> repairDamagePage = new ArrayList<>();

        Option checkTrojanOption = OptionPath.buyOption("Check for Trojan Mails", ()->{
            uim.setMessage2(uim.getColoredText("yellow", "Moves Left: " + currentMovesLeft));
            if(trojanMail != null){
                trojanMail = null;
                uim.setMessage1("You have removed " + uim.getColoredText("red", "Trojan Mail"));
                uim.sendAndReceive(OptionPath.mainPage);
            }else{
                uim.setMessage1(uim.getColoredText("red", "You did not have any trojan mails."));
                uim.sendAndReceive(OptionPath.mainPage);
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
                uim.setMessage1("You have removed all damages.");
                uim.setMessage2(uim.getColoredText("yellow", "Moves Left: " + currentMovesLeft));
                uim.sendAndReceive(OptionPath.mainPage);
            }, numOfDamage * costToRepair, "Repairs all damage took");

            repairDamagePage.add(repairEconOption);
        }

        return Options.buildPage(repairDamagePage, "Repair Damage");

    }

    public void updateDamageTook() {
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
        updateDamageTook();
        
        currentMoneyLeft += incomeAffected;
        return incomeAffected;
    }
}
