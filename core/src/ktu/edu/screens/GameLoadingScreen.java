package ktu.edu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import ktu.edu.UndergroundBlasterGame;
import ktu.edu.managers.GameScreenManager;
import ktu.edu.tools.BlasterRunning;

public class GameLoadingScreen extends AbstractScreen {

    private final ShapeRenderer shapeRenderer;
    private float progress;
    private float timer = 0;
    private BlasterRunning blasterRunning;
    private boolean finish;
    private float x;
    private float y;
    private Skin skin;

    public GameLoadingScreen(UndergroundBlasterGame game){
        super(game);
        this.shapeRenderer = new ShapeRenderer();
        this.progress = 0f;
        System.out.println("Loading SCreen Constructor");
    }

    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal("ui/shadow/uiskin.json"));
        System.out.println("Loading SCreen show");
        stage.clear();
        blasterRunning = new BlasterRunning(this);
        Label name = new Label("Entering the world", skin, "title-plain");
        name.setSize(stage.getWidth(), 70);
        name.setPosition(0, stage.getHeight()/2-70);
        name.setAlignment(Align.center);
        stage.addActor(name);

        x = 0; y = stage.getHeight()/2 + blasterRunning.getHeight() / 3;
        game.assMan.load();
        name.addAction(Actions.forever(Actions.sequence(Actions.fadeOut(1f, Interpolation.pow5),Actions.fadeIn(1f, Interpolation.pow2), Actions.delay(0.5f))));
        shapeRenderer.setProjectionMatrix(stageCam.combined);

    }

    public void update(float delta){
        progress = MathUtils.lerp(progress, game.assMan.manager.getProgress(), .15f);
        if(game.assMan.manager.update()){
            finish = true;
        }

        if(x > stage.getWidth() + 64){
            game.assMan.manager.get("sounds/Helper2.wav", Sound.class).play();
            game.gsm.setScreen(GameScreenManager.STATE.PLAY);
        }
        if(finish){
            blasterRunning.update(delta, x,y);
            x += 4;
        }
        stage.act();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);

        if(finish) {
            game.batch.begin();
            blasterRunning.draw(game.batch);
            game.batch.end();
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor( skin.getColor("light_grey"));
        shapeRenderer.rect(0, stage.getHeight() / 2 - 8, stage.getWidth(), 16);

        shapeRenderer.setColor(skin.getColor("grey"));
        shapeRenderer.rect(0, stage.getHeight() / 2 - 8, (stage.getWidth()) * progress, 16);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(0, stage.getHeight() / 2 - 8, stage.getWidth(), 16);
        shapeRenderer.end();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        stageResize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        System.out.println("Dispose GameLoadingDispose");
        shapeRenderer.dispose();
        stage.dispose();
        skin.dispose();
    }
}
