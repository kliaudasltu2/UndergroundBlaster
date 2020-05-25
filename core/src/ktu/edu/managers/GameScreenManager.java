package ktu.edu.managers;

import java.util.HashMap;

import ktu.edu.UndergroundBlasterGame;
import ktu.edu.screens.AbstractScreen;
import ktu.edu.screens.FirstLoadingScreen;
import ktu.edu.screens.GameLoadingScreen;
import ktu.edu.screens.GameOverScreen;
import ktu.edu.screens.MainMenu;
import ktu.edu.screens.PlayScreen;
import ktu.edu.screens.RatingScreen;
import ktu.edu.screens.ShopScreen;
import ktu.edu.screens.SplashScreen;

public class GameScreenManager {

    private final UndergroundBlasterGame game;

    private final HashMap<STATE, AbstractScreen> gameScreens;

    public enum STATE {
        LOADMAINMENU,
        SPLASH,
        MAINMENU,
        LOADGAME,
        PLAY,
        SHOP,
        RATING,
        GAMEOVER
    }

    public GameScreenManager(final UndergroundBlasterGame  game){
        this.game = game;
        this.gameScreens = new HashMap<>();

        initGamesScreens();

    }

    private void initGamesScreens(){
        this.gameScreens.put(STATE.PLAY, new PlayScreen(game));
        this.gameScreens.put(STATE.LOADMAINMENU, new FirstLoadingScreen(game));
        this.gameScreens.put(STATE.MAINMENU, new MainMenu(game));
        this.gameScreens.put(STATE.LOADGAME, new GameLoadingScreen(game));
        this.gameScreens.put(STATE.SPLASH, new SplashScreen(game));
        this.gameScreens.put(STATE.GAMEOVER, new GameOverScreen(game));
        this.gameScreens.put(STATE.SHOP, new ShopScreen(game));
        this.gameScreens.put(STATE.RATING, new RatingScreen(game));
    }

    public void setScreen(STATE nextScreen){
        game.setScreen(gameScreens.get(nextScreen));
    }

    public void dispose(){
        for(AbstractScreen screen : gameScreens.values()){
            if(screen != null){
                screen.dispose();
            }
        }
    }
}
