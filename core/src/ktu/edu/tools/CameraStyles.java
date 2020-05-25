package ktu.edu.tools;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

import ktu.edu.UndergroundBlasterGame;

public class CameraStyles {

    public static void lerpToTarget(Camera camera, Vector2 target){
        //a + (b - a ) * lerp factor
        Vector3 position = camera.position;
        position.x = camera.position.x + (target.x - camera.position.x) * 0.1f;
        position.y = camera.position.y + (target.y - camera.position.y) * 0.1f;
        camera.position.set(position);
        camera.update();
    }

    public static void customStyle(Camera camera, Vector2 target, Viewport gamePort){
        float x = 28 / UndergroundBlasterGame.PPM + gamePort.getWorldWidth()/2;
        float y = 224 / UndergroundBlasterGame.PPM + gamePort.getWorldHeight()/2;
        float x2 = 612 / UndergroundBlasterGame.PPM - gamePort.getWorldWidth()/2;
        float y2 = 640 / UndergroundBlasterGame.PPM - gamePort.getWorldHeight() / 2;
        Vector2 vector = new Vector2();
        if(target.x < x)
            vector.x = x;
        else if(target.x > x2)
            vector.x = x2;
        else
            vector.x = target.x;

        if(target.y < y)
            vector.y = y;
        else if(target.y > y2)
            vector.y = y2;
        else
            vector.y = target.y;

        lerpToTarget(camera, vector);

    }
}
