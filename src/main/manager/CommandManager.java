package main.manager;

import java.util.HashMap;

import main.option.Action;

public class CommandManager{
    private static CommandManager instance;

    static{
        instance = new CommandManager();
    }

    private HashMap<String,Action> commandMap;

    private CommandManager(){
        commandMap = new HashMap<>();
    }

    public void addCommand(String name, Action action){
        commandMap.put(name,action);
    }

    public void invokeCommand(String name){
        if(commandMap.containsKey(name)){
            commandMap.get(name).execute();
        }else{
            UIManager.getInstance().setMessage1(UIManager.getInstance().getColoredText("red", "Incorrect Input"));
        }
    }

    public static CommandManager getInstance(){
        return instance;
    }
}