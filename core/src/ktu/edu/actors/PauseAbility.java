package ktu.edu.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import ktu.edu.UndergroundBlasterGame;

public class PauseAbility extends Actor {

    private final Image image;
    private final Label abilityValue;

    public PauseAbility(Image image, String labelText){
        this.image = image;
        this.image.setDebug(true);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = UndergroundBlasterGame.font2;
        abilityValue = new Label(labelText, labelStyle);
        abilityValue.setDebug(true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        image.draw(batch, parentAlpha);
        abilityValue.draw(batch, parentAlpha);
    }

    public void setActorBounds(float x, float y){
        setBounds(x,y, 100,120);
        image.setBounds(getX(), getY()+20,100,100);
        abilityValue.setSize(100,20);
        abilityValue.setPosition(getX(), getY());
        abilityValue.setAlignment(Align.center);

    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        image.setVisible(visible);
        abilityValue.setVisible(visible);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        image.act(delta);
        abilityValue.act(delta);
    }
    public void clicked(float delayTime){
        image.clearActions();
        abilityValue.clearActions();
        image.addAction(Actions.sequence(Actions.alpha(0), Actions.delay(delayTime), Actions.fadeIn(.5f)));
        abilityValue.addAction(Actions.sequence(Actions.alpha(0), Actions.delay(delayTime), Actions.fadeIn(.5f)));
    }

    public void setText(String labelText){
        abilityValue.setText(labelText);
    }

}
