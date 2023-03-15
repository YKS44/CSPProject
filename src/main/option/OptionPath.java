package main.option;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BooleanSupplier;

import main.manager.*;


public class OptionPath {
    private static Option nextPage(String name, Options page){
        return new Option(name, ()->UIManager.getInstance().sendAndReceive(page));
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
                action.execute();
                GameManager.getInstance().getCurrentPlayer().decreseMoneyLeft(price);
                addActionTook(name);
                decrementMovesLeft();
            }else{
                setMessage(UIManager.getInstance().getColoredText("red", "You do not have enough money."));
            }
        });
    }

    public static Option buyOption(String name, Action action, int price, BooleanSupplier isUnlocked){
        String title = String.format("%-35s "+UIManager.getInstance().getColoredText("yellow", "$%d"),name,price);

        return new Option(title,()->{
            if(GameManager.getInstance().getCurrentPlayer().getMoneyLeft() >= price){
                action.execute();
                GameManager.getInstance().getCurrentPlayer().decreseMoneyLeft(price);
                addActionTook(name);
                decrementMovesLeft();
            }else{
                setMessage(UIManager.getInstance().getColoredText("red", "You do not have enough money."));
            }
        },isUnlocked);
    }

    /////////////////////////////////////////////////

    private static Option[] econOptions3 = {

    };

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
        nextPage("Page 3", null)
    };

    private static Options econPage2 = new Options(new ArrayList<>(Arrays.asList(econOptions2)), "Page 2");

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

    private static Options econPage1 = new Options(new ArrayList<>(Arrays.asList(econOptions1)), "Build up Economy");


    private static Option[] attackOptions = {
        buyOption("Decrease Opponent Income", ()->{
            setMessage("You have decreased opponent's income");
            GameManager.getInstance().getOtherPlayer().addDamage("Decreased Income",()->{
                GameManager.getInstance().getCurrentPlayer().decreaseIncomeAffected(100);
            },100);
        }, 100)  
    };

    private static Options attackPage = new Options(new ArrayList<>(Arrays.asList(attackOptions)), "Attack Moves");


    private static Option[] reputationOptions = {
        
    };

    private static Options reputationPage = new Options(new ArrayList<>(Arrays.asList(reputationOptions)), "Reputation Moves");


    private static Option[] mainOptions = {
        nextPage("Build Economy", econPage1),
        nextPage("Launch Offensive", attackPage),
        nextPage("Improve Reputation", reputationPage),
        new Option("Repair Infrastructure",()->{
            GameManager.getInstance().getCurrentPlayer().printDamageTook();
        }),
        new Option("Markets", ()->{

        }) 
    };

    public static Options mainPage = new Options(new ArrayList<>(Arrays.asList(mainOptions)), "Main Page");
    
}
