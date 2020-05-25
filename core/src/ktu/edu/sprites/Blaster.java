package ktu.edu.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import ktu.edu.managers.GameScreenManager;
import ktu.edu.scenes.Hud;
import ktu.edu.screens.PlayScreen;
import ktu.edu.UndergroundBlasterGame;

public class Blaster extends Sprite {
    // STATE
    public enum State{ STANDING, RUNNING, HURT, DEAD}
    public State currentState;
    private State previuosState;
    private int currentSide; // 0 - up; 1 - down; 2 - left; 3 - right;

    //Box2Dvariable
    private final World world;
    public Body b2body;

    //Animation
    private TextureAtlas atlas;
    private final Animation [] runningAnimation;
    private final Animation [] idleAnimation;
    private final Animation [] hurtAnimation;
    private final Animation [] dieingAnimation;

    //Skills and abilities
    public float maxHp;
    public float currentHp;
    public int bombCount;
    public int damage;
    public int explosionToRightAndLeft;
    public int explosionToUpAndDown;
    private float explosionSize;
    public float speed;
    public float armour;


    //HealBar
    private final Array<TextureRegion> hpTextures;


    private float stateTimer;
    private int stepIndex;

    private final Array<Bomb> bombs;

    private final PlayScreen screen;

    public Blaster(PlayScreen screen, float x, float y){
        this.screen = screen;
        this.world = screen.getWorld();
        stateTimer = 0;
        stepIndex = 0;

        initSkills();

        atlas = screen.assets.manager.get("sprites/Blaster/Blaster.pack", TextureAtlas.class);
        runningAnimation = new Animation[4];
        runningAnimation[1] = createAnimation2("runningFront");
        runningAnimation[1].setPlayMode(Animation.PlayMode.LOOP);
        runningAnimation[0] = createAnimation2("runningBack");
        runningAnimation[0].setPlayMode(Animation.PlayMode.LOOP);
        runningAnimation[2] = createAnimation2("runningLeft");
        runningAnimation[2].setPlayMode(Animation.PlayMode.LOOP);
        runningAnimation[3] = createAnimation2("runningRight");
        runningAnimation[3].setPlayMode(Animation.PlayMode.LOOP);

        idleAnimation = new Animation[4];
        idleAnimation[1] = createAnimation("idleFront");
        idleAnimation[0] = createAnimation("idleBack");
        idleAnimation[2] = createAnimation("idleLeft");
        idleAnimation[3] = createAnimation("idleRight");


        hurtAnimation = new Animation[4];
        hurtAnimation[1] = createAnimation2("hurtFront");
        hurtAnimation[0] = createAnimation2("hurtBack");
        hurtAnimation[2] = createAnimation2("hurtLeft");
        hurtAnimation[3] = createAnimation2("hurtRight");

        dieingAnimation = new Animation[4];
        dieingAnimation[1] = createAnimation("diedFront");
        dieingAnimation[0] = createAnimation("diedBack");
        dieingAnimation[2] = createAnimation("diedLeft");
        dieingAnimation[3] = createAnimation("diedRight");

        atlas  = screen.assets.manager.get("healthBar.pack", TextureAtlas.class);
        hpTextures = new Array<>();
        hpTextures.add(new TextureRegion(atlas.findRegion("background"),  0, 0 , 32, 8));
        hpTextures.add((new TextureRegion(atlas.findRegion("foreground"),  0, 0 , 32, 8)));
        hpTextures.add((new TextureRegion(atlas.findRegion("border"),  0, 0 , 32, 8)));

        currentState = State.STANDING;
        currentSide = 3;

        defineBlaster(x, y);
        setBounds(0,0,64 / UndergroundBlasterGame.PPM, 64 / UndergroundBlasterGame.PPM);
        setRegion((TextureRegion)idleAnimation[0].getKeyFrame(0));

        bombs = new Array<>();
        maxHp = screen.game.maxHp;
        currentHp = maxHp*0.75f;
        damage = screen.game.damage;
    }

    private void defineBlaster(float x, float y){
        BodyDef bdef = new BodyDef();
        bdef.position.set(x , y);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(14 / UndergroundBlasterGame.PPM);
        fdef.filter.categoryBits = UndergroundBlasterGame.BLASTER_BIT;
        fdef.filter.maskBits = UndergroundBlasterGame.BOMB_BIT |
                UndergroundBlasterGame.EXPLOSION_BIT |
                UndergroundBlasterGame.GROUND_BIT |
                UndergroundBlasterGame.HELPER_BIT |
                UndergroundBlasterGame.ENEMY_BIT |
                UndergroundBlasterGame.TRIGGER_BIT |
                UndergroundBlasterGame.ITEM_BIT |
                UndergroundBlasterGame.BULLET_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    private State getState(){
        if(currentState != State.DEAD) {
            if (currentState != State.HURT || hurtAnimation[currentSide].isAnimationFinished(stateTimer)) {

                if (b2body.getLinearVelocity().y != 0 || b2body.getLinearVelocity().x != 0)
                    return State.RUNNING;
                else
                    return State.STANDING;
            }
            return State.HURT;
        } return State.DEAD;
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

    private TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;

        switch (currentState){
            case RUNNING:
                currentSide = getSide();
                region = (TextureRegion) runningAnimation[currentSide].getKeyFrame(stateTimer, true);
                break;
            case HURT:
                region = (TextureRegion) hurtAnimation[currentSide].getKeyFrame(stateTimer, false);
                break;
            case DEAD:
                region = (TextureRegion) dieingAnimation[currentSide].getKeyFrame(stateTimer, false);
                break;
            case STANDING:

                //break;
            default:
                region = (TextureRegion) idleAnimation[currentSide].getKeyFrame(stateTimer, true);
        }

        stateTimer = currentState == previuosState ? stateTimer + dt : 0;
        previuosState = currentState;
        return region;
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight()/2);
        setRegion(getFrame(dt));

        if(currentState == State.RUNNING && (runningAnimation[currentSide].getKeyFrameIndex(stateTimer) != stepIndex )){
            if(runningAnimation[currentSide].getKeyFrameIndex(stateTimer) == 0 || runningAnimation[currentSide].getKeyFrameIndex(stateTimer) == 8){
                screen.assets.manager.get("sounds/footStep3.wav", Sound.class).play(.5f);
                stepIndex = runningAnimation[currentSide].getKeyFrameIndex(stateTimer);
            }
        }

        for(Bomb  bomb : bombs) {
            bomb.update(dt);
            if(bomb.isDestroyed())
                bombs.removeValue(bomb, true);
        }

        if(currentState == State.DEAD &&  dieingAnimation[currentSide].isAnimationFinished(stateTimer)){
            screen.game.coins += Hud.coins;
            screen.addCoinsToStorage();
            screen.game.gsm.setScreen(GameScreenManager.STATE.GAMEOVER);
        }
    }

    public void setMovingVector(Vector2 movingVector){
        movingVector = movingVector.nor();
        b2body.setLinearVelocity(new Vector2(speed * -movingVector.x, speed * -movingVector.y));
    }

    public void resetMovingVector(){
        b2body.setLinearVelocity(0,0);
    }

    public void boom(){
        if(bombCount > bombs.size) {
            int y = 0, x = 0;

            switch (currentSide) {
                case 0:
                    y = -24;
                    break;
                case 1:
                    y = 24;
                    break;
                case 2:
                    x = 24;
                    break;
                case 3:
                    x = -24;
                    break;
            }
            screen.assets.manager.get("sounds/dropTheBomb.wav", Sound.class).play();
            bombs.add(new Bomb(screen, b2body.getPosition().x + x / UndergroundBlasterGame.PPM, b2body.getPosition().y + y / UndergroundBlasterGame.PPM, damage, explosionSize));
        }
    }

    public void draw(Batch batch){
        super.draw(batch);
        for(Bomb bomb : bombs)
            bomb.draw(batch);
        drawHitPointsBar(batch);
    }

    private void drawHitPointsBar(Batch batch){
        float x = b2body.getPosition().x - getWidth() / 4;
        float y  = b2body.getPosition().y + getHeight() / 3;
        float width = getWidth() /2;
        float height = 5 / UndergroundBlasterGame.PPM;
        batch.draw(hpTextures.get(0),x,y, width, height);
        batch.draw(hpTextures.get(1),x,y, width * (currentHp / maxHp), height);
        batch.draw(hpTextures.get(2),x,y, width, height);
    }

    public void reduceHp(int hp){
//        if(currentState != State.DEAD) {
//            if (currentHp - hp >= 0) {
//                currentHp -= hp;
//                hurt();
//            } else {
//                currentHp = 0;
//                currentState = State.DEAD;
//            }
//            screen.getHud().updateHp();
//        }
    }

    private Animation createAnimation(String region){
        Array<TextureRegion> frames = new Array<>();
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 6; j++){
                //TextureRegion frame = new TextureRegion(atlas.findRegion(region),j*175,i*175,175,175);
                frames.add(new TextureRegion(atlas.findRegion(region),j*330,i*330,330,330));
            }
        }
        return new Animation(1/30f, frames);
    }

    private Animation createAnimation2(String region){
        Array<TextureRegion> frames = new Array<>();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 5; j++){
                frames.add(new TextureRegion(atlas.findRegion(region),j*330,i*330,330,330));
            }
        }
        return new Animation(1/30f, frames);
    }

    public void hurt(){
        currentState = Blaster.State.HURT;
        stateTimer = 0;
        screen.assets.manager.get("sounds/Hurt.ogg",Sound.class).play();
    }

    private void initSkills(){
        maxHp = screen.game.maxHp;
        currentHp = maxHp;
        bombCount = 1;
        damage = screen.game.damage;
        explosionToRightAndLeft = 1;
        explosionToUpAndDown = 1;
        speed = 0.8f;
        explosionSize = screen.game.explosionSize;
        armour = screen.game.armour;

    }

    public void upgrade(String powerName){
        System.out.println(powerName.toLowerCase());
        float i;
        switch (powerName.toLowerCase()){
            case "bombcount":
                bombCount++;
                break;
            case "heal":
                if(currentHp + screen.game.heal <= maxHp)
                    currentHp += screen.game.heal;
                else
                    currentHp = maxHp;
                screen.getHud().updateHp();
                break;
            case "increasemaxhp":
                i = maxHp * .1f;
                maxHp += i ;
                currentHp += i;
                screen.getHud().updateHp();
                break;
            case "increasemaxhp2":
                 i = maxHp * .3f;
                maxHp += i ;
                currentHp += i;
                screen.getHud().updateHp();
                break;
            case "explosionside" :
                explosionToUpAndDown++;
                damage *= 0.9f;
                break;
            case "explosionupdown":
                explosionToRightAndLeft++;
                damage *= 0.9f;
                break;
            case "coin50":
                Hud.addCoins(50);
                break;
            case "coin100":
                Hud.addCoins(100);
                break;
            case "coin300":
                Hud.addCoins(300);
                break;
            case "damage10":
                damage = (int)(damage * 1.1);
                break;
            case "damage30":
                damage = (int)(damage * 1.3);
                break;
            case "stun":
                screen.stunTime *= 1.2f;
                break;
            case "armour10":
                armour = armour + 0.05f;
                break;
            case "armour30":
                armour = armour + 0.15f;
                break;
            case "speed":
                speed += 0.1f;
                break;
        }
    }

    public void onTrigger(){
        screen.setToFollowBlaster();
        screen.creator.activated = true;
    }

}
