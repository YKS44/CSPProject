package main.option;

public class Option{
    private String title;
	private Action action;

	public Option(String title, Action action){
		this.title = title;
		this.action = action;
    }

    public String getTitle(){
    	return title;
    }

    public Action getAction(){
	    return action;   
    }

}