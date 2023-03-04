package main.option;

import java.util.Arrays;

import main.manager.*;


public class OptionPath {

    /*
     * Test
     */
    private static Option[] options2 = {

    };
    public static Options page2 = new Options(Arrays.asList(options2));

    private static Option[] options1 = {
        new Option("This will do something", ()->{
            UIManager.getInstance().sendAndReceive(page2);
        }),
        new Option("This will do another thing", ()->{
            System.out.println("Wow you did it!");
        })
    };



    public static Options page1 = new Options(Arrays.asList(options1));
}
