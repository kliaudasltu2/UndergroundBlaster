package ktu.edu.screens;



import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import ktu.edu.UndergroundBlasterGame;
import ktu.edu.managers.GameScreenManager;

public class SplashScreen extends AbstractScreen {

    public SplashScreen(UndergroundBlasterGame game) {
        super(game);
    }

    @Override
    public void show() {
        Texture splashTex = new Texture("logo.png");
        //game.gsm.disposeScreen(GameScreenManager.STATE.);
        Runnable transitionRunnable = new Runnable() {
            @Override
            public void run() {
                game.gsm.setScreen(GameScreenManager.STATE.MAINMENU);
            }
        };

        Image splashImg = new Image(splashTex);
        splashImg.setSize(110.08f,64);
        splashImg.setOrigin(splashImg.getWidth() / 2, splashImg.getHeight() / 2);
        splashImg.setPosition(stage.getWidth() / 2- splashImg.getWidth() /2 , stage.getHeight() / 2 + splashImg.getHeight() / 2 );
        splashImg.addAction(sequence(alpha(0), scaleTo(.1f, .1f),
                parallel(fadeIn(2f, Interpolation.pow2),
                        scaleTo(2f, 2f, 2.5f, Interpolation.pow5),
                        moveTo(stage.getWidth() / 2 - 32, stage.getHeight() / 2 - 32, 2f, Interpolation.swing)),
                delay(1.0f), fadeOut(1.25f), run(transitionRunnable)));
        Label.LabelStyle labelStyle= new Label.LabelStyle();
        labelStyle.font = UndergroundBlasterGame.titleFont;
        Label label = new Label("Xeno games", labelStyle);
        label.setPosition(stage.getWidth()/2, 100);
        label.setAlignment(Align.center);
        stage.addActor(label);
        stage.addActor(splashImg);
    }

    @Override
    public void render(float delta) {
       super.render(delta);

        update(delta);

        stage.draw();
    }

    @Override
    public void update(float delta) {
        stage.act();
    }

    @Override
    public void resize(int width, int height) {
        stageResize(width,height);

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

        stage.dispose();
        System.out.println("Dispose Splash");
    }
}
