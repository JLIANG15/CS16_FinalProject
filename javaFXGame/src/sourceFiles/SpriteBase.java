package sourceFiles;

import java.util.List;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public abstract class SpriteBase {

    Image image;
    ImageView imageView;

    Pane layer;

    double x;
    double y;
    double r;

    double dx;
    double dy;
    double dr;

    double health;
    double damage;

    boolean removable = false;

    double w;
    double h;

    boolean canMove = true;
	private boolean canBeDamaged = true;
	FadeTransition ft;

    public SpriteBase(Pane layer, Image image, double x, double y, double r, double dx, double dy, double dr, double health, double damage) {

        this.layer = layer;
        this.image = image;
        this.x = x;
        this.y = y;
        this.r = r;
        this.dx = dx;
        this.dy = dy;
        this.dr = dr;

        this.health = health;
        this.damage = damage;

        this.imageView = new ImageView(image);
        this.imageView.relocate(x, y);
        this.imageView.setRotate(r);

        this.w = imageView.getBoundsInParent().getWidth(); //image.getWidth();
        this.h = imageView.getBoundsInParent().getHeight(); //image.getHeight();
        
		ft = new FadeTransition(Duration.millis(30), imageView);
	    ft.setFromValue(1.0);
	    ft.setToValue(0.1);
	    ft.setCycleCount(Timeline.INDEFINITE);
	    ft.setAutoReverse(true);

        addToLayer();

    }

    public void addToLayer() {
        this.layer.getChildren().add(this.imageView);
    }

    public void removeFromLayer() {
        this.layer.getChildren().remove(this.imageView);
    }

    public Pane getLayer() {
        return layer;
    }

    public void setLayer(Pane layer) {
        this.layer = layer;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getDr() {
        return dr;
    }

    public void setDr(double dr) {
        this.dr = dr;
    }

    public double getHealth() {
        return health;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public boolean isRemovable() {
        return removable;
    }

    public void setRemovable(boolean removable) {
        this.removable = removable;
    }
    
    
    public void checkMovability(List<Rectangle> obstacles) {
    	  for(Rectangle obst: obstacles) {
              if(obst.intersects(x + dx, y + dy, imageView.getBoundsInParent().getWidth(), imageView.getBoundsInParent().getHeight())) 
                  canMove = false;
           }
    }
    
    public void move() {

        if( !canMove)
            return;

        x += dx;
        y += dy;
        r += dr;

    }

    public boolean isAlive() {
        return Double.compare(health, 0) > 0;
    }

    public ImageView getView() {
        return imageView;
    }

    public void updateUI() {

        imageView.relocate(x, y);
        imageView.setRotate(r);

    }

    public double getWidth() {
        return w;
    }

    public double getHeight() {
        return h;
    }

    public double getCenterX() {
        return x + w * 0.5;
    }

    public double getCenterY() {
        return y + h * 0.5;
    }

    // TODO: per-pixel-collision
    public boolean collidesWith( SpriteBase otherSprite) {

    	return imageView.getBoundsInParent().intersects(otherSprite.imageView.getBoundsInParent());
    }

    /**
     * Reduce health by the amount of damage that the given sprite can inflict
     * @param sprite
     */
    public void getDamagedBy( SpriteBase sprite) {
        health -= sprite.getDamage();
    }

    /**
     * Set health to 0
     */
    public void kill() {
        setHealth( 0);
    }

    /**
     * Set flag that the sprite can be removed from the UI.
     */
    public void remove() {
        setRemovable(true);
    }

    /**
     * Set flag that the sprite can't move anymore.
     */
    public void stopMovement() {
        this.canMove = false;
    }

    public abstract void checkRemovability();
    
	public boolean canBeDamaged() {
		return canBeDamaged;
	}

	public void setCanBeDamaged(boolean canBeDamaged) {
		this.canBeDamaged = canBeDamaged;
	}
	
	public void flash() {

		ft.play();	    
	}
	public void stopFlash() {
		if (ft.getStatus() == Animation.Status.RUNNING) {
			ft.stop();
			imageView.setOpacity(1.0);
		}
	}
	
	
}