package sourceFiles;


import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import javafx.scene.shape.*;
import javafx.util.Duration;

public class Enemy extends SpriteBase {
	
	SpriteAnimation enemy_animation;
	double speed;
	int direction = 6;
	
	Input input;
	
	Image enemyBubbleImage;
    ImageView enemyBubbleView;

    public Enemy(Pane layer, Image image, double x, double y, double r, double dx, 
    		double dy, double dr, double health, double damage, double speed, 
    		Input input, Image enemyBubbleImage) {
        super(layer, image, x, y, r, dx, dy, dr, health, damage);
        
        this.speed = speed;
        this.input = input;

        this.enemyBubbleImage = enemyBubbleImage;
    }

    public void walk() {

        // ------------------------------------
        // movement
        // ------------------------------------
    	Path path = new Path();
        path.getElements().add(new MoveTo(0, 0));
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(5000));
        pathTransition.setPath(path);
        pathTransition.setNode(this.imageView);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
    	
        path.getElements().add(new LineTo(0, 100)); //1
        path.getElements().add(new LineTo(160, 100));//2
        path.getElements().add(new LineTo(160, 0));//3        
        path.getElements().add(new LineTo(0, 0));//4
        dx = -speed;
        direction = 9;
        enemy_animation.setOffsetY(Settings.Eheight);
        enemy_animation.play();
        pathTransition.play();
    	

    }

   
    @Override
    public void move() {
      	
        super.move();  
        
        
        
    }
    
    
    
    public void setSpriteAnimation() {
        imageView.setViewport(new Rectangle2D(Settings.EoffsetX, Settings.EoffsetY, Settings.Ewidth, Settings.Eheight));
        enemy_animation = new SpriteAnimation(
        		imageView,
                Duration.millis(900),
                Settings.Ecount, Settings.Ecolumns,
                Settings.EoffsetX, Settings.EoffsetY,
                Settings.Ewidth, Settings.Eheight
        );
        enemy_animation.setCycleCount(Animation.INDEFINITE);	// sets animation to run indefinitely
    }
    
    public ImageView getSwordImageView() {
		return enemyBubbleView;
	}

	public void setSwordImageView(ImageView enemyBubbleView) {
		this.enemyBubbleView = enemyBubbleView;
	}
     

    @Override
    public void checkRemovability() {

        if( Double.compare( getY(), Settings.SCENE_HEIGHT) > 0) {
            setRemovable(true);
        }
        if( health <= 0) {
            setRemovable(true);
        }


    }
}