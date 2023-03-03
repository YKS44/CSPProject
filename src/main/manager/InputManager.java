package main.manager;

public class InputManager {
    private static final InputManager instance;
    static{
        instance = new InputManager();
    }

    public static InputManager getInstance(){
        return instance;
    }
}
