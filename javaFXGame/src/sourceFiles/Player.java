package sourceFiles;


import java.util.List;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Player extends SpriteBase {

    double playerMinX;
    double playerMaxX;
    double playerMinY;
    double playerMaxY;

    Input input;

    double speed;
    
    SpriteAnimation player_animation;
    
    Image playerBubbleImage;
    ImageView playerBubbleView;
   
    
    int direction = 6;
    
    int time = 0;
    int time2 = 0;
    private boolean canShoot = true;
    


    public Player(Pane layer, Image image, double x, double y, double r, double dx,
    		double dy, double dr, double health, double damage, double speed,
    		Input input, Image playerBubbleImage) {

        super(layer, image, x, y, r, dx, dy, dr, health, damage);

        this.speed = speed;
        this.input = input;
        this.playerBubbleImage = playerBubbleImage;

        
        playerBubbleView= new ImageView(playerBubbleImage);

        init();
    }

    private void init() {

        // calculate movement bounds of the player
        playerMinX = 0;
        playerMaxX = Settings.SCENE_WIDTH - Settings.Pwidth;
        playerMinY = 0;
        playerMaxY = Settings.SCENE_HEIGHT - Settings.Pheight;

    }

    public void processInput() {

        // ------------------------------------
        // movement
        // ------------------------------------
    	
    	if (!input.isMoveUp() && !input.isMoveDown() && !input.isMoveLeft() && !input.isMoveRight())
    		player_animation.pause();
    	
        // vertical direction
        if( input.isMoveUp()) {
            dy = -speed;
            direction = 12;
            player_animation.setOffsetY(Settings.Pheight*3);
            player_animation.play();
        } else if( input.isMoveDown()) {
            dy = speed;
            direction = 6;
            player_animation.setOffsetY(0);
            player_animation.play();
        } else {
            dy = 0d;
        }

        // horizontal direction
        if( input.isMoveLeft()) {
            dx = -speed;
            direction = 9;
            player_animation.setOffsetY(Settings.Pheight);
            player_animation.play();
        } else if( input.isMoveRight()) {
            dx = speed;
            direction = 3;
            player_animation.setOffsetY(Settings.Pheight *2);
            player_animation.play();
        } else {
            dx = 0d;
        }
    }
   
    @Override
    public void move() {
      	
        super.move();  
        
        // ensure the player can't move outside of the screen
        checkBounds();
    }
    
    public void attack(List<Item> weapons) {
		double x = super.getX();
		double y = super.getY();
		int offsetY = 0;
		int offsetY2 = 0;
		double dx = 0;
		double dy = 0;
		
		if (direction == 12) {
			offsetY = 0;
			offsetY2 = Settings.Pheight*3;
    		y -= 40;
    		dy = -Settings.PLAYER_WEAPON_SPEED;
		}
		else if (direction == 6) {
			offsetY = 0;
			offsetY2 = 0;
    		y += 50;
    		dy = Settings.PLAYER_WEAPON_SPEED;
		}
		else if (direction == 9) {
			offsetY = 30;
			offsetY2 = Settings.Pheight ;
    		x -= 40;
    		y += 20;
    		dx = -Settings.PLAYER_WEAPON_SPEED;
		}
		else if (direction == 3) {
			offsetY = 30;
			offsetY2 = Settings.Pheight * 2;
    		x += 50;
    		y += 20;
    		dx = Settings.PLAYER_WEAPON_SPEED;
		}
    	
    	if (input.isFireSecondaryWeapon()) {
    		canMove = false;

    		if (canShoot) {
        		Item weapon = new Item(layer, playerBubbleImage, x, y, 0, dx, dy, 0, 1, Settings.PLAYER_SECONDARY_WEAPON_DAMAGE, weapons);
        		weapon.setSpriteAnimation(offsetY, 26, 26, 2, 2);
        		weapon.bullet_animation.setOffsetY(offsetY);
        		canShoot = false; 
        		time2 = 30;
    		} 	    
    	    
    	    imageView.setViewport(new Rectangle2D(0,offsetY2,Settings.Pwidth,Settings.Pheight));    		
    	}
    	else if (!input.isFireSecondaryWeapon()) {
		canMove = true;
		playerBubbleView.setX(0);
		playerBubbleView.setY(0);
		if (layer.getChildren().contains(playerBubbleView))
			layer.getChildren().remove(playerBubbleView);
		
	}
    }
    
    public void reloadSword() {
    	time2--;
		if (time2 <= 0) {
			canShoot = true;
		}
    }
    
    public void setSpriteAnimation() {
        imageView.setViewport(new Rectangle2D(Settings.PoffsetX, Settings.PoffsetY, Settings.Pwidth, Settings.Pheight));
        player_animation = new SpriteAnimation(
        		imageView,
                Duration.millis(900),
                Settings.Pcount, Settings.Pcolumns,
                Settings.PoffsetX, Settings.PoffsetY,
                Settings.Pwidth, Settings.Pheight
        );
        player_animation.setCycleCount(Animation.INDEFINITE);	// sets animation to run indefinitely
    }

    private void checkBounds() {
    	
        // vertical
        if( Double.compare( y, playerMinY) < 0) {
            y = playerMinY;
        } else if( Double.compare(y, playerMaxY) > 0) {
            y = playerMaxY;
        }

        // horizontal
        if( Double.compare( x, playerMinX) < 0) {
            x = playerMinX;
        } else if( Double.compare(x, playerMaxX) > 0) {
            x = playerMaxX;
        }

    }
    
    public Rectangle getBounds() {
    	double x = super.getX();
		double y = super.getY();
        return new Rectangle(x,y, Settings.Pwidth, Settings.Pheight);
    }

    public ImageView getSwordImageView() {
		return playerBubbleView;
	}

	public void setSwordImageView(ImageView playerBubbleView) {
		this.playerBubbleView = playerBubbleView;
	}
	
	public void setInvincibility(int time) {
		this.time = time;
		super.setCanBeDamaged(false);
		super.flash();
	}
	
	public void checkInvincibility() {
		time--;
		if (time <= 0) {
			super.stopFlash();
        	super.setCanBeDamaged(true);
		}
	}

    @Override
    public void checkRemovability() {
        // TODO Auto-generated method stub
    }

}