package main.manager;

import java.util.HashMap;

import main.option.Action;
import main.option.Options;

public class CommandManager{
    private static CommandManager instance;

    private HashMap<String,Action> commandMap;

    private CommandManager(){
        commandMap = new HashMap<>();

    }

    public void addCommand(String name, Action action){
        commandMap.put(name,action);
    }

    public void invokeCommand(String name, Options page){
        if(commandMap.containsKey(name)){
            commandMap.get(name).execute();
        }else{
            UIManager.getInstance().setMessage1(UIManager.getInstance().getColoredText("red", "Incorrect Input"));
            UIManager.getInstance().sendAndReceive(page);
        }
    }

    public static CommandManager getInstance(){
        if(instance == null)
        {
            instance = new CommandManager();
        }

        return instance;
    }
}