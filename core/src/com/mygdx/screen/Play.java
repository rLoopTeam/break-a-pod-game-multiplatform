package com.mygdx.screen;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.entities.HUD;
import com.mygdx.entities.Player;
import com.mygdx.entities.PowerPickup;
import com.mygdx.handlers.GameContactListener;
import com.mygdx.handlers.GameInput;
import com.mygdx.handlers.B2DVars;
import com.mygdx.rloop.BreakAPod;

public class Play extends GameScreen {

    private boolean debug = false;
    private BitmapFont font = new BitmapFont();
    private World world;
    private Box2DDebugRenderer b2dr;

    private OrthographicCamera b2dCam;

    private GameContactListener cl;

    private TiledMap tiledMap;
    private float tileSize;
    private OrthogonalTiledMapRenderer tmr;

    private Player player;
    private Array<PowerPickup> powerPickups;

    private HUD hud;

    public Play(ScreenManager sm){

        super(sm);

        //box2d stuff
        world = new World(new Vector2(0, -9.8f), true);
        cl = new GameContactListener();
        world.setContactListener(cl);
        b2dr = new Box2DDebugRenderer();

        // create player
        createPlayer();

        // create player
        createTiles();

        // create power pickups
        createPowerPickups();

        // set up box2d cam
        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, BreakAPod.WIDTH / B2DVars.PPM, BreakAPod.HEIGHT / B2DVars.PPM);

        // set hud
        hud = new HUD(player);

    }

    public void handleInput(){
        if(GameInput.isPressed(GameInput.BUTTON1)) {
            if(cl.isPlayerOnGround()) {
               player.getBody().applyForceToCenter(0, 200, true);
            }
        }

        if(GameInput.isPressed(GameInput.BUTTON2)) {
            System.out.println("x");
        }

//        if(BBInput.isPressed()) {
//            if(BBInput.x  Gdx.graphics.getWidth() / 2) {
//
//            } else {
//                if(cl.isPlayerOnGround()) {
//                    player.getBody().applyForceToCenter(0, 200, true);
//                }
//            }
//        }
    }

    public void update(float dt){

        handleInput();

        // update box2d
        world.step(BreakAPod.STEP, 6, 1);

        // remove powerups (must be after the world has updated)
        Array<Body> bodies = cl.getBodiesToRemove();

        for (int i = 0; i < bodies.size; i++) {
            Body b = bodies.get(i);
            powerPickups.removeValue((PowerPickup) b.getUserData(), true);
            world.destroyBody(b);
            player.collectPower();
        }
        bodies.clear();

        player.update(dt);

        for (int i = 0; i < powerPickups.size; i++) {
            powerPickups.get(i).update(dt);
        }
    }

    public void render(){

        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // camera follow player
        cam.position.set(
                player.getPosition().x * B2DVars.PPM + BreakAPod.WIDTH / 4,
                BreakAPod.WIDTH / 2 - 70,
                0
        );
        cam.update();

        // draw tilemap
        tmr.setView(cam);
        tmr.render();

        // draw player
        sb.setProjectionMatrix(cam.combined);
        player.render(sb);

        for (int i = 0; i < powerPickups.size; i++) {
            powerPickups.get(i).render(sb);
        }

        // draw hud
        sb.setProjectionMatrix(hudCam.combined);
        hud.render(sb);

        if(debug) {
            b2dr.render(world, b2dCam.combined);
        }
    }

    public void dispose(){

    }

    private void createPlayer() {

        //// PLAYER
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.position.set(160 / B2DVars.PPM, 200 / B2DVars.PPM);
        bdef.type = BodyType.DynamicBody;
        bdef.linearVelocity.set(BreakAPod.PLAYER_SPEED, 0);
        Body body = world.createBody(bdef);

        // create player
        player = new Player(body);
        body.setUserData(player);

        shape.setAsBox(player.getWidth() / 2 / B2DVars.PPM, player.getHeight() / 2 / B2DVars.PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_TUBE | B2DVars.BIT_PICKUP;
        body.createFixture(fdef).setUserData("player");

        // create foot sensor
        shape.setAsBox(player.getWidth() / 2 / B2DVars.PPM, 4 / B2DVars.PPM, new Vector2(0, -(player.getHeight() + 2) / 2 / B2DVars.PPM), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_TUBE;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("foot");

        // dispose shape
        shape.dispose();
    }

    private void createTiles() {
        // load tile map
        tiledMap = new TmxMapLoader().load("environments/nightGrass.tmx");
        tmr = new OrthogonalTiledMapRenderer(tiledMap);
        tileSize = (int) tiledMap.getProperties().get("tilewidth", Integer.class);

        TiledMapTileLayer layer;

        layer = (TiledMapTileLayer) tiledMap.getLayers().get("ground");
        createLayer(layer, B2DVars.BIT_TUBE);
    }

    private void createLayer(TiledMapTileLayer layer, short bits) {

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        // generate tiles from the cells in the layer
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
                fdef.filter.categoryBits = bits;
                fdef.filter.maskBits = B2DVars.BIT_PLAYER;
                fdef.isSensor = false;
                world.createBody(bdef).createFixture(fdef);

                cs.dispose();

            }
        }
    }

    private void createPowerPickups() {

        powerPickups = new Array<PowerPickup>();

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        for (int i = 0; i < 10; i++) {

            bdef.type = BodyType.StaticBody;
            bdef.position.set(((i * 80) + 120) / B2DVars.PPM, 140 / B2DVars.PPM);

            CircleShape cshape = new CircleShape();
            cshape.setRadius(35/B2DVars.PPM);

            fdef.shape = cshape;
            fdef.isSensor = true;
            fdef.filter.categoryBits = B2DVars.BIT_PICKUP;
            fdef.filter.maskBits = B2DVars.BIT_PLAYER;

            Body body = world.createBody(bdef);
            body.createFixture(fdef).setUserData("powerpickup");

            PowerPickup p = new PowerPickup(body);
            powerPickups.add(p);

            body.setUserData(p);

            // dispose shape
            cshape.dispose();
        }
    }
}
