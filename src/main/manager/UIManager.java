package main.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import main.option.*;

public class UIManager{
    private final boolean canClearScreen = true; //for testing purposes

    //https://www.w3schools.blog/ansi-colors-java

    private static UIManager instance;

    private final Scanner scanner;

    private final String RESET;

    private HashMap<String, String> colorMap = new HashMap<>();

    public List<Options> prev;
    private Options currentPage;
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
        currentPage = null;

        setUpCommand();
    }

    public static UIManager getInstance(){
        if(instance == null)
        {
            instance = new UIManager();
        }
        
        return instance;
    } 

    public void sendAndReceive(Options options){
        if(currentPage != null && !prev.contains(currentPage)){
            prev.add(currentPage);
        }
        currentPage = options;

        prev = (List<Options>) prev.stream().distinct().collect(Collectors.toList());

        printOptions(options);

        String input = scanner.nextLine();
        
        clearScreen();

        try{
            if(GameManager.getInstance().turnDone && !input.equals("end")){
                setMessage1(getColoredText("red", "Unfortunately, you are unable to make any more actions. Please end the turn."));
                return;
            }
            else if(GameManager.getInstance().getCurrentPlayer().getCurrentHealth() <= 0)
            {
                setMessage1(getColoredText("red", "Your infrastructure health has reached zero. You cannot make anymore moves."));
                return;
            }

            int idx = Integer.parseInt(input) - 1;
            
            Option selected = options.getOptions().get(idx);

            if(selected.getIsUnlocked() == true){
                prev.add(options);
                selected.getAction().execute();
            }else{
                setMessage1(getColoredText("red", "This option is currently locked."));
                sendAndReceive(options);
            }
            
        }catch(Exception e){
            if(input.startsWith("i") && input.split(" ").length == 2){
                String[] cmd = input.split(" ");
                try{
                    int idx = Integer.parseInt(cmd[1]);

                    setMessage1(getDescription(idx - 1));
                }catch(NumberFormatException e2){
                    setMessage1(getColoredText("red", "Please input a number, not a character."));
                }
                sendAndReceive(options);
            }else{
                CommandManager.getInstance().invokeCommand(input, options);
            }
        }

    }

    private String getDescription(int idx){
        if(idx < 0 || idx > currentPage.getOptions().size()){
            return getColoredText("red", "Please type a correct argument.");
        }else{
            return currentPage.getOptions().get(idx).getDescription();
        }
    }

    public void printOptions(Options options){
        System.out.printf(getColoredText("green", "%-31s ")+ getColoredText("cyan", "Money: $%d\n\n"),options.getTitle(), GameManager.getInstance().getCurrentPlayer().getMoneyLeft());

        for(int i = 0; i < options.getOptions().size(); i++){
            Option option = options.getOptions().get(i);
            String title = option.getTitle();

            if(option.getIsUnlocked() == true){
                System.out.println((i+1) + ". " + title);
            }else{

                System.out.println((i+1) + ". " + getColoredText("red", "[LOCKED]"));
            }
            
        }

        System.out.println("");

        System.out.println(message1);
        System.out.println(message2);

        message1 = "";
    }

    public void clearScreen(){
        if(canClearScreen){
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
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
        System.out.println("Your income was " + getColoredText("cyan", "$" + GameManager.getInstance().getCurrentPlayer().calculateStuffEarnedPerRound()));
        System.out.println("Your current reputation is " + getColoredText("cyan", GameManager.getInstance().getCurrentPlayer().getReputation()+""));
        System.out.println("You currently have " + getColoredText("cyan", "$" + GameManager.getInstance().getCurrentPlayer().getMoneyLeft()));
        System.out.println("You have "+ getColoredText("yellow", GameManager.getInstance().getCurrentPlayer().getCurrentHealth()+"") + " infrastructure health out of " + getColoredText("yellow", GameManager.getInstance().getCurrentPlayer().getMaxHealth()+""));
        System.out.println("Last turn, the opponent has taking the following actions:");
        for(int i = 0; i < GameManager.getInstance().getActionsTook().size(); i++){
            System.out.println("\t-"+GameManager.getInstance().getActionsTook().get(i));
        }
        GameManager.getInstance().getActionsTook().clear();
        System.out.println("\nPlease press Enter to go to the main page.");
        scanner.nextLine();
       clearScreen();
    }

    private String getColor(String color){
        return colorMap.get(color);
    }

    public Options getCurrentPage()
    {
        return currentPage;
    }

    private void setUpCommand(){
        CommandManager cmd = CommandManager.getInstance();

        cmd.addCommand("h", () -> sendAndReceive(OptionPath.mainPage));
        cmd.addCommand("b", () -> {
            if(!prev.isEmpty()){
                Options back = prev.get(prev.size() - 1);
                prev.remove(prev.size() - 1);
                currentPage = null;
                sendAndReceive(back);
            }else{
                sendAndReceive(OptionPath.mainPage);
            }
        });
    }
}
