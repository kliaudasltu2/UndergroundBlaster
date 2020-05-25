package ktu.edu.sprites.Items;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;

import ktu.edu.UndergroundBlasterGame;
import ktu.edu.screens.PlayScreen;

public class GoldCoin extends  Item{

    private final Animation animation;

    public GoldCoin(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        //setRegion();
        Array<TextureRegion> frames1 = new Array<>();
        TextureAtlas atlas = screen.assets.manager.get("sprites/item/gold.pack", TextureAtlas.class);

        //TextureRegion frame = new TextureRegion(atlas.findRegion("gold", 30));
        Array<TextureRegion> frames = new Array<>();
        for(int i = 21; i <= 30; i++){
            frames.add(new TextureRegion(atlas.findRegion("Gold",i)));
        }
        animation = new Animation(1/15f, frames);

        setRegion((TextureRegion) animation.getKeyFrame(0));
        setBounds(x, y, 10 / UndergroundBlasterGame.PPM, 10 / UndergroundBlasterGame.PPM);
        setPosition(x,y);

        value = 10;

    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.KinematicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / UndergroundBlasterGame.PPM);
        fdef.filter.categoryBits = UndergroundBlasterGame.ITEM_BIT;
        fdef.filter.maskBits = UndergroundBlasterGame.BLASTER_BIT;
        fdef.isSensor = true;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }


    @Override
    public void update(float dt, Vector2 movingVector) {
        super.update(dt, movingVector);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) animation.getKeyFrame(stateTimer, true));
    }
}
