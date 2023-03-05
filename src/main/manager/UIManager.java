package main.manager;

import java.util.HashMap;
import java.util.Scanner;

import main.option.*;

public class UIManager{

    //https://www.w3schools.blog/ansi-colors-java

    private static UIManager instance;

    private static final Scanner scanner = new Scanner(System.in);

    private final static String RESET = "\033[0m";

    private static HashMap<String, String> colorMap = new HashMap<>();
    
    static{
        colorMap.put("black","\033[0;30m");
        colorMap.put("red","\033[0;31m");
        colorMap.put("green","\033[0;32m");
        colorMap.put("yellow","\033[0;33m");
        colorMap.put("blue","\033[0;34m");
        colorMap.put("purple","\033[0;35m");
        colorMap.put("cyan","\033[0;36m");
        colorMap.put("white","\033[0;37m");
    }

    private UIManager(){}

    public static UIManager getInstance(){
        if(instance == null){instance = new UIManager(); }

        return instance;
    } 

    public static void sendAndReceive(Options options){
        System.out.println(options.getTitle() + "\n");

        for(int i = 0; i < options.getOptions().size(); i++){
            System.out.println((i+1) + ". " + options.getOptions().get(i).getTitle());
        }

        printInColor("yellow", "\nType 'home' or 'h' to go back to the main screen");

        String input = scanner.nextLine();
        clearScreen();

        try{
            int idx = Integer.parseInt(input) - 1;
    
            Option selected = options.getOptions().get(idx);
            selected.getAction().execute();
        }catch(Exception e){
            if(input.toLowerCase().equals("home") || input.toLowerCase().equals("h")){
                sendAndReceive(OptionPath.mainPage);
            }else{
                printInColor("red", "Incorrect Input");
                sendAndReceive(options);
                return;
            }
        }
    }

    public static void send(Options options){
        String[] messages = (String[]) options.getOptions().stream().toArray();

        for(int i = 0; i < messages.length; i++){
            System.out.println((i+1) + messages[i]);
        }
    }

    public static void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void printInColor(String color, String message){
        System.out.println(getColor(color) + message + RESET);
    }

    public static String coloredText(String color, String text){
        return getColor(color) + text + RESET;
    }

    private static String getColor(String color){
        return colorMap.get(color);
    }
}
