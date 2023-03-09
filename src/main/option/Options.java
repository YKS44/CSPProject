package main.option;

import java.util.ArrayList;
import java.util.List;

public class Options {
    private ArrayList<Option> options;
    private String title;

	public Options(ArrayList<Option> options, String title){
		this.options = options;
        this.title = title;
    }

    public List<Option> getOptions(){
	    return options;
    }

    public String getTitle(){
        return title;
    }
}
