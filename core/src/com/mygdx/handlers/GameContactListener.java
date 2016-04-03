package com.mygdx.handlers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;


public class GameContactListener implements ContactListener {

    private int numFootContacts;
    private Array<Body> bodiesToRemove;

    public GameContactListener() {
        super();
        bodiesToRemove = new Array<Body>();
    }

    public void beginContact(Contact c) {

        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        if(fa == null || fb == null) return;

        if(fa.getUserData() != null && fa.getUserData().equals("foot")) {
            numFootContacts++;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("foot")) {
            numFootContacts++;
        }

        if(fa.getUserData() != null && fa.getUserData().equals("powerpickup")) {
            bodiesToRemove.add(fa.getBody());
        }

        if(fb.getUserData() != null && fb.getUserData().equals("powerpickup")) {
            bodiesToRemove.add(fb.getBody());
        }

    }

    public void endContact(Contact c) {

        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        if(fa == null || fb == null) return;

        if(fa.getUserData() != null && fa.getUserData().equals("foot")) {
            numFootContacts--;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("foot")) {
            numFootContacts--;
        }

    }

    public boolean isPlayerOnGround() { return numFootContacts > 0; }
    public Array<Body> getBodiesToRemove() { return bodiesToRemove; }

    public void preSolve(Contact c, Manifold m) {}
    public void postSolve(Contact c, ContactImpulse ci) {}

}
