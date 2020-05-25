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


public class ShopItem extends Actor {

    private final Image image;

    private String name;
    private int value;
    private final Label nameLabel;
    private final Label costLabel;


    public ShopItem(Image image){
        this.image = image;
        setTouchable(Touchable.enabled);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = UndergroundBlasterGame.font3;
        nameLabel= new Label(getItemName(), labelStyle);
        costLabel= new Label(getItemName(), labelStyle);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        costLabel.draw(batch, parentAlpha);
        nameLabel.draw(batch, parentAlpha);
        image.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        image.act(delta);
        nameLabel.act(delta);
        costLabel.act(delta);
    }

    public String getName(){
        return this.name;
    }

    public void setActorBounds(float x, float y, float height){
        setBounds(x,y, 120,160);
        image.setBounds(getX(), getY()+40 - height,120,120);

        nameLabel.setPosition(x,y+160 - height);
        nameLabel.setSize(120,20);
        nameLabel.setAlignment(Align.center);

        costLabel.setPosition(x,y+20 - height);
        costLabel.setSize(120,20);
        costLabel.setAlignment(Align.center);
    }

    private String getItemName() {
        return name;
    }

    public void setItemName(String name) {
        this.name = name;
        nameLabel.setText(name);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        this.costLabel.setText(value);
    }

    public Image getImage(){
        return image;
    }

    public void changeCostLabel(String text){
        costLabel.setText(text);
    }

    public void clicked(){
        image.clearActions();
        image.setPosition(getX(), getY()+40);
        image.addAction(Actions.sequence(Actions.parallel(Actions.scaleTo(1.5f,1.5f),Actions.moveBy(-30,-30)),
                                        Actions.parallel(Actions.scaleTo(1,1, 0.5f), Actions.moveBy(30,30,0.5f))));
    }

    public void transition(float height, float time){
        image.addAction(Actions.sequence(Actions.delay(time),Actions.moveBy(0,height, 2, Interpolation.pow5Out)));
        nameLabel.addAction(Actions.sequence(Actions.delay(time),Actions.moveBy(0,height, 2, Interpolation.pow5Out)));
        costLabel.addAction(Actions.sequence(Actions.delay(time),Actions.moveBy(0,height, 2, Interpolation.pow5Out)));
    }

    public void leaveStage(float height, float time){
        image.addAction(Actions.sequence(Actions.delay(time),Actions.moveBy(0,height, 2, Interpolation.pow5Out)));
        nameLabel.addAction(Actions.sequence(Actions.delay(time),Actions.moveBy(0,height, 2, Interpolation.pow5Out)));
        costLabel.addAction(Actions.sequence(Actions.delay(time),Actions.moveBy(0,height, 2, Interpolation.pow5Out)));
    }
}
