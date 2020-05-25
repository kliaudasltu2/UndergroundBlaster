package ktu.edu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import ktu.edu.managers.Assets;
import ktu.edu.managers.GameScreenManager;

public class UndergroundBlasterGame extends Game {

	public static final int V_WIDTH = 1200;
	public static final int V_HEIGHT = 675;
	public static final float PPM =  100; //pixels per meter

	//Box2D Collision Bits
	public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short BLASTER_BIT = 2;
	public static final short BOMB_BIT = 4;
	public static final short EXPLOSION_BIT = 8;
	public static final short HELPER_BIT = 16;
	public static final short ENEMY_BIT = 32;
	public static final short TRIGGER_BIT = 64;
	public static final short ITEM_BIT = 128;
	public static final short BULLET_BIT = 256;

	//Managers
	public Assets assMan;
	public GameScreenManager gsm;

	public SpriteBatch batch;

	public  BitmapFont font1;
	public static BitmapFont font2, font3, titleFont;
	public BitmapFont greenFont12;
	public BitmapFont whiteFont12;

    //Player stats
	public int damage;
	public float maxHp;
	public float heal;
	public float stunTime;
	public boolean artist, citizen, astrologer, magnet;
	public float explosionSize;
	public float armour;

	//Game statistic
	public int coins;
	public int totalKill;
	public int bestScore;

    public Preferences preferences;

	private FPSLogger fpsLoger;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		assMan = new Assets();
		preferences = Gdx.app.getPreferences("GameValues");
		initFonts();
		iniGameVariable();
		gsm = new GameScreenManager(this);
        Music music = Gdx.audio.newMusic(Gdx.files.internal("sounds/backgroundMusic.mp3"));
		gsm.setScreen(GameScreenManager.STATE.LOADMAINMENU);
		fpsLoger = new FPSLogger();
	}

	@Override
	public void render () {
		super.render();
		fpsLoger.log();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		assMan.manager.dispose();
		gsm.dispose();
		font1.dispose();
	}

	private void initFonts(){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Savage.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

		params.size = 72;
		params.borderWidth = 1;
		params.color = Color.WHITE;
		params.shadowOffsetX = 1;
		params.shadowOffsetY = 1;
		params.shadowColor = new Color(0, 0.0f,0, 1f);

		font1 = generator.generateFont(params);

		FreeTypeFontGenerator.FreeTypeFontParameter params2 = new FreeTypeFontGenerator.FreeTypeFontParameter();

		params2.size = 24;
		params2.color = Color.WHITE;
		params2.borderWidth = 1;
		params2.borderColor = Color.BLACK;

		font2 = generator.generateFont(params2);

		FreeTypeFontGenerator.FreeTypeFontParameter params3 = new FreeTypeFontGenerator.FreeTypeFontParameter();

		params3.size = 24;
		params3.borderWidth = 1;
		params3.borderColor = Color.BLACK;
		params3.color = Color.GREEN;

		font3 = generator.generateFont(params3);

		params3.size = 40;
		params3.color = Color.WHITE;
		params3.borderColor = Color.BLACK;
		params3.borderWidth = 3;

		whiteFont12 = generator.generateFont(params3);

		params3.size = 32;
		params3.color = Color.WHITE;

		greenFont12 = generator.generateFont(params3);

		params3.size = 24;
		params3.color = Color.RED;

        BitmapFont redFont12 = generator.generateFont(params3);

		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 108;
		parameter.borderWidth = 1;
		parameter.color = Color.WHITE;
		parameter.shadowOffsetX = 3;
		parameter.shadowOffsetY = 3;
		parameter.shadowColor = Color.BLACK;
		titleFont = generator.generateFont(parameter);

		generator.dispose();


	}

	private void iniGameVariable(){
		if(!preferences.contains("damage")) {
			preferences.putInteger("damage", 200);
			preferences.putInteger("damageCost",200);
		}

		if(!preferences.contains("maxHP")) {
			preferences.putFloat("maxHP", 2000);
			preferences.putInteger("maxHPCost",200);
		}

		if(!preferences.contains("heal")) {
			preferences.putFloat("heal", 250);
			preferences.putInteger("healCost",200);
		}


		if(!preferences.contains("size")) {
			preferences.putFloat("size", 24);
			preferences.putInteger("sizeCost",200);
		}

		if(!preferences.contains("stun")) {
			preferences.putFloat("stun", 1.6f);
			preferences.putInteger("stunCost",500);
		}

		if(!preferences.contains("coins")) {
			preferences.putInteger("coins", 100);
		}

		if(!preferences.contains("totalKills")) {
			preferences.putInteger("totalKills", 0);
		}

		if(!preferences.contains("bestScore")) {
			preferences.putInteger("bestScore", 100);
		}

		if(!preferences.contains("armour")) {
			preferences.putFloat("armour", 0);
			preferences.putInteger("armourCost",500);
		}

		preferences.flush();

		damage = preferences.getInteger("damage");
		maxHp = preferences.getFloat("maxHP");
		heal = preferences.getFloat("heal");
		artist = preferences.getBoolean("artist");
		artist = preferences.getBoolean("artist");
		astrologer = preferences.getBoolean("astrologer");
		explosionSize = preferences.getFloat("size");
		stunTime = preferences.getFloat("stun");
		coins  = preferences.getInteger("coins");
		armour = preferences.getFloat("armour");
		magnet = preferences.getBoolean("magnet");
		totalKill = preferences.getInteger("totalKills");
		bestScore = preferences.getInteger("bestScore");

	}

	public void resetValues() {
		preferences.putInteger("damage", 200);
		preferences.putInteger("damageCost", 200);

		damage = preferences.getInteger("damage");


		preferences.putFloat("maxHP", 2000);
		preferences.putInteger("maxHPCost", 200);

		maxHp = preferences.getFloat("maxHP");


		preferences.putFloat("heal", 250);
		preferences.putInteger("healCost", 200);

		heal = preferences.getFloat("heal");


		preferences.putBoolean("artist", false);
		artist = preferences.getBoolean("artist");


		preferences.putBoolean("astrologer", false);
		astrologer = preferences.getBoolean("astrologer");

		preferences.putBoolean("magnet", false);
		astrologer = preferences.getBoolean("magnet");


		preferences.putFloat("size", 24);
		preferences.putInteger("sizeCost", 200);

		explosionSize = preferences.getFloat("size");


		preferences.putFloat("stun", 1.6f);
		preferences.putInteger("stunCost", 500);

		stunTime = preferences.getFloat("stun");


		preferences.putInteger("coins", 50000);
		coins = preferences.getInteger("coins");


		preferences.putFloat("armour", 0);
		preferences.putInteger("armourCost", 500);

		preferences.putInteger("totalKills", 0);
		totalKill = preferences.getInteger("totalKills");

		preferences.putInteger("bestScore", 0);
		bestScore = preferences.getInteger("bestScore");


		preferences.flush();
	}

	public void playClick(){
		assMan.manager.get("sounds/click.wav", Sound.class).play();
	}
}
