package ktu.edu.managers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

import ktu.edu.actors.PauseAbility;
import ktu.edu.screens.PlayScreen;

public class PauseAbilities {

    private final Array<PauseAbility> abilities;
    private final PlayScreen screen;

    public PauseAbilities(PlayScreen screen){
        this.screen = screen;
        abilities = new Array<>();
        TextureAtlas atlas = screen.assets.manager.get("sprites/pauseAbilities/pauseAbilities.pack", TextureAtlas.class);
        abilities.add(new PauseAbility(new Image(atlas.findRegion("hp")), String.format("%.0f",screen.blaster.maxHp)));
        abilities.add(new PauseAbility(new Image(atlas.findRegion("damage")), Integer.toString( screen.blaster.damage)));
        abilities.add(new PauseAbility(new Image(atlas.findRegion("stun")),  String.format("%.1f", screen.stunTime)));
        abilities.add(new PauseAbility(new Image(atlas.findRegion("bombimg")),  Integer.toString( screen.blaster.bombCount)));
        abilities.add(new PauseAbility(new Image(atlas.findRegion("armour")), "BBZ"));
        abilities.add(new PauseAbility(new Image(atlas.findRegion("greenCross")), String.format("%.0f", screen.game.heal)));
        abilities.add(new PauseAbility(new Image(atlas.findRegion("speed")), String.format("%.2f", screen.blaster.speed)));
        abilities.add(new PauseAbility(new Image(atlas.findRegion("arrow")), Integer.toString(screen.blaster.explosionToRightAndLeft)));
        abilities.add(new PauseAbility(new Image(atlas.findRegion("arrow2")), Integer.toString(screen.blaster.explosionToUpAndDown)));
    }

    public Array<PauseAbility> getPauseAbilities(){
        return abilities;
    }

    public void setText() {
        abilities.get(0).setText(String.format("%.0f", screen.blaster.maxHp));
        abilities.get(1).setText(Integer.toString(screen.blaster.damage));
        abilities.get(2).setText(String.format("%.1f", screen.stunTime) + "s ");
        abilities.get(3).setText(Integer.toString(screen.blaster.bombCount));
        abilities.get(4).setText("0%");
        abilities.get(5).setText(String.format("%.0f", screen.game.heal));
        abilities.get(6).setText(String.format("%.2f", screen.blaster.speed));
        abilities.get(7).setText(Integer.toString(screen.blaster.explosionToRightAndLeft));
        abilities.get(8).setText(Integer.toString(screen.blaster.explosionToUpAndDown));
    }
}
