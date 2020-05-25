package ktu.edu.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Assets {

    public AssetManager manager;

    public void load(){
        if(manager == null){
            manager = new AssetManager();
        }
        manager.setLoader(TiledMap.class, new TmxMapLoader());
        manager.load("level1dungeon.tmx", TiledMap.class);
        manager.load("sprites/Blaster/Blaster.pack", TextureAtlas.class);
        manager.load("sprites/Helper/Helpers.pack", TextureAtlas.class);
        manager.load("sprites/enemies/skeleton/skeleton.pack", TextureAtlas.class);
        manager.load("sprites/enemies/darkwitch/Darkwitch.pack", TextureAtlas.class);
        manager.load("healthBar.pack", TextureAtlas.class);
        manager.load("sprites/pauseAbilities/pauseAbilities.pack", TextureAtlas.class);
        manager.load("ui/controller/controlleris.pack", TextureAtlas.class);

        manager.load("ribbon.png", Texture.class);
        manager.load("sprites/abilities/abilities.pack", TextureAtlas.class);
        manager.load("background2.png", Texture.class);

        manager.load("sounds/explosion.wav", Sound.class);
        manager.load("sounds/dropTheBomb.wav", Sound.class);
        manager.load("sounds/footStep3.wav", Sound.class);
        manager.load("sounds/Helper2.wav", Sound.class);
        manager.load("sounds/Hurt.ogg", Sound.class);
        manager.load("sounds/swordSlash.wav", Sound.class);
        manager.load("sounds/collectcoin.wav", Sound.class);
    }

    public void loadingScreenAsset(){
        if(manager == null){
            manager = new AssetManager();
        }
        manager.load("sprites/explosion.pack", TextureAtlas.class);
        manager.load("sprites/Blaster/Blaster.pack", TextureAtlas.class);
        manager.load("sounds/footStep3.wav", Sound.class);
        manager.load("sounds/purchase.wav", Sound.class);
        manager.load("sounds/cancel.wav", Sound.class);
        manager.load("ui/shadow/uiskin.atlas", TextureAtlas.class);
        manager.load("sprites/item/gold.pack", TextureAtlas.class);
        manager.load("sprites/item/silver.pack", TextureAtlas.class);
        manager.load("sprites/item/bronze.pack", TextureAtlas.class);
        manager.load("sprites/shop/shop.pack", TextureAtlas.class);
        manager.load("ui/screens/mainMenuItems.pack", TextureAtlas.class);
        manager.load("ui/screens/gameover.pack", TextureAtlas.class);
        manager.load("sounds/click.wav", Sound.class);
    }
}
