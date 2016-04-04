package com.mygdx.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
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
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.entities.HUD;
import com.mygdx.entities.Player;
import com.mygdx.entities.PowerPickup;
import com.mygdx.entities.Tube;
import com.mygdx.handlers.Background;
import com.mygdx.handlers.GameContactListener;
import com.mygdx.handlers.GameInput;
import com.mygdx.handlers.B2DVars;
import com.mygdx.rloop.BreakAPod;


public class Play extends GameScreen {

    private boolean debug = true;

    private World world;
    private GameContactListener cl;
    private Box2DDebugRenderer b2dr;
    private OrthographicCamera b2dCam;

    private Tube tube;
    private Player player;
    private Vector2 startingPosition = new Vector2(160 / B2DVars.PPM, 200 / B2DVars.PPM);

    private TiledMap tiledMap;
    private float tileSize;
    private OrthogonalTiledMapRenderer tmr;

    private Array<PowerPickup> powerPickups;

    private Background[] backgrounds;
    private HUD hud;

    // font
    private BitmapFont font = new BitmapFont();

    public static int level;

    public Play(ScreenManager sm){

        super(sm);

        //box2d stuff
        world = new World(new Vector2(0, -9.8f), true);
        cl = new GameContactListener();
        world.setContactListener(cl);
        b2dr = new Box2DDebugRenderer();

        // create tube
        createTube(startingPosition);

        // create player
        createPlayer(startingPosition);

        // create player
        createBackground();

        // create power pickups
        createPowerPickups();

        // set hud
        hud = new HUD(player);

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

        if(GameInput.isPressed(GameInput.EXIT)) {
            System.out.println("exit");
            Gdx.app.exit();
        }
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

        // update background
        updateBackground();

        // update player
        player.update(dt);

        for (int i = 0; i < powerPickups.size; i++) {
            powerPickups.get(i).update(dt);
        }
    }

    public void render(){

        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // camera follow player
        cam.position.set(player.getPosition().x * B2DVars.PPM + BreakAPod.WIDTH / 4, BreakAPod.WIDTH / 2 - 70, 0);
        cam.update();

        // draw bgs
        sb.setProjectionMatrix(hudCam.combined);
        for(int i = 0; i < backgrounds.length; i++) {
            if(backgrounds[i] != null) {
                backgrounds[i].render(sb);
            }
        }

        // draw tilemap
        tmr.setView(cam);
        tmr.render();

        // draw player
        sb.setProjectionMatrix(cam.combined);
        tube.render(sb);
        player.render(sb);

        // draw pickups
        for (int i = 0; i < powerPickups.size; i++) {
            powerPickups.get(i).render(sb);
        }

        // draw hud
        sb.setProjectionMatrix(hudCam.combined);
        //hud.render(sb);

        if(debug) {
            b2dr.render(world, b2dCam.combined);
        }
    }

    public void dispose(){

    }

    private void createPlayer(Vector2 startingPos) {

        //// PLAYER
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.position.set(startingPos.x, startingPos.y);
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

    private void createBackground() {

        // set the environment id to the select environment
        String environmentId = "night_grass";
        Texture bgs = BreakAPod.res.getTexture(environmentId);
        JsonValue bgsatlas = BreakAPod.res.getAtlas(environmentId);
        JsonValue frames = bgsatlas.get("frames");

        // load tile map
        tiledMap = new TmxMapLoader().load("environments/night_grass/night_grass_map.tmx");

        tmr = new OrthogonalTiledMapRenderer(tiledMap);
        //tileSize = (int) tiledMap.getProperties().get("tilewidth", Integer.class);

        //TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("ground");
        //createLayer(layer, B2DVars.BIT_TUBE);

        TiledMapTileSet tileset = tiledMap.getTileSets().getTileSet(0);
        MapLayers layers = tiledMap.getLayers();

        int backgroundCount = layers.getCount();
        backgrounds = new Background[backgroundCount];

        for (int i = 0; i < backgroundCount; i++) {

            // properties
            MapLayer cLayer = layers.get(i);
            boolean fixedToCamera = (cLayer.getProperties().get("fixedToCamera") != null);
            boolean repeatx = (cLayer.getProperties().get("repeatx") != null);
            boolean unique = (cLayer.getProperties().get("unique") != null);

            // iterate over objects i layer
            MapObjects objects = cLayer.getObjects();
            int objectTotal = objects.getCount();

            for (int j = 0; j < objectTotal; j++) {

                // get object properties like gid, position etc
                MapProperties properties = objects.get(j).getProperties();
                int gid = (Integer)properties.get("gid");
                Float x = (Float)properties.get("x");
                Float y = (Float)properties.get("y");

                float parallax = 0.0f;
                if (properties.get("parallax") != null)
                    parallax = Float.parseFloat(properties.get("parallax").toString());

                // get the texture name of the object so we can use it to render the object
                String textureName = ((FileTextureData)tileset.getTile(gid).getTextureRegion().getTexture().getTextureData()).getFileHandle().name();
                JsonValue frame = frames.get(textureName).get("frame");

                System.out.println(i + ": " + j + "- GID: " + gid + ", textureName: " + textureName);
                System.out.println(i + ": Map Propperties - x:" + x + ", y:" + y  + ", parallax:" + parallax);
                System.out.println(i + ": Atlas properties - x:" + frame.getInt("x") + ", y:" + frame.getInt("y") + ", w:" + frame.getInt("w") + ", h:" + frame.getInt("h"));

                TextureRegion backgroundRegion = new TextureRegion(bgs,
                        frame.getInt("x"), // x
                        frame.getInt("y"), // y
                        frame.getInt("w"), // W
                        frame.getInt("h")); // h

                // create a background type correct type
                Background background = new Background(backgroundRegion, cam, parallax, repeatx, false);
                background.setPosition(x, y);
                backgrounds[i] = background;

            }
        }

    }

//    //// PLAYER
//    BodyDef bdef = new BodyDef();
//    FixtureDef fdef = new FixtureDef();
//    PolygonShape shape = new PolygonShape();
//
//    bdef.position.set(startingPos.x, startingPos.y);
//    bdef.type = BodyType.DynamicBody;
//    bdef.linearVelocity.set(BreakAPod.PLAYER_SPEED, 0);
//    Body body = world.createBody(bdef);
//
//    // create player
//    player = new Player(body);
//    body.setUserData(player);
//
//    shape.setAsBox(player.getWidth() / 2 / B2DVars.PPM, player.getHeight() / 2 / B2DVars.PPM);
//    fdef.shape = shape;
//    fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
//    fdef.filter.maskBits = B2DVars.BIT_TUBE | B2DVars.BIT_PICKUP;
//    body.createFixture(fdef).setUserData("player");
//
//    // create foot sensor
//    shape.setAsBox(player.getWidth() / 2 / B2DVars.PPM, 4 / B2DVars.PPM, new Vector2(0, -(player.getHeight() + 2) / 2 / B2DVars.PPM), 0);
//    fdef.shape = shape;
//    fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
//    fdef.filter.maskBits = B2DVars.BIT_TUBE;
//    fdef.isSensor = true;
//    body.createFixture(fdef).setUserData("foot");
//
//    // dispose shape
//    shape.dispose();

    private void createTube(Vector2 startingPos) {

        tube = new Tube(startingPos);
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        int resolution = 50; // pixels per section
        int totalSections = 20;
        int tubeWidth = 300;
        int startLength = 200;
        float freq = 10f;
        float prevx;
        float prevy;
        float x;
        float y;

        prevx = startingPos.x - startLength;
        prevy = (startingPos.y + tubeWidth) - (tubeWidth/2);

        for(int i = 1; i < totalSections; i++) {

            x = startingPos.x - startLength + ((i + 0.5f) * resolution) ;
            y = (startingPos.y + tubeWidth + (MathUtils.sinDeg(i*freq) * tubeWidth)) - (tubeWidth/2);

            bdef.type = BodyType.StaticBody;
            bdef.position.set(
                    x / B2DVars.PPM,
                    y / B2DVars.PPM
                    //(i + 0.5f) * resolution / B2DVars.PPM
            );

            ChainShape cs = new ChainShape();
            Vector2[] v = new Vector2[]{
                    new Vector2( -resolution / B2DVars.PPM, -resolution / 2 / B2DVars.PPM),
                    new Vector2( prevx / B2DVars.PPM, prevy / B2DVars.PPM ),
                    new Vector2( x / B2DVars.PPM, y / B2DVars.PPM )
//                    new Vector2( -resolution / 2 / B2DVars.PPM, -resolution / 2 / B2DVars.PPM),
//                    new Vector2( -resolution / 2 / B2DVars.PPM, resolution / 2 / B2DVars.PPM),
//                    new Vector2( resolution / 2 / B2DVars.PPM, resolution / 2 / B2DVars.PPM)
            };
            cs.createChain(v);

            prevx = x;
            prevy = y;

            fdef.friction = 0;
            fdef.shape = cs;
            fdef.filter.categoryBits = B2DVars.BIT_TUBE;
            fdef.filter.maskBits = B2DVars.BIT_PLAYER;
            fdef.isSensor = false;
            world.createBody(bdef).createFixture(fdef);

            cs.dispose();

        }

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

    private void updateBackground() {



    }

}
