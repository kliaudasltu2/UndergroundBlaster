package ktu.edu.sprites.Helper;

import com.badlogic.gdx.math.Vector2;

public class HelperDef {

    public final Vector2 position;
    public final Class<?> type;
    public final boolean canWalk;

    public HelperDef(Vector2 position, Class<?> type, boolean canWalk){
        this.position = position;
        this.type = type;
        this.canWalk = canWalk;
    }
}
