package ktu.edu.managers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import ktu.edu.screens.PlayScreen;
import ktu.edu.sprites.enemies.Darkwitch;
import ktu.edu.sprites.enemies.Enemy;
import ktu.edu.sprites.enemies.Skeleton;

public class WaveManager {

    private Random random;
    private Array<Vector2> spawnSpotCoords;
    private Array<Class> enemiesType;
    private PlayScreen screen;

    public WaveManager(PlayScreen screen){
        this.screen = screen;
        random = new Random();
        spawnSpotCoords = screen.creator.getEnemiesSpots();
        enemiesType = new Array<>();
        enemiesType.add(Skeleton.class);
    }

    public void nextWave(int wave, Array<Enemy> enemies){

        if(wave == 10){
            enemiesType.add(Darkwitch.class);
        }

        if(wave < 5){
            getStandardWaveEnemies(wave, enemies);
        }

        int sk = random.nextInt(2);
        if(wave > 5) {
            if (sk == 1)
                getBossWavesEnemies(wave, enemies);
            else
                getStandardWaveEnemies(wave, enemies);
        }
    }

    private Array<Enemy> getStandardWaveEnemies(int wave, Array<Enemy> enemies){
        for(int i = 0; i <= wave / 5; i++)
            enemies.add(getEnemy(600 + wave * 50, 40 + wave * 3, Enemy.Type.BASIC, random.nextInt(5)));
        return enemies;
    }

    private  Array<Enemy> getBossWavesEnemies(int wave, Array<Enemy> enemies){
        enemies.add(getEnemy(1800  + wave * 150, 120 + wave * 9,Enemy.Type.BOSS, random.nextInt(6)+5));
        if(wave > 15){
            for(int i = 0; i <= (wave - 15)/5; i++)
                enemies.add(getEnemy(600 + wave * 50, 40 + wave * 3, Enemy.Type.BASIC, random.nextInt(5)));
            return enemies;
        }
        return enemies;
    }

    private Enemy getEnemy(int hp, int damage, Enemy.Type type, int dropRatio){
        int i = random.nextInt(enemiesType.size);
        switch (i){
            case 0:
                return new Skeleton(screen, getCoord(), hp, damage, type, dropRatio);
            case 1:
                default:
                    return new Darkwitch(screen, getCoord(),type, hp/2, damage, dropRatio);
        }
    }

    private Vector2 getCoord(){
        return spawnSpotCoords.get(random.nextInt(spawnSpotCoords.size));
    }
}
