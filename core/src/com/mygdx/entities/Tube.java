package com.mygdx.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.handlers.B2DVars;
import com.mygdx.rloop.BreakAPod;

public class Tube {

    private float x;
    private float y;
    private float w;
    private float tubeLength = BreakAPod.LEVEL_START_LENGTH;
    private float startLength;
    private float endLength;

    private Section[] sections;
    private float step = 50;
    private float sectionLength;

    private World world;
    OrthographicCamera gameCam;

    BodyDef bdef;
    FixtureDef fdef;
    Vector2[] v;

    public Tube(World world, OrthographicCamera gameCam, float x, float y, float w) {
        this.gameCam = gameCam;
        this.world = world;
        this.x = x;
        this.y = y;
        this.w = w;
        this.startLength = 2000;
        this.endLength = 2000;

        initialise();
    }

    public Tube(World world, OrthographicCamera gameCam, float x, float y, float w, float startLength, float endLength) {
        this.gameCam = gameCam;
        this.world = world;
        this.x = x;
        this.y = y;
        this.w = w;
        this.startLength = startLength;
        this.endLength = endLength;

        initialise();
    }

    public void update(float dt) {


    }

    public void render(SpriteBatch sb, Player player) {
        player.render(sb);

        sb.begin();

        sb.end();
    }




    private void initialise() {
        float x = this.x;
        float y = this.y;

        bdef = new BodyDef();
        fdef = new FixtureDef();

        sectionLength = (tubeLength + startLength + endLength) / step;
        System.out.println(sectionLength);
        sectionLength = step;

        v = new Vector2[3];
        v[0] = new Vector2(
                -sectionLength / B2DVars.PPM, -25 / B2DVars.PPM);
        v[1] = new Vector2(
                -sectionLength / B2DVars.PPM, 25 / B2DVars.PPM);
        v[2] = new Vector2(
                sectionLength / B2DVars.PPM, 25 / B2DVars.PPM);

        for(int point = 0; point < 10; point++) {
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(
                    (x + (2f * point * sectionLength)) / B2DVars.PPM,
                    y / B2DVars.PPM
            );
            ChainShape cs = new ChainShape();
            cs.createChain(v);
            fdef.friction = 0;
            fdef.shape = cs;
            fdef.filter.categoryBits = B2DVars.BIT_TUBE;
            fdef.filter.maskBits = B2DVars.BIT_PLAYER;
            fdef.isSensor = false;
            world.createBody(bdef).createFixture(fdef);
            cs.dispose();
        }
    }
}

class Section {

    public Section(float x, float y, float w) {


    }

    public void update(float dt) {


    }

    public void render(SpriteBatch sb, Player player) {
        sb.begin();

        sb.end();
    }
}

