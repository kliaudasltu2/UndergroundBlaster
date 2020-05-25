package ktu.edu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import ktu.edu.UndergroundBlasterGame;
import ktu.edu.managers.GameScreenManager;

public class MainMenu extends AbstractScreen {

    private Skin skin;

    private TextButton buttonPlay, buttonExit, buttonShop, buttonRanking;

    private Image imageCoin;
    private Label coinLabel;
    private final Label.LabelStyle labelStyle;
    private final Label.LabelStyle numberStyle;

    private Runnable goToShop, goToRanking;
    private final int buttonWidth;
    private final int buttonHeight;
    private final int gap;

    private TextureRegion currentFrame;
    private TextureRegion blasterCurrentFrame;
    private Animation bombAnimation;
    private Animation blasterAnimation;
    private float stateTime;
    public MainMenu(UndergroundBlasterGame game) {
        super(game);
        System.out.println("main menu Constructor");

        labelStyle = new Label.LabelStyle();
        labelStyle.font = UndergroundBlasterGame.titleFont;
        buttonWidth = 380;
        buttonHeight = 80;
        gap = 420;

        numberStyle = new Label.LabelStyle();
        numberStyle.font = UndergroundBlasterGame.font2;
    }



    @Override
    public void update(float delta) {
        stage.act();
    }

    @Override
    public void show() {
        System.out.println("main menu show");
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        TextureAtlas atlas = game.assMan.manager.get("sprites/explosion.pack", TextureAtlas.class);
        Array<TextureRegion> frames = new Array<>();
        for(int i = 0; i < 8; i++){
            frames.add(new TextureRegion(atlas.findRegion("bomb"),  0, (i*246) , 256, 246));
            if(i < 7) {
                frames.add(new TextureRegion(atlas.findRegion("bomb"), 258, (i * 246), 256, 246));
            }
        }
        bombAnimation = new Animation(1/15f, frames);
        bombAnimation.setPlayMode(Animation.PlayMode.LOOP);
        stateTime = 0;
        currentFrame = (TextureRegion) bombAnimation.getKeyFrame(stateTime, true);
        Label title = new Label("Underground \n Blaster", labelStyle);
        title.setSize(300,200);
        title.setPosition(stage.getWidth()/2- title.getWidth()/2, stage.getHeight() - title.getHeight()-20);
        title.setAlignment(Align.center);

        TextureAtlas atlas2 =  game.assMan.manager.get("ui/screens/mainMenuItems.pack", TextureAtlas.class);
        frames = new Array<>();
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 6; j++){
                //TextureRegion frame = new TextureRegion(atlas.findRegion(region),j*175,i*175,175,175);
                frames.add(new TextureRegion(atlas2.findRegion("walking"),j*330,i*330,330,330));
            }
        }
        blasterAnimation = new Animation(1/30f, frames);
        bombAnimation.setPlayMode(Animation.PlayMode.LOOP);
        blasterCurrentFrame = (TextureRegion) blasterAnimation.getKeyFrame(stateTime, true);

        Image backGround = new Image(new TextureRegion(atlas2.findRegion("background")));
        backGround.setSize(200,150);
        backGround.setPosition(-50,stage.getHeight() - backGround.getHeight());

        imageCoin =  new Image(new TextureRegion(atlas2.findRegion( "coin")));
        imageCoin.setSize(60,60);
        imageCoin.setPosition(backGround.getX() + 60, backGround.getY()+ backGround.getHeight()-imageCoin.getHeight()-10);

        coinLabel = new Label(Integer.toString(game.coins), numberStyle);
        coinLabel.setPosition(imageCoin.getX() + imageCoin.getWidth(), imageCoin.getY() + (imageCoin.getHeight()/2 - coinLabel.getHeight()/2));

        Image imageSkull = new Image(new TextureRegion(atlas2.findRegion("skull")));
        imageSkull.setSize(80,80);
        imageSkull.setPosition(backGround.getX() + 50, imageCoin.getY() - imageSkull.getHeight());

        Label skullLabel = new Label(Integer.toString(game.totalKill), numberStyle);
        skullLabel.setPosition(coinLabel.getX(), imageSkull.getY() + (imageSkull.getHeight()/2 - skullLabel.getHeight()/2));

        Image background2 = new Image(new TextureRegion(atlas2.findRegion("background")));
        background2.setSize(200,150);
        background2.setPosition(stage.getWidth()-150,stage.getHeight() - backGround.getHeight());

      //  highScoreLabel, scoreLabel;

        Label highScoreLabel = new Label("HIGHSCORE", numberStyle);
        highScoreLabel.setSize(backGround.getWidth()-50, 30);
        highScoreLabel.setPosition(background2.getX(), background2.getY() + background2.getHeight()-70);
        highScoreLabel.setAlignment(Align.center);

        Label scoreLabel = new Label(Integer.toString(game.bestScore), numberStyle);
        scoreLabel.setSize(backGround.getWidth()-50, 30);
        scoreLabel.setPosition(background2.getX(), background2.getY() + background2.getHeight()- 100);
        scoreLabel.setAlignment(Align.center);



        stage.addActor(title);
        stage.addActor(backGround);
        stage.addActor(background2);
        stage.addActor(imageCoin);
        stage.addActor(coinLabel);
        stage.addActor(imageSkull);
        stage.addActor(skullLabel);
        stage.addActor(highScoreLabel);
        stage.addActor(scoreLabel);

        this.skin  = new Skin(Gdx.files.internal("ui/shadow/uiskin.json"));
        initButtons();

        goToShop = new Runnable() {
            @Override
            public void run() {
                stage.clear();
                game.gsm.setScreen(GameScreenManager.STATE.SHOP);
            }
        };

        goToRanking  = new Runnable() {
            @Override
            public void run() {
                stage.clear();
                game.gsm.setScreen(GameScreenManager.STATE.RATING);
            }
        };
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        update(delta);
        stage.draw();
        currentFrame = (TextureRegion) bombAnimation.getKeyFrame(stateTime, true);
        blasterCurrentFrame = (TextureRegion) blasterAnimation.getKeyFrame(stateTime, true);
        stateTime += delta;

        game.batch.begin();
        game.batch.draw(blasterCurrentFrame, -100,
                -100, currentFrame.getRegionWidth() * 3, currentFrame.getRegionHeight() * 3);
        game.batch.draw(currentFrame, stage.getWidth() / 2 - currentFrame.getRegionWidth() /2,
                 50, currentFrame.getRegionWidth() * 0.75f, currentFrame.getRegionHeight() * 0.75f);
        game.batch.end();

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
        System.out.println("Dispose MainMenu");
        stage.dispose();
    }

    private void initButtons(){
        buttonPlay = new TextButton("Start Game", skin, "round");
        buttonPlay.setPosition(stage.getWidth()-buttonWidth -20,-buttonHeight);
        buttonPlay.setSize(buttonWidth,buttonHeight);
        buttonPlay.addAction(Actions.moveBy(0,gap, 2f, Interpolation.pow5Out));
        buttonPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playClick();
                game.gsm.setScreen(GameScreenManager.STATE.LOADGAME);
                stage.clear();
            }
        });

        buttonShop = new TextButton("Shop", skin, "round");
        buttonShop.setPosition(stage.getWidth()-buttonWidth -20,-2*buttonHeight-10);
        buttonShop.setSize(buttonWidth,buttonHeight);
        buttonShop.addAction(Actions.sequence(Actions.delay(.25f),
                Actions.moveBy(0,gap, 2f, Interpolation.pow5Out)));
        buttonShop.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playClick();
                leaveStage();
                buttonShop.clearActions();
                buttonShop.addAction(Actions.sequence(Actions.delay(.5f),
                        Actions.moveBy(0,-gap, 2f, Interpolation.pow5Out),
                        Actions.run(goToShop)));
            }
        });

        buttonRanking = new TextButton("Ranking", skin, "round");
        buttonRanking.setPosition(stage.getWidth()-buttonWidth -20,-3*buttonHeight-20);
        buttonRanking.setSize(buttonWidth,buttonHeight);
        buttonRanking.addAction(Actions.sequence(Actions.delay(.5f),
                Actions.moveBy(0,gap, 2f, Interpolation.pow5Out)));
        buttonRanking.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playClick();
                leaveStage();
                buttonRanking.clearActions();
                buttonRanking.addAction(Actions.sequence(Actions.delay(.25f),
                        Actions.moveBy(0,-gap, 2f, Interpolation.pow5Out),
                        Actions.run(goToRanking)));
            }
        });

        buttonExit = new TextButton("Exit!", skin, "round");
        buttonExit.setPosition(stage.getWidth()-buttonWidth -20,-4*buttonHeight-30);
        buttonExit.setSize(buttonWidth,buttonHeight);
        buttonExit.addAction(Actions.sequence(Actions.delay(.75f),
                Actions.moveBy(0,gap, 2, Interpolation.pow5Out)));

        buttonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playClick();
                game.resetValues();
                game.gsm.setScreen(GameScreenManager.STATE.GAMEOVER);
                //Gdx.app.exit();
            }
        });

        stage.addActor(buttonShop);
        stage.addActor(buttonExit);
        stage.addActor(buttonPlay);
        stage.addActor(buttonRanking);
    }

    private void initCoins(){
        imageCoin = new Image(new Texture(Gdx.files.internal("coinIcon.png")));
        imageCoin.setSize(80,80);
        imageCoin.setPosition(0, stage.getHeight()-80);
        //imageCoin.setDebug(true);
        coinLabel = new Label(Integer.toString(game.coins), skin, "title-plain" );
        //coinLabel.setSize(20,70);
        coinLabel.setPosition(80, stage.getHeight()-80);
        stage.addActor(imageCoin);
        stage.addActor(coinLabel);
    }

    private void leaveStage(){
        buttonPlay.addAction(Actions.sequence(Actions.delay(0.75f),
                Actions.moveBy(0,-gap, 2f, Interpolation.pow5Out)));
        buttonShop.addAction(Actions.sequence(Actions.delay(0.50f),
                Actions.moveBy(0,-gap, 2f, Interpolation.pow5Out)));
        buttonRanking.addAction(Actions.sequence(Actions.delay(0.25f),
                Actions.moveBy(0,-gap, 2f, Interpolation.pow5Out)));
        buttonExit.addAction(
                Actions.moveBy(0,-gap, 2f, Interpolation.pow5Out));
    }


}
