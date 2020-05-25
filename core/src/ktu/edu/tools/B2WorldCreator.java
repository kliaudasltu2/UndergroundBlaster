package ktu.edu.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import ktu.edu.UndergroundBlasterGame;

public class B2WorldCreator {

    private final Array<Vector2> enemiesSpot;
    private final Array<Vector2> helpersSpot;
    private Vector2 blasterSpot;
    private Vector2 midleOfRoom;
    private final Random random;
    private final Array<Rectangle> hallObstacles;
    private final Array<Rectangle> arenaObstacles;

    public boolean activated;

    public B2WorldCreator(World world, TiledMap map){
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        random = new Random();
        enemiesSpot = new Array<>();
        midleOfRoom = new Vector2();
        blasterSpot = new Vector2();
        helpersSpot = new Array<>();
        hallObstacles = new Array<>();
        arenaObstacles = new Array<>();
        activated = false;

        //create ground bodies
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rectangle.getX() + rectangle.getWidth()/2)/ UndergroundBlasterGame.PPM, (rectangle.getY() + rectangle.getHeight()/2)/ UndergroundBlasterGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox((rectangle.getWidth() / 2)/ UndergroundBlasterGame.PPM, (rectangle.getHeight() / 2)/ UndergroundBlasterGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);

            arenaObstacles.add(new Rectangle(rectangle.getX() / UndergroundBlasterGame.PPM,
                                             rectangle.getY() / UndergroundBlasterGame.PPM,
                                          rectangle.getWidth() / UndergroundBlasterGame.PPM ,
                                         rectangle.getHeight() / UndergroundBlasterGame.PPM));
        }

        //create obstacles;
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rectangle.getX() + rectangle.getWidth()/2)/ UndergroundBlasterGame.PPM, (rectangle.getY() + rectangle.getHeight()/2)/ UndergroundBlasterGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox((rectangle.getWidth() / 2)/ UndergroundBlasterGame.PPM, (rectangle.getHeight() / 2)/ UndergroundBlasterGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);

            hallObstacles.add(new Rectangle(rectangle.getX() / UndergroundBlasterGame.PPM, rectangle.getY() / UndergroundBlasterGame.PPM, rectangle.getWidth() / UndergroundBlasterGame.PPM , rectangle.getHeight() / UndergroundBlasterGame.PPM));



        }

        for(MapObject object: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect =((RectangleMapObject) object).getRectangle();

            enemiesSpot.add(new Vector2(rect.getX() / UndergroundBlasterGame.PPM, rect.getY() / UndergroundBlasterGame.PPM));
        }

        for(MapObject object: map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle =((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rectangle.getX() + rectangle.getWidth()/2)/ UndergroundBlasterGame.PPM, (rectangle.getY() + rectangle.getHeight()/2)/ UndergroundBlasterGame.PPM);
            body = world.createBody(bdef);

            shape.setAsBox((rectangle.getWidth() / 2)/ UndergroundBlasterGame.PPM, (rectangle.getHeight() / 2)/ UndergroundBlasterGame.PPM);
            fdef.filter.categoryBits = UndergroundBlasterGame.TRIGGER_BIT;
            fdef.filter.maskBits = UndergroundBlasterGame.BLASTER_BIT;
            fdef.shape = shape;
            fdef.isSensor = true;
            body.createFixture(fdef).setUserData(this);
        }

        for(MapObject object: map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect =((RectangleMapObject) object).getRectangle();

           blasterSpot = (new Vector2((rect.getX() / UndergroundBlasterGame.PPM) + (rect.getWidth()/2 / UndergroundBlasterGame.PPM), ((10 / UndergroundBlasterGame.PPM)+rect.getY() / UndergroundBlasterGame.PPM )+ ( rect.getHeight()/2 / UndergroundBlasterGame.PPM)));
        }

        for(MapObject object: map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect =((RectangleMapObject) object).getRectangle();

            midleOfRoom = (new Vector2(rect.getX() / UndergroundBlasterGame.PPM, rect.getY() / UndergroundBlasterGame.PPM));
        }

        for(MapObject object: map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect =((RectangleMapObject) object).getRectangle();

            helpersSpot.add(new Vector2(rect.getX() / UndergroundBlasterGame.PPM, rect.getY() / UndergroundBlasterGame.PPM));
        }
    }

    public Array<Vector2> getEnemiesSpots(){
        return enemiesSpot;
    }

    public Vector2 getBlasterSpot(){
        return blasterSpot;
    }

    public Vector2 getmiddleOfHall(){
        return midleOfRoom;
    }


    public Array<Vector2> getHelperSpots(){
        return helpersSpot;
    }

    public Vector2 getHelperSpot(){
        return  enemiesSpot.get(random.nextInt(enemiesSpot.size));
    }

    public boolean getObstacle(Vector2 start, Vector2 end) {
        if(!activated) {
            for (int i = 0; i < hallObstacles.size; i++) {
                if (Intersector.intersectSegmentRectangle(start, end, hallObstacles.get(i))) {
                    return true;
                }
            }
        }else{
            for (int i = 0; i < arenaObstacles.size; i++) {
                if (Intersector.intersectSegmentRectangle(start, end, arenaObstacles.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }
}
