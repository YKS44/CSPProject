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

        printOptions(options);

        String input = scanner.nextLine();
        
        clearScreen();

        try{
            if(GameManager.getInstance().turnDone && !input.equals("end")){
                setMessage1(getColoredText("red", "Unfortunately, you are unable to make any more actions. Please end the turn."));
                return;
            }

            int idx = Integer.parseInt(input) - 1;
            
            Option selected = options.getOptions().get(idx);

            if(selected.getIsUnlocked() == true){
                prev.add(options);
                selected.getAction().execute();
            }else{
                setMessage1(getColoredText("red", "This option is currently locked."));
            }
            
        }catch(Exception e){
            CommandManager.getInstance().invokeCommand(input);
        }

    }

    public void printOptions(Options options){
        System.out.printf(getColoredText("green", "%-31s ")+ getColoredText("cyan", "Money: $%d\n\n"),options.getTitle(), GameManager.getInstance().getCurrentPlayer().getMoneyLeft());

        for(int i = 0; i < options.getOptions().size(); i++){
            Option option = options.getOptions().get(i);

            if(option.getIsUnlocked() == true){
                System.out.println((i+1) + ". " + option.getTitle());
            }else{
                System.out.println((i+1) + ". " + option.getTitle() + getColoredText("red", "[LOCKED]"));
            }
            
        }

        System.out.println("");

        System.out.println(message1);
        System.out.println(message2);

        message1 = "";
        //message2 = "";
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

    public void turnChange(){
        System.out.println("It is your turn, " + getColoredText("green", GameManager.getInstance().getCurrentPlayer().getName()) + "\n");
        System.out.println("Your income was " + getColoredText("cyan", "$" + GameManager.getInstance().getCurrentPlayer().calculateIncome()));
        System.out.println("You currently have " + getColoredText("cyan", "$" + GameManager.getInstance().getCurrentPlayer().getMoneyLeft()));
        System.out.println("You have "+ getColoredText("yellow", GameManager.getInstance().getCurrentPlayer().getCurrentHealth()+"") + " infrastructure health out of " + getColoredText("yellow", GameManager.getInstance().getCurrentPlayer().getMaxHealth()+""));
        System.out.println("Last turn, the opponent has taking the following actions:");
        for(int i = 0; i < GameManager.getInstance().getActionsTook().size(); i++){
            System.out.println("\t-"+GameManager.getInstance().getActionsTook().get(i));
        }
        GameManager.getInstance().getActionsTook().clear();
        System.out.println("\nPlease input anything to go to the main page.");
        scanner.nextLine();
       clearScreen();
    }

    private String getColor(String color){
        return colorMap.get(color);
    }

    private void setUpCommand(){
        CommandManager cmd = CommandManager.getInstance();

        cmd.addCommand("h", () -> sendAndReceive(OptionPath.mainPage));
        cmd.addCommand("b", () -> {
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
