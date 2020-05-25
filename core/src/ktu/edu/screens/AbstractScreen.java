package ktu.edu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ktu.edu.UndergroundBlasterGame;

public abstract class AbstractScreen implements Screen {

    public final UndergroundBlasterGame game;
    final OrthographicCamera stageCam;
    private final Viewport stageViewport;
    public final Stage stage;

    AbstractScreen(final UndergroundBlasterGame game){
        this.game = game;

        stageCam = new OrthographicCamera();
        stageViewport = new FitViewport(UndergroundBlasterGame.V_WIDTH , UndergroundBlasterGame.V_HEIGHT, stageCam);
        stage = new Stage(stageViewport, game.batch);

    }

    public abstract void update(float delta);

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    void stageResize(int width, int height){
        stageViewport.update(width,height);
    }
}
