package ktu.edu.sprites.enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;

import ktu.edu.UndergroundBlasterGame;
import ktu.edu.screens.PlayScreen;
import ktu.edu.sprites.Blaster;
import ktu.edu.sprites.Items.ItemDef;

public class Skeleton extends Enemy {

    public enum State {WALKING, STUN, ATTACKING, HURT, DYING, CALLING}
    private State currentState, previuosState;
    private int currentSide;
    private float stateTimer;
    private TextureAtlas atlas;
    private final Array<TextureRegion> hpTextures;
    private final float stunTime;
    private final Animation[] walking;
    private final Animation[] idle;
    private final Animation[] slashing;
    private final Animation[] hurt;
    private final Animation dying;

    private Vector2 currentMovingVector;

    private int slashIndex;

    public Skeleton(PlayScreen screen, Vector2 coord, float Hp, float damage, Type type, int dropRatio) {
        super(screen, coord.x, coord.y, dropRatio);

        switch (type){
            case BASIC:
                size = 54;
                speed = 0.55f;
                score = 25;
                currentType = Type.BASIC;
                break;
            case BOSS:
                size = 80;
                speed = 0.35f;
                score = 99;
                currentType = Type.BOSS;
                break;
            case MINION:
                size = 32;
                speed = 0.65f;
                score = 10;
                currentType = Type.MINION;
                break;
                default:
                    speed = 0.55f;
                    score = 25;
                    currentType = Type.BASIC;
                    break;
        }

        defineEnemy();
        stunTime = screen.game.stunTime;
        atlas  = screen.assets.manager.get("healthBar.pack", TextureAtlas.class);
        hpTextures = new Array<>();
        hpTextures.add(new TextureRegion(atlas.findRegion("background"),  0, 0 , 32, 8));
        hpTextures.add((new TextureRegion(atlas.findRegion("foreground"),  0, 0 , 32, 8)));
        hpTextures.add((new TextureRegion(atlas.findRegion("border"),  0, 0 , 32, 8)));
        maxHitpoints = Hp;
        this.damage = damage;
        currentHitpoints = maxHitpoints;

        currentState = previuosState = State.STUN;
        stateTimer = 0;

        currentSide = 0;

        atlas = screen.assets.manager.get("sprites/enemies/skeleton/skeleton.pack", TextureAtlas.class);
        walking = new Animation[4];
        walking[0] = createAnimation("walkingBack",3);
        walking[1] = createAnimation("walkingFront",3);
        walking[2] = createAnimation("walkingLeft",3);
        walking[3] = createAnimation("walkingRight",3);

        idle = new Animation[4];
        idle[0] = createAnimation("idleBack",2);
        idle[1] = createAnimation("idleFront",2);
        idle[2] = createAnimation("idleLeft",2);
        idle[3] = createAnimation("idleRight",2);

        slashing = new Animation[4];
        slashing[0] = createAnimation("slashBack");
        slashing[0].setPlayMode(Animation.PlayMode.LOOP);
        slashing[1] = createAnimation("slashFront");
        slashing[1].setPlayMode(Animation.PlayMode.LOOP);
        slashing[2] = createAnimation("slashLeft");
        slashing[2].setPlayMode(Animation.PlayMode.LOOP);
        slashing[3] = createAnimation("slashRight");
        slashing[3].setPlayMode(Animation.PlayMode.LOOP);

        hurt = new Animation[4];
        hurt[0] = createAnimation("hurtBack",2);
        hurt[1] = createAnimation("hurtFront",2);
        hurt[2] = createAnimation("hurtLeft",2);
        hurt[3] = createAnimation("hurtRight",2);

        dying = createAnimation("dying",2);

        setBounds(coord.x, coord.y, size / UndergroundBlasterGame.PPM, size / UndergroundBlasterGame.PPM);
        setRegion((TextureRegion) walking[0].getKeyFrame(0));
        b2body.setLinearVelocity(0,0.5f);

        currentMovingVector = new Vector2(0,0);

        setMovingVector(screen.blaster.b2body.getPosition());
        slashIndex = -1;
        b2body.setLinearVelocity(0,0);

    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(size / 4 / UndergroundBlasterGame.PPM);
        fdef.filter.categoryBits = UndergroundBlasterGame.ENEMY_BIT;
        fdef.filter.maskBits = UndergroundBlasterGame.BOMB_BIT |
                UndergroundBlasterGame.GROUND_BIT |
                UndergroundBlasterGame.HELPER_BIT |
                UndergroundBlasterGame.BLASTER_BIT |
                UndergroundBlasterGame.EXPLOSION_BIT |
                UndergroundBlasterGame.ENEMY_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    private TextureRegion getFrame(float dt) {
//        currentState = getState();
        currentSide = getSide();
        TextureRegion region;

        switch (currentState) {

            case WALKING:
                region = (TextureRegion) walking[currentSide].getKeyFrame(stateTimer, true);
                break;
            case STUN:
                region = (TextureRegion) idle[currentSide].getKeyFrame(stateTimer, true);
                break;
            case CALLING:
            case ATTACKING:
                region = (TextureRegion) slashing[currentSide].getKeyFrame(stateTimer);
                break;
            case HURT:
                region = (TextureRegion) hurt[currentSide].getKeyFrame(stateTimer);
                break;
            case DYING:
                default:
                region = (TextureRegion) dying.getKeyFrame(stateTimer);
                break;
        }
        stateTimer = currentState == previuosState ? stateTimer + dt : 0;
        previuosState = currentState;
        return region;
    }

//    private State getState(){
////        currentSide = getSide();
////        if(currentState == State.ATTACKING)
////            return State.ATTACKING;
////        if(currentState == State.HURT)
////            return State.HURT;
////        if(currentState == State.DYING)
////            return State.DYING;
////        if(currentState != State.HURT) {
////            if (b2body.getLinearVelocity().y != 0 || b2body.getLinearVelocity().x != 0)
////                return State.WALKING;
////        }
////        return State.STUN;
//    }

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

    @Override
    public void update(float dt, Vector2 vector) {
        if(currentState != State.DYING) {

            switch (currentState){
                case HURT:
                    if (hurt[currentSide].isAnimationFinished(stateTimer))
                        currentState = State.STUN;
                    else if (stateTimer > 0.2f)
                        b2body.setLinearVelocity(0, 0);
                    break;
                case STUN:
                    if (stateTimer > stunTime) {
                        currentState = State.WALKING;
                    }
                    break;
                case WALKING:
                    setMovingVector(vector);
                    if(currentType == Type.BOSS && stateTimer > 3){
                        callMinions();
                    }
                    break;
                case ATTACKING:
                    if (slashing[currentSide].getKeyFrameIndex(stateTimer) == 8 && slashing[currentSide].getKeyFrameIndex(stateTimer) != slashIndex) {
                        attack();
                    }
                    slashIndex = slashing[currentSide].getKeyFrameIndex(stateTimer);
                    break;
                case CALLING:
                    if(slashing[currentSide].isAnimationFinished(stateTimer)) {
                        currentState = State.WALKING;
                    }
                    break;
            }

        }else if(dying.isAnimationFinished(stateTimer))
            setToDestroy = true;
        else
            b2body.setLinearVelocity(0,0);

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
        }

    }

    private void setMovingVector(Vector2 movingVector){
        currentMovingVector = new Vector2(movingVector.x - b2body.getPosition().x, movingVector.y - b2body.getPosition().y);
        currentMovingVector.nor();
        b2body.setLinearVelocity(new Vector2(speed * currentMovingVector.x, speed * currentMovingVector.y));
    }

    public void draw(Batch batch){
            super.draw(batch);
            drawHitPointsBar(batch);
    }

    private void drawHitPointsBar(Batch batch){
        float width = getWidth() * 0.75f ;
        float height = 5 / UndergroundBlasterGame.PPM;
        float x = b2body.getPosition().x - width/2;
        float y  = b2body.getPosition().y + width/2;
        batch.draw(hpTextures.get(0),x,y, width, height);
        batch.draw(hpTextures.get(1),x,y, width * (currentHitpoints / maxHitpoints), height);
        batch.draw(hpTextures.get(2),x,y, width, height);
    }

    @Override
    public void goToStun(float damage){
        if(currentHitpoints - damage <= 0 ) {
            currentHitpoints = 0;
            currentState = State.DYING;
            dropItems();
        }
        else {
            currentHitpoints -= damage;
            currentState = State.HURT;

        }
        stateTimer = 0;
        b2body.setLinearVelocity(0,0);
    }

    @Override
    public void setToAttack(Blaster blaster){
        blaster.reduceHp((int)(damage / 2));
        if(currentState != State.STUN) {
            currentState = State.ATTACKING;
            b2body.setLinearVelocity(0, 0);
        }
    }

    @Override
    public void setToWalking(){
        currentState = State.WALKING;
    }

    private void attack(){
        screen.assets.manager.get("sounds/swordSlash.wav", Sound.class).play();
        screen.blaster.reduceHp((int)(damage - (damage *screen.blaster.armour)));
        stateTimer = 0;
    }

    private Animation createAnimation(String region, int row){
        Array<TextureRegion> frames = new Array<>();
        for(int i = 0; i < row; i++){
            for(int j = 0; j < 6; j++){
                frames.add(new TextureRegion(atlas.findRegion(region),j*256,i*256,256,256));
            }
        }
        return new Animation(1/30f, frames);
    }

    private Animation createAnimation(String region){
        Array<TextureRegion> frames = new Array<>();
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 6; j++){
                frames.add(new TextureRegion(atlas.findRegion(region),j*256,i*256,256,256));
            }
        }
        return new Animation(1/30f, frames);
    }

    private void callMinions(){
        screen.addEnemy(new Skeleton(screen, new Vector2(b2body.getPosition().x + 0.5f, b2body.getPosition().y +0.5f),  500, 200, Type.MINION,0));
        currentState = State.CALLING;
        stateTimer = 0;
    }
}
