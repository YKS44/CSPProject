package main.option;

import java.util.List;

public class Options {
    private List<Option> options;
    private String title;

	public Options(List<Option> options, String title){
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
