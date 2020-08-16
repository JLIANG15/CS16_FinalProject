package sourceFiles;

public class Settings {
	public static final int CANVAS_WIDTH = 1900;
	public static final int CANVAS_HEIGHT = 998;
	
    public static double SCENE_WIDTH = 1100;
    public static double SCENE_HEIGHT = 570;

    public static double PLAYER_SPEED = 4.0;
    public static double PLAYER_HEALTH = 6.0;
    public static double PLAYER_DAMAGE = 1.0;
    public static double PLAYER_SECONDARY_WEAPON_DAMAGE = 1.0;
    public static double PLAYER_WEAPON_SPEED = 4.0;
    
    public static double ENEMY_SPEED = 1.0;
    public static double ENEMY_DAMAGE = 1.0;
    public static double ENEMY_HEALTH = 3.0;
    public static double ENEMY_BULLET_HEALTH = 1.0;
    public static double ENEMY_BULLET_SPEED = 3.0;
    
    public static double BOSS_SPEED = 1.0;
    public static double BOSS_DAMAGE = 2.0;
    public static double BOSS_HEALTH = 10.0;

//    public static double PLAYER_MISSILE_HEALTH = 200.0;
    

    public static int ENEMY_SPAWN_RANDOMNESS = 200;
    public static int HEART_DROP_RANDOMNESS = 3;
    
    // setup variables for Link
    public static int Pcolumns = 4;	// number of columns in sprite image
    public static int Pcount = 4;		// number of frames for sprite
    public static int PoffsetX = 0;	// offset X of where the image starts
    public static int PoffsetY = 0;	// offset Y of where the image starts
    public static int Pwidth = 50;	// width of the sprite
    public static int Pheight = 75;	// height of the sprite
    
    // set up variables for enemy
    public static int Ecolumns = 4; 
    public static int Ecount = 4;   
    public static int EoffsetX = 2; 
    public static int EoffsetY = 0 ; 
    public static int Ewidth = 53;  
    public static int Eheight = 75;
    
    //set up variables for boss
    public static int Bcolumns = 4; 
    public static int Bcount = 4;   
    public static int BoffsetX = 2; 
    public static int BoffsetY = 0 ; 
    public static int Bwidth = 100;  
    public static int Bheight = 150;

}