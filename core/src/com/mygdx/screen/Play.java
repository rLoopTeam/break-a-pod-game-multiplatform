package com.mygdx.screen;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
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
import com.mygdx.handlers.GameContactListener;
import com.mygdx.handlers.GameInput;
import com.mygdx.rloop.BreakAPod;

public class Play extends GameScreen {

    private BitmapFont font = new BitmapFont();
    private World world;
    private Box2DDebugRenderer b2dr;

    private OrthographicCamera b2dCam;

    private Body playerBody;
    private GameContactListener cl;

    private TiledMap tiledMap;
    private float tileSize;
    private OrthogonalTiledMapRenderer tmr;


    public Play(ScreenManager sm){

        super(sm);

        world = new World(new Vector2(0, -9.8f), true);
        cl = new GameContactListener();
        world.setContactListener(cl);

        b2dr = new Box2DDebugRenderer();

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        //// PLAYER
        bdef.position.set(160 / B2DVars.PPM, 200 / B2DVars.PPM);
        bdef.type = BodyType.DynamicBody;
        playerBody = world.createBody(bdef);
        // - set physics mesh
        shape.setAsBox(10 / B2DVars.PPM, 10 / B2DVars.PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_TUBE;
        // - apply mesh to body
        playerBody.createFixture(fdef).setUserData("player");

        // create foot sensor
        shape.setAsBox(2 / B2DVars.PPM, 2 / B2DVars.PPM, new Vector2(0, -10 / B2DVars.PPM), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_TUBE;
        fdef.isSensor = true;
        playerBody.createFixture(fdef).setUserData("foot");

        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, BreakAPod.WIDTH / B2DVars.PPM, BreakAPod.HEIGHT / B2DVars.PPM);


        ///////////////////////////////////////////////////////////////

        // load tile map
        tiledMap = new TmxMapLoader().load("environments/nightGrass.tmx");
        tmr = new OrthogonalTiledMapRenderer(tiledMap);

        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("ground");
        tileSize = layer.getTileWidth();
        for(int row = 0; row < layer.getTileHeight(); row++) {
            for(int col = 0; col < layer.getWidth(); col++) {
                // get cell
                Cell cell = layer.getCell(col, row);

                // check if exists
                if (cell == null) continue;
                if (cell.getTile() == null) continue;

                bdef.type = BodyType.StaticBody;
                bdef.position.set(
                        (col + 0.5f) * tileSize / B2DVars.PPM,
                        (row + 0.5f) * tileSize / B2DVars.PPM
                );

                ChainShape cs = new ChainShape();
                Vector2[] v = new Vector2[3];
                v[0] = new Vector2(
                        -tileSize / 2 / B2DVars.PPM, -tileSize / 2 / B2DVars.PPM);
                v[1] = new Vector2(
                        -tileSize / 2 / B2DVars.PPM, tileSize / 2 / B2DVars.PPM);
                v[2] = new Vector2(
                        tileSize / 2 / B2DVars.PPM, tileSize / 2 / B2DVars.PPM);
                cs.createChain(v);
                fdef.friction = 0;
                fdef.shape = cs;
                fdef.filter.categoryBits = B2DVars.BIT_TUBE;
                fdef.filter.maskBits = B2DVars.BIT_PLAYER;
                fdef.isSensor = false;
                world.createBody(bdef).createFixture(fdef);

            }
        }
    }

    public void handleInput(){
        if(GameInput.isPressed(GameInput.BUTTON1)) {
            if(cl.isPlayerOnGround()) {
                playerBody.applyForceToCenter(0, 200, true);
            }
        }

        if(GameInput.isPressed(GameInput.BUTTON2)) {
            System.out.println("x");
        }
    }

    public void update(float dt){
        handleInput();
        world.step(dt, 6, 1);
    }

    public void render(){
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tmr.setView(cam);
        tmr.render();
        b2dr.render(world, b2dCam.combined);

        sb.begin();
        font.draw(sb, "play state", 100, 100);
        sb.end();
    }

    public void dispose(){

    }
}
