//The state that appears when first loading up the game

package colgame;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.*;
import org.newdawn.slick.loading.*;
import java.io.*;

public class Splash extends BasicGameState {

	private static final int ID = 0;

	//records number of milliseconds since splash state has started
	private static float timer;
	
	//alpha value for splash screen fades
	private static float alpha;
	
	//Images for the splash screen
	private Image[] splash;
	private Image currentSplash;
	
	/** The next resource to load */
	private static DeferredResource nextResource;
	
	public Splash(int id) {}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sb) throws SlickException {
		splash = new Image[] {
				new Image("res/images/splash1.png"),
				new Image("res/images/splash2.png")
		};
		
		currentSplash = splash[0];
		currentSplash.setAlpha(0);
		

		//initialize and build maps for tileImages, tileMaps, sounds, and music
		LoadingList.setDeferredLoading(true);
		
		constructResourceMaps();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {
		currentSplash.draw(0,0);		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int d) throws SlickException {
		
		if(nextResource != null) {
			try {
				nextResource.load();
			} catch (IOException e) {
				throw new SlickException("Failed to load " + nextResource.getDescription(), e);
			}
		}
		
		if(LoadingList.get().getRemainingResources() > 0) {
			nextResource = LoadingList.get().getNext();
		} else {
			nextResource = null;
		}
		
		updateSplash(sb, d);
	}
	
	//update the splash screen
	public void updateSplash(StateBasedGame sb, int d) {
		timer += d;
		
		//show splash screen 1 for 5 seconds
		if(timer < 6500) {
			//fade in for first .5 s
			if(timer < 500)
				setAlpha((float)(getAlpha() + .1));
			
			if(timer > 2250)
				Resources.startMusic(Resources.retrieveMusic("naturalgreen"));
			
			//fade out for last .5s and start music
			if(timer > 6000) 
				setAlpha((float)(getAlpha() - .1));
			
		} else {
			//fade in for first .5 s
			if(timer < 7000) {
				setCurrentSplash(splash[1]);
				setAlpha((float)(getAlpha() + .1));
			}
		}
		
		getCurrentSplash().setAlpha(alpha);
		
		//Enter Menu state after 10s
		if(timer > 10000) {
			sb.enterState(1, new FadeOutTransition(Color.black, 2400), new FadeInTransition(Color.white, 500));
		}
	}

	//---------------------------
	//LOAD RESOURCES
	//---------------------------
	
	public static void constructResourceMaps() throws SlickException {
		constructMap("image", Resources.getImageKeys(), Resources.getImageFilepaths());
		constructMap("tiledmap", Resources.getTM_KEYS(), Resources.getTM_PATHS());
		constructMap("sound", Resources.getSoundKeys(), Resources.getSoundFilepaths());
		constructMap("music", Resources.getMusicKeys(), Resources.getMusicFilepaths());
		constructMap("ss", Resources.getSsKeys(), Resources.getSsPaths());
		
		//create animations
		for(int i = 0; i < Resources.getANIMATION_KEYS().length; i++) {
			Resources.registerAnimation(Resources.getANIMATION_KEYS()[i], Resources.getAnimationFilepaths()[i],
					Resources.getAnimationDurations()[i], Resources.getAnimationWidths()[i], Resources.getAnimationHeights()[i]);
		}
	}
	
	public static void constructMap(String type, String[] keys, String[] filepaths) throws SlickException {
		for(int i = 0; i < keys.length; i++) {
			Resources.registerResource(type, keys[i], filepaths[i]);
		}
	}
	
	@Override
	public int getID() {
		return ID;
	}

	public static float getAlpha() {
		return alpha;
	}

	public static void setAlpha(float alpha) {
		Splash.alpha = alpha;
	}

	public Image[] getSplash() {
		return splash;
	}

	public void setSplash(Image[] splash) {
		this.splash = splash;
	}

	public Image getCurrentSplash() {
		return currentSplash;
	}

	public void setCurrentSplash(Image currentSplash) {
		this.currentSplash = currentSplash;
	}

}
