package main.option;

import java.util.Arrays;

import main.manager.*;


public class OptionPath {
    private static void nextPage(Options page){
        UIManager.sendAndReceive(page);
    }

    /*
     * Test
     */

    private static Option[] options3 = {
        new Option("Wow! a third page!", ()->{
            System.out.println("third page!!!!");
        })
    };
    
    private static Options page3 = new Options(Arrays.asList(options3), "Third Page");

    private static Option[] options2 = {
        new Option("This is a second page", ()->{
            System.out.println("this is the first option of second page");
        }),

        new Option("Go to THIRD PAGE!", ()->{
            nextPage(page3);
        })
    };
    private static Options page2 = new Options(Arrays.asList(options2), "Second Page");

    private static Option[] options1 = {
        new Option("This will do something", ()->{
            nextPage(page2);
        }),
        new Option("This will do another thing", ()->{
            System.out.println("Wow you did it!");
        })
    };



    public static Options mainPage = new Options(Arrays.asList(options1), "Main Page");
}
