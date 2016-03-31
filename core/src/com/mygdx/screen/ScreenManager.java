package com.mygdx.screen;

public class ScreenManager {

    private static Screen currentScreen;

    public static void setSreen(Screen screen) {
        if (currentScreen != null)
            currentScreen.dispose();
        currentScreen = screen;
        currentScreen.create();
    }

    public static Screen getCurrentScreen() {
        return currentScreen;
    }


}
