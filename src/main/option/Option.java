package main.option;

import java.util.function.BooleanSupplier;

public class Option{
    private String title;
	private Action action;
    private BooleanSupplier isUnlockedSupplier;

	public Option(String title, Action action, BooleanSupplier isUnlocked){
		this.title = title;
		this.action = action;
        this.isUnlockedSupplier = isUnlocked;
    }

    public Option(String title, Action action){
		this.title = title;
		this.action = action;
        this.isUnlockedSupplier = ()->{return true;};
    }

    public String getTitle(){
    	return title;
    }

    public Action getAction(){
	    return action;   
    }

    public boolean getIsUnlocked(){
        return  isUnlockedSupplier.getAsBoolean();
    }

}