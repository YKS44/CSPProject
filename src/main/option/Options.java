package main.option;

import java.util.List;

public class Options {
    private final List<Option> options;

	public Options(List<Option> options){
		this.options = options;
    }

    public List<Option> getOptions(){
	    return options;
    }

}
