package ktu.edu.sprites.Helper;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ktu.edu.screens.PlayScreen;
import ktu.edu.UndergroundBlasterGame;
import ktu.edu.sprites.Items.ItemDef;

public class Astrologer extends Helper {

    private final TextureAtlas atlas;


    public Astrologer(PlayScreen screen,float x, float y, boolean canwalk){
        super(screen,x,y, canwalk);

        atlas = screen.assets.manager.get("sprites/Helper/Helpers.pack", TextureAtlas.class);

        walking = new Animation[4];
        walking[0] = createAnimation("aWalkBack", false);
        walking[1] = createAnimation("aWalkFront", false);
        walking[2] = createAnimation("aWalkSide", false);
        walking[3] = createAnimation("aWalkSide", true);

        waving = new Animation[4];
        waving[0] = createAnimation("aGreetingsBack", false);
        waving[1] = createAnimation("aGreetingsFront", false);
        waving[2] = createAnimation("aGreetingsSide", false);
        waving[3] = createAnimation("aGreetingsSide", true);

        setRegion((TextureRegion) walking[0].getKeyFrame(0));
        setBounds(x, y, 40 / UndergroundBlasterGame.PPM, 40 / UndergroundBlasterGame.PPM);

        defineHelper();

    }

    @Override
    public void update(float delta, float x, float y) {

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        if (canWalk) {
            currentMovingVector = new Vector2(x - b2body.getPosition().x, y - b2body.getPosition().y);
            currentMovingVector.nor();
            if (currentState == State.WAVING && stateTime > 2)
                currentState = State.RUNNING;

            if (currentState == State.RUNNING)
                b2body.setLinearVelocity(new Vector2(speed * currentMovingVector.x, speed * currentMovingVector.y));
        }

        setRegion(getFrame(delta));

        if (currentState == State.LEAVING && stateTime > 2) {
            setToDestroy = true;
        }

        super.update(delta, x, y);

        if(setToDestroy && !destroyed){
            screen.spawnItem(new ItemDef(b2body.getPosition()));
            world.destroyBody(b2body);
            destroyed = true;
        }


    }

    private Animation createAnimation(String region, boolean flip){
        Array<TextureRegion> frames = new Array<>();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 10; j++){
                TextureRegion frame = new TextureRegion(atlas.findRegion(region),j*175,i*175,175,175);
                if(flip)
                    frame.flip(true, false);
                frames.add(frame);
            }
        }
        return new Animation(1/30f, frames);
    }

}
