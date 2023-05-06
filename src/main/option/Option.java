package main.option;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;


/**
 * @author Yuhyun Kim
 * 
 */
public class Option{
    private String title;
    private Supplier<String> description;
    private Supplier<String> lockDescription;
	private Action action;
    private BooleanSupplier isUnlockedSupplier;

    /**
     * Option with a lock condition
     */
	public Option(String title, Action action, BooleanSupplier isUnlocked, String lockDescription, String description){
		this.title = title;
		this.action = action;
        this.isUnlockedSupplier = isUnlocked;
        this.lockDescription = ()->lockDescription;
        this.description = ()->description;
    }

    /**
     * Normal option
     */
    public Option(String title, Action action, String description){
        this.title = title;
        this.action = action;
        this.description = ()->description;
        this.isUnlockedSupplier = ()->true;
        
    }

    /**
     * Bought once option with a lock condition
     */
    public Option(String title, Action action, BooleanSupplier isUnlocked, Supplier<String> lockDescription, String description)
    {
        this.title = title;
        this.action = action;
        this.isUnlockedSupplier = isUnlocked;
        this.lockDescription = lockDescription;
        this.description = ()->description;
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
        return isUnlockedSupplier.getAsBoolean() ? description.get() : lockDescription.get();
    }
}