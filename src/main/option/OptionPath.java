package main.option;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BooleanSupplier;

import main.manager.*;


public class OptionPath {
    private static UIManager uim = UIManager.getInstance();
    private static GameManager gm = GameManager.getInstance();

    private static Random random = new Random();

    private static Option nextPage(String name, Options page, String description){
        return new Option(name, ()->{
            uim.setMessage2(uim.getColoredText("yellow", "Moves Left: " + gm.getCurrentPlayer().getTurnsLeft()));
            uim.sendAndReceive(page);
        }, description);
    }

    private static Option nextPage(String name, Options page){
        return new Option(name, ()->{
            uim.setMessage2(uim.getColoredText("yellow", "Moves Left: " + gm.getCurrentPlayer().getTurnsLeft()));
            uim.sendAndReceive(page);
        }, "");
    }

    private static Option nextPage(String name, Options page, BooleanSupplier unlockCondition, String lockDescription, String description){
        return new Option(name, ()->{
            uim.setMessage2(uim.getColoredText("yellow", "Moves Left: " + gm.getCurrentPlayer().getTurnsLeft()));
            uim.sendAndReceive(page);
        }, unlockCondition, lockDescription, description);
    }

    public static void decrementMovesLeft(){
        gm.getCurrentPlayer().decrementMovesLeft();
    }

    private static void setMessage(String message){
        String[] words = message.split(" ");
        List<String> highlightedWords = new ArrayList<>();
        for(String word : words){
            if(word.startsWith("%")){
                String wrd = word.replace("%","");
                highlightedWords.add(uim.getColoredText("green", wrd));
            }else{
                highlightedWords.add(word);
            }
        }

        uim.setMessage1(String.join(" ", highlightedWords));
    }
    
    private static void addActionTook(String action){
        gm.addActionTook(action);
    }

    public static Option buyOption(String name, Action action, int price, String description){
        String title = String.format("%-35s "+uim.getColoredText("yellow", "$%d"),name,price);

        return new Option(title,()->{
            if(gm.getCurrentPlayer().getMoneyLeft() >= price){
                decrementMovesLeft();
                action.execute();
                gm.getCurrentPlayer().decreseCurrentMoneyLeft(price);
                addActionTook(name);
            }else{
                setMessage(uim.getColoredText("red", "You do not have enough money."));
                uim.sendAndReceive(uim.getCurrentPage());
            }
        }, description);
    }

    public static Option buyOption(String name, Action action, int price, BooleanSupplier isUnlocked, String lockDescription, String description){
        String title = String.format("%-35s "+uim.getColoredText("yellow", "$%d"),name,price);

        return new Option(title,()->{
            if(gm.getCurrentPlayer().getMoneyLeft() >= price){
                decrementMovesLeft();
                action.execute();
                gm.getCurrentPlayer().decreseCurrentMoneyLeft(price);
                addActionTook(name);
            }else{
                setMessage(uim.getColoredText("red", "You do not have enough money."));
                uim.sendAndReceive(uim.getCurrentPage());
            }
        },isUnlocked, lockDescription, description);
    }

    public static Option buyOptionHidden(String name, Action action, int price, BooleanSupplier isUnlocked, String lockDescription, String description){
        String title = String.format("%-35s "+uim.getColoredText("yellow", "$%d"),name,price);

        return new Option(title,()->{
            if(gm.getCurrentPlayer().getMoneyLeft() >= price){
                decrementMovesLeft();
                action.execute();
                gm.getCurrentPlayer().decreseCurrentMoneyLeft(price);
            }else{
                setMessage(uim.getColoredText("red", "You do not have enough money."));
            }
        },isUnlocked, lockDescription, description);
    }

    private static Option boughtOnceOption(Option option, BooleanSupplier bool)
    {
        return new Option(option.getTitle(), option.getAction(), bool, "This option can only be bought once.", option.getDescription() + " This option can only be bought once.");
    }

    private static String lockedDescriptionBuy(String item){
        return uim.getColoredText("red", "This option can be unlocked by purchasing ") + uim.getColoredText("green", item);
    }

    private static String lockedDescription(String txt){
        return uim.getColoredText("red", "This option can be unlocked by " + txt);
    }

    private static String lockedDescriptionBuyOppo(String item){
        return uim.getColoredText("red", "This option can be unlocked when opponent purchases ") + uim.getColoredText("green", item);
    }

    private static String lockedDescriptionCustom(String txt){
        String[] words = txt.split(" ");
        List<String> highlightedWords = new ArrayList<>();
        for(String word : words){
            if(word.startsWith("%")){
                String wrd = word.replace("%","");
                highlightedWords.add(uim.getColoredText("green", wrd));
            }else{
                highlightedWords.add(word);
            }
        }

        return "This option can be unlocked by " + String.join(" ", highlightedWords);
    }

    private static String description(String txt)
    {
        String[] words = txt.split(" ");
        List<String> highlightedWords = new ArrayList<>();
        for(String word : words){
            if(word.startsWith("%")){
                String wrd = word.replace("%","");
                highlightedWords.add(uim.getColoredText("green", wrd));
            }else{
                highlightedWords.add(word);
            }
        }

        return String.join(" ", highlightedWords);
    }

    /////////////////////////////////////////////////

    /* ------------------------------------------------------------------------------------------
     *
     * ECON OPTIONS
     * 
     * ------------------------------------------------------------------------------------------
     */

    private static Option[] econOptions3 = {
        boughtOnceOption(buyOption("ACTUALPRODUCT", ()->{
            gm.getCurrentPlayer().increaseIncomePerRound(1000);

            setMessage("Increased income per round by %$1000");
        }, 160000, ()->gm.getCurrentPlayer().managers, lockedDescriptionBuy("Managers"), "Sells a tangible product, drastically increasing income"), ()->gm.getCurrentPlayer().actualProduct),

        buyOption("Overtime", ()->{
            int amountOfMovesAdded = (int) (1 + Math.round((gm.getCurrentPlayer().getOvertimeMultiplier() * gm.getCurrentPlayer().getNumOfEmployees())));

            gm.getCurrentPlayer().increaseNumOfMoves(amountOfMovesAdded);

            setMessage("You have increased your number of moves per turn by %"+amountOfMovesAdded);
        }, 250000, ()->gm.getCurrentPlayer().managers, lockedDescriptionBuy("Managers"), "Increases number of moves per turn."),

        buyOption("More Space", ()->{
            gm.getCurrentPlayer().increaseHealthLeft(3);
            gm.getCurrentPlayer().increaseIncomePerRound(50);

            setMessage("You have increased your current health and income per round.");
        }, 300, ()->gm.getCurrentPlayer().managers, lockedDescriptionBuy("Managers"), "Increases income and slightly increases infrastructure health."),

        boughtOnceOption(buyOption("Socialism", ()->{
            int equalIncomeForAllBecauseHaHaSocialismFunny =  Math.round((gm.getCurrentPlayer().getIncome() + gm.getOtherPlayer().getIncome()) / 2) + (random.nextInt(101) - 50);

            gm.getCurrentPlayer().setIncomePerRound(equalIncomeForAllBecauseHaHaSocialismFunny);
            gm.getOtherPlayer().setIncomePerRound(equalIncomeForAllBecauseHaHaSocialismFunny);

            setMessage("You have set the income for both players to " + equalIncomeForAllBecauseHaHaSocialismFunny);
        }, 999999, ()->gm.getCurrentPlayer().managers, lockedDescriptionBuy("Managers"), "Turns the income of both sides equal to a value that ranges from +-50 of the average of the income between player one and two."), ()->gm.getCurrentPlayer().socialism)
        
    };

    private static Options econPage3 = Options.buildPage(econOptions3, "Page 3");


    private static Option[] econOptions2 = {
        buyOption("Employees For Hire", ()->{
            gm.getCurrentPlayer().decreaseIncome(10);
            gm.getCurrentPlayer().employeesForHire = true;
            gm.getCurrentPlayer().increaseNumOfEmployees(2);
            gm.getCurrentPlayer().increaseMaxHealth(1);
            gm.getCurrentPlayer().increaseHealthLeft(1);

            setMessage("You have decreased your income, increased your number of employees, max health, and current health. Your opponent can now start attacking you with scam emails and malware emails. You have also unlocked many econ upgrades.");

        }, 0,()->gm.getCurrentPlayer().legitimateInc, lockedDescriptionBuy("Legitimate Inc."), "Hire two employees, increasing infrastructure HP but slightly decreasing income. Also allows the opponent to start attacking with scam emails and malware emails. Gatekeeper for the rest of Econ upgrades."),

        buyOption("Improve Website+", ()->{
            gm.getCurrentPlayer().increaseHealthLeft(3);
            gm.getCurrentPlayer().increaseIncomePerRound(50);

            setMessage("You have increased your current health and income.");
        }, 300, ()->gm.getCurrentPlayer().employeesForHire, lockedDescriptionBuy("Employees for Hire"), description("Increases infrastructure HP and income per round, increases more than %Improve %Website")),

        buyOption("Repair Technicians", ()->{
            gm.getCurrentPlayer().decreaseIncome(100);
            gm.getCurrentPlayer().setRepairTechnicianMultiplier(0.9);
            gm.getCurrentPlayer().setCostOfRepairMultiplier(0.9);

            setMessage("You have decreased your income, opponent's email-type attack effectiveness, and cost of repair.");

        }, 5000, ()->gm.getCurrentPlayer().employeesForHire, lockedDescriptionBuy("Employees for Hire"), "Decreases income per round, hires three cybersecurity experts; experts do not increase email-type attacks’ effectiveness. Also decreases cost of repair and amount of infra damage done by all attacks from the opponent."),

        boughtOnceOption(buyOption("Megacorp", ()->{
            gm.getCurrentPlayer().setCanTakeReputationalAttack(true);
            gm.getCurrentPlayer().megacorp = true;

            setMessage("You can now take reputational damage from your opponent. You also unlocked %Managers");
        }, 100000, ()->gm.getCurrentPlayer().employeesForHire && gm.getCurrentPlayer().getReputation() >= 10/* TBD */, lockedDescriptionBuy("Employees for Hire"), description("Gatekeeper for %Managers, allows the opponent to start committing reputational damage (if reputation is low enough, this may not be purchased and may even be taken away)")), ()->gm.getCurrentPlayer().megacorpBO),

        buyOption("Managers", ()->{
            gm.getCurrentPlayer().multiplyIncomeBy(0.5);
            gm.getCurrentPlayer().multiplyNumOfEmployeesMultiplierBy(0.97);
            gm.getCurrentPlayer().managers = true;

            setMessage("You have decreased your income by 50 percent. The amount of damage you take from the number of your employees is now 3 percent less effective. You also unlocked the rest of econ options.");

        },90000, ()->gm.getCurrentPlayer().megacorp, lockedDescriptionBuy("Megacorp"), "Decreases the multiplier of employees to the amount of damage email-type attacks do. Gatekeeper for rest of Endgame econ upgrades."),

        nextPage("Page 3", econPage3)
    };

    private static Options econPage2 = Options.buildPage(econOptions2, "Page 2");

    private static Option[] econOptions1 = {
        buyOption("Improve Website", ()->{
            gm.getCurrentPlayer().increaseMaxHealth(1);
            gm.getCurrentPlayer().increaseHealthLeft(1);
            gm.getCurrentPlayer().increaseIncomePerRound(20);

            setMessage("You have increased your max health, current health, and your income per round.");
        }, 30, "Increases infrastructure HP and income per round"),

        boughtOnceOption(buyOption("Improve Efficiency", ()->{
            gm.getCurrentPlayer().increaseNumOfMoves(1);

            setMessage("You have increased your moves per turn by %1");
        }, 500, "Increase number of moves per turn."), ()->gm.getCurrentPlayer().improveEfficiency),

        buyOption("Advertisements", ()->{
            gm.getCurrentPlayer().increaseIncomePerRound(100);

            setMessage("You have increased your income per round by %$50");
        }, 200, "Increases income per round."),

        buyOption("Longer Work Hours", ()->{
            gm.getCurrentPlayer().increaseReputation(gm.getCurrentPlayer().getLongerWorkHours());

            setMessage("You have increased your reputation by %0.1");
        }, 1000, "Very slightly increases reputation and medium income increase."),
    
        boughtOnceOption(buyOption("Legitimate Inc.", ()->{
            gm.getCurrentPlayer().setCanTakePhysicalAttack(true);
            gm.getCurrentPlayer().legitimateInc = true;

            setMessage("You have unlocked %Employees %For %Hire. Your opponent can now commit physical attacks on you.");
        }, 10000, description("Gatekeeper for %Employees %For %Hire, also allows the opponent to start committing physical attacks")), ()->gm.getCurrentPlayer().legitimateIncBO),

        nextPage("Page 2", econPage2)
    };

    private static Options econPage1 = Options.buildPage(econOptions1, "Build up Economy");


    /* ------------------------------------------------------------------------------------------
     *
     * ATTACK OPTIONS
     * 
     * ------------------------------------------------------------------------------------------
     */

    private static Option[] attackOption2 = {
        buyOption("Shut Down Website", ()->{
            gm.setVictoriousPlayer(gm.getCurrentPlayer());
            gm.endGame();
        }, 0, ()->true, lockedDescription("decreasing opponent's infrastructure HP to zero"), "Winning move after severe infrastructure damage (The opponent cannot make a move after infra turns to zero, allowing the player to use this move and win)"),

        buyOption("Monopolize", ()->{
            gm.setVictoriousPlayer(gm.getCurrentPlayer());
            gm.endGame();
        }, 0, ()->true, lockedDescription("meeting the win condition."), "Econ win move, move unlocked only after severe economic advantage"),

        buyOption("Wipeout", ()->{
            gm.setVictoriousPlayer(gm.getCurrentPlayer());
            gm.endGame();
        }, 999999,()->true, lockedDescriptionCustom("meeting win conditions and purchasing %DARKNET"), "Winning move, only unlocked after high reputation and several requirements.")

    };

    private static Options attackPage2 = Options.buildPage(attackOption2, "Page 2");

    private static Option[] attackOptions1 = {
        // buyOption("Decrease Opponent Income (TEST)", ()->{
        //     // setMessage("You have decreased opponent's income");
        //     // gm.getOtherPlayer().addDamageTook("Decreased Income",()->{
        //     //     gm.getCurrentPlayer().affectIncomeAffected(-10000);
        //     // });
        //     gm.setVictoriousPlayer(gm.getCurrentPlayer());
        //     gm.endGame();
        // }, 100, "Decreases oppo income"),

        buyOption("Scam Emails", ()->{
            int damageTook = gm.getOtherPlayer().doEmailTypeAttack(1, "scam");

            setMessage("You have sent a scam email to your opponent. They have taken %" + damageTook + " damage.");
        }, 1, ()->gm.getOtherPlayer().getNumOfEmployees() > 0, lockedDescriptionBuyOppo("Employees for Hire"), "Damages opponent's infra HP. Effectiveness increases with the number of employees of the opponent."),

        buyOption("Malware Emails", ()->{
            int damageTook = gm.getOtherPlayer().doEmailTypeAttack(1, "malware");

            setMessage("You have sent a malware email to your opponent. They have taken %" + damageTook + " damage.");
        }, 80, ()->gm.getOtherPlayer().getNumOfEmployees() > 0, lockedDescriptionBuyOppo("Employees for Hire"), "Damages opponent's infra HP. Effectiveness can increase with better techniques learned later in the game, but at the beginning effectiveness increases with more employees"),

        buyOption("Malware+", ()->{
            gm.getOtherPlayer().decreaseHealth(2);

            setMessage("You have decreased your opponent's health by 2");
        }, 4000, ()->gm.getOtherPlayer().employeesForHire, lockedDescriptionBuyOppo("Employees for Hire"), description("“Downloads harmful software directly onto the computer just by opening the email”, does more infrastructure damage than normal %Malware %Emails")),

        buyOptionHidden("Trojan Mails", ()->{
            gm.getOtherPlayer().trojanMail = ()->{
                gm.getCurrentPlayer().decreaseReputation(1);
                gm.getOtherPlayer().increaseReputation(1);
            };
            setMessage("You have sent a trojan mail to the enemy.");
        }, 1000, ()->gm.getOtherPlayer().employeesForHire, lockedDescriptionBuyOppo("Employees for Hire"), "Does not show up at the beginning of the turn, and siphons one infra every turn. Better memorize your health!"),

        buyOption("DDOS", ()->{
            gm.getOtherPlayer().decreaseReputation(0.1);
            gm.getOtherPlayer().decreaseHealth(3);

            setMessage("You have decreased your opponent's current health and reputation");
        }, 10000, "Does reputation damage, and a decent amount of infrastructure damage"),

        nextPage("Page 2", attackPage2)
    };

    private static Options attackPage = Options.buildPage(attackOptions1, "Attack Moves");


    /* ------------------------------------------------------------------------------------------
     *
     * REPUTATION OPTIONS
     * 
     * ------------------------------------------------------------------------------------------
     */

    private static Option[] reputationOptions = {
        buyOption("Vendor", ()->{
            gm.getCurrentPlayer().increaseReputation(0.01);
            
            setMessage("You have increased your reputation by %0.01");
        }, 10, "Increases reputation slightly."),

        buyOption("Contractor", ()->{
            gm.getCurrentPlayer().increaseReputation(0.1);
            
            setMessage("You have increased your reputation by %0.1");
        }, 20, description("More expensive form of %Vendor, increases reputation more than %Vendor but still not that much")),

        boughtOnceOption(buyOption("PR Team", ()->{
            double newReputationEarnedPerRound = gm.getCurrentPlayer().increaseReputationEarnedPerRound(0.5);
            gm.getCurrentPlayer().multiplyNumOfEmployeesMultiplierBy(1.5);

            setMessage("You now gain %"+newReputationEarnedPerRound + " reputation per round. You also receive 1.5x more damage from email-type attacks.");
        }, 4000, "Severely increases vulnerability to email-type attacks, but you gain reputation per turn"), ()->gm.getCurrentPlayer().prTeam),

        buyOption("BLACKMARKET", ()->{
            gm.getCurrentPlayer().blackMarketCounter++;

            setMessage("Your BLACKMARKET level is now %" + gm.getCurrentPlayer().blackMarketCounter);
        }, 9999, description("Allows you access to more black markets. Costs a ton order of markets")),

        buyOption("DARKNET", ()->{
            gm.getCurrentPlayer().darkNetCounter++;

            setMessage("Your DARKNET level is now %" + gm.getCurrentPlayer().darkNetCounter);
        }, 14999, ()->gm.getCurrentPlayer().blackMarketCounter > 0, lockedDescriptionBuy("BLACKMARKET"), description("Allows you access to more advanced black markets situated on the %DARKNET"))
    };

    private static Options reputationPage = Options.buildPage(reputationOptions, "Reputation Moves");


    /* ------------------------------------------------------------------------------------------
     *
     * MARKET Sky.net
     * 
     * ------------------------------------------------------------------------------------------
     */

    private static Option[] marketSkyNetOptions = {
        boughtOnceOption(buyOption("Out of the Blue", ()->{
            gm.getCurrentPlayer().increaseOvertimeMultiplier(0.01); //TBD
            gm.getCurrentPlayer().increaseLongerWorkHours(100); //TBD

            setMessage("You have increased the efficiency of %Overtime and %Longer %Work %Hours.");
        }, 99134, description("Improves %Overtime and %Longer %Work %Hours.")), ()->gm.getCurrentPlayer().outOfTheBlue),

        buyOption("temere verba", ()->{
            gm.getCurrentPlayer().increaseReputation(10);//TBD
            gm.getCurrentPlayer().increaseIncomePerRound(1000);//TBD

            setMessage("You have increased your reputation and income per round.");
        }, 10000, "Improves infra and income.")
    };

    private static Options marketSkynetPage = Options.buildPage(marketSkyNetOptions, "Heisenberg.net");

    /* ------------------------------------------------------------------------------------------
     *
     * MARKET Conflix
     * 
     * ------------------------------------------------------------------------------------------
     */

    private static Option[] marketConflixOptions = {
        boughtOnceOption(buyOption("TotallyLegalMovies", ()->{
            gm.getCurrentPlayer().increaseNumOfMoves(1); //TBD

            setMessage("You have increased your moves per turn.");
        }, 4000, "Increases moves per turn"), ()->gm.getCurrentPlayer().totallyLegalMovies),

        boughtOnceOption(buyOption("Side Hustle", ()->{  
            int amount = gm.getCurrentPlayer().getNumOfMovesLeft() * 100;
            gm.getCurrentPlayer().increaseCurrentMoneyLeft(amount);

            setMessage("You have increased your money left by %" + amount);
        }, 104203, "Gives you money. The amount depends on how many spare moves you have."), ()->gm.getCurrentPlayer().sideHustle)
        
    };


    private static Options marketConflixPage = Options.buildPage(marketConflixOptions, "Plunder Bay");

    /* ------------------------------------------------------------------------------------------
     *
     * MARKET LivelyOcean.org
     * 
     * ------------------------------------------------------------------------------------------
     */

    private static Option[] marketLivelyOceanOptions = {
        boughtOnceOption(buyOption("Basic FakeID", ()->{
            gm.getCurrentPlayer().increaseReputation(10); //TBD

            setMessage("You can now access %DARKNET upgrades. You have also increased your reputation.");
        }, 2000, description("Allows you access to %DARKNET upgrades, increases reputation.")), ()->gm.getCurrentPlayer().basicFakeID),

        boughtOnceOption(buyOption("Basicer FakeID", ()->{
            setMessage("You have wasted a ton of money. You stooopid.");
        }, 999999999, "Wastes money, free of charge ;)"), ()->gm.getCurrentPlayer().basicerFakeID)
        
    };

    private static Options marketLivelyOceanPage = Options.buildPage(marketLivelyOceanOptions, "DEADSEA.org");

    /* ------------------------------------------------------------------------------------------
     *
     * MARKET PPProfessionalll
     * 
     * ------------------------------------------------------------------------------------------
     */

    private static Option[] marketProfessionalOptions = {
        buyOption("Filching", ()->{
            gm.getOtherPlayer().decreaseReputation(10); //TBD
            gm.getCurrentPlayer().increaseCurrentMoneyLeft(10); //TBD

            setMessage("You stole a little bit of money from your opponent.");
        }, 2000, ()->gm.getOtherPlayer().canTakePhysicalAttack(), "Your opponent cannot take physical attack yet.", "Siphons a small amount of money."),
            
        buyOption("HARMFULMEMES", ()->{
            gm.getOtherPlayer().decreaseReputation(10); //TBD

            setMessage("You have decreased your opponent's reputation.");
        }, 1000, "Does small damage to the opponent’s reputation"),

        buyOption("TWITTERHACK", ()->{
            gm.getOtherPlayer().decreaseReputation(10); //TBD
            gm.getOtherPlayer().decreaseHealth(10); //TBD
        }, 50000, "Does small damage to the opponent’s reputation and infrastructure.")
    };

    private static Options marketProfessionalPage = Options.buildPage(marketProfessionalOptions, "ZeroSquad.com");

    /* ------------------------------------------------------------------------------------------
     *
     * MARKET Center East
     * 
     * ------------------------------------------------------------------------------------------
     */


    private static Option[] marketCenterEastOptions = {
        buyOption("FREEMARKET", ()->{
            gm.getCurrentPlayer().increaseIncomePerRound(9000); //TBD
            
            setMessage("You have slightly increased your income.");
        }, 19234, "Slightly increases income."),

        boughtOnceOption(buyOption("Reputable Seller", ()->{
            gm.getCurrentPlayer().increaseReputation(10); //TBD

            setMessage("You have increased your reputation.");
        }, 20180, "Increases reputation, like duh what else would it do."), ()-> gm.getCurrentPlayer().reputableSeller)
    };

    private static Options marketCenterEastPage = Options.buildPage(marketCenterEastOptions, "Silky Street");

    /* ------------------------------------------------------------------------------------------
     *
     * MARKET Fuji Apples
     * 
     * ------------------------------------------------------------------------------------------
     */

    private static Option[] marketFujuApplesOptions = {
        boughtOnceOption(buyOption("LEGALIZATION", ()->{
            gm.getCurrentPlayer().increaseIncomePerRound(50000); //TBD

            setMessage("You have increased your income per round.");
        }, 56724, "Increases income per round. "), () ->gm.getCurrentPlayer().legalization),

        buyOption("Demoralize", ()->{
            gm.getOtherPlayer().decreaseNumOfEmployees(10); //TBD

            setMessage("You have decreased the number of opponent's employees. ");
        }, 99999, ()->gm.getOtherPlayer().canTakePhysicalAttack(), "Your opponent cannot take physical attack yet.", "Decreases the number of opponent’s employees")
    };

    private static Options marketFujiApplesPage = Options.buildPage(marketFujuApplesOptions, "GreenUS");

    /* ------------------------------------------------------------------------------------------
     *
     * MARKET Romeo.com
     * 
     * ------------------------------------------------------------------------------------------
     */

    private static Option[] marketRomeoComOptions = {
        buyOption("Legally Ghost", ()->{
            gm.setVictoriousPlayer(gm.getCurrentPlayer());
            gm.endGame();
        }, 666666, "One of the winning conditions."),

        buyOption("Databased", ()->{
            gm.setVictoriousPlayer(gm.getCurrentPlayer());
            gm.endGame();
        },202350, "Does nothing, one of the winning conditions.")
    };

    private static Options marketRomeoComPage = Options.buildPage(marketRomeoComOptions, "Empire.onion");

    /* ------------------------------------------------------------------------------------------
     *
     * MARKET armageddon.org
     * 
     * ------------------------------------------------------------------------------------------
     */

    private static Option[] marketArmageddonOrgOptions = {
        buyOption("Hippity Hoppity", ()->{
            gm.setVictoriousPlayer(gm.getCurrentPlayer());
            gm.endGame();
        }, 69696969, "One of the winning conditions")
    };

    private static Options marketArmageddonOrgPage = Options.buildPage(marketArmageddonOrgOptions, "armageddon.onion");

    /* ------------------------------------------------------------------------------------------
     *
     * MARKET OPTIONS
     * 
     * ------------------------------------------------------------------------------------------
     */

    private static Option[] marketOptions2 = {
        nextPage("Romeo.com", marketRomeoComPage, ()->gm.getCurrentPlayer().darkNetCounter >= 3, lockedDescription("purchasing DARKNET for the third time."), ""),
        nextPage("armageddon.org", marketArmageddonOrgPage, ()->gm.getCurrentPlayer().darkNetCounter >= 4, lockedDescription("purchasing DARKNET for the fourth time"), "")
    };

    private static Options marketPage2 = Options.buildPage(marketOptions2, "Page 2");

    private static Option[] marketOptions1 = {
        nextPage("Sky.net", marketSkynetPage, ()->gm.getCurrentPlayer().blackMarketCounter >= 1, lockedDescriptionCustom("purchasing %BLACKMARKET for the first time"), ""),
        nextPage("Conflix", marketConflixPage, ()->gm.getCurrentPlayer().blackMarketCounter >= 2, lockedDescriptionCustom("purchasing %BLACKMARKET for the second time"), ""),
        nextPage("LivelyOcean.org", marketLivelyOceanPage, ()->gm.getCurrentPlayer().blackMarketCounter >= 3, lockedDescriptionCustom("purchasing %BLACKMARKET for the third time"), ""),
        nextPage("PPProfessionalll", marketProfessionalPage, ()->gm.getCurrentPlayer().blackMarketCounter >= 4, lockedDescriptionCustom("purchasing %BLACKMARKET for the fourth time"), ""),
        nextPage("Center East", marketCenterEastPage, ()->gm.getCurrentPlayer().darkNetCounter >= 1, lockedDescriptionCustom("purchasing %DARKNET for the first time"), ""),
        nextPage("Fuji Apples", marketFujiApplesPage, ()->gm.getCurrentPlayer().darkNetCounter >= 2, lockedDescriptionCustom("purchasing %DARKNET for the second time"), ""),

        nextPage("Page 2", marketPage2)
    };

    private static Options marketPage1 = Options.buildPage(marketOptions1, "Markets");

    private static Option[] mainOptions = {
        nextPage("Build Economy", econPage1),
        nextPage("Launch Offensive", attackPage),
        nextPage("Improve Reputation", reputationPage),
        new Option("Repair Economy", ()->{
            uim.setMessage2(uim.getColoredText("yellow", "Moves Left: " + gm.getCurrentPlayer().getTurnsLeft()));
            uim.sendAndReceive(gm.getCurrentPlayer().getRepairDamagePage());
        }, ""),
        nextPage("Markets", marketPage1)
    };

    public static Options mainPage = Options.buildPage(mainOptions, "Main Page");
}
