package ktu.edu.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import ktu.edu.UndergroundBlasterGame;

public class shopIcon extends Actor {

    private final Image image;
    private final Label valueLabel;
    private final Label.LabelStyle greenStyle;

    public shopIcon(Image image, String value){
        this.image = image;
        setTouchable(Touchable.enabled);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = UndergroundBlasterGame.font2;
        valueLabel= new Label(value, labelStyle);

        greenStyle = new Label.LabelStyle();
        greenStyle.font = UndergroundBlasterGame.font3;

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        valueLabel.draw(batch, parentAlpha);
        image.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        image.act(delta);
        valueLabel.act(delta);
    }

    public void setActorBounds(float x, float y, float height){
        setBounds(x,y, 100,50);
        image.setBounds(getX(), getY()-height,50,50);
        valueLabel.setPosition(getX() + 60,y-height);
        valueLabel.setSize(50,50);
        valueLabel.setAlignment(Align.left);
    }

    private void setLabel(String value) {
        valueLabel.setText(value);
        valueLabel.setStyle(greenStyle);
    }

    public void useIcon(String value){
        setLabel(value);
        image.clearActions();
        image.setPosition(getX(), getY());
        image.addAction(Actions.sequence(Actions.parallel(Actions.scaleTo(1.5f,1.5f),Actions.moveBy(-image.getWidth()/4,-image.getHeight()/4)),
                Actions.parallel(Actions.scaleTo(1,1, 0.5f), Actions.moveBy(image.getWidth()/4,image.getHeight()/4,0.5f))));
    }
    public void transition(float height, float time){
        image.addAction(Actions.sequence(Actions.delay(time),Actions.moveBy(0,height, 2f, Interpolation.pow5Out)));
        valueLabel.addAction(Actions.sequence(Actions.delay(time),Actions.moveBy(0,height, 2f, Interpolation.pow5Out)));
    }


}
