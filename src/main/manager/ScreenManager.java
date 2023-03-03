package main.manager;

public class ScreenManager {
    private static final ScreenManager instance;
    static{
        instance = new ScreenManager();
    }

    public static ScreenManager getInstance(){
        return instance;
    }
}
