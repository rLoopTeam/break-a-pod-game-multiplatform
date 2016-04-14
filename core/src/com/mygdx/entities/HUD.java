package com.mygdx.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.handlers.B2DVars;
import com.mygdx.rloop.BreakAPod;


public class HUD {

    private  Player player;
    private TextureRegion[] elements;
    private BitmapFont font;

    public HUD(Player player, BitmapFont font) {

        this.player = player;
        this.font = font;
        Texture hudtextureatlas = BreakAPod.res.getTexture("hud");

        FileHandle handle = Gdx.files.internal("UI/UI_atlas.json");
        String fileContent = handle.readString();

        elements = new TextureRegion[10];
        elements[0] = getElementRegion(hudtextureatlas, fileContent, "topUI.png");
        elements[1] = getElementRegion(hudtextureatlas, fileContent, "UI.png");

    }

    public void render(SpriteBatch sb) {

        sb.begin();
        sb.draw(elements[0], 0, BreakAPod.HEIGHT - elements[0].getRegionHeight()); //top UI
        sb.draw(elements[1], 0, elements[1].getRegionHeight() - 80); //bottom UI
        font.draw(sb, Float.toString(player.getPosition().x * B2DVars.PPM), 0f, (float)BreakAPod.HEIGHT - 100);
        sb.end();

    }

    // get the texture region of a given element name
    private TextureRegion getElementRegion(Texture tex, String jsonstring, String name) {

        JsonValue jsonobj = new JsonReader().parse(jsonstring);
        JsonValue element = jsonobj.get("frames").get(name);
        if(element == null) System.out.println(name+" NOT FOUND!");
        JsonValue frame = element.get("frame");

        return new TextureRegion(tex, frame.getInt("x"), frame.getInt("y"), frame.getInt("w"), frame.getInt("h"));

    }
}
