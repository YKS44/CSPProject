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
        });
    }

    private static Option nextPage(String name, Options page, BooleanSupplier unlockCondition){
        return new Option(name, ()->{
            UIManager.getInstance().setMessage2(UIManager.getInstance().getColoredText("yellow", "Moves Left: " + GameManager.getInstance().getCurrentPlayer().getTurnsLeft()));
            UIManager.getInstance().sendAndReceive(page);
        }, unlockCondition);
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

    public static Option buyOption(String name, Action action, int price){
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
        });
    }

    public static Option buyOption(String name, Action action, int price, BooleanSupplier isUnlocked){
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
        },isUnlocked);
    }

    public static Option buyOptionHidden(String name, Action action, int price, BooleanSupplier isUnlocked){
        String title = String.format("%-35s "+UIManager.getInstance().getColoredText("yellow", "$%d"),name,price);

        return new Option(title,()->{
            if(GameManager.getInstance().getCurrentPlayer().getMoneyLeft() >= price){
                decrementMovesLeft();
                action.execute();
                GameManager.getInstance().getCurrentPlayer().decreseMoneyLeft(price);
            }else{
                setMessage(UIManager.getInstance().getColoredText("red", "You do not have enough money."));
            }
        },isUnlocked);
    }

    /////////////////////////////////////////////////

    private static Option[] econOptions3 = {
        buyOption("ACTUALPRODUCT", ()->{

        }, 0, ()->{return true;}),

        buyOption("Overtime", ()->{

        }, 0, ()->{return true;}),

        buyOption("More Space", ()->{

        }, 0, ()->{return true;}),

        buyOption("I.C.B.M", ()->{

        }, 0, ()->{return true;})
    };

    private static Options econPage3 = Options.buildPage(econOptions3, "Page 3");


    private static Option[] econOptions2 = {
        buyOption("Employees For Hire", ()->{

        }, 0,()->{return true;}),

        buyOption("Improve Website+", ()->{

        }, 0, ()->{return true;}),

        buyOption("Repair Technicians", ()->{

        }, 0, ()->{return true;}),

        buyOption("Megacorp", ()->{

        }, 0, ()->{return true;}),

        buyOption("Managers", ()->{

        },0, ()->{return true;}),

        nextPage("Page 3", econPage3)
    };

    private static Options econPage2 = Options.buildPage(econOptions2, "Page 2");

    private static Option[] econOptions1 = {
        buyOption("Improve Website", ()->{
            
        }, 0, ()->{return false;}),

        buyOption("Improve Efficiency", ()->{
            setMessage("improve effi");
        }, 0),

        buyOption("Advertisements", ()->{
            setMessage("advertisement");
        }, 0),

        buyOption("Longer Work Hours", ()->{

        }, 0),

        buyOption("Legitimate Inc.", ()->{

        }, 0),

        nextPage("Page 2", econPage2)
    };

    private static Options econPage1 = Options.buildPage(econOptions1, "Build up Economy");

    private static Option[] attackOption2 = {
        buyOption("Shut Down Website", ()->{

        }, 0, ()->{return true;}),

        buyOption("Monopolize", ()->{

        }, 0, ()->{return true;})
    };

    private static Options attackPage2 = Options.buildPage(attackOption2, "Page 2");

    private static Option[] attackOptions = {
        buyOption("Decrease Opponent Income", ()->{
            setMessage("You have decreased opponent's income");
            GameManager.getInstance().getOtherPlayer().addDamageTook("Decreased Income",()->{
                GameManager.getInstance().getCurrentPlayer().decreaseIncomeAffected(100);
            });
        }, 100),
        buyOption("Scam Emails", ()->{

        }, 0, ()->{return true;}),

        buyOption("Malware Emails", ()->{

        }, 0, ()->{return true;}),

        buyOption("Malware+", ()->{

        }, 0, ()->{return true;}),

        buyOptionHidden("Trojan Mails", ()->{
            setMessage("You have sent a trojan mail to the enemy.");
            GameManager.getInstance().getOtherPlayer().trojanMail = ()->{
                GameManager.getInstance().getCurrentPlayer().decreaseHealth(10);
            };
        }, 0, ()->{return true;}),

        buyOption("DDOS", ()->{

        }, 0),

        buyOption("Dronestrike", ()->{

        }, 0,()->{return true;}),

        nextPage("Page 2", attackPage2)
    };

    private static Options attackPage = Options.buildPage(attackOptions, "Attack Moves");


    private static Option[] reputationOptions = {
        buyOption("Vendor", ()->{

        }, 0),

        buyOption("Contractor", ()->{

        }, 0),

        buyOption("PR Team", ()->{

        }, 0),

        buyOption("BLACKMARKET", ()->{

        }, 0),

        buyOption("DARKNET", ()->{

        }, 0)
    };

    private static Options reputationPage = Options.buildPage(reputationOptions, "Reputation Moves");

    private static Option[] marketHeisenbergOptions = {
        buyOption("Narcotica Maxima", ()->{

        }, 0),

        buyOption("temere verba", ()->{

        }, 0)
    };

    private static Options marketHeisenbergPage = Options.buildPage(marketHeisenbergOptions, "Heisenberg.net");

    private static Option[] marketPlunderbayOptions = {
        buyOption("TotallyLegalMovies", ()->{

        }, 0),

        buyOption("Side Hustle", ()->{

        }, 0)
    };

    private static Options marketPlunderbayPage = Options.buildPage(marketPlunderbayOptions, "Plunder Bay");

    private static Option[] marketDEADSEAOptions = {
        buyOption("Basic FakeID", ()->{

        }, 0),

        buyOption("Basicer FakeID", ()->{

        }, 0)
    };

    private static Options marketDEADSEAPage = Options.buildPage(marketDEADSEAOptions, "DEADSEA.org");

    private static Option[] marketZeroSquadOptions = {
        buyOption("Burglary", ()->{

        }, 0),

        buyOption("HARMFULMEMES", ()->{

        }, 0),

        buyOption("TWITTERHACK", ()->{

        }, 0)
    };

    private static Options marketZeroSquadPage = Options.buildPage(marketZeroSquadOptions, "ZeroSquad.com");

    private static Option[] marketSilkyStreetOptions = {
        buyOption("FREEMARKET", ()->{

        }, 0),

        buyOption("Reputable Seller", ()->{

        }, 0)
    };

    private static Options marketSilkyStreetPage = Options.buildPage(marketSilkyStreetOptions, "Silky Street");

    private static Option[] marketGreenUSOptions = {
        buyOption("LEGALIZATION", ()->{

        }, 0),

        buyOption("Cruelty Squad", ()->{

        }, 0)
    };

    private static Options marketGreenUSPage = Options.buildPage(marketGreenUSOptions, "GreenUS");

    private static Option[] marketEmpireOnionOptions = {
        buyOption("FakestID", ()->{

        }, 0),

        buyOption("Databased", ()->{

        }, 0)
    };

    private static Options marketEmpireOnionPage = Options.buildPage(marketEmpireOnionOptions, "Empire.onion");

    private static Option[] marketArmageddonOnionOptions = {
        buyOption("Governmental Access", ()->{

        }, 0),

        buyOption("Drone Strike", ()->{

        }, 0, ()->{return true;})
    };

    private static Options marketArmageddonOnionPage = Options.buildPage(marketArmageddonOnionOptions, "armageddon.onion");

    private static Option[] marketOptions2 = {
        nextPage("Empire.onion", marketEmpireOnionPage, ()->{return true;}),
        nextPage("armageddon.onion", marketArmageddonOnionPage, ()->{return true;})
    };

    private static Options marketPage2 = Options.buildPage(marketOptions2, "Page 2");

    private static Option[] marketOptions1 = {
        nextPage("Heiesnberg.net", marketHeisenbergPage, ()->{return true;}),
        nextPage("Plunder Bay", marketPlunderbayPage, ()->{return true;}),
        nextPage("DEADSEA.org", marketDEADSEAPage, ()->{return true;}),
        nextPage("ZeroSquad.com", marketZeroSquadPage, ()->{return true;}),
        nextPage("Silky Street", marketSilkyStreetPage, ()->{return true;}),
        nextPage("GreenUS", marketGreenUSPage, ()->{return true;}),

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
        }),
        nextPage("Markets", marketPage1)
    };

    public static Options mainPage = Options.buildPage(mainOptions, "Main Page");

    
}
