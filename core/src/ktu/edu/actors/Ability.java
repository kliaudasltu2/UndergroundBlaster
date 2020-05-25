package ktu.edu.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

public class Ability extends Actor {

    private final Image image;
    private final String name;
    private final Label abilityName;
    private final String description;

    public Ability(Image image, String powerName, String labelText, String description){
        Skin skin = new Skin(Gdx.files.internal("ui/shadow/uiskin.json"));
        this.image = image;
        this.name = powerName;
        //this.image.setDebug(true);
        this.description = description;
        abilityName = new Label(labelText, skin, "customtitle2");
        //abilityName.setDebug(true);
        setTouchable(Touchable.enabled);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        image.draw(batch, parentAlpha);
        abilityName.draw(batch, parentAlpha);
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public Image getImage() {
        return image;
    }

    public String getAbilityName() {
        return abilityName.getText().toString();
    }

    public void setActorBounds(float x, float y){
        setBounds(x,y, 200,220);
        image.setBounds(getX(), getY(),200,200);
        abilityName.setSize(200,20);
        abilityName.setPosition(getX(), getY()+200);
        abilityName.setAlignment(Align.center);

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        image.act(delta);

    }


}
