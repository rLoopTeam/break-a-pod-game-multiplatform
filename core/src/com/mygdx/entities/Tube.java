package com.mygdx.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.handlers.B2DVars;

public class Tube {

    private float x;
    private float y;
    private float w;
    private float startLength;
    private float endLength;

    private Section[] sections;
    private float res;
    private float sectionLength;

    private World world;

    BodyDef bdef;
    FixtureDef fdef;

    public Tube(World world, float x, float y, float w) {

        this.world = world;
        this.x = x;
        this.y = y;
        this.w = w;
        this.startLength = 2000;
        this.endLength = 2000;

        initialise();
    }

    public Tube(World world, float x, float y, float w, float startLength, float endLength) {

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

        bdef = new BodyDef();
        fdef = new FixtureDef();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(
                x / B2DVars.PPM,
                y / B2DVars.PPM
        );
        sectionLength = startLength / 3f;

        ChainShape cs = new ChainShape();
        Vector2[] v = new Vector2[3];
        v[0] = new Vector2(
                -sectionLength / 2 / B2DVars.PPM, -sectionLength / 2 / B2DVars.PPM);
        v[1] = new Vector2(
                -sectionLength / 2 / B2DVars.PPM, sectionLength / 2 / B2DVars.PPM);
        v[2] = new Vector2(
                sectionLength / 2 / B2DVars.PPM, sectionLength / 2 / B2DVars.PPM);
        cs.createChain(v);
        fdef.friction = 0;
        fdef.shape = cs;
        fdef.filter.categoryBits = B2DVars.BIT_TUBE;
        fdef.filter.maskBits = B2DVars.BIT_PLAYER;
        fdef.isSensor = false;
        world.createBody(bdef).createFixture(fdef);

        cs.dispose();


//        for(int point = 0; point < totalPoints; point++) {
//
//            bdef.type = BodyDef.BodyType.StaticBody;
//            bdef.position.set(
//                    (point + 0.5f) * sectionLength / B2DVars.PPM,
//                    w / B2DVars.PPM
//            );
//
//            ChainShape cs = new ChainShape();
//            Vector2[] v = new Vector2[3];
//            v[0] = new Vector2(
//                    -sectionLength / 2 / B2DVars.PPM, -sectionLength / 2 / B2DVars.PPM);
//            v[1] = new Vector2(
//                    -sectionLength / 2 / B2DVars.PPM, sectionLength / 2 / B2DVars.PPM);
//            v[2] = new Vector2(
//                    sectionLength / 2 / B2DVars.PPM, sectionLength / 2 / B2DVars.PPM);
//            cs.createChain(v);
//            fdef.friction = 0;
//            fdef.shape = cs;
//            fdef.filter.categoryBits = B2DVars.BIT_TUBE;
//            fdef.filter.maskBits = B2DVars.BIT_PLAYER;
//            fdef.isSensor = false;
//            world.createBody(bdef).createFixture(fdef);
//
//            cs.dispose();
//        }
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

