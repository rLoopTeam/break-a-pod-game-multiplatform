package com.mygdx.screen;

import com.mygdx.handlers.B2DVars;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.rloop.BreakAPod;

public class Play extends GameScreen {

    private BitmapFont font = new BitmapFont();
    private World world;
    private Box2DDebugRenderer b2dr;

    private OrthographicCamera b2dCam;

    public Play(ScreenManager sm){

        super(sm);

        world = new World(new Vector2(0, -9.8f), true);
        b2dr = new Box2DDebugRenderer();

        //// platform
        BodyDef bdef = new BodyDef();
        bdef.position.set(160 / B2DVars.PPM, 120 / B2DVars.PPM);
        bdef.type = BodyType.StaticBody;
        Body body = world.createBody(bdef);
        // - set physics mesh
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(100 / B2DVars.PPM, 10 / B2DVars.PPM);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        // - apply mesh to body
        body.createFixture(fdef);

        //// box
        bdef.position.set(160 / B2DVars.PPM, 200 / B2DVars.PPM);
        bdef.type = BodyType.DynamicBody;
        body = world.createBody(bdef);
        // - set physics mesh
        shape.setAsBox(10 / B2DVars.PPM, 10 / B2DVars.PPM);
        fdef.shape = shape;
        // - apply mesh to body
        body.createFixture(fdef);

        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, BreakAPod.WIDTH / B2DVars.PPM, BreakAPod.HEIGHT / B2DVars.PPM);
    }

    public void handleInput(){
    }

    public void update(float dt){
        world.step(dt, 6, 1);
    }

    public void render(){
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        b2dr.render(world, b2dCam.combined);
        sb.begin();
        font.draw(sb, "play state", 100, 100);
        sb.end();
    }

    public void dispose(){

    }
}
