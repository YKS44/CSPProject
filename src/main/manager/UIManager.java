package main.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import main.option.*;

public class UIManager{

    //https://www.w3schools.blog/ansi-colors-java

    private static UIManager instance;

    static{
        instance = new UIManager();
    }

    private final Scanner scanner;

    private final String RESET;

    private HashMap<String, String> colorMap = new HashMap<>();

    private List<Options> prev;
    private Options current;
    private String message1;
    private String message2;

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
        message1 = "";
        message2 = "";
        current = null;

        setUpCommand();
    }

    public static UIManager getInstance(){
        return instance;
    } 

    public void sendAndReceive(Options options){
        if(current != null && !prev.contains(current)){
            prev.add(current);
        }
        current = options;

        print(options);

        String input = scanner.nextLine();
        
        clearScreen();

        try{
            if(GameManager.getInstance().turnDone && !input.equals("end")){
                setMessage1(getColoredText("red", "You have run out of moves. Please end your turn"));
                return;
            }

            int idx = Integer.parseInt(input) - 1;
    
            Option selected = options.getOptions().get(idx);
            prev.add(options);
            selected.getAction().execute();
        }catch(Exception e){
            CommandManager.getInstance().invokeCommand(input);
        }

    }

    public void print(Options options){
        System.out.printf(getColoredText("green", "%-25s ")+ getColoredText("cyan", "Money: $%d\n\n"),options.getTitle(), GameManager.getInstance().getCurrentPlayer().getMoneyLeft());

        for(int i = 0; i < options.getOptions().size(); i++){
            System.out.println((i+1) + ". " + options.getOptions().get(i).getTitle());
        }

        System.out.println("");

        System.out.println(message1);
        System.out.println(message2);

        message1 = "";
        message2 = "";
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

    public void setMessage1(String message){
        this.message1 = message;
    }

    public void setMessage2(String message){
        this.message2 = message;
    }

    private String getColor(String color){
        return colorMap.get(color);
    }

    private void setUpCommand(){
        CommandManager cmd = CommandManager.getInstance();

        cmd.addCommand("home", () -> sendAndReceive(OptionPath.mainPage));
        cmd.addCommand("back", () -> {
            if(!prev.isEmpty()){
                Options back = prev.get(prev.size() - 1);
                prev.remove(prev.size() - 1);
                current = null;
                sendAndReceive(back);
            }else{
                sendAndReceive(OptionPath.mainPage);
            }
        });
    }
}
