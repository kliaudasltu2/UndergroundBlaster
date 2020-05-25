package ktu.edu.sprites.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import ktu.edu.scenes.Hud;
import ktu.edu.screens.PlayScreen;
import ktu.edu.UndergroundBlasterGame;
import ktu.edu.sprites.enemies.Darkwitch;

public abstract class Item extends Sprite {

    enum State {APPEAR, MOVING}
    private State currenState;
    final World world;
    private boolean toDestroy;
    public boolean destroyed;
    Body body;
    int value;
    private float speed;
    public boolean move;
    float stateTimer;

    Item(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        toDestroy = false;
        destroyed = false;

        setPosition(x, y);
        setBounds(getX(), getY(), 16 / UndergroundBlasterGame.PPM, 16 / UndergroundBlasterGame.PPM);
        defineItem();
        speed = 0;
        move = false;
        stateTimer = 0;
        setMovingVector();

    }

    protected abstract void defineItem();
    public  void use(){
        toDestroy = true;
    }

    public void update(float dt, Vector2 movingVector){
        stateTimer += dt;
        if(currenState == State.APPEAR && stateTimer > 0.5f)
            body.setLinearVelocity(0,0);

        if(currenState == State.MOVING)
            setLinearVelocity(movingVector);

        if(toDestroy && !destroyed){
            world.destroyBody(body);
            destroyed = true;
        }
    }

    private void setLinearVelocity(Vector2 vector){
        speed += .03f;
        Vector2 currentMovingVector = new Vector2(vector.x - body.getPosition().x, vector.y - body.getPosition().y);
        currentMovingVector.nor();
        body.setLinearVelocity(new Vector2(speed * currentMovingVector.x, speed * currentMovingVector.y));

    }

    public void draw(Batch batch){
        if(!destroyed)
            super.draw(batch);
    }

    public void destroy(){
        Hud.addCoins(value);
        toDestroy = true;
    }

    private void setMovingVector(){

        currenState = State.APPEAR;
        Random rand = new Random();
        float velocityX = ((rand.nextInt(10) % 10) *0.1f);
        System.out.println(velocityX);
        float velocityY = 1 - Math.abs(velocityX);

        if(rand.nextInt(2) ==1 )
            velocityX *= -1;
        if(rand.nextInt(2) == 1)
            velocityY *= -1;

        body.setLinearVelocity(velocityX * 0.75f,velocityY * 0.75f);


    }

    public void setMovable(){
        currenState = State.MOVING;
    }
}
