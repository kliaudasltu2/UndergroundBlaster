package ktu.edu.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
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

import ktu.edu.UndergroundBlasterGame;
import ktu.edu.screens.PlayScreen;
import ktu.edu.sprites.Blaster;

public class Bullet extends Sprite {

    private PlayScreen screen;
    private World world;
    public Body b2body;
    private boolean destroyed;
    private boolean setToDestroy;
    private Animation flying;
    private float stateTime;
    private int damage;

    public Bullet(PlayScreen screen, Vector2 coordinates, Vector2 target, int damage){
        this.screen = screen;
        this.world = screen.getWorld();
        this.damage = damage;

        TextureAtlas atlas = screen.assets.manager.get("sprites/enemies/darkwitch/Darkwitch.pack", TextureAtlas.class);

        Array<TextureRegion> frames = new Array<>();
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                frames.add(new TextureRegion(atlas.findRegion("Orb"),j*91,i*103,91,103));
            }
        }
        flying = new Animation(1/15f, frames);

        setRegion((TextureRegion) flying.getKeyFrame(0));
        setBounds(coordinates.x, coordinates.y, 32 / UndergroundBlasterGame.PPM, 32 / UndergroundBlasterGame.PPM);
        defineBullet();

        Vector2 currentMovingVector2 = new Vector2(target.x - b2body.getPosition().x, target.y - b2body.getPosition().y);
        currentMovingVector2.nor();
        b2body.setLinearVelocity(new Vector2( 1.75f * currentMovingVector2.x, 1.75f *  currentMovingVector2.y));
        stateTime = 0;
    }

    private void defineBullet() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(8 / UndergroundBlasterGame.PPM);
        fdef.filter.categoryBits = UndergroundBlasterGame.BULLET_BIT;
        fdef.filter.maskBits = UndergroundBlasterGame.BLASTER_BIT |
                UndergroundBlasterGame.GROUND_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void update(float dt){
        stateTime += dt;
        setRegion((TextureRegion) flying.getKeyFrame(stateTime, true));
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        if( setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }
    }

    public void setToDestroy(){
        setToDestroy = true;
    }

    public void doDamage(Blaster blaster){
        blaster.reduceHp(damage);
        setToDestroy();
    }

    public boolean isDestroyed(){
        return destroyed;
    }
}
