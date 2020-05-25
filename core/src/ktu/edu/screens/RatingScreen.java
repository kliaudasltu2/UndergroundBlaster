package ktu.edu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import ktu.edu.UndergroundBlasterGame;
import ktu.edu.managers.GameScreenManager;

public class RatingScreen extends AbstractScreen  {

    private final Label.LabelStyle labelStyle;

    public RatingScreen(UndergroundBlasterGame game) {
        super(game);
        labelStyle = new Label.LabelStyle();
        labelStyle.font = UndergroundBlasterGame.titleFont;
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void show() {
        stage.clear();
        Skin skin = new Skin(Gdx.files.internal("ui/shadow/uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        Label title = new Label("Ranking", labelStyle);
        title.setSize(300,200);
        title.setPosition(stage.getWidth()/2- title.getWidth()/2, stage.getHeight() - title.getHeight()-20);
        title.setAlignment(Align.center);

        stage.addActor(title);

        TextButton buttonMenu = new TextButton("Main Menu", skin, "round");
        buttonMenu.setSize(380,80);
        buttonMenu.setPosition(stage.getWidth() - buttonMenu.getWidth() - 10, 10);
        //buttonMenu.addAction(Actions.moveBy(0,gap, 2f, Interpolation.pow5Out));
        buttonMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playClick();
                game.gsm.setScreen(GameScreenManager.STATE.MAINMENU);
            }
        });

        stage.addActor(title);
        stage.addActor(buttonMenu);

    }

    @Override
    public void resize(int width, int height) {

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

    }

    @Override
    public void render(float delta) {
        super.render(delta);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
}
