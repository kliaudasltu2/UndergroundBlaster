package ktu.edu.tools;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import ktu.edu.screens.GameLoadingScreen;

public class BlasterRunning extends Sprite {

    private final TextureAtlas atlas;
    private final Animation animation;
    private float stateTime;
    private final GameLoadingScreen screen;
    private int stepIndex;

    public BlasterRunning(GameLoadingScreen screen){
        this.screen = screen;
        atlas = screen.game.assMan.manager.get("sprites/Blaster/Blaster.pack", TextureAtlas.class);
        animation = createAnimation2();
        animation.setPlayMode(Animation.PlayMode.LOOP);
        setBounds(0,0,128 , 128);
        setRegion((TextureRegion)animation.getKeyFrame(0));
        stepIndex = 0;


    }

    public void update(float dt, float x, float y){
        stateTime += dt;
        setRegion((TextureRegion)animation.getKeyFrame(stateTime,true));
        setPosition(x - getWidth() / 2, y - getHeight()/2);
        if((animation.getKeyFrameIndex(stateTime) != stepIndex )){
            if(animation.getKeyFrameIndex(stateTime) == 0 || animation.getKeyFrameIndex(stateTime) == 8){
                screen.game.assMan.manager.get("sounds/footStep3.wav", Sound.class).play(.5f);
                stepIndex = animation.getKeyFrameIndex(stateTime);
            }
        }
    }

    private Animation createAnimation2(){
        Array<TextureRegion> frames = new Array<>();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 5; j++){
                frames.add(new TextureRegion(atlas.findRegion("runningRight"),j*330,i*330,330,330));
            }
        }
        return new Animation(1/30f, frames);
    }
}
