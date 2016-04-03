package com.mygdx.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.rloop.BreakAPod;


public class HUD {

    private  Player player;
    private TextureRegion[] elements;
    private JsonValue hud_atlas;

    public HUD(Player player) {

        this.player = player;
        Texture tex = BreakAPod.res.getTexture("hud");

        FileHandle handle = Gdx.files.internal("UI/UI_atlas.json");
        String fileContent = handle.readString();
        hud_atlas = new JsonReader().parse(fileContent);

        elements = new TextureRegion[10];
        JsonValue bottomUI = hud_atlas.get("frames").get("topUI.png");
        elements[0] = new TextureRegion(tex, bottomUI.get("frame").getInt("x"), bottomUI.get("frame").getInt("y"), bottomUI.get("frame").getInt("w"), bottomUI.get("frame").getInt("h"));

//        "UI.png": {
//            "trimmed": "false",
//                    "frame": {
//                "y": 600,
//                        "x": 800,
//                        "w": 800,
//                        "h": 84
//            },
//            "rotated": "false",
//                    "sourceSize": {
//                "h": 84,
//                        "w": 800
//            },
//            "spriteSourceSize": {
//                "y": 0,
//                        "x": 0,
//                        "w": 800,
//                        "h": 84
//            }
//        },
    }

    public void render(SpriteBatch sb) {

        sb.begin();
        sb.draw(elements[0], 40, 50);
        sb.end();

    }
}
