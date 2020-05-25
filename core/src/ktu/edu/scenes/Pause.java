package ktu.edu.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;

import ktu.edu.managers.GameScreenManager;
import ktu.edu.managers.PauseAbilities;
import ktu.edu.screens.PlayScreen;

public class Pause implements Disposable {
    private final Image image;
    private final Stage stage;
    private final Label name;
    private final TextButton buttonResume;
    private final TextButton buttonMainMenu;
    private PauseAbilities pauseAbilities;
    private final Button buttonSound;

    public Pause(final PlayScreen screen){
        this.stage = screen.stage;
        BitmapFont font = screen.game.font1;
        Skin skin = new Skin(Gdx.files.internal("ui/shadow/uiskin.json"));

        image = new Image(new Texture("backgroundBlack.png"));
        image.setSize(stage.getWidth(), stage.getHeight());
        image.setPosition(0,0);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        name = new Label("PAUSE", labelStyle);
        name.setSize(stage.getWidth(), 100);
        name.setPosition(0, stage.getHeight()-150);
        name.setAlignment(Align.center);

        buttonResume = new TextButton("Resume", skin, "round");
        buttonResume.setPosition(stage.getWidth() / 2 - 140, 110);
        buttonResume.setSize(280,60);
        buttonResume.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.game.playClick();
                screen.controller.pressPause = false;
                removePauseActors();
            }
        });

        buttonMainMenu = new TextButton("Main Menu", skin, "round");
        buttonMainMenu.setPosition(stage.getWidth() / 2 - 140, 40);
        buttonMainMenu.setSize(280,60);
        buttonMainMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.game.playClick();
                screen.game.gsm.setScreen(GameScreenManager.STATE.MAINMENU);
                removePauseActors();
            }
        });

        buttonSound = new Button(skin, "sound");

        buttonSound.setPosition(stage.getWidth()-120,stage.getHeight()-120);
        buttonSound.setSize(80,80);

        pauseAbilities = new PauseAbilities(screen);

        stage.addActor(image);
        stage.addActor(name);
        stage.addActor(buttonResume);
        stage.addActor(buttonMainMenu);
        stage.addActor(buttonSound);

        pauseAbilities = new PauseAbilities(screen);
        float x = stage.getWidth() / 2 - 360;
        float y = stage.getHeight()- 160;
        for(int i = 0; i < pauseAbilities.getPauseAbilities().size; i++) {
            if(i % 5 == 0){
                x = stage.getWidth() / 2 - 360;
                y -= 160;
            }
            pauseAbilities.getPauseAbilities().get(i).setActorBounds(x,y);
            stage.addActor(pauseAbilities.getPauseAbilities().get(i));
            x += 140;
        }

        removePauseActors();
    }

    public void addPauseActors() {
        name.clearActions();
        image.clearActions();
        name.setVisible(true);
        image.setVisible(true);
        buttonResume.setVisible(true);
        buttonMainMenu.setVisible(true);
        buttonSound.setVisible(true);
        name.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f)));
        image.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f)));
        buttonResume.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f)));
        buttonMainMenu.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f)));
        pauseAbilities.setText();
        for(int i = 0; i < pauseAbilities.getPauseAbilities().size; i++) {
            pauseAbilities.getPauseAbilities().get(i).setVisible(true);
            pauseAbilities.getPauseAbilities().get(i).clicked(i * 0.2f);
        }
    }

    private void removePauseActors(){

        name.setVisible(false);
        image.setVisible(false);
        buttonResume.setVisible(false);
        buttonMainMenu.setVisible(false);
        buttonSound.setVisible(false);
        for(int i = 0; i < pauseAbilities.getPauseAbilities().size; i++) {
            pauseAbilities.getPauseAbilities().get(i).setVisible(false);
        }
        System.out.println(stage.getActors());

    }
    @Override
    public void dispose() {
        System.out.println("susinaikino pauze stage ir t.t.");
        stage.dispose();
    }
}
