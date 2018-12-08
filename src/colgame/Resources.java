package colgame;


//Utility class to load, store, and retrieve resources used in the game

import java.util.HashMap;
import java.util.Map;
 
import org.newdawn.slick.Font;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.SpriteSheetFont;
import org.newdawn.slick.tiled.*;
import org.newdawn.slick.Animation; 

public class Resources {
	
		//generic method to load all resources
		public static void registerResource(String type, String key, String filepath) throws SlickException {
			switch(type) {
				case "image": 		registerImage(key, filepath);
							  		break;
				case "sound": 		registerSound(key, filepath);
									break;
				case "music":		registerMusic(key, filepath);
									break;
				case "tiledmap":	registerTileMap(key, filepath);
									break;
				case "ss":			registerSpriteSheet(key, filepath);
			}
		}
		
		
	    // ----------------------------------------------------------------------
        // --- SpriteSHeets
        // ----------------------------------------------------------------------

        private static final String[] SS_KEYS = new String[] {"bullets", "bulletMasks", "enemiesSmall", "enemiesSmallMasks"};
        private static final String[] SS_PATHS = new String [] {"bulletSheet.png", "bulletSheetMask.png", "enemySheetSmall.png", "enemySheetSmallMasks.png"};
            	
    	//The various TileMaps
    	private static HashMap<String, SpriteSheet> ssMap = new HashMap<String, SpriteSheet>();
        
    	public static void registerSpriteSheet(String key, String path) throws SlickException {
    			ssMap.put(key, new SpriteSheet("res/images/tilesets/".concat(path), Entity.IMG_PIXELS, Entity.IMG_PIXELS));
    	}
    	
    	public static SpriteSheet retrieveSpriteSheet(String key) {
    			return ssMap.get(key);
    	}
	
	    // ----------------------------------------------------------------------
	    // --- IMAGE
	    // ----------------------------------------------------------------------
 
		private static final String[] IMAGE_KEYS = new String[] {"greenTile", "blackTile", "grayTile", "blueTile", "yellowTile", 
				"redTile", "orangeTile", "indigoTile", "violetTile", "whiteTile", "greenPlayer", "blackPlayer", "grayPlayer", 
				"bluePlayer", "yellowPlayer", "redPlayer", "orangePlayer", "indigoPlayer", "violetPlayer", "whitePlayer",
				"taskbar", "pauseScreen", "resHover", "mmHover", "lsHover", "settingsHover", "quitHover","menuB", "playBox", 
				"tutBox", "coopBox", "dataBox", "setBox", "cojBox1", "cojBox2", "heart", "playerHurtbox", "black", "white",
				"healthbar", "postgame", "gameover", "complete", "statlabels"};
		private static final String [] IMAGE_FILEPATHS = new String[] {
				"greentile.png", "blacktile.png", "graytile.png", "bluetile.png", "yellowtile.png", "redtile.png", "orangetile.png", 
				"indigotile.png", "violettile.png", "whitetile.png", "playerGreen.png", "playerBlack.png",	"playerGray.png", 
				"playerBlue.png", "playerYellow.png", "playerRed.png",	"playerOrange.png", "playerIndigo.png", "playerViolet.png", 
				"playerWhite.png", "taskbar.png", "pause.png", "resumeHover.png", "mmHover.png", "lsHover.png", "settingsHover.png",
				"quitHover.png",	"menu.png",	"playBox.png", "tutorialBox.png", "coopBox.png", "dataBox.png", "setBox.png",
				"cojBox1.png", "cojBox2.png", "heart.png", "playerHurtbox.png", "black.png", "white.png", "healthbar.png",
				"postgame.png", "gameOverTxt.png", "lvlCompleteTxt.png", "statLabels.png"};
	
        private static Map<String, Image> imageMap = new HashMap<String, Image>();
 
        public static void registerImage(String key, String path) throws SlickException {
                Image image = new Image("res/images/".concat(path));
                image.setFilter(Image.FILTER_NEAREST);
                imageMap.put(key, image);
        }
 
        public static Image retrieveImage(String key) {
                return imageMap.get(key);
        }
 
        // ----------------------------------------------------------------------
        // --- FONT
        // ----------------------------------------------------------------------
        private static Map<String, Font> fontMap = new HashMap<String, Font>();
 
        public static void registerFont(String key, String path, int width, int height) throws SlickException {
                fontMap.put(key, new SpriteSheetFont(new SpriteSheet(path, width, height), ' '));
        }
 
        public static Font retrieveFont(String key) {
                return fontMap.get(key);
        }
 
        private static final String[] SOUND_KEYS = new String[] {};
		private static final String[] SOUND_FILEPATHS = new String[] {};
        
        // ----------------------------------------------------------------------
        // --- SOUND
        // ----------------------------------------------------------------------
        private static Map<String, Sound> soundMap = new HashMap<String, Sound>();
 
        public static void registerSound(String key, String path) throws SlickException {
                soundMap.put(key, new Sound(path));
        }
 
        public static Sound retrieveSound(String key) {
                return soundMap.get(key);
        }
 

        // ----------------------------------------------------------------------
        // --- MUSIC
        // ----------------------------------------------------------------------
        
        private static final String[] MUSIC_KEYS = new String[] 
        {	"briskyellow", "calmgrey", "cleverred", "darksepia", "distantgraphite", 
			"doublesilver", "earlyorange", "essentialviolet", "exoticazure", "finered", "fragilegold", 
			"freshgreen", "freshorange", "illusiveazure", "inspirationgold", "lastsapphire", "lightblue",
			"liquidsilver", "livelypurple", "mellowbrown", "mistysapphire", "moonwhite", "naturalgreen",
			"purewhite", "rainyindygo", "reflectingpurple", "sharpbrown","skyblue", "smokygrey", "sunriseyellow",
			"tendergraphite", "throughthejazz", "transientsepia", "vinylblack"
    	};
        

		private static final String[] MUSIC_FILEPATHS = new String[] 
		{   "briskYellow.ogg", "calmGrey.ogg", "cleverRed.ogg", "darkSepia.ogg", "distantGraphite.ogg",
			"doubleSilver.ogg", "earlyOrange.ogg", "essentialViolet.ogg", "exoticAzure.ogg", "fineRed.ogg", "fragileGold.ogg", 
			"freshGreen.ogg", "freshOrange.ogg", "illusiveAzure.ogg", "inspirationGold.ogg", "lastSapphire.ogg", "lightBlue.ogg",
			"liquidSilver.ogg", "livelyPurple.ogg", "mellowBrown.ogg", "mistySapphire.ogg", "moonWhite.ogg", "naturalGreen.ogg",
			"pureWhite.ogg", "rainyIndygo.ogg", "reflectingPurple.ogg",	"sharpBrown.ogg", "skyBlue.ogg", "smokyGrey.ogg", 
			"sunriseYellow.ogg", "tenderGraphite.ogg", "throughthejazz.ogg", "transientSepia.ogg", "vinylBlack.ogg"
		};
        
        private static Map<String, Music> musicMap = new HashMap<String, Music>();
 
        public static void registerMusic(String key, String path) throws SlickException {
                musicMap.put(key, new Music("res/music/".concat(path), true));
        }
 
        public static Music retrieveMusic(String key) {
                return musicMap.get(key);
        }

      //starts muisc
    	public static void startMusic(Music music) {
    		if(!music.playing()) {
    			music.loop();
    			music.fade(1200, .7f, false);
    		}
    	}
        
        
        // ----------------------------------------------------------------------
        // --- TiledMaps
        // ----------------------------------------------------------------------

        private static final String[] TM_KEYS = new String[] {"-1", "0", "1", "2", "3" };
        private static final String[] TM_PATHS = new String [] {"test.tmx", "menu.tmx", 
        		"levelSelect.tmx", "tutorial.tmx", "test2.tmx"
        };
            	
    	//The various TileMaps
    	private static HashMap<Integer, TiledMap> tiledMapMap = new HashMap<Integer, TiledMap>();
        
    	public static void registerTileMap(String key, String path) throws SlickException {
    			tiledMapMap.put(Integer.parseInt(key), new TiledMap("res/maps/".concat(path)));
    	}
    	
    	public static TiledMap retrieveTiledMap(int key) {
    			return tiledMapMap.get(key);
    	}
	
        // ----------------------------------------------------------------------
        // --- Animations
        // ----------------------------------------------------------------------

    	private static final String[] ANIMATION_KEYS = new String[] {"colorsplash", "speedup", "damageup", "bullet1up", "bullet2up",
    			"extraLife", "reloadup", "shield"};
    	private static final String[] ANIMATION_FILEPATHS = new String[] {"stillColorsplash.png", "stillSpeedup.png", "stillDamageup.png",
    			"stillBullet1up.png", "stillBullet2up.png", "stillExtraLife.png", "stillReloadup.png", "stillShield.png"
    	};
    	private static final int[] ANIMATION_DURATIONS = new int[] {200, 200, 200, 200, 200, 200, 200, 200};
    	private static final int[] ANIMATION_WIDTHS = new int[] {48, 48, 48, 48, 48, 48, 48, 48};
    	private static final int[] ANIMATION_HEIGHTS = new int[] {48, 48, 48, 48, 48, 48, 48, 48};
    	
    	
    	private static HashMap<String, Animation> animationMap = new HashMap<String, Animation>();
    	
    	public static void registerAnimation(String key, String path, int duration, int width, int height)  throws SlickException {
    		SpriteSheet ss = new SpriteSheet("res/images/".concat(path), width, height);
    		animationMap.put(key, new Animation(ss, duration));
    	}
    	
    	public static Animation retrieveAnimationMap(String key) {
    		return animationMap.get(key);
    	}
    	
        //getters for KEYS and FILEPATHS
        
		public static String[] getImageKeys() {
			return IMAGE_KEYS;
		}

		public static String[] getImageFilepaths() {
			return IMAGE_FILEPATHS;
		}

		public static String[] getSoundKeys() {
			return SOUND_KEYS;
		}

		public static String[] getSoundFilepaths() {
			return SOUND_FILEPATHS;
		}

		public static String[] getMusicKeys() {
			return MUSIC_KEYS;
		}

		public static String[] getMusicFilepaths() {
			return MUSIC_FILEPATHS;
		}

		public static String[] getTM_KEYS() {
			return TM_KEYS;
		}

		public static String[] getTM_PATHS() {
			return TM_PATHS;
		}
		public static String[] getANIMATION_KEYS() {
			return ANIMATION_KEYS;
		}

		public static String[] getAnimationKeys() {
			return ANIMATION_KEYS;
		}

		public static String[] getAnimationFilepaths() {
			return ANIMATION_FILEPATHS;
		}

		public static int[] getAnimationDurations() {
			return ANIMATION_DURATIONS;
		}

		public static int[] getAnimationWidths() {
			return ANIMATION_WIDTHS;
		}

		public static int[] getAnimationHeights() {
			return ANIMATION_HEIGHTS;
		}

		public static String[] getSsKeys() {
			return SS_KEYS;
		}

		public static String[] getSsPaths() {
			return SS_PATHS;
		}

}