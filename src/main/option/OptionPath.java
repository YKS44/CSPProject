package main.option;

import java.util.ArrayList;
import java.util.Arrays;

import main.manager.*;


public class OptionPath {

    private static void nextPage(Options page){
        UIManager.getInstance().sendAndReceive(page);
    }

    private static void decrementMovesLeft(){
        GameManager.getInstance().getCurrentPlayer().decrementMovesLeft();
    }

    private static void removeFromList(Options page, String title){
        page.getOptions().removeIf(option -> option.getTitle().equals(title));
    }

    private static void setMessage(String message){
        UIManager.getInstance().setMessage1(message);
    }
    
    private static void buy(String message, int price, boolean removeFromList, Options page, String name){
        if(GameManager.getInstance().getCurrentPlayer().getMoneyLeft() >= price){
            setMessage(message);
            GameManager.getInstance().getCurrentPlayer().decreseMoneyLeft(price);

            if(removeFromList){
                removeFromList(page, name);
            }
            decrementMovesLeft();
        }else{
            setMessage(UIManager.getInstance().getColoredText("red", "You do not have enough money."));
        }
    }

    /*
     * Test
     */

    private static Option[] options3 = {
        new Option("Wow! a third page!", ()->{
            System.out.println("third page!!!!");
            decrementMovesLeft();
        })
    };
    
    private static Options page3 = new Options(new ArrayList<>(Arrays.asList(options3)), "Third Page");

    private static Option[] options2 = {
        new Option("This is a second page", ()->{
            setMessage("FIRst option OF THE SECOND PAGE");
            decrementMovesLeft();
        }),

        new Option("Go to THIRD PAGE!", ()->{
            nextPage(page3);
        })
    };
    private static Options page2 = new Options(new ArrayList<>(Arrays.asList(options2)), "Second Page");


    public static Options mainPage;

    private static int m1 = 100;
    private static Option[] mainOptions = {
        new Option("This will go to the second page!", ()->{
            nextPage(page2);
        }),
        new Option("This will do another thing $"+m1, ()->{
            buy("WOW you did it!", m1, true, mainPage, "This will do another thing $"+m1);
        })
    };


     static{
        mainPage = new Options(new ArrayList<>(Arrays.asList(mainOptions)), "Main Screen");
    }
}
