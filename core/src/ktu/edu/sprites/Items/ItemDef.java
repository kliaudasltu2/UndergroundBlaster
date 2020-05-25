package ktu.edu.sprites.Items;

import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class ItemDef {

    public final Vector2 position;
    public Class<?> type;

    public ItemDef(Vector2 position){
        Random random = new Random();
        int i = random.nextInt(3);

        switch (i){
            case 0:
                type = GoldCoin.class;
                break;
            case 1:
                type = SilverCoin.class;
                break;
            case 2:
                type = BronzeCoin.class;
                break;
        }

        this.position = position;
    }
}
