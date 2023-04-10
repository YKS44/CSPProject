package main.option;

import java.util.function.BooleanSupplier;

public class Option{
    private String title;
    private String description;
    private String lockDescription;
	private Action action;
    private BooleanSupplier isUnlockedSupplier;

	public Option(String title, Action action, BooleanSupplier isUnlocked, String lockDescription, String description){
		this.title = title;
		this.action = action;
        this.isUnlockedSupplier = isUnlocked;
        this.lockDescription = lockDescription;
        this.description = description;
    }

    public Option(String title, Action action, String description){
        this.title = title;
        this.action = action;
        this.description = description;
        this.isUnlockedSupplier = ()->true;
        
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

    public String getDescription(){
        return isUnlockedSupplier.getAsBoolean() ? description : lockDescription;
    }
}