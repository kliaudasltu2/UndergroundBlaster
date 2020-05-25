package ktu.edu.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import ktu.edu.sprites.Blaster;
import ktu.edu.sprites.Bomb;
import ktu.edu.sprites.Explosion;
import ktu.edu.sprites.Helper.Helper;
import ktu.edu.UndergroundBlasterGame;
import ktu.edu.sprites.Items.Item;
import ktu.edu.sprites.enemies.Bullet;
import ktu.edu.sprites.enemies.Enemy;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            case UndergroundBlasterGame.BLASTER_BIT | UndergroundBlasterGame.EXPLOSION_BIT:
                if(fixA.getFilterData().categoryBits == UndergroundBlasterGame.BLASTER_BIT)
                    ((Explosion)fixB.getUserData()).onHit((Blaster)fixA.getUserData());
                 else
                    ((Explosion)fixA.getUserData()).onHit((Blaster)fixB.getUserData());
                break;
            case UndergroundBlasterGame.BLASTER_BIT | UndergroundBlasterGame.HELPER_BIT:
                if(fixA.getFilterData().categoryBits == UndergroundBlasterGame.BLASTER_BIT)
                   ((Helper)fixB.getUserData()).contactWithBlaster();
                else
                    ((Helper)fixA.getUserData()).contactWithBlaster();
                break;
            case UndergroundBlasterGame.ENEMY_BIT | UndergroundBlasterGame.EXPLOSION_BIT:
                if(fixA.getFilterData().categoryBits == UndergroundBlasterGame.ENEMY_BIT)
                    ((Explosion)fixB.getUserData()).hitOnEnemey((Enemy)fixA.getUserData());
                else
                    ((Explosion)fixA.getUserData()).hitOnEnemey((Enemy)fixB.getUserData());
                break;
            case UndergroundBlasterGame.EXPLOSION_BIT | UndergroundBlasterGame.BOMB_BIT:
                if(fixA.getFilterData().categoryBits == UndergroundBlasterGame.EXPLOSION_BIT) {
                    ((Bomb) fixB.getUserData()).setToExplode();
                }
                else{
                    ((Bomb) fixA.getUserData()).setToExplode();
                }
                break;
            case UndergroundBlasterGame.ENEMY_BIT | UndergroundBlasterGame.BOMB_BIT:
                if(fixA.getFilterData().categoryBits == UndergroundBlasterGame.ENEMY_BIT)
                    ((Bomb) fixB.getUserData()).setToExplode();
                else
                    ((Bomb) fixA.getUserData()).setToExplode();
                break;
            case UndergroundBlasterGame.ENEMY_BIT | UndergroundBlasterGame.BLASTER_BIT:
                if(fixA.getFilterData().categoryBits == UndergroundBlasterGame.ENEMY_BIT)
                    ((Enemy) fixA.getUserData()).setToAttack((Blaster)fixB.getUserData());
                else
                    ((Enemy) fixB.getUserData()).setToAttack((Blaster)fixA.getUserData());
                break;
            case UndergroundBlasterGame.BLASTER_BIT | UndergroundBlasterGame.TRIGGER_BIT:
                if(fixA.getFilterData().categoryBits == UndergroundBlasterGame.TRIGGER_BIT)
                    ((Blaster)fixB.getUserData()).onTrigger();
                else
                    ((Blaster)fixA.getUserData()).onTrigger();
                break;
            case UndergroundBlasterGame.ITEM_BIT | UndergroundBlasterGame.BLASTER_BIT:
                if(fixA.getFilterData().categoryBits == UndergroundBlasterGame.ITEM_BIT)
                    ((Item)fixA.getUserData()).destroy();
                else
                    ((Item)fixB.getUserData()).destroy();
                break;
            case UndergroundBlasterGame.GROUND_BIT | UndergroundBlasterGame.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == UndergroundBlasterGame.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity();
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity();
                break;
            case UndergroundBlasterGame.GROUND_BIT | UndergroundBlasterGame.BULLET_BIT:
                if(fixB.getFilterData().categoryBits == UndergroundBlasterGame.BULLET_BIT)
                    ((Bullet)fixB.getUserData()).setToDestroy();
                else
                    ((Bullet)fixA.getUserData()).setToDestroy();
                break;
            case UndergroundBlasterGame.BLASTER_BIT | UndergroundBlasterGame.BULLET_BIT:
                if(fixB.getFilterData().categoryBits == UndergroundBlasterGame.BULLET_BIT)
                    ((Bullet)fixB.getUserData()).doDamage((Blaster)fixA.getUserData());
                else
                    ((Bullet)fixA.getUserData()).doDamage((Blaster)fixB.getUserData());
                break;

        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case UndergroundBlasterGame.ENEMY_BIT | UndergroundBlasterGame.BLASTER_BIT:
                if(fixA.getFilterData().categoryBits == UndergroundBlasterGame.ENEMY_BIT)
                    ((Enemy) fixA.getUserData()).setToWalking();
                else
                    ((Enemy) fixB.getUserData()).setToWalking();
                break;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
