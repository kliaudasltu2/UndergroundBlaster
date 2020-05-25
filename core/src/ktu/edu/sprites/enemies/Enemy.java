package ktu.edu.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import ktu.edu.UndergroundBlasterGame;
import ktu.edu.screens.PlayScreen;
import ktu.edu.sprites.Blaster;
import ktu.edu.sprites.Items.ItemDef;

public abstract class Enemy extends Sprite {

    final World world;
    final PlayScreen screen;
    public Body b2body;
    float speed;
    float maxHitpoints;
    float currentHitpoints;
    boolean setToDestroy;
    public boolean destroyed;
    float size;
    float damage;
    int dropRatio;
    public int score;

    public enum Type {BASIC, BOSS, MINION}
    Type currentType;

    Enemy(PlayScreen screen, float x, float y, int dropRatio) {
        this.world = screen.getWorld();
        this.screen = screen;
        this.dropRatio = dropRatio;
        setPosition(x, y);
    }

    protected abstract void defineEnemy();

    public abstract void update(float dt, Vector2 vector);

    public abstract void goToStun(float damage);

    public abstract void setToAttack(Blaster blaster);

    public abstract void setToWalking();

    public  void dropItems(){
        //Random random = new Random();
        for(int i = 0; i < dropRatio; i++) {
//            screen.spawnItem(new ItemDef(new Vector2(b2body.getPosition().x + ((random.nextInt(50)-25)/ UndergroundBlasterGame.PPM),
//                                                     b2body.getPosition().y + ((random.nextInt(50)-25)/ UndergroundBlasterGame.PPM))));
            screen.spawnItem(new ItemDef(b2body.getPosition()));
        }
    }

    public void reverseVelocity() {
        if (this instanceof Darkwitch ) {
              b2body.setLinearVelocity(b2body.getLinearVelocity().x * -1, b2body.getLinearVelocity().y * -1);
        }
    }

}
