package com.mygdx.screen;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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
import com.mygdx.entities.Tube;
import com.mygdx.handlers.Background;
import com.mygdx.handlers.GameContactListener;
import com.mygdx.handlers.GameInput;
import com.mygdx.handlers.B2DVars;
import com.mygdx.rloop.BreakAPod;

import java.util.Iterator;

public class Play extends GameScreen {

    private boolean debug = true;
    private BitmapFont font = new BitmapFont();
    private World world;
    private Box2DDebugRenderer b2dr;

    private OrthographicCamera b2dCam;

    private GameContactListener cl;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tmr;

    private Player player;
    private Array<PowerPickup> powerPickups;

    private Background[] backgrounds;
    private Tube tube;
    private Vector2 playerStart;
    private float tubeWidth;
    private HUD hud;

    public Play(ScreenManager sm){

        super(sm);

        // initial settings
        playerStart = new Vector2(0f, 420f); // needs to be 150y or the pod isn't visible

        // font
        font = BreakAPod.res.getFont("default_font");

        //box2d stuff
        world = new World(new Vector2(0, -9.8f), true);
        cl = new GameContactListener();
        world.setContactListener(cl);
        b2dr = new Box2DDebugRenderer();

        // load tile map
        tiledMap = new TmxMapLoader().load("environments/nightGrass.tmx");

        // create backgroudn
        createBackground();

        // create player
        createPlayer();

        // create tube
        tubeWidth = 200f;
        createTube(); // probly need to combine this with the background creation so it is in the correct order

        // create power pickups
        createPowerPickups();

        // set hud
        hud = new HUD(player, font);

        // set up box2d cam
        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, BreakAPod.WIDTH / B2DVars.PPM, BreakAPod.HEIGHT / B2DVars.PPM);

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

        if(GameInput.isPressed(GameInput.ESC)) {
            Gdx.app.exit();
        }

    }

    public void update(float dt){

        handleInput();

        // update box2d
        world.step(BreakAPod.STEP, 6, 1);

        // update map

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

        // pickups
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

//        tmr.setView(cam);
//        tmr.render();

        // draw bgs
        sb.setProjectionMatrix(hudCam.combined);
        for (int i = 0; i < backgrounds.length; i++) {
            if(backgrounds[i] != null) {
                backgrounds[i].render(sb);
            }
        }

        // draw player
        sb.setProjectionMatrix(cam.combined);
        tube.render(sb, player); // pass the player in the tube render function so we can sort the rendering order
        //player.render(sb);

        for (int i = 0; i < powerPickups.size; i++) {
            powerPickups.get(i).render(sb);
        }

        // draw hud
        sb.setProjectionMatrix(hudCam.combined);
        hud.render(sb);


        if(debug) {
            b2dCam.position.set(
                    cam.position.x / B2DVars.PPM,
                    cam.position.y / B2DVars.PPM,
                    0);
            b2dCam.update();
            b2dr.render(world, b2dCam.combined);
        }
    }

    public void dispose(){

    }

    private void createBackground() {

        MapLayers tiledLayers = tiledMap.getLayers();
        backgrounds = new Background[tiledLayers.getCount()];

        for (int i = 0; i < tiledLayers.getCount(); i++) {
            MapLayer cLayer = tiledLayers.get(i);
            Iterator<MapObject> objs = cLayer.getObjects().iterator();
            if (objs.hasNext()) {
                MapObject obj = objs.next();
                MapProperties properties = obj.getProperties();
                Float x = Float.parseFloat(properties.get("X", "0", String.class));
                Float y = BreakAPod.HEIGHT - Float.parseFloat(properties.get("Y", "0", String.class));
                Float parallax = Float.parseFloat(properties.get("parallax", "0", String.class));
                Boolean repeatX = Boolean.parseBoolean(properties.get("repeatX", "false", String.class));

                if (repeatX) {
                    backgrounds[i] = new Background(((TextureMapObject)obj).getTextureRegion(), cam, x, y, parallax, repeatX, false);
                } else {
                    backgrounds[i] = new Background(((TextureMapObject)obj).getTextureRegion(), cam, x, y, parallax);
                }
            }
        }
    }

    private void createPlayer() {
        //// PLAYER
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.position.set(playerStart.x / B2DVars.PPM, playerStart.y / B2DVars.PPM);
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

    private void createTube() {
        tube = new Tube(world, cam, 0, 200, tubeWidth / B2DVars.PPM);
    }

    /*private void createTiles() {
        tmr = new OrthogonalTiledMapRenderer(tiledMap);
        tileSize = 48;
        createLayer(B2DVars.BIT_TUBE);
    }

    private void createLayer( short bits) {


        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        for(int col = 0; col < 10; col++) {

            bdef.type = BodyType.StaticBody;
            bdef.position.set(
                    (col + 0.5f) * 50 / B2DVars.PPM,
                    100 / B2DVars.PPM
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

        }*/

        // generate tiles from the cells in the layer
//        for(int row = 0; row < layer.getTileHeight(); row++) {
//            for(int col = 0; col < layer.getWidth(); col++) {
//                // get cell
//                Cell cell = layer.getCell(col, row);
//
//                // check if exists
//                if (cell == null) continue;
//                if (cell.getTile() == null) continue;
//
//                bdef.type = BodyType.StaticBody;
//                bdef.position.set(
//                        (col + 0.5f) * tileSize / B2DVars.PPM,
//                        (row + 0.5f) * tileSize / B2DVars.PPM
//                );
//
//                ChainShape cs = new ChainShape();
//                Vector2[] v = new Vector2[3];
//                v[0] = new Vector2(
//                        -tileSize / 2 / B2DVars.PPM, -tileSize / 2 / B2DVars.PPM);
//                v[1] = new Vector2(
//                        -tileSize / 2 / B2DVars.PPM, tileSize / 2 / B2DVars.PPM);
//                v[2] = new Vector2(
//                        tileSize / 2 / B2DVars.PPM, tileSize / 2 / B2DVars.PPM);
//                cs.createChain(v);
//                fdef.friction = 0;
//                fdef.shape = cs;
//                fdef.filter.categoryBits = bits;
//                fdef.filter.maskBits = B2DVars.BIT_PLAYER;
//                fdef.isSensor = false;
//                world.createBody(bdef).createFixture(fdef);
//
//                cs.dispose();
//
//            }
//        }
//    }

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
