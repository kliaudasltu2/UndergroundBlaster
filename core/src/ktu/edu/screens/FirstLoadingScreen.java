package ktu.edu.screens;

import com.badlogic.gdx.Gdx;
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

public class FirstLoadingScreen extends AbstractScreen {

    private final ShapeRenderer shapeRenderer;
    private float progress;
    private float timer;

    private Skin skin;

    public FirstLoadingScreen(UndergroundBlasterGame game){
        super(game);
        this.shapeRenderer = new ShapeRenderer();
        this.progress = 0f;
        this.timer = 0;
    }

    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal("ui/shadow/uiskin.json"));
        Label name = new Label("Loading...", skin, "title-plain");
        name.setSize(stage.getWidth(), 70);
        name.setPosition(0, stage.getHeight()/2-70);
        name.setAlignment(Align.center);
        stage.addActor(name);

        game.assMan.loadingScreenAsset();

        name.addAction(Actions.forever(Actions.sequence(Actions.fadeOut(1f, Interpolation.pow5),Actions.fadeIn(1f, Interpolation.pow2), Actions.delay(0.5f))));
        shapeRenderer.setProjectionMatrix(stageCam.combined);

    }

    public void update(float delta){
        progress = MathUtils.lerp(progress, game.assMan.manager.getProgress(), .15f);
        if(game.assMan.manager.update()){
            timer += delta;
        }

        if(timer > 2) {
            game.gsm.setScreen(GameScreenManager.STATE.SPLASH);
        }
        stage.act();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);

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
        System.out.println("Dispose FirsLoadingScreen");
        shapeRenderer.dispose();
        stage.dispose();
        skin.dispose();
    }
}
