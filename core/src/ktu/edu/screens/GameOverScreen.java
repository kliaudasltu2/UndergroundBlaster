package ktu.edu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import ktu.edu.UndergroundBlasterGame;
import ktu.edu.managers.GameScreenManager;
import ktu.edu.scenes.Hud;

public class GameOverScreen extends AbstractScreen {

    private Skin skin;

    private final Label.LabelStyle labelStyle;
    private final Label.LabelStyle labelStyle2;

    public GameOverScreen(UndergroundBlasterGame game) {
        super(game);
        System.out.println("game OVer Constructor");

        labelStyle = new Label.LabelStyle();
        labelStyle.font = UndergroundBlasterGame.titleFont;

        labelStyle2 = new Label.LabelStyle();
        labelStyle2.font = game.whiteFont12;
    }

    @Override
    public void update(float delta) {
        stage.act();
    }

    @Override
    public void show() {
        System.out.println("game over show");
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        this.skin  = new Skin(Gdx.files.internal("ui/shadow/uiskin.json"));

        TextureAtlas atlas = game.assMan.manager.get("ui/screens/gameover.pack", TextureAtlas.class);
        Image blaster = new Image(atlas.findRegion("dead"));
        blaster.setSize(550,550);
        blaster.setPosition(stage.getWidth()/2 - blaster.getWidth()/2,stage.getHeight()/2 - blaster.getHeight()/2);
        stage.addActor(blaster);

        Image background = new Image(atlas.findRegion("backgroundBlack"));
        background.setSize(stage.getWidth(),stage.getHeight());
        background.setPosition(0,0);
        stage.addActor(background);

        Label title = new Label("GAME OVER!", labelStyle);
        title.setSize(stage.getWidth(), 50);
        title.setPosition(0, stage.getHeight() - 100);
        title.setAlignment(Align.center);

        Label description = new Label("Ohh.. Try again to beat the best record!", labelStyle2);
        description.setSize(stage.getWidth(), 50);
        description.setPosition(0, title.getY() - 70);
        description.setAlignment(Align.center);

        if(game.bestScore < Hud.score) {
            description.setText("Congrats! You reached a new best record!");
            game.bestScore = Hud.score;
            game.preferences.putInteger("bestScore", Hud.score);
        }

        Label scoreName = new Label("Score:", labelStyle2);
        scoreName.setSize(stage.getWidth()/2, 50);
        scoreName.setPosition(0, description.getY() - 120);
        scoreName.setAlignment(Align.right);

        Label score = new Label(Integer.toString(Hud.score), labelStyle2);
        score.setSize(stage.getWidth()/2, 50);
        score.setPosition(stage.getWidth()/2, description.getY() - 120);

        Label killName = new Label("Kills:", labelStyle2);
        killName.setSize(stage.getWidth()/2, 50);
        killName.setPosition(0, scoreName.getY() - 50);
        killName.setAlignment(Align.right);

        Label kills = new Label(Integer.toString(Hud.kills), labelStyle2);
        kills.setSize(stage.getWidth()/2, 50);
        kills.setPosition(stage.getWidth()/2, score.getY() - 50);

        Label surviveName = new Label("Wave:", labelStyle2);
        surviveName.setSize(stage.getWidth()/2, 50);
        surviveName.setPosition(0, kills.getY() - 50);
        surviveName.setAlignment(Align.right);

        Label survive = new Label(Integer.toString(Hud.waveCount), labelStyle2);
        survive.setSize(stage.getWidth()/2, 50);
        survive.setPosition(stage.getWidth()/2, kills.getY() - 50);


        stage.addActor(title);
        stage.addActor(description);
        stage.addActor(scoreName);
        stage.addActor(score);
        stage.addActor(killName);
        stage.addActor(kills);
        stage.addActor(surviveName);
        stage.addActor(survive);
        initButtons();

        game.totalKill += Hud.kills;
        game.preferences.putInteger("totalKills", game.totalKill);
        game.preferences.flush();


    }

    @Override
    public void resize(int width, int height) {
        stageResize(width,height);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);


        stage.draw();
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
        System.out.println("Dispose GameOverScene");
        stage.dispose();
    }

    private void initButtons(){
        TextButton buttonPlayAgain = new TextButton("Play again!", skin, "round");
        buttonPlayAgain.setSize(350,75);
        buttonPlayAgain.setPosition(stage.getWidth()/2 - buttonPlayAgain.getWidth(),100);
        buttonPlayAgain.addAction(Actions.sequence(Actions.alpha(0f),
                Actions.parallel(Actions.fadeIn(.5f), Actions.moveBy(0,-20, .5f, Interpolation.pow5Out))));
        buttonPlayAgain.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playClick();
                game.gsm.setScreen(GameScreenManager.STATE.LOADGAME);
            }
        });

        TextButton buttonMainMenu = new TextButton("Shop", skin, "round");
        buttonMainMenu.setSize(350,75);
        buttonMainMenu.setPosition(stage.getWidth()/2,100);
        buttonMainMenu.addAction(Actions.sequence(Actions.alpha(0f),
                Actions.parallel(Actions.fadeIn(.5f), Actions.moveBy(0,-20, .5f, Interpolation.pow5Out))));

        buttonMainMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playClick();
                game.gsm.setScreen(GameScreenManager.STATE.SHOP);
            }
        });

        TextButton buttonExit = new TextButton("MainMenu", skin, "round");
        buttonExit.setSize(350,75);
        buttonExit.setPosition(stage.getWidth()/2 - buttonExit.getWidth()/2,20);
        buttonExit.addAction(Actions.sequence(Actions.alpha(0f),
                Actions.parallel(Actions.fadeIn(.5f), Actions.moveBy(0,-20, .5f, Interpolation.pow5Out))));

        buttonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playClick();
                game.gsm.setScreen(GameScreenManager.STATE.MAINMENU);
            }
        });

        stage.addActor(buttonMainMenu);
        stage.addActor(buttonExit);
        stage.addActor(buttonPlayAgain);
    }
}
