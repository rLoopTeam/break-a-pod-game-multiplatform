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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Tube {

    private float x;
    private float y;
    private float w;
    private float tubeLength = BreakAPod.LEVEL_START_LENGTH;
    private float startLength;
    private float endLength;

    private float prevx;
    private float prevy;

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

        for (Section section : sections) {
            section.render(sb, player);
        }
        sb.begin();
        sb.end();
    }




    private void initialise() {
        float x = this.x;
        float y = this.y;

//        bdef = new BodyDef();
//        fdef = new FixtureDef();
//
//        sectionLength = (tubeLength + startLength + endLength) / step;
        sectionLength = step;
//
//        v = new Vector2[3];
//        v[0] = new Vector2(
//                -sectionLength / B2DVars.PPM, -25 / B2DVars.PPM);
//        v[1] = new Vector2(
//                -sectionLength / B2DVars.PPM, 25 / B2DVars.PPM);
//        v[2] = new Vector2(
//                sectionLength / B2DVars.PPM, 25 / B2DVars.PPM);

        sections = new Section[10];
        for(int point = 0; point < 10; point++) {
//            bdef.type = BodyDef.BodyType.StaticBody;
//            bdef.position.set(
//                    (x + (2f * point * sectionLength)) / B2DVars.PPM,
//                    y / B2DVars.PPM
//            );

            sections[point] = new Section(this.world, (x + (2f * point * sectionLength)), y, this.w, sectionLength);

            this.prevx = (x + (2f * point * sectionLength));
            this.prevy = y;

//            ChainShape cs = new ChainShape();
//            cs.createChain(v);
//            fdef.friction = 0;
//            fdef.shape = cs;
//            fdef.filter.categoryBits = B2DVars.BIT_TUBE;
//            fdef.filter.maskBits = B2DVars.BIT_PLAYER;
//            fdef.isSensor = false;
//            world.createBody(bdef).createFixture(fdef);
//            cs.dispose();
        }

    }
}

class Section {

    private float x;
    private float y;
    private float w;

    private float step = 50;
    private float sectionLength;

    World world;
    BodyDef bdef;
    FixtureDef fdef;
    Vector2[] v;


    public Section(World world, float x, float y, float w, float sectionLength) {

        this.x = x;
        this.y = y;
        this.sectionLength = sectionLength;
        this.world = world;
        this.bdef = new BodyDef();
        this.fdef = new FixtureDef();

    }

    public void update(float dt) {


    }

    public void render(SpriteBatch sb, Player player) {

        sb.begin();
        v = new Vector2[3];
        v[0] = new Vector2(
                -this.sectionLength / B2DVars.PPM, -25 / B2DVars.PPM);
        v[1] = new Vector2(
                -this.sectionLength / B2DVars.PPM, 25 / B2DVars.PPM);
        v[2] = new Vector2(
                this.sectionLength / B2DVars.PPM, 25 / B2DVars.PPM);

        this.bdef.type = BodyDef.BodyType.StaticBody;
        this.bdef.position.set(
                this.x / B2DVars.PPM,
                this.y / B2DVars.PPM
        );

        ChainShape cs = new ChainShape();
        cs.createChain(v);
        this.fdef.friction = 0;
        this.fdef.shape = cs;
        this.fdef.filter.categoryBits = B2DVars.BIT_TUBE;
        this.fdef.filter.maskBits = B2DVars.BIT_PLAYER;
        this. fdef.isSensor = false;
        this.world.createBody(this.bdef).createFixture(this.fdef);
        cs.dispose();
        sb.end();

    }
}

