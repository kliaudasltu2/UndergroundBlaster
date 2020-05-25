package ktu.edu.scenes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import ktu.edu.screens.PlayScreen;

public class Controller implements Disposable {
    private final Stage stage;
    private boolean isTouched;
    private SpriteDrawable pause;
    private Image pauseButton;
    private final float radius;

    private final Image controller;
    private final Image navigator;
    public boolean pressButton, pressPause;

    public Controller(final PlayScreen screen){
        this.stage = screen.stage;
        isTouched = false;
        pressButton = false;

        TextureAtlas atlas = screen.assets.manager.get("ui/controller/controlleris.pack", TextureAtlas.class);
        Image bombButton = new Image(new TextureRegion(atlas.findRegion("boombutton")));
        bombButton.setSize(250,250 );
        bombButton.setPosition(stage.getWidth()- bombButton.getWidth(), 0);

        stage.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if( !pressButton && !pressPause && !isTouched) {
                    setPosition(x, y);
                    return true;
                }
                return false;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                    isTouched = false;
                    setToConer();

            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                setNavigatorCord(x, y);
            }
        });

        controller = new Image(new TextureRegion(atlas.findRegion("controller")));
        controller.setSize(250,250);

        navigator = new Image(new TextureRegion(atlas.findRegion("knob")));
        navigator.setSize(90,90);

        bombButton.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(pauseButton.getDrawable() == pause) {
                    pressButton = true;
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                     pressButton = false;
            }
        });

        pause = new SpriteDrawable(new Sprite(new Texture("pause.png")));
        pauseButton = new Image();
        pauseButton.setDrawable(pause);
        pauseButton.setSize(80,80);
        pauseButton.setPosition(stage.getWidth() - 80, stage.getHeight()-80);

        pauseButton.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                pressPause = true;
                screen.pauseStage.addPauseActors();
                return true;
            }
        });

        stage.addActor(pauseButton);
        stage.addActor(controller);
        stage.addActor(navigator);
        stage.addActor(bombButton);

        setToConer();

        radius = controller.getHeight()/2;

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private void setPosition(float x, float y){
        controller.setPosition( x - (controller.getWidth()/2), y - (controller.getHeight()/2));
        navigator.setPosition( x - (navigator.getWidth()/2), y - (navigator.getHeight()/2));
    }

    private void setToConer(){
        controller.setPosition(0,0);
        navigator.setPosition(controller.getWidth()/2 - navigator.getWidth() / 2,controller.getHeight() /2 - navigator.getHeight() / 2);
    }

    private void setNavigatorCord(float x, float y){
        Vector2 controllerCord = controllerCenter();
        float distance = (float)(Math.sqrt(((x - controllerCord.x)*(x-controllerCord.x))+((y - controllerCord.y)*(y-controllerCord.y))));

        if(distance > 15 || isTouched) {
            isTouched = true;
            if (distance < radius) {
                navigator.setPosition(x - (navigator.getWidth() / 2), y - (navigator.getHeight() / 2));
            } else {
                float times = distance / radius;
                navigator.setPosition(((((x - controllerCord.x) / times) + controllerCord.x) - (navigator.getWidth() / 2)),
                        ((((y - controllerCord.y) / times) + controllerCord.y) - (navigator.getHeight() / 2)));
            }
        }

    }

    public boolean isTouched() {
        return isTouched;
    }

    private Vector2 controllerCenter(){
        return new Vector2(controller.getX() + controller.getWidth()/2, controller.getY() + controller.getHeight()/2 );
    }

    private Vector2 navigatorCenter(){
        return new Vector2(navigator.getX() + navigator.getWidth()/2, navigator.getY() + navigator.getHeight()/2);
    }

    public Vector2 difference(){
        return new Vector2(controllerCenter().x - navigatorCenter().x, controllerCenter().y - navigatorCenter().y) ;
    }
}
