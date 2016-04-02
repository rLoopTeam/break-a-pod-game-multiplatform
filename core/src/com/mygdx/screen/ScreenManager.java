package com.mygdx.screen;

import com.mygdx.rloop.BreakAPod;

import java.util.Stack;

public class ScreenManager {

    private BreakAPod game;

    private Stack<GameScreen> gameScreens;

    public static final int PLAY = 912837;

    public ScreenManager(BreakAPod game) {
        this.game = game;
        gameScreens = new Stack<GameScreen>();
        pushScreen(PLAY);
    }

    public BreakAPod game() {
        return game;
    }

    public void update(float dt) {
        gameScreens.peek().update(dt);
    }

    public void render() {
        gameScreens.peek().render();
    }

    private GameScreen getScreen(int screen) {
        if(screen == PLAY) return new Play(this);
        return null;
    }

    private void setScreen(int screen) {
        popScreen();
        pushScreen(screen);
    }

    private void pushScreen(int screen) {
        gameScreens.push(getScreen(screen));
    }

    private void popScreen() {
        GameScreen g = gameScreens.pop();
        g.dispose();
    }
}
