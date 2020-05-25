package ktu.edu.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import ktu.edu.UndergroundBlasterGame;
import ktu.edu.screens.PlayScreen;
import ktu.edu.sprites.Blaster;
import ktu.edu.sprites.Items.ItemDef;

public class Darkwitch extends Enemy {

    public enum State {WALKING, STUN, RUNNING, ATTACKING, HURT, DYING}
    private TextureAtlas atlas;
    private final Animation[] walking;
    private final Animation[] running;
    private final Animation[] idle;
    private final Animation[] attacking;
    private final Animation[] hurt;
    private final Animation[] dying;
    private final Array<TextureRegion> hpTextures;

    private State currentState, previousState;
    private Vector2 currentMovingVector;
    private int currentSide;
    private float stateTimer;

    private Array<Bullet> bullets;

    public Darkwitch(PlayScreen screen, Vector2 coord, Type type, int hp, int damage, int dropRatio) {
        super(screen, coord.x, coord.y, dropRatio);


        switch (type){
            case BOSS:
                size = 80;
                speed = 0.35f;
                score = 150;
                currentType = Type.BOSS;
                break;
                default:
                    size = 54;
                    speed = 0.55f;
                    score = 50;
                    currentType = Type.BASIC;
                    break;
        }

        atlas  = screen.assets.manager.get("healthBar.pack", TextureAtlas.class);
        hpTextures = new Array<>();
        hpTextures.add(new TextureRegion(atlas.findRegion("background"),  0, 0 , 32, 8));
        hpTextures.add((new TextureRegion(atlas.findRegion("foreground"),  0, 0 , 32, 8)));
        hpTextures.add((new TextureRegion(atlas.findRegion("border"),  0, 0 , 32, 8)));

        atlas = screen.assets.manager.get("sprites/enemies/darkwitch/Darkwitch.pack", TextureAtlas.class);

        walking = new Animation[4];
        walking[0] = createAnimation("WalkingBack",3,6, 1/30f);
        walking[1] = createAnimation("WalkingFront",3,6, 1/30f);
        walking[2] = createAnimation("WalkingLeft",3,6, 1/30f);
        walking[3] = createAnimation("WalkingRight",3,6, 1/30f);

        running = new Animation[4];
        running[0] = createAnimation("RunningBack",2,6, 1/30f);
        running[1] = createAnimation("RunningFront",2,6, 1/30f);
        running[2] = createAnimation("RunningLeft",2,6, 1/30f);
        running[3] = createAnimation("RunningRight",2,6, 1/30f);

        idle = new Animation[4];
        idle[0] = createAnimation("IdleBack",3,6, 1/30f);
        idle[1] = createAnimation("IdleFront",3,6, 1/30f);
        idle[2] = createAnimation("IdleLeft",3,6, 1/30f);
        idle[3] = createAnimation("IdleRight",3,6, 1/30f);

        hurt = new Animation[4];
        hurt[0] = createAnimation("HurtBack",2,6, 1/15f);
        hurt[1] = createAnimation("HurtFront",2,6, 1/15f);
        hurt[2] = createAnimation("HurtLeft",2,6, 1/15f);
        hurt[3] = createAnimation("HurtRight",2,6, 1/15f);

        attacking = new Animation[4];
        attacking[0] = createAnimation("AttackingBack",1,8);
        attacking[1] = createAnimation("AttackingFront",1,8);
        attacking[2] = createAnimation("AttackingLeft",1,8);
        attacking[3] = createAnimation("AttackingRight",1,8);

        dying = new Animation[4];
        dying[0] = createAnimation("DyingBack",1,6, 1/15f);
        dying[1] = createAnimation("DyingFront",1,6, 1/15f);
        dying[2] = createAnimation("DyingLeft",1,6, 1/15f);
        dying[3] = createAnimation("DyingRight",1,6, 1/15f);

        setBounds(coord.x, coord.y, size / UndergroundBlasterGame.PPM, size / UndergroundBlasterGame.PPM);
        setRegion((TextureRegion) walking[0].getKeyFrame(0));
        defineEnemy();
        b2body.setLinearVelocity(0,0);

        currentState = State.STUN;
        currentMovingVector = new Vector2(0,0);
        currentSide = 1;

        maxHitpoints = hp;
        currentHitpoints = maxHitpoints;
        this.damage = damage;

        b2body.setLinearVelocity(0,0);

        bullets = new Array<>();
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
                UndergroundBlasterGame.EXPLOSION_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void update(float dt, Vector2 vector) {
        if(currentState != State.DYING) {

            switch (currentState){
                case STUN:
                    if(stateTimer > 2)
                        setMovingVector();
                    break;
                case WALKING:
                    if(stateTimer > 3)
                        setAttackStyle(vector);
                case RUNNING:
                    if(stateTimer > 3)
                        setMovingVector();
                    break;
                case HURT:
                    if (hurt[currentSide].isAnimationFinished(stateTimer))
                        currentState = State.STUN;
                    else if (stateTimer > 0.2f)
                        b2body.setLinearVelocity(0, 0);
                    break;
                case ATTACKING:
                    if(attacking[currentSide].isAnimationFinished(stateTimer)) {
                        bullets.add(new Bullet(screen, b2body.getPosition(), vector, (int)damage));
                        if(currentType == Type.BOSS){
                            bullets.add(new Bullet(screen, b2body.getPosition(), new Vector2(vector.x + 0.8f, vector.y +0.8f), (int)damage));
                            bullets.add(new Bullet(screen, b2body.getPosition(), new Vector2(vector.x - 0.8f, vector.y -0.8f), (int)damage));
                        }
                        setMovingVector();
                    }
                    break;
            }
        }else if(dying[currentSide].isAnimationFinished(stateTimer))
            setToDestroy = true;
        else
            b2body.setLinearVelocity(0,0);

        for(Bullet bullet: bullets) {
            bullet.update(dt);
            if (bullet.isDestroyed())
                bullets.removeValue(bullet, true);
        }

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

        if(setToDestroy && !destroyed){
            screen.spawnItem(new ItemDef(b2body.getPosition()));
            world.destroyBody(b2body);
            destroyed = true;
        }
    }

    public void draw(Batch batch){
        super.draw(batch);
        drawHitPointsBar(batch);
        for(Bullet bullet: bullets)
            bullet.draw(batch);
    }

    private TextureRegion getFrame(float dt) {
//        currentState = getState();
        currentSide = getSide();
        TextureRegion region;

        switch (currentState) {

            case WALKING:
                region = (TextureRegion) walking[currentSide].getKeyFrame(stateTimer, true);
                break;
            case RUNNING:
                region = (TextureRegion) running[currentSide].getKeyFrame(stateTimer, true);
                break;
            case STUN:
                region = (TextureRegion) idle[currentSide].getKeyFrame(stateTimer, true);
                break;
            case ATTACKING:
                region = (TextureRegion) attacking[currentSide].getKeyFrame(stateTimer);
                break;
            case HURT:
                region = (TextureRegion) hurt[currentSide].getKeyFrame(stateTimer);
                break;
            case DYING:
            default:
                region = (TextureRegion) dying[currentSide].getKeyFrame(stateTimer);
                break;
        }
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    private int getSide(){
        if (b2body.getLinearVelocity().x > 0) {
            if (b2body.getLinearVelocity().y > 0 && Math.abs(b2body.getLinearVelocity().x) < Math.abs(b2body.getLinearVelocity().y)) {
                return 0;
            } else if (b2body.getLinearVelocity().y < 0 && Math.abs(b2body.getLinearVelocity().x) < Math.abs(b2body.getLinearVelocity().y)) {
                return 1;
            } else {
                return 3;
            }
        } else {
            if (b2body.getLinearVelocity().y > 0 && Math.abs(b2body.getLinearVelocity().x) < Math.abs(b2body.getLinearVelocity().y)) {
                return 0;
            } else if (b2body.getLinearVelocity().y < 0 && Math.abs(b2body.getLinearVelocity().x) < Math.abs(b2body.getLinearVelocity().y)) {
                return 1;
            } else {
                return 2;
            }
        }
    }

    @Override
    public void goToStun(float damage) {
        if(currentHitpoints - damage <= 0 ) {
            currentHitpoints = 0;
            currentState = State.DYING;
        }
        else {
            currentHitpoints -= damage;
            currentState = State.HURT;

        }
        stateTimer = 0;
        b2body.setLinearVelocity(0,0);
    }

    @Override
    public void setToAttack(Blaster blaster) {
        blaster.reduceHp((int)damage);
        reverseVelocity();
    }

    @Override
    public void setToWalking() {

    }

    private Animation createAnimation(String region, int row, int column, float frameRate){
        Array<TextureRegion> frames = new Array<>();
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                frames.add(new TextureRegion(atlas.findRegion(region),j*256,i*256,256,256));
            }
        }
        return new Animation(frameRate, frames);
    }

    private Animation createAnimation(String region, int row, int column){
        Array<TextureRegion> frames = new Array<>();
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                frames.add(new TextureRegion(atlas.findRegion(region),j*253,i*253,253,253));
            }
        }
        return new Animation(1/15f, frames);
    }

    private void setMovingVector(){

        currentState = State.WALKING;
        Random rand = new Random();

        float velocityX = rand.nextFloat();
        float velocityY = 1 - Math.abs(velocityX);

        if(rand.nextInt(2) == 1)
            velocityY *= -1;

        b2body.setLinearVelocity(velocityX * speed,velocityY * speed);
        stateTimer = 0;
    }

    private void setAttackStyle(Vector2 vector2){
        Random random = new Random();
        if(random.nextInt(2) == 1){
            runToBlaster(vector2);
        }else{
            shoot();
        }
    }
    private void runToBlaster(Vector2 vector2){
        currentState = State.RUNNING;
        stateTimer = 0;
        Vector2 currentMovingVector2 = new Vector2(vector2.x - b2body.getPosition().x, vector2.y - b2body.getPosition().y);
        currentMovingVector2.nor();
        b2body.setLinearVelocity(new Vector2(speed * 3 * currentMovingVector2.x, speed * 3 * currentMovingVector2.y));
    }

    private void shoot(){
        currentState = State.ATTACKING;
        stateTimer = 0;
        b2body.setLinearVelocity(0,0);
    }

    private void drawHitPointsBar(Batch batch){
        float width = getWidth() * 0.75f;
        float height = 5 / UndergroundBlasterGame.PPM;
        float x = b2body.getPosition().x - width/2;
        float y  = b2body.getPosition().y + width/2;
        batch.draw(hpTextures.get(0),x,y, width, height);
        batch.draw(hpTextures.get(1),x,y, width * (currentHitpoints / maxHitpoints), height);
        batch.draw(hpTextures.get(2),x,y, width, height);
    }
}
