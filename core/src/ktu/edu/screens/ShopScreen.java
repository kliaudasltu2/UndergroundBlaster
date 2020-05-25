package ktu.edu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
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
import ktu.edu.actors.ShopItem;
import ktu.edu.managers.GameScreenManager;
import ktu.edu.managers.ShopItems;

public class ShopScreen extends AbstractScreen {

    private Skin skin;

    private TextButton buttonMenu, buttonPlay;
    private Label title;
    private Image imageCoin;
    private Label coinLabel, coinLabelAdditional;

    private final Runnable goToMainMenu;
    private final Runnable gotToBattle;

    private ShopItems shopItems;
    public ShopScreen(final UndergroundBlasterGame game) {
        super(game);

         goToMainMenu = new Runnable() {
            @Override
            public void run() {
                game.gsm.setScreen(GameScreenManager.STATE.MAINMENU);
            }
        };

         gotToBattle = new Runnable() {
            @Override
            public void run() {
                game.gsm.setScreen(GameScreenManager.STATE.LOADGAME);
            }
        };
    }

    @Override
    public void update(float delta) {
        stage.act();
    }

    @Override
    public void show() {
        System.out.println("shop menu show");

        Gdx.input.setInputProcessor(stage);
        stage.clear();

        this.skin  = new Skin(Gdx.files.internal("ui/shadow/uiskin.json"));
        title = new Label("Upgrade your character!", skin, "title");
        title.setSize(stage.getWidth()/2-40, 80);
        title.setPosition(stage.getWidth()/2+40, stage.getHeight()-90-stage.getHeight());
        title.setAlignment(Align.center);
        title.addAction(Actions.moveBy(0,stage.getHeight(), 2f, Interpolation.pow5Out));

        stage.addActor(title);
        initButtons();

        shopItems = new ShopItems(this);
        float x = 30;
        float y = stage.getHeight() - 207.5f;
        float time = 0;

        for(int i = 0; i < shopItems.getItems().size; i++) {
            if(i % 3 == 0 && i != 0){
                x = 30;
                y -= 225;
                time += 0.25f;
            }

            shopItems.getItems().get(i).setActorBounds(x,y, stage.getHeight());
            shopItems.getItems().get(i).transition(stage.getHeight(), time);
            stage.addActor(shopItems.getItems().get(i));
            x+=225;
        }

        shopItems.getItems().get(0).addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    purchase(shopItems.getItems().get(0));
                }
            });

        shopItems.getItems().get(1).addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                purchase(shopItems.getItems().get(1));
            }
        });

        shopItems.getItems().get(2).addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                purchase(shopItems.getItems().get(2));
            }
        });

        shopItems.getItems().get(3).addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                purchase(shopItems.getItems().get(3));
            }
        });

        shopItems.getItems().get(4).addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                purchase(shopItems.getItems().get(4));
            }
        });

        shopItems.getItems().get(5).addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                purchase(shopItems.getItems().get(5));
            }
        });

        shopItems.getItems().get(6).addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                purchase(shopItems.getItems().get(6));
            }
        });

        shopItems.getItems().get(7).addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                purchase(shopItems.getItems().get(7));
            }
        });

        shopItems.getItems().get(8).addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                purchase(shopItems.getItems().get(8));
            }
        });

        y  = stage.getHeight()-85;
        time = 0.25f;
        for(int i = 0; i < shopItems.getIcons().size; i++) {
            y -= 60;
            shopItems.getIcons().get(i).setActorBounds(stage.getWidth()/2+40, y, stage.getHeight());
            shopItems.getIcons().get(i).transition(stage.getHeight(), time);
            stage.addActor(shopItems.getIcons().get(i));
            time += .041f;
        }

        initCoins();

        if(game.citizen)
          shopItems.getItems().get(0).changeCostLabel("purchased");

        if(game.artist)
            shopItems.getItems().get(1).changeCostLabel("purchased");

        if(game.astrologer)
            shopItems.getItems().get(2).changeCostLabel("purchased");
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        stage.draw();

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            game.resetValues();
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

    }
    private void initButtons(){
        buttonMenu = new TextButton("Main Menu", skin, "round");
        buttonMenu.setSize(stage.getWidth()/2-40,80);
        buttonMenu.setPosition(stage.getWidth() - buttonMenu.getWidth(),35 - stage.getHeight());
        buttonMenu.addAction(Actions.sequence(Actions.delay(0.5f),Actions.moveBy(0,stage.getHeight(), 2f, Interpolation.pow5Out)));
        buttonMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playClick();
                buttonMenu.clearActions();
                addActions();
                buttonMenu.addAction(Actions.sequence(Actions.delay(0.25f),Actions.moveBy(0,-stage.getHeight(), 2f, Interpolation.pow5Out), Actions.run(goToMainMenu)));
            }
        });

        buttonPlay = new TextButton("Go to battle", skin, "round");
        buttonPlay.setSize(stage.getWidth()/2-40,80);
        buttonPlay.setPosition(stage.getWidth() - buttonPlay.getWidth(),125 - stage.getHeight());
        buttonPlay.addAction(Actions.sequence(Actions.delay(0.5f),Actions.moveBy(0,stage.getHeight(), 2f, Interpolation.pow5Out)));
        buttonPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playClick();
                buttonPlay.clearActions();
                addActions();
                buttonPlay.addAction(Actions.sequence(Actions.delay(0.25f),Actions.moveBy(0,-stage.getHeight(), 2f, Interpolation.pow5Out), Actions.run(gotToBattle)));
            }
        });

        stage.addActor(buttonMenu);
        stage.addActor(buttonPlay);
    }

    private void purchase(ShopItem item){
        switch (item.getName()){
            case "Magnet":
                if(game.coins >= item.getValue() && !game.magnet) {
                    game.magnet = true;
                    game.coins -= item.getValue();
                    coinLabel.setText(Integer.toString(game.coins));
                    coinLabelAnimation(item.getValue());
                    item.changeCostLabel("purchased");
                    item.clicked();
                    game.preferences.putInteger("coins",game.coins);
                    game.preferences.putBoolean("magnet", true);
                    game.preferences.flush();
                }
                else
                    game.assMan.manager.get("sounds/cancel.wav", Sound.class).play();
                break;
            case "Artist":
                if(game.coins >= item.getValue() && !game.artist) {
                    game.artist = true;
                    game.coins -= item.getValue();
                    coinLabelAnimation(item.getValue());
                    coinLabel.setText(Integer.toString(game.coins));
                    item.changeCostLabel("purchased");
                    item.clicked();
                    game.preferences.putInteger("coins",game.coins);
                    game.preferences.putBoolean("artist", true);
                    game.preferences.flush();
                }
                else
                    game.assMan.manager.get("sounds/cancel.wav", Sound.class).play();
                break;
            case "Astrologer":
                if(game.coins >= item.getValue() && !game.astrologer) {
                    game.astrologer = true;
                    game.coins -= item.getValue();
                    coinLabel.setText(Integer.toString(game.coins));
                    coinLabelAnimation(item.getValue());
                    item.changeCostLabel("purchased");
                    item.clicked();
                    game.preferences.putInteger("coins",game.coins);
                    game.preferences.putBoolean("astrologer", true);
                    game.preferences.flush();
                }
                else
                    game.assMan.manager.get("sounds/cancel.wav", Sound.class).play();
                break;
            case "Damage":
                if(game.coins >= item.getValue()) {

                    game.damage += 50;
                    game.coins -= item.getValue();

                    coinLabel.setText(Integer.toString(game.coins));
                    coinLabelAnimation(item.getValue());

                    item.setValue(item.getValue() + 200);
                    item.clicked();

                   shopItems.getIcons().get(0).useIcon(Integer.toString(game.damage));

                    game.preferences.putInteger("coins",game.coins);
                    game.preferences.putInteger("damage", game.damage);
                    game.preferences.putInteger("damageCost", item.getValue());
                    game.preferences.flush();
                }
                else
                    game.assMan.manager.get("sounds/cancel.wav", Sound.class).play();
                break;
            case "Max Hitpoints":
                if(game.coins >= item.getValue()) {
                    game.maxHp += 200;
                    game.coins -= item.getValue();
                    coinLabel.setText(Integer.toString(game.coins));
                    coinLabelAnimation(item.getValue());
                    item.setValue(item.getValue() + 200);
                    item.clicked();

                    shopItems.getIcons().get(1).useIcon(String.format("%.0f",game.maxHp));

                    game.preferences.putInteger("coins",game.coins);
                    game.preferences.putFloat("maxHP", game.maxHp);
                    game.preferences.putInteger("maxHPCost", item.getValue());
                    game.preferences.flush();
                    System.out.println(game.maxHp);
                }
                else
                    game.assMan.manager.get("sounds/cancel.wav", Sound.class).play();
                break;
            case "Heal":
                if(game.coins >= item.getValue()) {
                    game.heal += 50;
                    game.coins -= item.getValue();
                    coinLabel.setText(Integer.toString(game.coins));
                    coinLabelAnimation(item.getValue());
                    item.setValue(item.getValue() + 200);
                    item.clicked();
                    shopItems.getIcons().get(2).useIcon((String.format("%.0f",game.heal)));
                    game.preferences.putInteger("coins",game.coins);
                    game.preferences.putFloat("heal", game.heal);
                    game.preferences.putInteger("healCost", item.getValue());
                    game.preferences.flush();
                }
                else
                    game.assMan.manager.get("sounds/cancel.wav", Sound.class).play();
                break;
            case "Stun":
                if(game.coins >= item.getValue()) {
                    game.stunTime += 0.2f;
                    game.coins -= item.getValue();
                    coinLabel.setText(Integer.toString(game.coins));
                    coinLabelAnimation(item.getValue());
                    item.setValue(item.getValue() + 500);
                    item.clicked();
                    shopItems.getIcons().get(3).useIcon((String.format("%.1f",game.stunTime)) + "s");
                    game.preferences.putInteger("coins",game.coins);
                    game.preferences.putFloat("stun", game.stunTime);
                    game.preferences.putInteger("stunCost", item.getValue());
                    game.preferences.flush();
                }
                else
                    game.assMan.manager.get("sounds/cancel.wav", Sound.class).play();
                break;
            case "Armour":
                if(game.coins >= item.getValue()) {
                    game.armour += 0.02f;
                    game.coins -= item.getValue();
                    coinLabel.setText(Integer.toString(game.coins));
                    coinLabelAnimation(item.getValue());
                    item.setValue(item.getValue() + 500);
                    item.clicked();
                    shopItems.getIcons().get(4).useIcon((String.format("%.0f",game.armour * 100)) +  "%");
                    game.preferences.putInteger("coins",game.coins);
                    game.preferences.putFloat("armour", game.armour);
                    game.preferences.putInteger("armourCost", item.getValue());
                    game.preferences.flush();
                }
                else
                    game.assMan.manager.get("sounds/cancel.wav", Sound.class).play();
                break;
            case "Explosion size":
                if(game.coins >= item.getValue()) {
                    game.explosionSize += 2.4f;
                    game.coins -= item.getValue();
                    coinLabel.setText(Integer.toString(game.coins));
                    coinLabelAnimation(item.getValue());
                    item.setValue(item.getValue() + 200);
                    item.clicked();
                    shopItems.getIcons().get(5).useIcon(String.format("%.1f", game.explosionSize));

                    game.damage += 25;
                    shopItems.getIcons().get(0).useIcon(Integer.toString(game.damage));

                    game.preferences.putInteger("damage", game.damage);
                    game.preferences.putInteger("coins",game.coins);
                    game.preferences.putFloat("size", game.explosionSize);
                    game.preferences.putInteger("sizeCost", item.getValue());
                    game.preferences.flush();
                }
                else
                    game.assMan.manager.get("sounds/cancel.wav", Sound.class).play();
                break;
        }
    }

    private void initCoins(){
        imageCoin = new Image(new Texture(Gdx.files.internal("coinIcon.png")));
        imageCoin.setSize(40,40);
        imageCoin.setPosition(stage.getWidth() - 120, stage.getHeight()-125);

//        imageCoin.addAction(Actions.moveBy(0,stage.getHeight(), 2f, Interpolation.pow5Out));
        imageCoin.addAction(Actions.sequence(Actions.alpha(0),Actions.delay(2.0f), Actions.fadeIn(2f, Interpolation.pow5Out)));
        //imageCoin.setDebug(true);
        coinLabel = new Label(Integer.toString(game.coins), skin, "title-plain" );
        coinLabel.setSize(20,20);
        coinLabel.setPosition(stage.getWidth()- 80, stage.getHeight()-115);

//        coinLabel.addAction(Actions.moveBy(0,stage.getHeight(), 2f, Interpolation.pow5Out));
        coinLabel.addAction(Actions.sequence(Actions.alpha(0),Actions.delay(2.0f), Actions.fadeIn(2f, Interpolation.pow5Out)));
        stage.addActor(imageCoin);
        stage.addActor(coinLabel);

        coinLabelAdditional = new Label("", skin, "customtitle3");
        coinLabelAdditional.addAction(Actions.alpha(0f));
        coinLabelAdditional.setPosition(stage.getWidth()- 80, stage.getHeight()-125);
        stage.addActor(coinLabelAdditional);
    }

    private void coinLabelAnimation(int cost){
        game.assMan.manager.get("sounds/purchase.wav", Sound.class).play();
        coinLabelAdditional.clearActions();
        coinLabelAdditional.setText("-" + cost);
        coinLabelAdditional.setPosition(stage.getWidth()- 80, stage.getHeight()-135);
        coinLabelAdditional.addAction(Actions.sequence(Actions.alpha(1f),
                                               Actions.parallel(Actions.moveBy(0, -20,1f),
                                                                Actions.fadeOut(1f))));
    }

    private void addActions(){
        float time = .5f;
        for(int i = 0; i < shopItems.getItems().size; i++){
            if(i % 3 == 0 && i != 0)
                time -= 0.25f;
            shopItems.getItems().get(i).transition(-stage.getHeight(),time);
        }

        buttonMenu.addAction(Actions.sequence(Actions.moveBy(0,-stage.getHeight(), 2f, Interpolation.pow5Out)));
        buttonPlay.addAction(Actions.sequence(Actions.delay(0.25f),Actions.moveBy(0,-stage.getHeight(), 2f, Interpolation.pow5Out)));
        title.addAction(Actions.sequence(Actions.delay(0.5f),Actions.moveBy(0,-stage.getHeight(), 2f, Interpolation.pow5Out)));
        coinLabel.addAction(Actions.sequence(Actions.delay(0.5f),Actions.moveBy(0,-stage.getHeight(), 2f, Interpolation.pow5Out)));
        imageCoin.addAction(Actions.sequence(Actions.delay(0.5f),Actions.moveBy(0,-stage.getHeight(), 2f, Interpolation.pow5Out)));
        time = 0.5f;
        for(int i = 0; i < shopItems.getIcons().size; i++){
            time -= .041f;
            shopItems.getIcons().get(i).transition(-stage.getHeight(), time);
        }


    }
}
