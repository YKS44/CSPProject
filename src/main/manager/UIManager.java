package main.manager;

import java.util.Scanner;
import java.util.function.Consumer;

import main.option.*;

public class UIManager{
    private static UIManager instance;

    private final Scanner scanner;

    private UIManager(){
        scanner = new Scanner(System.in);
    }

    public static UIManager getInstance(){
        if(instance == null){instance = new UIManager(); }

        return instance;
    } 

    public Action sendAndReceive(Options options){
        String[] messages = (String[]) options.getOptions().stream().toArray();

        for(int i = 0; i < messages.length; i++){
            System.out.println((i+1) + messages[i]);
        }

        int input = scanner.nextInt();
        Option selected = options.getOptions().get(input);

        return selected.getAction();
    }
}
