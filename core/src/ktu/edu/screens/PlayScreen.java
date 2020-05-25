package ktu.edu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.concurrent.LinkedBlockingQueue;

import ktu.edu.managers.Assets;
import ktu.edu.managers.WaveManager;
import ktu.edu.scenes.Abilities;
import ktu.edu.scenes.Controller;
import ktu.edu.scenes.Description;
import ktu.edu.scenes.Hud;
import ktu.edu.scenes.Pause;
import ktu.edu.sprites.Blaster;
import ktu.edu.sprites.Helper.Artist;
import ktu.edu.sprites.Helper.Astrologer;
import ktu.edu.sprites.Helper.Citizen;
import ktu.edu.sprites.Helper.Helper;
import ktu.edu.sprites.Helper.HelperDef;
import ktu.edu.sprites.Items.BronzeCoin;
import ktu.edu.sprites.Items.GoldCoin;
import ktu.edu.sprites.Items.Item;
import ktu.edu.sprites.Items.ItemDef;
import ktu.edu.sprites.Items.SilverCoin;
import ktu.edu.sprites.enemies.Enemy;
import ktu.edu.tools.B2WorldCreator;
import ktu.edu.tools.CameraStyles;
import ktu.edu.tools.WorldContactListener;
import ktu.edu.UndergroundBlasterGame;

public class PlayScreen extends AbstractScreen {

    private final OrthographicCamera gameCam;
    private final Viewport gamePort;

    public Controller controller;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    public Blaster blaster;

    public boolean stop;

    private Array<Helper> helpers;
    private LinkedBlockingQueue<HelperDef> helpersToSpawn;

    private Array<Item> coins;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;

    public B2WorldCreator creator;

    private Array<Enemy> enemies;

    public final Assets assets;

    private int waveCount;

    public boolean followBlaster;

    public Pause pauseStage;

    //trying
    private Hud hud;

    public float stunTime;

    public Abilities abilities;

    public Description description;

    private WaveManager waveManager;

    public PlayScreen(UndergroundBlasterGame game){
        super(game);
        this.assets = game.assMan;
        //camera
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(456 / UndergroundBlasterGame.PPM,256.5f  / UndergroundBlasterGame.PPM, gameCam);

        System.out.println("Play screen Konstruktorius");
    }

    private void spawnHelper(HelperDef helperDef){
        helpersToSpawn.add(helperDef);
    }

    public void spawnItem(ItemDef itemDef){
        itemsToSpawn.add(itemDef);
    }

    private void handleSpawningHelpers(){
        if(!helpersToSpawn.isEmpty()){
            HelperDef hdef = helpersToSpawn.poll();
            if(hdef.type == Astrologer.class)
                helpers.add(new Astrologer(this, hdef.position.x, hdef.position.y, hdef.canWalk));
            else if(hdef.type == Artist.class)
                helpers.add(new Artist(this, hdef.position.x, hdef.position.y, hdef.canWalk));
            else
                helpers.add(new Citizen(this, hdef.position.x, hdef.position.y, hdef.canWalk));
        }
    }

    private void handleSpawningItems(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef idef = itemsToSpawn.poll();
            if(idef.type == GoldCoin.class)
                coins.add(new GoldCoin(this, idef.position.x, idef.position.y));
            else if(idef.type == SilverCoin.class)
                coins.add(new SilverCoin(this, idef.position.x, idef.position.y));
            else
                coins.add(new BronzeCoin(this, idef.position.x, idef.position.y));
        }
    }

    @Override
    public void show() {
        System.out.println("Play screen Show");

        stage.clear();

        Gdx.input.setInputProcessor(stage);

        gameCam.position.set(gamePort.getWorldWidth() / 2 - 10, gamePort.getWorldHeight() / 2, 0);
        //gameCam.position.set(0 + gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2,0);

        //B2d world
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new WorldContactListener());
        b2dr = new Box2DDebugRenderer();

        //create map
        map = assets.manager.get("level1dungeon.tmx", TiledMap.class);
        renderer = new OrthogonalTiledMapRenderer(map, 1 / UndergroundBlasterGame.PPM);
        creator = new B2WorldCreator(world, map);

        //main player
        blaster = new Blaster(this, creator.getBlasterSpot().x, creator.getBlasterSpot().y);

        enemies = new Array<>();

        //hud
        hud = new Hud(this);

        //controller
        controller = new Controller(this);

        //Game description
        description = new Description(this);

        //Pause state
        pauseStage = new Pause(this);


        //helpers
        helpers = new Array<>();
        helpersToSpawn = new LinkedBlockingQueue<>();

        //items
        coins = new Array<>();
        itemsToSpawn = new LinkedBlockingQueue<>();

        stop = false;

        followBlaster = false;

        stunTime = game.stunTime;

        if (game.artist)
            spawnHelper(new HelperDef(creator.getHelperSpots().get(0), Artist.class, false));
        if (game.astrologer)
            spawnHelper(new HelperDef(creator.getHelperSpots().get(1), Astrologer.class, false));

        spawnHelper(new HelperDef(creator.getHelperSpots().get(2), Citizen.class, false));

        abilities = new Abilities(this);

        waveManager = new WaveManager(this);

        waveCount = 9;
    }

    private void handleInput(float dt){
        if (controller.isTouched() && (blaster.currentState != Blaster.State.DEAD)) {
            blaster.setMovingVector(controller.difference());
        } else {
            blaster.resetMovingVector();
        }
        if (controller.pressButton) {
            blaster.boom();
            controller.pressButton = false;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.B))
            blaster.boom();

        if(Gdx.input.isKeyJustPressed(Input.Keys.A))
            Hud.coins = 5000;

    }

    public void update(float dt) {

        world.step(1 / 60f, 6, 2);

        handleInput(dt);
        handleSpawningHelpers();
        handleSpawningItems();

        for(Helper helper: helpers){
            helper.update(dt, blaster.b2body.getPosition().x, blaster.b2body.getPosition().y);
            if (helper.destroyed) {
                helpers.removeValue(helper, true);
            }
        }

        for (Enemy enemy : enemies) {
            enemy.update(dt, blaster.b2body.getPosition());
            if (enemy.destroyed) {
                hud.addInfo(enemy.score);
                enemies.removeValue(enemy, true);
            }
        }

        for (Item item : coins) {
            item.update(dt, blaster.b2body.getPosition());
            if(item.destroyed)
                coins.removeValue(item, true);
        }

        blaster.update(dt);

        if(followBlaster) {
            hud.update(dt);
            if (enemies.size == 0 || hud.worldTimer <= 0) {
                waveGenerator();
            }
        }
        updateCamera();
        //tell our renderer to draw only what your camera can see in our game world.
        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if(!controller.pressPause && !stop)
            update(delta);

        renderer.render();

//        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();

        for (Item item : coins)
            item.draw(game.batch);

        for (Helper helper : helpers)
            helper.draw(game.batch);

        for(Enemy enemy : enemies)
            enemy.draw(game.batch);

        blaster.draw(game.batch);

        game.batch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);
        stageResize(width,height);
        System.out.println("Play screen resize");

    }

    @Override
    public void pause() {

        pauseStage.addPauseActors();
        controller.pressPause = true;

        System.out.println("Play screen pause");
    }

    @Override
    public void resume() {
        System.out.println("Play screen resume");
    }

    @Override
    public void hide() {
        System.out.println("Play screen hide");
    }

    @Override
    public void dispose() {
        System.out.println("Dispose PlayScreen");
        if(map != null) {
            map.dispose();
            world.dispose();
            b2dr.dispose();
            pauseStage.dispose();
            hud.dispose();
            stage.dispose();

        }
        if(renderer != null) {
            renderer.dispose();
        }
    }

    private void updateCamera(){
        if(followBlaster)
            CameraStyles.customStyle(gameCam, blaster.b2body.getPosition(),gamePort);
        else
            CameraStyles.lerpToTarget(gameCam, creator.getmiddleOfHall());
    }

    public World getWorld(){
        return world;
    }

    private void waveGenerator(){
        waveCount++;
        hud.addWave(waveCount);
        hud.addScore((int)(hud.worldTimer * 5));
        hud.worldTimer = 2f ;//+ (waveCount / 5);

        if(waveCount % 3 == 0)
            spawnHelper(new HelperDef( creator.getHelperSpot(), Citizen.class, true));
        if(waveCount % 7 == 0)
            spawnHelper(new HelperDef( creator.getHelperSpot(), Artist.class, true));
        if(waveCount % 10 == 0)
            spawnHelper(new HelperDef( creator.getHelperSpot(), Astrologer.class, true));

        for (Item item : coins) {
            item.setMovable();
        }

        waveManager.nextWave(waveCount, enemies);

        description.show("Wave " + waveCount);
        System.out.println("priesai: " + enemies.size + " bodziu: " + world.getBodyCount());

    }

    public void setToFollowBlaster(){
        followBlaster = true;
    }

    public void addCoinsToStorage(){
        game.preferences.putInteger("coins", game.coins);
        game.preferences.flush();
    }

    public Hud getHud(){
        return hud;
    }

    public void addEnemy(Enemy enemy){
        enemies.add(enemy);
    }
}
