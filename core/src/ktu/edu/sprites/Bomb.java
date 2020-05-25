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

import ktu.edu.screens.PlayScreen;
import ktu.edu.UndergroundBlasterGame;

public class Bomb extends Sprite {

    public enum State {STANDING, EXPLOSION}
    private State currentState;
    private State previousState;

    private final PlayScreen screen;
    private final World world;

    private final Animation bombAnimation;
    private float stateTimer;

    private final float explosionSize;

    private final Array<Explosion> explosions;

    private boolean setToDestroy;
    private boolean destroyed;
    private boolean explode;

    private Body b2body;
    private final int damage;

    public Bomb(PlayScreen screen, float x, float y, int damage, float explosionSize){
        this.screen = screen;
        this.world = screen.getWorld();
        this.damage = damage;
        TextureAtlas atlas = screen.assets.manager.get("sprites/explosion.pack", TextureAtlas.class);
        Array<TextureRegion> frames = new Array<>();
        for(int i = 0; i < 8; i++){
            frames.add(new TextureRegion(atlas.findRegion("bomb"),  0, (i*246) , 256, 246));
            if(i < 7) {
                frames.add(new TextureRegion(atlas.findRegion("bomb"), 258, (i * 246), 256, 246));
            }
        }
        bombAnimation = new Animation(1/15f, frames);

       explosions = new Array<>();

        setRegion((TextureRegion) bombAnimation.getKeyFrame(0));
        setBounds(x, y, 24 / UndergroundBlasterGame.PPM, 24 / UndergroundBlasterGame.PPM);
        defineBomb();
        currentState = State.STANDING;

        this.explosionSize = explosionSize;

        setToDestroy = false;
        destroyed = false;
        explode = false;

    }

    private void defineBomb(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        if(!world.isLocked())
            b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10 / UndergroundBlasterGame.PPM);

        fdef.filter.categoryBits = UndergroundBlasterGame.BOMB_BIT;
        fdef.filter.maskBits = UndergroundBlasterGame.BLASTER_BIT |
                UndergroundBlasterGame.GROUND_BIT |
                UndergroundBlasterGame.BOMB_BIT |
                UndergroundBlasterGame.ENEMY_BIT |
                UndergroundBlasterGame.EXPLOSION_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

    }

    private TextureRegion getFrame(float dt){

        TextureRegion region;

        switch (currentState){
            case EXPLOSION:
            case STANDING:

            default:
                region = (TextureRegion) bombAnimation.getKeyFrame(stateTimer, true);
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public void update(float dt){
        if(currentState == State.STANDING) {
            setRegion(getFrame(dt));
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        }
        if((stateTimer > 3 && currentState == State.STANDING) || explode)
            explode();

        if(currentState == State.EXPLOSION){
            for(Explosion  explosion : explosions) {
                explosion.update(dt);
                if(explosion.isDestroyed()) {
                    explosions.removeValue(explosion, true);
                }
            }
        }

        if(setToDestroy && !destroyed && !explode && explosions.size == 0){
            world.destroyBody(b2body);
            destroyed = true;
        }
    }

    public boolean isDestroyed(){
        return destroyed;
    }

    public void draw(Batch batch){
        //super.draw(batch);
        if(currentState == State.EXPLOSION) {
            for (Explosion explosion : explosions)
                explosion.draw(batch);
        }else
            super.draw(batch);
    }

    private void explosionGenerator(int explosionWidth, int explosionHight){

        explosions.add(new Explosion(screen,  b2body.getPosition().x,  b2body.getPosition().y, explosionSize / UndergroundBlasterGame.PPM, damage));

        explodeToright(explosionWidth,  b2body.getPosition().x,  b2body.getPosition().y);
        explodeToLeft(explosionWidth, b2body.getPosition().x, b2body.getPosition().y);
        explodeToDown(explosionHight, b2body.getPosition().x, b2body.getPosition().y);
        explodeToUp(explosionHight, b2body.getPosition().x, b2body.getPosition().y);


        explosions.reverse();
    }

    private void explodeToright(int explosionWidth, float x, float y){
        float allWidth  = explosionSize / 2 / UndergroundBlasterGame.PPM;
        float percent = 0.9f; // 1 = 100 %
        outerloop:
        for(int i = 1; i < explosionWidth; i++){
            float width = ((explosionSize / UndergroundBlasterGame.PPM)* percent);
            allWidth+= width/2;
            if(screen.creator.getObstacle(new Vector2(x + allWidth - (width/2), y), new Vector2(x + allWidth + (width / 5),y))) {
                break outerloop;

            }
            explosions.add(new Explosion(screen, x + allWidth, y, width,(int)(damage * percent)));
            allWidth += width/2;
            percent -= 0.1f;
        }
    }

    private void explodeToLeft(int explosionWidth, float x, float y){
        float allWidth  = explosionSize / 2 / UndergroundBlasterGame.PPM;
        float percent = 0.9f; // 1 = 100 %
        outerloop:
        for(int i = 1; i < explosionWidth; i++){
            float width = ((explosionSize / UndergroundBlasterGame.PPM)* percent);
            allWidth+= width/2;
            if(screen.creator.getObstacle(new Vector2(x - allWidth - (width/2), y), new Vector2(x - allWidth + (width / 5),y))) {
                break outerloop;

            }
            explosions.add(new Explosion(screen, x - allWidth, y, width,(int)(damage * percent)));
            allWidth += width/2;
            percent -= 0.1f;
        }
    }

    private void explodeToDown(int explosionHeight, float x, float y){
        float allHeight  = explosionSize / 2 / UndergroundBlasterGame.PPM;
        float percent = 0.9f; // 1 = 100 %
        outerloop:
        for(int i = 1; i < explosionHeight; i++){
            float height = ((explosionSize / UndergroundBlasterGame.PPM)* percent);
            allHeight+= height/2;
            if(screen.creator.getObstacle(new Vector2(x, y + allHeight - (height/2)), new Vector2(x ,y+ allHeight + (height / 5))))
                break outerloop;
            explosions.add(new Explosion(screen, x, y  + allHeight, height,(int)(damage * percent)));
            allHeight += height/2;
            percent -= 0.1f;
        }
    }

    private void explodeToUp(int explosionHeight, float x, float y){
        float allHeight  = explosionSize / 2 / UndergroundBlasterGame.PPM;
        float percent = 0.9f; // 1 = 100 %
        outerloop:
        for(int i = 1; i < explosionHeight; i++){
            float height = ((explosionSize / UndergroundBlasterGame.PPM)* percent);
            allHeight+= height/2;
            if(screen.creator.getObstacle(new Vector2(x, y - allHeight - (height/2)), new Vector2(x ,y- allHeight + (height / 5))))
                break outerloop;
            explosions.add(new Explosion(screen, x, y  - allHeight, height,(int)(damage * percent)));
            allHeight += height/2;
            percent -= 0.1f;
        }
    }

    public void setToExplode(){
        explode = true;
    }

    private void explode(){
        currentState = State.EXPLOSION;
        b2body.setActive(false);
        explosionGenerator(screen.blaster.explosionToRightAndLeft,screen.blaster.explosionToUpAndDown);
        explode = false;
        setToDestroy = true;
        screen.assets.manager.get("sounds/explosion.wav", Sound.class).play(.5f);
    }

}
