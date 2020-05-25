package ktu.edu.managers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

import ktu.edu.actors.shopIcon;
import ktu.edu.actors.ShopItem;
import ktu.edu.screens.ShopScreen;

public class ShopItems {

    private final Array<ShopItem> shopItems;
    private final Array<shopIcon> shopIcons;

    public ShopItems(ShopScreen screen){
        shopItems = new Array<>();
        shopIcons  = new Array<>();

        TextureAtlas atlas = screen.game.assMan.manager.get("sprites/shop/shop.pack",TextureAtlas.class);

        for(int i = 0; i < 9; i++){
            System.out.println(i+1);
            shopItems.add(new ShopItem(new Image(new TextureRegion(atlas.findRegion(Integer.toString(i+1))))));
        }

        shopItems.get(0).setItemName("Magnet");
        shopItems.get(1).setItemName("Artist");
        shopItems.get(2).setItemName("Astrologer");
        shopItems.get(3).setItemName("Damage");
        shopItems.get(4).setItemName("Max Hitpoints");
        shopItems.get(5).setItemName("Heal");
        shopItems.get(6).setItemName("Stun");
        shopItems.get(7).setItemName("Armour");
        shopItems.get(8).setItemName("Explosion size");

        shopItems.get(0).setValue(3000);
        shopItems.get(1).setValue(2000);
        shopItems.get(2).setValue(3000);
        shopItems.get(3).setValue(screen.game.preferences.getInteger("damageCost"));
        shopItems.get(4).setValue(screen.game.preferences.getInteger("maxHPCost"));
        shopItems.get(5).setValue(screen.game.preferences.getInteger("healCost"));
        shopItems.get(6).setValue(screen.game.preferences.getInteger("stunCost"));
        shopItems.get(7).setValue(screen.game.preferences.getInteger("armourCost"));
        shopItems.get(8).setValue(screen.game.preferences.getInteger("sizeCost"));

        shopIcons.add(new shopIcon(new Image(new TextureRegion(atlas.findRegion("damage"))), Integer.toString(screen.game.damage)));
        shopIcons.add(new shopIcon(new Image(new TextureRegion(atlas.findRegion("heart"))), String.format("%.0f", screen.game.maxHp)));
        shopIcons.add(new shopIcon(new Image(new TextureRegion(atlas.findRegion("heal"))), String.format("%.0f",screen.game.heal)));
        shopIcons.add(new shopIcon(new Image(new TextureRegion(atlas.findRegion("stun"))), String.format("%.1f",screen.game.stunTime) + "s"));
        shopIcons.add(new shopIcon(new Image(new TextureRegion(atlas.findRegion("armour"))), String.format("%.0f", screen.game.armour * 100) + "%"));
        shopIcons.add(new shopIcon(new Image(new TextureRegion(atlas.findRegion("size"))),String.format("%.1f", screen.game.explosionSize)));
    }

    public Array<ShopItem> getItems(){
        return shopItems;
    }

    public Array<shopIcon> getIcons(){
        return shopIcons;
    }
}
