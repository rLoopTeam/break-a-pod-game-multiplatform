package com.mygdx.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;

public class Content {

    private  HashMap<String, BitmapFont> fonts;
    private  HashMap<String, Texture> textures;
    private  HashMap<String, JsonValue> atlases;
    private  HashMap<String, Music> music;
    private  HashMap<String, Sound> sounds;

    public Content() {
        fonts = new HashMap<String, BitmapFont>();
        textures = new HashMap<String, Texture>();
        atlases = new HashMap<String, JsonValue>();
        music = new HashMap<String, Music>();
        sounds = new HashMap<String, Sound>();
    }

    public void loadFont(String path, String key) {
        BitmapFont font = new BitmapFont(Gdx.files.internal(path));
        fonts.put(key, font);
    }

    public void loadTexture(String path, String key) {
        Texture tex = new Texture(Gdx.files.internal(path));
        textures.put(key, tex);
    }

    public void loadAtlas(String path, String key) {
        FileHandle handle = Gdx.files.internal(path);
        JsonValue jsonobj = new JsonReader().parse(handle.readString());
        atlases.put(key, jsonobj);
    }


    // getters
    public Texture getTexture(String key) {
        return textures.get(key);
    }

    public BitmapFont getFont(String key) {
        return fonts.get(key);
    }

    public JsonValue getAtlas(String key) {
        return atlases.get(key);
    }


    // dispose assets

    public void disposeTexture(String key) {
        Texture tex = textures.get(key);
        if(tex != null) tex.dispose();
    }

}
