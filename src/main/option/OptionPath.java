package main.option;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BooleanSupplier;

import main.manager.*;


public class OptionPath {
    private static Option nextPage(String name, Options page){
        return new Option(name, ()->{
            UIManager.getInstance().setMessage2(UIManager.getInstance().getColoredText("yellow", "Moves Left: " + GameManager.getInstance().getCurrentPlayer().getTurnsLeft()));
            UIManager.getInstance().sendAndReceive(page);
        }, "");
    }

    private static Option nextPage(String name, Options page, BooleanSupplier unlockCondition, String lockDescription, String description){
        return new Option(name, ()->{
            UIManager.getInstance().setMessage2(UIManager.getInstance().getColoredText("yellow", "Moves Left: " + GameManager.getInstance().getCurrentPlayer().getTurnsLeft()));
            UIManager.getInstance().sendAndReceive(page);
        }, unlockCondition, lockDescription, description);
    }

    public static void decrementMovesLeft(){
        GameManager.getInstance().getCurrentPlayer().decrementMovesLeft();
    }

    private static void setMessage(String message){
        UIManager.getInstance().setMessage1(message);
    }
    
    private static void addActionTook(String action){
        GameManager.getInstance().addActionTook(action);
    }

    public static Option buyOption(String name, Action action, int price, String description){
        String title = String.format("%-35s "+UIManager.getInstance().getColoredText("yellow", "$%d"),name,price);

        return new Option(title,()->{
            if(GameManager.getInstance().getCurrentPlayer().getMoneyLeft() >= price){
                decrementMovesLeft();
                action.execute();
                GameManager.getInstance().getCurrentPlayer().decreseMoneyLeft(price);
                addActionTook(name);
            }else{
                setMessage(UIManager.getInstance().getColoredText("red", "You do not have enough money."));
            }
        }, description);
    }

    public static Option buyOption(String name, Action action, int price, BooleanSupplier isUnlocked, String lockDescription, String description){
        String title = String.format("%-35s "+UIManager.getInstance().getColoredText("yellow", "$%d"),name,price);

        return new Option(title,()->{
            if(GameManager.getInstance().getCurrentPlayer().getMoneyLeft() >= price){
                decrementMovesLeft();
                action.execute();
                GameManager.getInstance().getCurrentPlayer().decreseMoneyLeft(price);
                addActionTook(name);
            }else{
                setMessage(UIManager.getInstance().getColoredText("red", "You do not have enough money."));
            }
        },isUnlocked, lockDescription, description);
    }

    public static Option buyOptionHidden(String name, Action action, int price, BooleanSupplier isUnlocked, String lockDescription, String description){
        String title = String.format("%-35s "+UIManager.getInstance().getColoredText("yellow", "$%d"),name,price);

        return new Option(title,()->{
            if(GameManager.getInstance().getCurrentPlayer().getMoneyLeft() >= price){
                decrementMovesLeft();
                action.execute();
                GameManager.getInstance().getCurrentPlayer().decreseMoneyLeft(price);
            }else{
                setMessage(UIManager.getInstance().getColoredText("red", "You do not have enough money."));
            }
        },isUnlocked, lockDescription, description);
    }

    private static String lockedDescriptionBuy(String item){
        return UIManager.getInstance().getColoredText("red", "This option can be unlocked by purchasing ") + UIManager.getInstance().getColoredText("green", item);
    }

    private static String lockedDescription(String txt){
        return UIManager.getInstance().getColoredText("red", "This option can be unlocked by " + txt);
    }

    private static String lockedDescriptionBuyOppo(String item){
        return UIManager.getInstance().getColoredText("red", "This option can be unlocked when opponent purchases ") + UIManager.getInstance().getColoredText("green", item);
    }

    /////////////////////////////////////////////////

    private static Option[] econOptions3 = {
        buyOption("ACTUALPRODUCT", ()->{

        }, 0, ()->{return true;}, lockedDescriptionBuy("Managers"), ""),

        buyOption("Overtime", ()->{

        }, 0, ()->{return true;}, lockedDescriptionBuy("Managers"), ""),

        buyOption("More Space", ()->{

        }, 0, ()->{return true;}, lockedDescriptionBuy("Managers"), ""),

        buyOption("I.C.B.M", ()->{

        }, 0, ()->{return true;}, lockedDescriptionBuy("Managers"), "")
    };

    private static Options econPage3 = Options.buildPage(econOptions3, "Page 3");


    private static Option[] econOptions2 = {
        buyOption("Employees For Hire", ()->{

        }, 0,()->{return true;}, lockedDescriptionBuy("Legitimate Inc."), ""),

        buyOption("Improve Website+", ()->{

        }, 0, ()->{return true;}, lockedDescriptionBuy("Employees for Hire"), ""),

        buyOption("Repair Technicians", ()->{

        }, 0, ()->{return true;}, lockedDescriptionBuy("Employees for Hire"), ""),

        buyOption("Megacorp", ()->{

        }, 0, ()->{return true;}, lockedDescriptionBuy("Employees for Hire"), ""),

        buyOption("Managers", ()->{

        },0, ()->{return true;}, lockedDescriptionBuy("Megacorp"), ""),

        nextPage("Page 3", econPage3)
    };

    private static Options econPage2 = Options.buildPage(econOptions2, "Page 2");

    private static Option[] econOptions1 = {
        buyOption("Improve Website", ()->{
            
        }, 0, ""),

        buyOption("Improve Efficiency", ()->{
            setMessage("improve effi");
        }, 0, ""),

        buyOption("Advertisements", ()->{
            setMessage("advertisement");
        }, 0, ""),

        buyOption("Longer Work Hours", ()->{

        }, 0, ""),

        buyOption("Legitimate Inc.", ()->{

        }, 0, ""),

        nextPage("Page 2", econPage2)
    };

    private static Options econPage1 = Options.buildPage(econOptions1, "Build up Economy");

    private static Option[] attackOption2 = {
        buyOption("Shut Down Website", ()->{

        }, 0, ()->{return true;}, lockedDescription("decreasing opponent's infrastructure HP to zero"), ""),

        buyOption("Monopolize", ()->{

        }, 0, ()->{return true;}, lockedDescription("meeting the win condition."), "")
    };

    private static Options attackPage2 = Options.buildPage(attackOption2, "Page 2");

    private static Option[] attackOptions1 = {
        buyOption("Decrease Opponent Income", ()->{
            setMessage("You have decreased opponent's income");
            GameManager.getInstance().getOtherPlayer().addDamageTook("Decreased Income",()->{
                GameManager.getInstance().getCurrentPlayer().decreaseIncomeAffected(100);
            });
        }, 100, ()->{return GameManager.getInstance().getRoundNumber() == 2;}, lockedDescription("going to round #2"), "Decreases oppo income"),
        buyOption("Scam Emails", ()->{

        }, 0, ()->{return true;}, lockedDescriptionBuyOppo("Employees for Hire"), ""),

        buyOption("Malware Emails", ()->{

        }, 0, ()->{return true;}, lockedDescriptionBuyOppo("Employees for Hire"), ""),

        buyOption("Malware+", ()->{

        }, 0, ()->{return true;}, lockedDescriptionBuyOppo("Employees for Hire"), ""),

        buyOptionHidden("Trojan Mails", ()->{
            setMessage("You have sent a trojan mail to the enemy.");
            GameManager.getInstance().getOtherPlayer().trojanMail = ()->{
                GameManager.getInstance().getCurrentPlayer().decreaseHealth(10);
            };
        }, 0, ()->{return true;}, lockedDescriptionBuyOppo("Employees for Hire"), ""),

        buyOption("DDOS", ()->{

        }, 0, ""),

        buyOption("Dronestrike", ()->{

        }, 0,()->{return true;}, lockedDescription("meeting win conditions"), ""),

        nextPage("Page 2", attackPage2)
    };

    private static Options attackPage = Options.buildPage(attackOptions1, "Attack Moves");


    private static Option[] reputationOptions = {
        buyOption("Vendor", ()->{

        }, 0, ""),

        buyOption("Contractor", ()->{

        }, 0, ""),

        buyOption("PR Team", ()->{

        }, 0, ""),

        buyOption("BLACKMARKET", ()->{

        }, 0, ""),

        buyOption("DARKNET", ()->{

        }, 0, ()->{return true;}, lockedDescriptionBuy("BLACKMARKET"), "")
    };

    private static Options reputationPage = Options.buildPage(reputationOptions, "Reputation Moves");

    private static Option[] marketHeisenbergOptions = {
        buyOption("Narcotica Maxima", ()->{

        }, 0, ""),

        buyOption("temere verba", ()->{

        }, 0, "")
    };

    private static Options marketHeisenbergPage = Options.buildPage(marketHeisenbergOptions, "Heisenberg.net");

    private static Option[] marketPlunderbayOptions = {
        buyOption("TotallyLegalMovies", ()->{

        }, 0, ""),

        buyOption("Side Hustle", ()->{

        }, 0, "")
    };

    private static Options marketPlunderbayPage = Options.buildPage(marketPlunderbayOptions, "Plunder Bay");

    private static Option[] marketDEADSEAOptions = {
        buyOption("Basic FakeID", ()->{

        }, 0, ""),

        buyOption("Basicer FakeID", ()->{

        }, 0, "")
    };

    private static Options marketDEADSEAPage = Options.buildPage(marketDEADSEAOptions, "DEADSEA.org");

    private static Option[] marketZeroSquadOptions = {
        buyOption("Burglary", ()->{

        }, 0, ""),

        buyOption("HARMFULMEMES", ()->{

        }, 0, ""),

        buyOption("TWITTERHACK", ()->{

        }, 0, "")
    };

    private static Options marketZeroSquadPage = Options.buildPage(marketZeroSquadOptions, "ZeroSquad.com");

    private static Option[] marketSilkyStreetOptions = {
        buyOption("FREEMARKET", ()->{

        }, 0, ""),

        buyOption("Reputable Seller", ()->{

        }, 0, "")
    };

    private static Options marketSilkyStreetPage = Options.buildPage(marketSilkyStreetOptions, "Silky Street");

    private static Option[] marketGreenUSOptions = {
        buyOption("LEGALIZATION", ()->{

        }, 0, ""),

        buyOption("Cruelty Squad", ()->{

        }, 0, "")
    };

    private static Options marketGreenUSPage = Options.buildPage(marketGreenUSOptions, "GreenUS");

    private static Option[] marketEmpireOnionOptions = {
        buyOption("FakestID", ()->{

        }, 0, ""),

        buyOption("Databased", ()->{

        }, 0, "")
    };

    private static Options marketEmpireOnionPage = Options.buildPage(marketEmpireOnionOptions, "Empire.onion");

    private static Option[] marketArmageddonOnionOptions = {
        buyOption("Governmental Access", ()->{

        }, 0, ""),

        buyOption("Drone Strike", ()->{

        }, 0, ()->{return true;}, lockedDescription("purchasing FakestID, Databased, and Governmental Access."), "")
    };

    private static Options marketArmageddonOnionPage = Options.buildPage(marketArmageddonOnionOptions, "armageddon.onion");

    private static Option[] marketOptions2 = {
        nextPage("Empire.onion", marketEmpireOnionPage, ()->{return true;}, lockedDescription("purchasing DARKNET for the third time."), ""),
        nextPage("armageddon.onion", marketArmageddonOnionPage, ()->{return true;}, lockedDescription("purchasing DARKNET for the fourth time"), "")
    };

    private static Options marketPage2 = Options.buildPage(marketOptions2, "Page 2");

    private static Option[] marketOptions1 = {
        nextPage("Heiesnberg.net", marketHeisenbergPage, ()->{return true;}, lockedDescription("purchasing BLACKMARKET for the first time"), ""),
        nextPage("Plunder Bay", marketPlunderbayPage, ()->{return true;}, lockedDescription("purchasing BLACKMARKET for the second time"), ""),
        nextPage("DEADSEA.org", marketDEADSEAPage, ()->{return true;}, lockedDescription("purchasing BLACKMARKET for the third time"), ""),
        nextPage("ZeroSquad.com", marketZeroSquadPage, ()->{return true;}, lockedDescription("purchasing BLACKMARKET for the fourth time"), ""),
        nextPage("Silky Street", marketSilkyStreetPage, ()->{return true;}, lockedDescription("purchasing DARKNET for the first time"), ""),
        nextPage("GreenUS", marketGreenUSPage, ()->{return true;}, lockedDescription("purchasing DARKNET for the second time"), ""),

        nextPage("Page 2", marketPage2)
    };

    private static Options marketPage1 = Options.buildPage(marketOptions1, "Markets");

    private static Option[] mainOptions = {
        nextPage("Build Economy", econPage1),
        nextPage("Launch Offensive", attackPage),
        nextPage("Improve Reputation", reputationPage),
        new Option("Repair Economy", ()->{
            UIManager.getInstance().setMessage2(UIManager.getInstance().getColoredText("yellow", "Moves Left: " + GameManager.getInstance().getCurrentPlayer().getTurnsLeft()));
            UIManager.getInstance().sendAndReceive(GameManager.getInstance().getCurrentPlayer().getRepairDamagePage());
        }, ""),
        nextPage("Markets", marketPage1)
    };

    public static Options mainPage = Options.buildPage(mainOptions, "Main Page");

    
}
