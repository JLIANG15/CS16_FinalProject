package sourceFiles;

import java.util.List;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Item extends SpriteBase {
	
	List<Item> items;
	SpriteAnimation bullet_animation;
	
	public Item(Pane layer, Image image, double x, double y, double r, double dx, double dy, double dr, double health,
			double damage, List<Item> items) {
		super(layer, image, x, y, r, dx, dy, dr, health, damage);
		// TODO Auto-generated constructor stub
		this.items = items;
		addItem();
	}
	
    public void addItem() {
    	items.add(this);
    }

	@Override
	public void checkRemovability() {
		// TODO Auto-generated method stub	
		this.setRemovable(true);
	}

	public void checkRemovability(List<Rectangle> obstacles) {
		if (x < 0 || x > Settings.SCENE_WIDTH || y < 0 || y > Settings.SCENE_HEIGHT) this.setRemovable(true);

		for (Rectangle obst: obstacles) {
			if (obst.intersects(this.getX(), this.getY(), this.getView().getBoundsInParent().getWidth(), this.getView().getBoundsInParent().getHeight())) {
				this.setRemovable(true);
			}	
		}
        if( health <= 0) {
            setRemovable(true);
        }
	}
	
    public void setSpriteAnimation(int offsetY, int width, int height, int count, int columns) {
        imageView.setViewport(new Rectangle2D(0,offsetY,width,height));
        bullet_animation = new SpriteAnimation(
        		imageView,
                Duration.millis(900),
                count, columns,
                0, 0,
                width, height
        );
        bullet_animation.setCycleCount(Animation.INDEFINITE);	// sets animation to run indefinitely
        bullet_animation.play();
    }

}
