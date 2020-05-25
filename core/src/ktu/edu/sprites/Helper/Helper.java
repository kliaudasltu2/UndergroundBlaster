package ktu.edu.sprites.Helper;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import ktu.edu.UndergroundBlasterGame;
import ktu.edu.screens.PlayScreen;

public abstract class Helper extends Sprite {

    final World world;
    final PlayScreen screen;

    // Helper STATE
    public enum State{RUNNING, WAVING, LEAVING}
    State currentState;
    private State previuosState;
    private int currentSide; // 0 - up; 1 - down; 2 - side;
    float stateTime;

    Body b2body;

    Vector2 currentMovingVector;
    final float speed;

    Animation[] walking;
    Animation[] waving;

    private boolean abilityClicked;
    final boolean canWalk;

    boolean setToDestroy;
    public boolean destroyed;

    Helper(PlayScreen screen, float x, float y, boolean canWalk){
        this.screen = screen;
        this.world = screen.getWorld();
        this.canWalk = canWalk;

        setToDestroy = false;
        destroyed = false;

        currentMovingVector = new Vector2(0,0);
        currentState = State.WAVING;
        speed = 1f;
        stateTime = 0;
        abilityClicked = false;
        currentSide = 1;
        currentMovingVector = new Vector2(1,-1);
        setPosition(x, y);

        if(screen.followBlaster) {
            screen.assets.manager.get("sounds/Helper2.wav", Sound.class).play(0.2f);
        }
    }

    void defineHelper() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(14 / UndergroundBlasterGame.PPM);
        fdef.filter.categoryBits = UndergroundBlasterGame.HELPER_BIT;
        fdef.filter.maskBits = UndergroundBlasterGame.BOMB_BIT | UndergroundBlasterGame.BLASTER_BIT | UndergroundBlasterGame.GROUND_BIT | UndergroundBlasterGame.HELPER_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    public  void contactWithBlaster(){
        if(!abilityClicked) {
            currentState = State.LEAVING;
            screen.abilities.addAbilities(this);
            //b2body.setActive(false);
            abilityClicked = true;
            stateTime = 0;
        }
    }

    public  void update(float delta, float x, float y){
        if(abilityClicked)
            b2body.setActive(false);

    }

    public void draw(Batch batch){
        if(!destroyed)
            super.draw(batch);

    }

    private State getState(){
        if(canWalk)
            currentSide = getSide();
        if(currentState != State.LEAVING) {
            if (b2body.getLinearVelocity().y != 0 || b2body.getLinearVelocity().x != 0)
                return State.RUNNING;
            else
                return State.WAVING;
        }
        return State.LEAVING;
    }

    private int getSide(){
        if (currentMovingVector.x > 0) {
            if (currentMovingVector.y > 0 && Math.abs(currentMovingVector.x) < Math.abs(currentMovingVector.y)) {
                return 0;
            } else if (currentMovingVector.y < 0 && Math.abs(currentMovingVector.x) < Math.abs(currentMovingVector.y)) {
                return 1;
            } else {
                return 3;
            }
        } else {
            if (currentMovingVector.y > 0 && Math.abs(currentMovingVector.x) < Math.abs(currentMovingVector.y)) {
                return 0;
            } else if (currentMovingVector.y < 0 && Math.abs(currentMovingVector.x) < Math.abs(currentMovingVector.y)) {
                return 1;
            } else {
                return 2;
            }
        }
    }

    TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;

        switch (currentState){

            case RUNNING:
                region = (TextureRegion)walking[currentSide].getKeyFrame(stateTime,true);
                break;
            case WAVING:
            case LEAVING:
            default:
                region = (TextureRegion)waving[currentSide].getKeyFrame(stateTime,true);
                break;
        }


        stateTime = currentState == previuosState ? stateTime + dt : 0;
        previuosState = currentState;
        return region;
    }
}
