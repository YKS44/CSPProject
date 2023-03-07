package main.option;

import java.util.Arrays;

import main.manager.*;


public class OptionPath {
    private static void nextPage(Options page){
        UIManager.getInstance().sendAndReceive(page);
    }

    private static void decrementMovesLeft(){
        GameManager.getInstance().getCurrentPlayer().decrementMovesLeft();
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
    
    private static Options page3 = new Options(Arrays.asList(options3), "Third Page");

    private static Option[] options2 = {
        new Option("This is a second page", ()->{
            System.out.println("this is the first option of second page");
            decrementMovesLeft();
        }),

        new Option("Go to THIRD PAGE!", ()->{
            nextPage(page3);
        })
    };
    private static Options page2 = new Options(Arrays.asList(options2), "Second Page");

    private static Option[] mainOptions = {
        new Option("This will go to the second page!", ()->{
            nextPage(page2);
        }),
        new Option("This will do another thing", ()->{
            System.out.println("Wow you did it!");
            decrementMovesLeft();
        })
    };



    public static Options mainPage = new Options(Arrays.asList(mainOptions), UIManager.getInstance().getColoredText("green", "Main Screen"));
}
