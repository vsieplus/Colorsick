package colgame;

import org.newdawn.slick.tiled.*;
import org.newdawn.slick.state.*;
import org.newdawn.slick.*;

import javax.vecmath.*;
import java.util.LinkedList;

public class Level {
	
	//the tiledmap corresponding to the level
	private TiledMap map;
	
	//unique id for each level
	private int levelid;
	
	//grid of Entities corresponding to each level, and dimensions (in grid units)
	private Entity[][] grid;
	private int width, height;
	
	//grid of color strings corresponding to each level
	private ColoredTile[][] colorGrid;
		
	//the bullets currently active in the level
	private LinkedList<Bullet> bulletArray;
	
	//the enemies in the level
	private LinkedList<Enemy> enemyArraySmall;
	private LinkedList<Enemy> enemyArrayMed;
	private LinkedList<Enemy> enemyArrayBig;
	private LinkedList<Enemy> enemyArrayBoss;
	
	private LinkedList<Enemy>[] enemies;
	
	//the music track for the level
	private Music levelBgm;
	
	private Player player;
	
	//constants for positioning map/player
	public static final int X_OFFSET = 480;
	public static final int Y_OFFSET = 288;
	
	//screen coordinates to draw the map
	private float mapX, mapY;
	
	private float scrollSpeed;
	
	//Level constructor
	public Level(int levelid, String bgm, float scrollspeed) throws SlickException {
		setLevelnum(levelid);
		setLevelBgm(Resources.retrieveMusic(bgm));
		setScrollSpeed(scrollspeed);
		
		map = Resources.retrieveTiledMap(levelid);
		
		setMapY(-1 * map.getHeight() * Entity.IMG_PIXELS + 13 * Entity.IMG_PIXELS);
		
		setWidth(map.getWidth());
		setHeight(map.getHeight());
		grid = new Entity[width][height];
				
		storeColors();
		
		bulletArray = new LinkedList<Bullet>();
		enemyArraySmall = new LinkedList<Enemy>();
		enemyArrayMed = new LinkedList<Enemy>();
		enemyArrayBig = new LinkedList<Enemy>();
		enemyArrayBoss = new LinkedList<Enemy>();
		
		enemies = new LinkedList[] {enemyArraySmall, enemyArrayMed, enemyArrayBig, enemyArrayBoss};
		
		//add player 
		setPlayer(new Player(Resources.retrieveImage("bluePlayer")));
		getPlayer().setLevel(this);
		getPlayer().setCurrentPos(new Vector2f(5 * Entity.IMG_PIXELS, (map.getHeight() - 1) * Entity.IMG_PIXELS));
		
		//add powerups and enemies
		parsePowerups();
		parseEnemies();		
	}	
	
	//Adds an entity to the grid
	public void addEntity(Entity e, int x, int y) {
		if(grid[x][y] == null) {
			grid[x][y] = e;
			
			//set grid coordinates of entity
			e.setX(x);
			e.setY(y);
			
			//set actual coordinates of entity
			e.setCurrentPos(new Vector2f(x * Entity.IMG_PIXELS, y * Entity.IMG_PIXELS));
			
			//if first time adding entity
			if(e.getLevel() == null) {
				e.setLevel(this);
				e.positionEntity();
			}
			
			e.setCurrentTileColor(e.getLevel().getColorGrid()[x][y].getColor());
		}
	}
	
	//Adds a colored tile to the color grid
	public void addColoredTile(ColoredTile c, int x, int y) {
		colorGrid[x][y] = c;
		
		//update the colored tile's coordinates
		c.setX(x);
		c.setY(y);
		
		c.setLevel(this);
	}
	
	//gets a colored tile from the color grid
	public ColoredTile getColoredTile(int x, int y) {
		return(colorGrid[x][y]);
	}
	
	public void storeColors() {
		//store colors of entire map
		colorGrid = new ColoredTile[width][height];
		
		for(int i = 0; i < width; i ++) {
			for(int j = 0; j < height; j ++) {
				int currentTileId = map.getTileId(i, j, 0);
				
				String col = map.getTileProperty(currentTileId, "color", "");
				
				int r = Integer.parseInt(map.getTileProperty(currentTileId, "r", ""));
				int g = Integer.parseInt(map.getTileProperty(currentTileId, "g", ""));
				int b = Integer.parseInt(map.getTileProperty(currentTileId, "b", ""));
				
				Color tileCol = new Color(r,g,b);
				
				//for player collision only
				if(col.equals("black")) {
					addColoredTile(new ColoredTile(tileCol, Resources.retrieveImage("white"), col, i, j), i, j);
				} else {
					addColoredTile(new ColoredTile(tileCol, Resources.retrieveImage("black"), col, i, j), i, j);
				}
				colorGrid[i][j].updateTileImage(col);
			}
		}
	}
	
	//parse the powerups in the tiledmap (1st layer)
	public void parsePowerups() {
		for(int i = 0; i < width; i ++) {
			for(int j = 0; j < height; j ++) {
				int currentTileId = map.getTileId(i, j, 1);
				String currentPowerup = map.getTileProperty(currentTileId, "powerup", "");
				
				if(!currentPowerup.equals(null)) {
					switch(currentPowerup) {
						case "splash": 		addEntity(new Colorsplash(), i ,j);
											break;
						case "speedUp":		addEntity(new Speedup(), i,j);
											break;
						case "dmgUp":		addEntity(new Damageup(), i,j);
											break;
						case "bullet2up":	addEntity(new Bullet2up(), i, j);
											break;
						case "bullet1up":	addEntity(new Bullet1up(), i, j);
											break;
						case "reloadUp":	addEntity(new Reloadup(), i, j);
											break;
						case "extraLife":	addEntity(new ExtraLife(), i, j);
											break;
						case "shield":		addEntity(new Shield(), i, j);
											break;
											
					}
				}
					
			}
		}
	}
	
	//parse and generate enemies from the tilemap (2nd layer)
	public void parseEnemies() {
		
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				int currentTileId = map.getTileId(i, j, 2);
				String currentEnemy = map.getTileProperty(currentTileId, "enemyName", ""); 
				if(!currentEnemy.equals(null)) {
					switch(currentEnemy) {
						case "guardian":	addEnemy(new Guardian(i, j));
											break;
						case "curvance":	if(i > width/2) {
												addEnemy(new Curvance(i,j,-1));
											} else {
												addEnemy(new Curvance(i,j,1));
											}
											break;
					}
				}
			}
		}
		
	}
	
	//renders the color grid
	public void renderColors(GameContainer gc, StateBasedGame sb, Graphics g) throws SlickException {				
		//Render each color in the level's grid if it exists
		for(int i = 0; i < getWidth(); i ++) {
			for(int j = 0; j < getHeight(); j++) {
				if(getColorGrid()[i][j].HasChanged())
					getColorGrid()[i][j].render(gc,sb,g);
			}
		}
	}
	
	//starts muisc
	public void startMusic() {
		Resources.startMusic(levelBgm);
	}
	
	//adds a bullet
	public void addBullet(Bullet b, Actionable shooter) {
		bulletArray.add(b);
		b.setLevel(this);
		b.setShooter(shooter);
		b.setColor(shooter.getColor());
		
		if(!(shooter instanceof Player))
			player.setBulletsDodged(player.getBulletsDodged()+1);
		
		float distFromShooter = b.getCurrentPos().getY() - shooter.getCurrentPos().getY();
	
		b.setScreeny(shooter.getScreeny() + distFromShooter);
		
		b.setBitmask(new Bitmask(Resources.retrieveSpriteSheet("bulletMasks").getSprite(b.getBulletType(), b.getBulletSeries()), 
					 Color.black, new Vector2f(b.getCurrentPos().getX(), b.getScreeny())));
		
		b.setMoving(true);
	}
	
	//adds an enemy
	public void addEnemy(Enemy e) {		
		switch(e.getSize()) {
			case "Small": 	enemyArraySmall.add(e);
						  	break;
			case "Medium": 	enemyArrayMed.add(e);
							break;
			case "Big":		enemyArrayBig.add(e);
							break;
			case "Boss": 	enemyArrayBoss.add(e);
							break;
		}			
		
		e.setSs(Resources.retrieveSpriteSheet("enemies".concat(e.getSize())));
		e.setLevel(this);
		
		e.setScreeny(getMapY() + e.getCurrentPos().getY());
	}
	
	//remove entity at given x,y
	public void removeEntity(int x, int y) {
		grid[x][y] = null;
	}
	
	//remove a given entity
	public void removeEntity(Entity e) {
		e.removeSelf();
	}
	
	public Entity getEntity(int x, int y) {
		return(grid[x][y]);
	}
	
	//reset the level
	public void reset() throws SlickException {
		setMapY(-1 * map.getHeight() * Entity.IMG_PIXELS + 13 * Entity.IMG_PIXELS);
		
		//add player 
		setPlayer(new Player(Resources.retrieveImage("grayPlayer")));
		getPlayer().setLevel(this);
		getPlayer().setCurrentPos(new Vector2f(5 * Entity.IMG_PIXELS, (map.getHeight() - 1) * Entity.IMG_PIXELS));
		
		//add other entities and powerups
	}
	
	//SETTERS/GETTERS BELOW
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}


	public void setHeight(int height) {
		this.height = height;
	}	
	
	public TiledMap getMap() {
		return map;
	}

	public void setMap(TiledMap map) {
		this.map = map;
	}

	public Entity[][] getGrid() {
		return grid;
	}

	public void setGrid(Entity[][] grid) {
		this.grid = grid;
	}
	
	public int getLevelid() {
		return levelid;
	}

	public void setLevelnum(int levelid) {
		this.levelid = levelid;
	}


	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public ColoredTile[][] getColorGrid() {
		return colorGrid;
	}

	public void setColorGrid(ColoredTile[][] colorGrid) {
		this.colorGrid = colorGrid;
	}

	public Music getLevelBgm() {
		return levelBgm;
	}

	public void setLevelBgm(Music levelBgm) {
		this.levelBgm = levelBgm;
	}

	public float getMapX() {
		return mapX;
	}

	public void setMapX(float mapX) {
		this.mapX = mapX;
	}

	public float getMapY() {
		return mapY;
	}

	public void setMapY(float mapY) {
		this.mapY = mapY;
	}


	public float getScrollSpeed() {
		return scrollSpeed;
	}

	public void setScrollSpeed(float scrollSpeed) {
		this.scrollSpeed = scrollSpeed;
	}

	public LinkedList<Bullet> getBulletArray() {
		return bulletArray;
	}

	public void setBulletArray(LinkedList<Bullet> bulletArray) {
		this.bulletArray = bulletArray;
	}

	public LinkedList<Enemy> getEnemyArraySmall() {
		return enemyArraySmall;
	}

	public void setEnemyArraySmall(LinkedList<Enemy> enemyArraySmall) {
		this.enemyArraySmall = enemyArraySmall;
	}

	public LinkedList<Enemy> getEnemyArrayMed() {
		return enemyArrayMed;
	}

	public void setEnemyArrayMed(LinkedList<Enemy> enemyArrayMed) {
		this.enemyArrayMed = enemyArrayMed;
	}

	public LinkedList<Enemy> getEnemyArrayBig() {
		return enemyArrayBig;
	}

	public void setEnemyArrayBig(LinkedList<Enemy> enemyArrayBig) {
		this.enemyArrayBig = enemyArrayBig;
	}

	public LinkedList<Enemy> getEnemyArrayBoss() {
		return enemyArrayBoss;
	}

	public void setEnemyArrayBoss(LinkedList<Enemy> enemyArrayBoss) {
		this.enemyArrayBoss = enemyArrayBoss;
	}

	public LinkedList<Enemy>[] getEnemies() {
		return enemies;
	}

	public void setEnemies(LinkedList<Enemy>[] enemies) {
		this.enemies = enemies;
	}



}
