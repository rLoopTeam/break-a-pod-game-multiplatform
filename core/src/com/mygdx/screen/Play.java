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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.entities.Player;
import com.mygdx.handlers.GameContactListener;
import com.mygdx.handlers.GameInput;
import com.mygdx.handlers.B2DVars;
import com.mygdx.rloop.BreakAPod;

public class Play extends GameScreen {

    private BitmapFont font = new BitmapFont();
    private World world;
    private Box2DDebugRenderer b2dr;

    private OrthographicCamera b2dCam;

    private GameContactListener cl;

    private TiledMap tiledMap;
    private float tileSize;
    private OrthogonalTiledMapRenderer tmr;

    private Player player;

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
    }

    public void update(float dt){
        handleInput();
        world.step(dt, 6, 1);
        player.update(dt);
    }

    public void render(){

        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tmr.setView(cam);
        tmr.render();
        b2dr.render(world, b2dCam.combined);
        player.render(sb);

        //text
        sb.begin();
        font.draw(sb, "play state", 100, 100);
        sb.end();
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
        fdef.filter.maskBits = B2DVars.BIT_TUBE;
        body.createFixture(fdef).setUserData("player");

        // create foot sensor
        shape.setAsBox(player.getWidth() / 2 / B2DVars.PPM, 4 / B2DVars.PPM, new Vector2(0, -(player.getHeight() + 2) / 2 / B2DVars.PPM), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_TUBE;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("foot");


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

            }
        }
    }
}
