package ktu.edu.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;


import ktu.edu.screens.PlayScreen;

public class Hud implements Disposable {

    private final Stage stage;

    private static Image imageCoin;
    private static Label coinLabel;
    private final Label labelWave;
    private final Label countDown;
    private final Label hpLabel;
    private final Label killslabel;
    private final Label scoreLabel;

    public static int coins;
    public static int waveCount;
    public float worldTimer;
    public static int kills;
    public static int score;

    private static PlayScreen screen;

    public Hud(PlayScreen screen){
        Hud.screen = screen;
        this.stage = screen.stage;
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = screen.game.greenFont12;

        Table table = new Table();
        table.setFillParent(true);
        table.left().top();

        imageCoin = new Image(new Texture(Gdx.files.internal("coinIcon.png")));

        coinLabel = new Label("0", labelStyle );

        Image imageWave = new Image(new Texture(Gdx.files.internal("swords.png")));

        labelWave = new Label("0", labelStyle );

        Image imageClock = new Image(new Texture(Gdx.files.internal("clock.png")));

        countDown = new Label("20s",labelStyle );

        Image imageHp = new Image(new Texture(Gdx.files.internal("heart.png")));

        hpLabel = new Label(String.format("%.0f/%.0f",screen.blaster.currentHp, screen.blaster.maxHp), labelStyle );

        Image imageKills = new Image(new Texture(Gdx.files.internal("skull.png")));

        killslabel = new Label(Integer.toString(kills), labelStyle);

        Label scoreNameLabel = new Label("SCORE: ", labelStyle);
        scoreLabel = new Label(Integer.toString(score), labelStyle);

        table.add(imageCoin).width(60).height(60).padRight(5);
        table.add(coinLabel).padRight(10);
        table.add(imageWave).width(60).height(60).padRight(5);
        table.add(labelWave).padRight(10);
        table.add(imageClock).width(60).height(60).padRight(5);
        table.add(countDown).padRight(10);
        table.add(imageHp).width(60).height(60).padRight(5);
        table.add(hpLabel).padRight(10);

        table.add(imageKills).width(60).height(60);
        table.add(killslabel).padRight(30);
        table.add(scoreNameLabel).width(60).height(60).padRight(45);
        table.add(scoreLabel).padRight(10);


        Image background = new Image(screen.assets.manager.get("background2.png", Texture.class));
        background.setSize(stage.getWidth() + 200, 150);
        background.setPosition(-120,stage.getHeight()-100);
        stage.addActor(background);
        stage.addActor(table);

        coins = 0;
        waveCount = 0;
        worldTimer = 15;
        kills = 0;
        score = 0;

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public static void addCoins(int value)
    {
        if(imageCoin.getActions().size == 0) {
            imageCoin.addAction(Actions.sequence(Actions.scaleTo(1.5f, 1.5f),
                    Actions.moveBy(-10, -10),
                    Actions.parallel(Actions.scaleTo(1, 1, 0.5f),
                            Actions.moveBy(10, 10, 0.5f))));
            screen.assets.manager.get("sounds/collectcoin.wav", Sound.class).play();
        }
        coins += value;
        coinLabel.setText(coins);
    }

    public void addWave(int wave){
        waveCount = wave;
        labelWave.setText(waveCount);
    }

    public void addInfo(int score){
        kills++;
        killslabel.setText(kills);

        addScore(score);
    }

    public void addScore(int score){
        this.score += score;
        scoreLabel.setText(this.score);
    }

    public void update(float delta){
        worldTimer -= delta;
        countDown.setText((int)worldTimer + "s");
    }

    public void updateHp(){
        hpLabel.setText(String.format("%.0f / %.0f",screen.blaster.currentHp, screen.blaster.maxHp));
    }
}
