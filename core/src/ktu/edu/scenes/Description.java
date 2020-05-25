package ktu.edu.scenes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;

import ktu.edu.screens.PlayScreen;

public class Description implements Disposable {

    private final Stage stage;
    private final Image image;
    private final Label label;
    private final Runnable runnable;

    public Description(PlayScreen screen){
        this.stage = screen.stage;

        image = new Image(screen.assets.manager.get("background2.png", Texture.class));
        image.setVisible(false);
        image.setBounds(stage.getWidth()/2 - 300, stage.getHeight()-200,600,100);
        stage.addActor(image);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = screen.game.whiteFont12;
        label = new Label("cirkelis", labelStyle);
        label.setSize(300, 80);
        label.setPosition(stage.getWidth()/2 - 150, stage.getHeight()-190);
        label.setAlignment(Align.center);
        label.setVisible(false);
        stage.addActor(label);

        runnable = new Runnable() {
            @Override
            public void run() {
                label.setVisible(false);
                image.setVisible(false);
            }
        };
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void show(String text){
        label.clearActions();
        image.clearActions();
        label.setVisible(true);
        image.setVisible(true);
        label.setText(text);
        label.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(1f), Actions.delay(1f), Actions.fadeOut(1f)));
        image.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(0.5f), Actions.delay(2f), Actions.fadeOut(0.5f), Actions.run(runnable)));
    }
}
