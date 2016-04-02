package com.mygdx.handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class GameContactListener implements ContactListener {

    private boolean playerOnGround;
    public void beginContact(Contact c) {

        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        if(fa.getUserData() != null && fa.getUserData().equals("foot")) {
            playerOnGround = true;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("foot")) {
            playerOnGround = true;
        }

    }

    public void endContact(Contact c) {

        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        if(fa.getUserData() != null && fa.getUserData().equals("foot")) {
            playerOnGround = false;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("foot")) {
            playerOnGround = false;
        }

    }

    public boolean isPlayerOnGround() { return playerOnGround; }

    public void preSolve(Contact c, Manifold m) {}
    public void postSolve(Contact c, ContactImpulse ci) {}

}
