package com.mygdx.handlers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class GameInputProcessor extends InputAdapter {

    public boolean keyDown(int k) {
        if(k == Keys.Z) {
            GameInput.setKey(GameInput.BUTTON1, true);
        }
        if(k == Keys.X) {
            GameInput.setKey(GameInput.BUTTON2, true);
        }
        if(k == Keys.ESCAPE) {
            GameInput.setKey(GameInput.EXIT, true);
        }
        return true;
    }

    public boolean keyUp(int k) {
        if(k == Keys.Z) {
            GameInput.setKey(GameInput.BUTTON1, false);
        }
        if(k == Keys.X) {
            GameInput.setKey(GameInput.BUTTON2, false);
        }
        if(k == Keys.ESCAPE) {
            GameInput.setKey(GameInput.EXIT, false);
        }
        return true;
    }

}
