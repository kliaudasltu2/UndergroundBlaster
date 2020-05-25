package ktu.edu.scenes;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Collections;

import ktu.edu.actors.Ability;
import ktu.edu.screens.PlayScreen;
import ktu.edu.sprites.Helper.Artist;
import ktu.edu.sprites.Helper.Citizen;
import ktu.edu.sprites.Helper.Helper;

public class Abilities implements Disposable {

    private final Image[] images;
    private final Stage stage;
    private final PlayScreen screen;
    private int [] numbers;

    private final Array<Ability> abilities;
    private ArrayList<Integer> citizien, artist, astrolog;

    public Abilities(final PlayScreen screen){
        this.screen = screen;
        this.stage = screen.stage;

        images = new Image[2];
        images[0] = new Image(new Texture("backgroundBlack.png"));
        images[0].setSize(stage.getWidth(), stage.getHeight());
        images[0].setPosition(0,0);

        images[1] = new Image(screen.assets.manager.get("ribbon.png", Texture.class));
        images[1].setSize(835, 141);
        images[1].setPosition(stage.getWidth()/2 - images[1].getWidth() / 2,stage.getHeight() - 200);

        images[0].setVisible(false);
        images[1].setVisible(false);

        stage.addActor(images[0]);
        stage.addActor(images[1]);

        abilities = new Array<>();
        TextureAtlas atlas = screen.assets.manager.get("sprites/abilities/abilities.pack", TextureAtlas.class);

        abilities.add(new Ability(new Image(atlas.findRegion("coins50")), "coin50", "Coins", "Collected 50 coins!"));
        abilities.add(new Ability(new Image(atlas.findRegion("coins100")), "coin100", "Bag of coins", "Collected 100 coins!"));
        abilities.add(new Ability(new Image(atlas.findRegion("coins300")), "coin300", "Chest of coins", "Collected 300 coins!"));
        abilities.add(new Ability(new Image(atlas.findRegion("greenCross")), "heal", "Heal", "Restored Hp partially"));
        abilities.add(new Ability(new Image(atlas.findRegion("heart")), "increasemaxhp", "Hp boost", "Slightly max hitpoints increased!"));
        abilities.add(new Ability(new Image(atlas.findRegion("heart30")), "increasemaxhp2", "Super Hp boost ", "Max hitpoints increased!"));
        abilities.add(new Ability(new Image(atlas.findRegion("speed")), "speed", "Speed", "You are faster!"));
        abilities.add(new Ability(new Image(atlas.findRegion("stun")), "stun", "Stun", "The enemies will be stunned longer!"));
        abilities.add(new Ability(new Image(atlas.findRegion("armour10")), "armour10", "Armour boost",  "Slightly armour increased."));
        abilities.add(new Ability(new Image(atlas.findRegion("armour30")), "armour30", "Super Armour Boost", "You armour is cool 30 proc."));
        abilities.add(new Ability(new Image(atlas.findRegion("damage10")), "damage10", "Damage boost", "Slightly increased explosion damage!"));
        abilities.add(new Ability(new Image(atlas.findRegion("damage30")), "damage30", "Extra Damage Boost", "Increased explosion damage!"));
        abilities.add(new Ability(new Image(atlas.findRegion("arrow")), "explosionupdown", "Explosion Width", "Explosion width expanded!"));
        abilities.add(new Ability(new Image(atlas.findRegion("arrow2")), "explosionside", "Explosion Height", "Explosion height expanded!!"));
        abilities.add(new Ability(new Image(atlas.findRegion("bombimg")), "BombCount", "Bomb", "Now you have more bombs!"));

        for(final Ability ability:abilities){
            ability.setVisible(false);
            stage.addActor(ability);
            ability.addListener(new InputListener() {

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    screen.assets.manager.get("sounds/click.wav", Sound.class).play(.5f);
                    screen.blaster.upgrade(ability.getName());
                    screen.description.show(ability.getDescription());
                    hideAll();
                    return true;
                }
            });
        }

        citizien = new ArrayList<>();
        citizien.add(0);
        citizien.add(1);
        citizien.add(2);
        citizien.add(3);
        citizien.add(4);
        citizien.add(5);

        artist = new ArrayList<>();
        artist.add(0);
        artist.add(1);
        artist.add(3);
        artist.add(4);
        artist.add(6);
        artist.add(7);
        artist.add(8);
        artist.add(9);
        artist.add(10);

        astrolog = new ArrayList<>();
        astrolog.add(0);
        astrolog.add(1);
        astrolog.add(3);
        astrolog.add(4);
        astrolog.add(10);
        astrolog.add(11);
        astrolog.add(12);
        astrolog.add(13);
        astrolog.add(14);
    }

    @Override
    public void dispose() {
        stage.dispose();

    }

    public void addAbilities(Helper helper) {
        if(helper instanceof Citizen)
            numbers = getThreeRandomNumber(citizien);
        else if(helper instanceof Artist)
            numbers = getThreeRandomNumber(artist);
        else
            numbers = getThreeRandomNumber(astrolog);

        images[0].setVisible(true);
        images[1].setVisible(true);
        screen.stop = true;

        abilities.get(numbers[0]).setActorBounds(stage.getWidth() / 2 - 250 - 100, stage.getHeight() / 2 - 150);
        abilities.get(numbers[0]).setVisible(true);

        abilities.get(numbers[1]).setActorBounds(stage.getWidth() / 2  - 100, stage.getHeight() / 2 - 150);
        abilities.get(numbers[1]).setVisible(true);

        abilities.get(numbers[2]).setActorBounds(stage.getWidth() / 2 + 250 - 100, stage.getHeight() / 2 - 150);
        abilities.get(numbers[2]).setVisible(true);

    }

    private void hideAll(){

        images[0].setVisible(false);
        images[1].setVisible(false);
        abilities.get(numbers[0]).setVisible(false);
        abilities.get(numbers[1]).setVisible(false);
        abilities.get(numbers[2]).setVisible(false);

        screen.stop = false;
    }

    private int [] getThreeRandomNumber(ArrayList<Integer> arrayList){
        int [] temporary = new int[3];
        Collections.shuffle(arrayList);
        for(int i = 0; i < 3; i++) {
            temporary[i] = arrayList.get(i);
        }
        return temporary;
    }


}
