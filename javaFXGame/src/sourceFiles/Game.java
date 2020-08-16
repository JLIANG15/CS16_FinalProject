//Jiayi Liang
//15 JavaFX Game 
//CIS016 #31478 
//6/6/20

/**
 * http://stackoverflow.com/questions/29057870/in-javafx-how-do-i-move-a-sprite-across-the-screen
 * http://silveiraneto.net/2008/12/08/javafx-how-to-create-a-rpg-like-game/
 * https://netopyr.com/2012/03/09/creating-a-sprite-animation-with-javafx/
 * http://zetcode.com/tutorials/javagamestutorial/collision/
 * https://stackoverflow.com/questions/31440024/javafx-simple-pathtransition-animation
 */

package sourceFiles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;

import javafx.stage.Stage;
import javafx.util.Duration;


public class Game extends Application {

    Random rnd = new Random();

    Pane playfieldLayer;
    Pane scoreLayer; 

    Image playerImage = new Image("images/player.png",300,300,true,true);
	Image playerBubbleImage = new Image("images/Player_Bubble_52x104.png");
	Image playerHealthImage=new Image("images/health.png",50,50,true,true);
    Image enemyImage = new Image("images/enemy.png",300,300,true,true);
    Image heartDropImage=new Image("images/Heartdrop_32x32.png");
    Image enemyBulletImage=new Image("images/Enemy_Bullet_72x29.png");
    Image maze01  = new Image("images/map0101.png");
    Image maze02=new Image("images/map0202.png");
    Image gameOverImage=new Image("images/gameOver.png",1100,570,false,false);
    Image victoryImage=new Image("images/tobecontinued.jpg",1100,570,false,false);
    
    Player player;
    Input input;
    
    
    Enemy enemy;

    List<Item> items = new ArrayList<>();
    List<Item> weapons = new ArrayList<>();
    List<Enemy> enemies = new ArrayList<>();
    List<Item> enemiesBullets = new ArrayList<>();

    //List<Rectangle>obstacles=new ArrayList<>();
    

    Text scoreText = new Text();
    int score = 0;
    
    Text levelText=new Text();
    int level=0;
    
    Rectangle healthBar=new Rectangle(80,20,240,35);
    
    Scene scene;
    
    boolean collision=false;

    int currentScene = 1;
    Group root;
    ImageView mazeView;
    ImageView heartDropView;
    ImageView gameOverScreen;
    ImageView victoryScreen;
    ImageView healthBarHeartView;
    
    List<Rectangle> obstacles01=new ArrayList<Rectangle>();
    List<Rectangle> obstacles02=new ArrayList<>();
    
    PathTransition pathTransition;

    AnimationTimer gameLoop;
    
    @Override
    public void start(Stage primaryStage) {
    	//room #1
    	obstacles01.add(new Rectangle(0,0,Settings.SCENE_WIDTH,140));
    	obstacles01.add(new Rectangle(0,0,110,Settings.SCENE_HEIGHT));
    	obstacles01.add(new Rectangle(0,0,340,240));
    	obstacles01.add(new Rectangle(120,460,625,320));
    	obstacles01.add(new Rectangle(120+625,520,400,150));
    	obstacles01.add(new Rectangle(990,350,300,400));
    	obstacles01.add(new Rectangle(870,0,300,240));
    	
    	//room #2
    	obstacles02.add(new Rectangle(0,0,Settings.SCENE_WIDTH,20));
    	obstacles02.add(new Rectangle(0,0,55,250));
    	obstacles02.add(new Rectangle(0,350,55,250));
    	obstacles02.add(new Rectangle(0,520,1100,55));
    	obstacles02.add(new Rectangle(1045,0,100,570));
    	obstacles02.add(new Rectangle(410,400,42,570));
    	
        root = new Group();
        
        mazeView = new ImageView(maze01);
        
        heartDropView=new ImageView(heartDropImage);
        heartDropView.setX(1000);
        heartDropView.setY(300);
        
        healthBarHeartView=new ImageView(playerHealthImage);
        healthBarHeartView.setX(25);
        healthBarHeartView.setY(15);
           
        //obstacles = obstacles01;
            
        // create layers
        playfieldLayer = new Pane();
        scoreLayer = new Pane();
        
        playfieldLayer.getChildren().add(mazeView);
        playfieldLayer.getChildren().add(heartDropView);
        playfieldLayer.getChildren().add(healthBarHeartView);
        
        
        root.getChildren().add( playfieldLayer);
        root.getChildren().add( scoreLayer);

        scene = new Scene( root, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
             
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // player input
        input = new Input(scene);

        // register input listeners
        input.addListeners(); // TODO: remove listeners on game over

        // create player
        player = new Player(playfieldLayer, playerImage, 128, 300, 0, 0, 0,0, Settings.PLAYER_HEALTH, Settings.PLAYER_DAMAGE, Settings.PLAYER_SPEED, input,playerBubbleImage);
        player.setSpriteAnimation();
        
        createScoreLayer();
        
        
        
        
        Item enemyBullet = new Item(playfieldLayer, enemyBulletImage, 730, 350, 0, 0, 0, 0, 1, Settings.ENEMY_DAMAGE, enemiesBullets);
        Path path = new Path();
        path.getElements().add(new MoveTo(0, 0));
        path.getElements().add(new LineTo(-1000, 0));
        pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(5000));
        pathTransition.setPath(path);
        pathTransition.setNode(enemyBullet.imageView);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.play();
        
        
        addEnemy(770, 350, enemyImage);

        gameLoop = new AnimationTimer() {
        	
            @Override
            public void handle(long now) {

                // player input
                player.processInput();
                
                // movement
                player.move();
                weapons.forEach(sprite -> sprite.move());
                
                player.attack(weapons);
                player.reloadSword();
                
                // check collisions
                checkCollisions();
                
                checkDamage();
                
                changeScene(primaryStage);

                // update sprites in scene
                player.updateUI();
                weapons.forEach(sprite -> sprite.updateUI());

                // check if sprite can be removed

                // remove removables from list, layer, etc 
                removeSprites(weapons);
                removeSprites(items);
                removeSprites(enemies);

                // update score, health, etc
                updateScore();
                
                if(!enemy.isAlive()) {
                	pathTransition.stop();
                	playfieldLayer.getChildren().remove(enemyBullet.imageView);
                	enemyBullet.damage=0;
                	player.setInvincibility(0);
                	
                }
                
                gameOver(primaryStage);
                
            }

        };
        gameLoop.start();

    }
   

    private void createScoreLayer() {
    	scoreText.setX(450);
    	scoreText.setY(50);
    	scoreText.setText("Score: "+score);
        scoreText.setFont(Font.font ("Serif", 50));      
        scoreText.setFill(Color.BLUE);
    	scoreLayer.getChildren().add(scoreText);
    	levelText.setX(800);
    	levelText.setY(50);
    	levelText.setText("Level: "+level);
    	levelText.setFont(Font.font ("Serif", 50));      
    	levelText.setFill(Color.WHITE);
    	scoreLayer.getChildren().add(levelText);
    	healthBar.setStroke(Color.BLACK);
    	healthBar.setStrokeWidth(3.0);
    	healthBar.setFill(Color.GREEN);
    	scoreLayer.getChildren().add(healthBar);
    }
    
    private void addEnemy(double x, double y, Image enemyImage) {
        enemy=new Enemy(playfieldLayer, enemyImage, x, y, 0, 0, 0, 0, Settings.ENEMY_HEALTH, Settings.ENEMY_DAMAGE, Settings.ENEMY_SPEED,input,enemyBulletImage);   	
        enemies.add(enemy);
        enemy.setSpriteAnimation();
        enemy.walk();
        
    }
    
    
    
    

    private void removeSprites(  List<? extends SpriteBase> spriteList) {
        Iterator<? extends SpriteBase> iter = spriteList.iterator();
        while( iter.hasNext()) {
            SpriteBase sprite = iter.next();

           if( sprite.isRemovable()) {

                // remove from layer
                sprite.removeFromLayer();

                // remove from list
                iter.remove();
                
            }
        }
    }
    

    
    private void checkCollisions() {	
    	Rectangle playerBounds=player.getBounds();
    	if(currentScene==1) {
    		player.checkMovability(obstacles01);
    	    if(playerBounds.intersects(heartDropView.getBoundsInParent())&&playfieldLayer.getChildren().contains(heartDropView)) {
    		    score+=1;
    		    level+=1;
    		    player.health=6;
    		    playfieldLayer.getChildren().remove(heartDropView);
    	    }
    	}
    	else if (currentScene==2) {
    		player.checkMovability(obstacles02);
    	}
    	
    	for (final Item weapon : new ArrayList<Item>(weapons)) {
    		if (currentScene==1) {	
    			weapon.checkRemovability(obstacles01);
	
    		}
    		else if (currentScene==2) {
    			weapon.checkRemovability(obstacles02);
    		}
    	}
    	
    	
    }
    
    private void checkDamage() {
    	for (final Enemy enemy : new ArrayList<Enemy>(enemies)) {
    	        if(player.collidesWith(enemy)&&player.canBeDamaged()==true) {
    	    	    player.getDamagedBy(enemy);
    		        player.setInvincibility(100);	
    	        }
    	        
    	        for (final Item weapon : new ArrayList<Item>(weapons)) {
    	            if (weapon.collidesWith(enemy)) {
    	    	        enemy.getDamagedBy(weapon);
    	    	        weapon.checkRemovability();
    	    	        score+=1;
    	            }
    	            
    	        }
    		enemy.checkRemovability();
    	
    	}
    	for (final Item enemyBullet : new ArrayList<Item>(enemiesBullets)) {
    		if (player.collidesWith(enemyBullet)&&player.canBeDamaged()) {
    			player.getDamagedBy(enemyBullet);
    			player.setInvincibility(100);
    			
    		}
    	}
    	
    	player.checkInvincibility();
    	
    }
    
    
    // go to a different level by exiting/entering through a door on the current level
    private void changeScene(Stage primaryStage) {
    	Rectangle nextRoom = new Rectangle(1100, 300, 100, 100);
    	if (player.getBounds().intersects(nextRoom.getBoundsInParent())) {
    		moveToScene02(0,270, primaryStage);
    	}
    	
    }
    
    
    private void moveToScene02(double x, double y, Stage primaryStage) {
    	currentScene = 2;
    	for (final Enemy enemy : new ArrayList<Enemy>(enemies)) {
    	enemy.setRemovable(true);}
    	/*root = new Group();
        playfieldLayer = new Pane();   	        
        scoreLayer = new Pane();  
		root.getChildren().addAll(playfieldLayer,scoreLayer);
        mazeView = new ImageView(maze02);   	           	        
        playfieldLayer.getChildren().add(mazeView); 
        playfieldLayer.getChildren().add(healthBarHeartView); 
        scene = new Scene(root,Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
        primaryStage.setScene(scene);
        createScoreLayer();
        checkDamage();
        player.setX(x);
        player.setY(y);
        player.setLayer(playfieldLayer);
        player.addToLayer();
        input.setScene(scene);
        input.addListeners();
        items.clear();
        gameOver(primaryStage);*/
        victory(primaryStage); 
    }
    
    private void updateScore() {
    	scoreText.setText("Score: "+score);
    	levelText.setText("Level: "+level);
    	if (player.health==5) {healthBar.setWidth(200);}
    	else if (player.health==4) {healthBar.setWidth(160);healthBar.setFill(Color.ORANGE);}
    	else if (player.health==3) {healthBar.setWidth(120);healthBar.setFill(Color.ORANGE);}
    	else if (player.health==2) {healthBar.setWidth(80);healthBar.setFill(Color.RED);}
    	else if (player.health==1) {healthBar.setWidth(40);healthBar.setFill(Color.RED);}
    	else if (player.health==6) {healthBar.setWidth(240);healthBar.setFill(Color.GREEN);}
    }
    
    private void gameOver(Stage primaryStage) {
    	if (player.health==0) {
    		root = new Group();
    		playfieldLayer = new Pane();
    		root.getChildren().add(playfieldLayer);
    		gameOverScreen = new ImageView(gameOverImage);   	           	        
            playfieldLayer.getChildren().add(gameOverScreen);   	               
            scene = new Scene(root,Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
            primaryStage.setScene(scene);
    	}
    }
    
    private void victory(Stage primaryStage) {
    	root = new Group();
		playfieldLayer = new Pane();
		root.getChildren().add(playfieldLayer);
		gameOverScreen = new ImageView(victoryImage);   	           	        
        playfieldLayer.getChildren().add(gameOverScreen);   	               
        scene = new Scene(root,Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
        primaryStage.setScene(scene);
    }
		

    public static void main(String[] args) {
        launch(args);
    }

}