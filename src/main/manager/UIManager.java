package main.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import main.option.*;

public class UIManager{

    //https://www.w3schools.blog/ansi-colors-java

    private static UIManager instance;

    private final Scanner scanner;

    private final String RESET;

    private HashMap<String, String> colorMap = new HashMap<>();

    private List<Options> prev;
    private Options current;

    private UIManager(){
        scanner = new Scanner(System.in);

        RESET = "\033[0m";

        colorMap.put("black","\033[0;30m");
        colorMap.put("red","\033[0;31m");
        colorMap.put("green","\033[0;32m");
        colorMap.put("yellow","\033[0;33m");
        colorMap.put("blue","\033[0;34m");
        colorMap.put("purple","\033[0;35m");
        colorMap.put("cyan","\033[0;36m");
        colorMap.put("white","\033[0;37m");

        prev = new ArrayList<>();
        current = null;
    }

    public static UIManager getInstance(){
        if(instance == null){instance = new UIManager(); }

        return instance;
    } 

    public void sendAndReceive(Options options){
        if(current != null && !prev.contains(current)){
            prev.add(current);
        }
        current = options;

        System.out.println(options.getTitle() + "\n");

        for(int i = 0; i < options.getOptions().size(); i++){
            System.out.println((i+1) + ". " + options.getOptions().get(i).getTitle());
        }

        System.out.println("\n");
        String input = scanner.nextLine();
        
        clearScreen();

        try{
            int idx = Integer.parseInt(input) - 1;
    
            Option selected = options.getOptions().get(idx);
            prev.add(options);
            selected.getAction().execute();
        }catch(Exception e){
            if(input.toLowerCase().equals("home") || input.toLowerCase().equals("h")){
                prev.clear();
                sendAndReceive(OptionPath.mainPage);
            }else if(input.toLowerCase().equals("back") || input.toLowerCase().equals("b")){
                if(!prev.isEmpty()){
                    Options back = prev.get(prev.size() - 1);
                    prev.remove(prev.size() - 1);
                    current = null;
                    sendAndReceive(back);
                }else{
                    sendAndReceive(options);
                }
            }else{
                printInColor("red", "Incorrect Input");
                sendAndReceive(options);
            }
        }
    }

    public void send(Options options){
        String[] messages = (String[]) options.getOptions().stream().toArray();

        for(int i = 0; i < messages.length; i++){
            System.out.println((i+1) + messages[i]);
        }
    }

    public void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void printInColor(String color, String message){
        System.out.println(getColor(color) + message + RESET);
    }

    public String getColoredText(String color, String text){
        return getColor(color) + text + RESET;
    }

    private String getColor(String color){
        return colorMap.get(color);
    }
}
