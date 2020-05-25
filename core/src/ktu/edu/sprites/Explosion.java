package ktu.edu.sprites;

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

import ktu.edu.screens.PlayScreen;
import ktu.edu.UndergroundBlasterGame;
import ktu.edu.sprites.enemies.Enemy;

public class Explosion extends Sprite {

    private final Animation explosionAnimation;
    private float stateTimer;
    private boolean destroyed;

    private final PlayScreen screen;
    private final World world;

    private Body b2body;

    private final int damage;

   public Explosion(PlayScreen screen, float x, float y, float explosionSize, int damage){
       this.screen = screen;
       this.world = screen.getWorld();

       this.damage = damage;

       Array<TextureRegion> frames = new Array<>();
       for(int i = 0; i < 6; i++){
           for(int j = 0; j < 8; j++) {
               frames.add(new TextureRegion(screen.assets.manager.get("sprites/explosion.pack", TextureAtlas.class).findRegion("explosion"), j*128, i*128, 128, 128));
           }
       }

       explosionAnimation = new Animation(1/30f, frames);

       setRegion((TextureRegion) explosionAnimation.getKeyFrame(0));
       setBounds(x, y, explosionSize * 1.75f , explosionSize * 1.75f);

       defineExplosion(explosionSize);

       setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

   }

    private void defineExplosion(float radius){

        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        if(!world.isLocked())
            b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(radius / 2);

        fdef.filter.categoryBits = UndergroundBlasterGame.EXPLOSION_BIT;
        fdef.filter.maskBits = UndergroundBlasterGame.BLASTER_BIT |
                UndergroundBlasterGame.GROUND_BIT |
                UndergroundBlasterGame.ENEMY_BIT |
                UndergroundBlasterGame.BOMB_BIT;
        fdef.shape = shape;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

    }

    public void update(float dt) {
        stateTimer += dt;
        setRegion((TextureRegion) explosionAnimation.getKeyFrame(stateTimer, false));
        if (explosionAnimation.isAnimationFinished(stateTimer)) {
            world.destroyBody(b2body);
            destroyed = true;
        }
    }

    public boolean isDestroyed(){
        return destroyed;
    }

    public void onHit(Blaster blaster){
       blaster.hurt();
       blaster.reduceHp((int)(blaster.damage - (blaster.damage * blaster.armour)));
    }

    public void hitOnEnemey(Enemy enemy){
       Vector2 vector2 = new Vector2(enemy.b2body.getPosition().x - b2body.getPosition().x, enemy.b2body.getPosition().y - b2body.getPosition().y);
       vector2.nor();
           enemy.b2body.applyForceToCenter(new Vector2(vector2.x * 40,
                   vector2.y * 40), true);
           enemy.goToStun(damage);
    }

}
